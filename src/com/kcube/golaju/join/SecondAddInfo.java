package com.kcube.golaju.join;

import java.net.URLEncoder;

import org.w3c.dom.Document;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.kcube.golaju.R;
import com.kcube.golaju.login.Login;
import com.kcube.golaju.util.PropertyUtil;
import com.kcube.golaju.util.XmlUtil;
import com.kucbe.golaju.common.LoadingProgress;
import com.kucbe.golaju.common.NormalActivity;

public class SecondAddInfo extends NormalActivity implements OnClickListener
{
	String loginId = null;
	String ageGroup = null;
	Integer gender = null;
	ImageView age10;
	ImageView age20;
	ImageView age30;
	ImageView age40;
	ImageView age50;
	ImageView age60;
	ImageView age70;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second_add_info);
		
		initSetting(this, false, false); // 공통적인 초기 새팅을 한다.
		
		
		
		
		age10 = (ImageView) findViewById(R.id.img_10Group);
		age20 = (ImageView) findViewById(R.id.img_20Group);
		age30 = (ImageView) findViewById(R.id.img_30Group);
		age40 = (ImageView) findViewById(R.id.img_40Group);
		age50 = (ImageView) findViewById(R.id.img_50Group);
		age60 = (ImageView) findViewById(R.id.img_60Group);
		age70 = (ImageView) findViewById(R.id.img_70Group);
		
		age10.setOnClickListener(this);
		age20.setOnClickListener(this);
		age30.setOnClickListener(this);
		age40.setOnClickListener(this);
		age50.setOnClickListener(this);
		age60.setOnClickListener(this);
		age70.setOnClickListener(this);

		Button ageGroupInput = (Button) findViewById(R.id.btn_ageGroupInput);
		ageGroupInput.setOnClickListener(new View.OnClickListener() { //연령 입력 버튼을 클릭했으면.
			@Override
			public void onClick(View v)
			{
				LoadingProgress.showLoadingDialog(SecondAddInfo.this);
				new Thread(new Runnable(){
					@Override
					public void run()
					{
						if(ageGroup != null){ //연령층을 선택했으면.
							try
							{
								
								

								if(getIntent().hasExtra("loginId") && getIntent().hasExtra("gender")){ // 게시글 목록에서 넘겨받은 loginId, gender 값이 있다면.
									loginId = getIntent().getExtras().get("loginId").toString(); //getIntent()메소드는 현재 자신을 호출했던 Intent를 반환
									gender = Integer.parseInt(getIntent().getExtras().getString("gender"));
					        	}else if(saveDataAsClosed.getLogin()){ // 메모리 문제로 인해 강제 종료된 경우를 대비하여 savedInstanceState안에 저장된 값이 있다면.
					        		String savedLoginId = saveDataAsClosed.getLoginId();
					        		Integer savedGender = saveDataAsClosed.getGender();
					    			if(savedLoginId != null && savedGender != null){
					    				loginId = savedLoginId;
										gender = savedGender;
					    			}
					        	}
								
								
								
								
								String url = PropertyUtil.getProperty("golaju.dbWork.url") + "/joinAddInfo.jsp?loginId=" + URLEncoder.encode(loginId, "UTF-8") + "&gender=" + URLEncoder.encode(gender.toString(), "UTF-8") + "&ageGroup=" + URLEncoder.encode(ageGroup, "UTF-8");
						            
					    		Document doc = XmlUtil.readXml(url);
					    		
					    		if (XmlUtil.isXPath(doc, "//AddInfoInput_Success"))
					    		{
					    			boolean isSuccess = Boolean.parseBoolean(XmlUtil.getXPath(doc, "//AddInfoInput_Success"));
					    			
					    			if(isSuccess){ //추가 정보 입력을 성공했다면.
					    				runOnUiThread(new Runnable(){
											@Override public void run(){
												Toast.makeText(SecondAddInfo.this, "추가 정보 입력에 성공하셨습니다. 가입된 ID로 로그인 하세요.", Toast.LENGTH_SHORT).show();
											}
					    				});
					    				Intent intent = new Intent(SecondAddInfo.this, Login.class);
					    				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										startActivity(intent);
										finish();
					    			}
					    		}
							} catch (Exception e){
								e.printStackTrace();
							}
						}else{ //연령층을 선택하지 않았으면.
							runOnUiThread(new Runnable(){
								@Override public void run(){
									Toast.makeText(SecondAddInfo.this, "연령을 선택해주세요.", Toast.LENGTH_SHORT).show();
								}
							});
						}
						LoadingProgress.getHandler().sendEmptyMessage(0);
					}
				}).start();
			} 
        }); 
	}

	@Override
	public void onClick(View v)
	{
		age10.setImageResource(R.drawable.ic_launcher);
		age20.setImageResource(R.drawable.ic_launcher);
		age30.setImageResource(R.drawable.ic_launcher);
		age40.setImageResource(R.drawable.ic_launcher);
		age50.setImageResource(R.drawable.ic_launcher);
		age60.setImageResource(R.drawable.ic_launcher);
		age70.setImageResource(R.drawable.ic_launcher);
		
		ImageView ageImg = (ImageView) findViewById(v.getId());
		ageImg.setImageResource(R.drawable.selected);
		
		ageGroup = v.getTag().toString();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    switch (keyCode) {
	    case KeyEvent.KEYCODE_BACK:
	    	Log.i("add guide2","add guide2");
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
