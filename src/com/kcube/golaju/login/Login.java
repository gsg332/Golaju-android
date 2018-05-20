package com.kcube.golaju.login;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;

import com.kcube.golaju.R;
import com.kcube.golaju.item.ItemList;
import com.kcube.golaju.item.ItemRead;
import com.kcube.golaju.join.Join;
import com.kcube.golaju.splash.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kcube.golaju.util.PropertyUtil;
import com.kcube.golaju.util.XmlUtil;
import com.kucbe.golaju.common.NormalActivity;

public class Login extends NormalActivity
{
	EditText idEdit = null;
	EditText pwEdit = null;
	CheckBox autoLogin = null;
	TextView autoLoginText = null;
	
	boolean isLogin = false;
	
	String pushCheck = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		
		Log.i("실행 액티비티", "Login");
		

		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		
		menulistView = (ListView) findViewById(R.id.menu_listView);
		
		
		initSetting(this, false, true); // 공통적인 초기 새팅을 한다.
		
		
		
		
		
		

		
		idEdit = (EditText) findViewById(R.id.loginIdEdit);
		pwEdit = (EditText) findViewById(R.id.loginPwEdit);
		autoLogin = (CheckBox) findViewById(R.id.autoLogin);
		autoLoginText = (TextView) findViewById(R.id.autoLoginText);
		
		autoLoginText.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) // 자동 로그인 텍스트 영역을 클릭하면 체크박스에 체크하기 및 체크해제하기.
			{
				if(autoLogin.isChecked()){
					autoLogin.setChecked(false);
				}else{
					autoLogin.setChecked(true);
				}
			}
		});
		
		
		
		
		Log.i("Login",getIntent().getExtras() + "");
		
		if(getIntent().getExtras() != null)
        {
	        if(getIntent().getExtras().getString("flag") != null)//
	    	{
	        	Log.i("onBackPressed","1차");
	        	if(getIntent().getExtras().getString("flag").equals("finish")){
	        		
	        		
	        		
	        		saveDataAsClosed.editClear();
	        		
	        		Log.i("onBackPressed","Login Main에서 프로페스 죽이고 종료.");
		    		android.os.Process.killProcess(android.os.Process.myPid()); // 프로세스 및 스레드 죽이기.
		        	//finish();
		        	return;
	        	}
	    	}
        }
		

		
		
		if(getIntent( ).getExtras( ) != null) {
			Intent intent = getIntent();
			Log.d("----intent----", "null 아님");
			if(intent.getStringExtra("pushCheck") != null){
				pushCheck = intent.getStringExtra("pushCheck");
				Log.d("----intent----", pushCheck);
			}
		}
		
		if(saveDataAsLogin.getAutoLogin()){ //자동로그인을 했으면.
			
			SettingLoginInfo(saveDataAsLogin.getLoginId(), saveDataAsLogin.getLoginPw()); //로그인 성공 여부와 로그인을 성공 했다면 해당 사용자 정보를 전역적으로 사용하기 위해 Application를 상속받는 LoginUserInfo 클래스를 이용하여 저장.
			
		} else { //자동로그인을 하지 않았으면.
			
			Button loginBtn = (Button) findViewById(R.id.btn_login);
			loginBtn.setOnClickListener(new View.OnClickListener() { //로그인하기 버튼을 클릭했으면.
				@Override
				public void onClick(View v)
				{
					String id = idEdit.getText().toString();
					String pw = pwEdit.getText().toString();
					Boolean isAutoLogin = autoLogin.isChecked();
					
					isLogin = SettingLoginInfo(id, pw); //로그인 성공 여부와 로그인을 성공 했다면 해당 사용자 정보를 전역적으로 사용하기 위해 Application를 상속받는 LoginUserInfo 클래스를 이용하여 저장.
					
					if(isLogin){
						// 자동 로그인이 되도록 정보 저장. 
						saveDataAsLogin.setLoginId(id);
						saveDataAsLogin.setLoginPw(pw);
						saveDataAsLogin.setAutoLogin(isAutoLogin);
						saveDataAsLogin.editCommit();
						
					}
				} 
	        });
			
			TextView joinBtn = (TextView) findViewById(R.id.btn_join);
			joinBtn.setOnClickListener(new View.OnClickListener() { //회원가입 버튼을 클릭했으면.
				@Override
				public void onClick(View v)
				{
					Intent intent = new Intent(Login.this, Join.class);
				    startActivity(intent);
				    //finish();
				} 
	        }); 
		}
	}
	
	
	
	
	
	
	
	@Override
	public void onBackPressed()
	{
		Log.i("onBackPressed","Login 프로페스 죽이고 종료.");
		android.os.Process.killProcess(android.os.Process.myPid()); // 프로세스 및 스레드 죽이기.

		super.onBackPressed();
	}
	
	
	/*
	 * xml 정보를 받아온후 <login_success> 값이 
	 * false(로그인 실패)이면 로그인 실패 토스트 띄우기. 
	 * true이면 사용자 정보를 전역적으로 사용하기 위해 Application를 상속 받는 Global 클래스를 사용하여 해당 사용자 정보를 저장. 
	 * 그 후 스플래쉬 화면으로 전환시킴.
	 */
	public boolean SettingLoginInfo(String id, String pw){
		
		boolean isLogin = false;
		
		try
		{
            String url = PropertyUtil.getProperty("golaju.dbWork.url") + "/login.jsp?id=" + URLEncoder.encode(id, "UTF-8") + "&pw=" + URLEncoder.encode(pw, "UTF-8");
            //String url = "http://125.140.114.140/App/DBLink/login.jsp?id=" + URLEncoder.encode(id, "UTF-8") + "&pw=" + URLEncoder.encode(pw, "UTF-8");
    		Document doc = XmlUtil.readXml(url);
    		
    		if (XmlUtil.isXPath(doc, "//Login_Success"))
    		{
    			isLogin = Boolean.parseBoolean(XmlUtil.getXPath(doc, "//Login_Success"));
    			if(isLogin){ //로그인 성공.
    				Map<String, Object> map = new HashMap<String, Object>();
    				map.put("userId", XmlUtil.getXPath(doc, "//userId"));
    				map.put("loginId", XmlUtil.getXPath(doc, "//loginId"));
    				map.put("loginPw", XmlUtil.getXPath(doc, "//loginPw"));
    				map.put("userName", XmlUtil.getXPath(doc, "//userName"));
    				map.put("jobCode", XmlUtil.getXPath(doc, "//jobCode"));
    				map.put("thumbPath", XmlUtil.getXPath(doc, "//thumbPath"));
    				map.put("savePath", XmlUtil.getXPath(doc, "//savePath"));
    				map.put("isAdmin", XmlUtil.getXPath(doc, "//isAdmin"));
    				map.put("eMail", XmlUtil.getXPath(doc, "//eMail"));
    				
    				//Global userInfo = (Global) getApplication(); 
    				userInfo.setLogin(true); // 로그인 중임을 세팅.
    				userInfo.setUserId(Long.parseLong(XmlUtil.getXPath(doc, "//userId")));
    				userInfo.setLoginId(XmlUtil.getXPath(doc, "//loginId"));
    				userInfo.setLoginPw(pw);
    				userInfo.setUserName(XmlUtil.getXPath(doc, "//userName"));
    				userInfo.setJobCode(XmlUtil.getXPath(doc, "//jobCode"));
    				userInfo.setThumbPath(XmlUtil.getXPath(doc, "//thumbPath"));
    				userInfo.setSavePath(XmlUtil.getXPath(doc, "//savePath"));
    				userInfo.setAdmin(Boolean.parseBoolean(XmlUtil.getXPath(doc, "//isAdmin")));
    				userInfo.setEmail(XmlUtil.getXPath(doc, "//eMail"));
    				if(!XmlUtil.getXPath(doc, "//gender").equals("null")){
    					userInfo.setGender(Integer.parseInt(XmlUtil.getXPath(doc, "//gender")));
    				}
    				if(!XmlUtil.getXPath(doc, "//ageGroup").equals("null")){
    					userInfo.setAgeGroup(Integer.parseInt(XmlUtil.getXPath(doc, "//ageGroup")));
    				}
    				
    				if(pushCheck != null){
    					Log.d("----pushCheck : ----", pushCheck);
    					
    					Long itemId = null;
    					
    					
    					
    					if(getIntent().hasExtra("itemId")){ // 게시글 목록에서 넘겨받은 itemId값이 있다면.
    						itemId = getIntent().getExtras().getLong("itemId");
    		        	}else if(saveDataAsClosed.getLogin()){ // 메모리 문제로 인해 강제 종료된 경우를 대비하여 sharedData에 저장한 데이터로 값을 세팅한다.
    		        		Long savedItemId = saveDataAsClosed.getItemId();
    		    			if(savedItemId != null){
    		    				itemId = savedItemId;
    		    			}
    		        	}
    					
    					
    					
    					
    					Log.d("----pushCheck itemId----", itemId + "");
    					
    					if(pushCheck.equals("item")){
    						Log.d("----pushCheck : ----", pushCheck);
    						Intent itemRead = new Intent(Login.this, ItemRead.class);
        					Bundle bundle = new Bundle() ;
        					bundle.putLong("itemId", itemId) ;
        					bundle.putString("list", "itemList") ;
        					bundle.putString("listType", "notClosed") ;
        					itemRead.putExtras(bundle) ;
        					startActivity( itemRead ) ;
    					}else if(pushCheck.equals("list")){
    						Log.d("----pushCheck : ----", pushCheck);
    						Intent itemList = new Intent(Login.this, ItemList.class);
        					Bundle bundle = new Bundle() ;
        					bundle.putLong("itemId", itemId) ;
        					bundle.putString("listType", "notClosed") ;
        					itemList.putExtras(bundle) ;
        					startActivity( itemList ) ;
    					}
    					
    				}else{
    					Intent intent = new Intent( Login.this, Splash.class ) ;
    					startActivity( intent ) ;
    					//finish();
    				}
    			}else{ //로그인 실패.
    				//현재 저장되어 있는 id와 pw값이 변경되었거나 삭제 되었다면 빈값으로 초기화시킴.
    				saveDataAsLogin.setLoginId("");
    				saveDataAsLogin.setLoginPw("");
    				saveDataAsLogin.setAutoLogin(false);
    				saveDataAsLogin.editCommit();
					
    				Toast.makeText(Login.this, "로그인에 실패하셨습니다. 아이디와 패스워드를 확인해 주세요.", Toast.LENGTH_SHORT).show();
    			}
    		}
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return isLogin;
	}
}
