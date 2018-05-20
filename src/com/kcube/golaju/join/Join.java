package com.kcube.golaju.join;

import java.net.URLEncoder;

import org.w3c.dom.Document;

import com.kcube.golaju.R;
import com.kcube.golaju.util.PropertyUtil;
import com.kcube.golaju.util.XmlUtil;
import com.kucbe.golaju.common.LoadingProgress;
import com.kucbe.golaju.common.NormalActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Join extends NormalActivity
{
	private AlertDialog mDialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.join);
		
		
		initSetting(this, false, false); // 공통적인 초기 새팅을 한다.
		
		
		
		
		Button doJoinBtn = (Button) findViewById(R.id.btn_doJoin);
		doJoinBtn.setOnClickListener(new View.OnClickListener() { //회원가입 버튼을 클릭했으면.
			@Override
			public void onClick(View v)
			{
				LoadingProgress.showLoadingDialog(Join.this);
				new Thread(new Runnable(){
					@Override
					public void run()
					{
						CheckBox isAgreements = (CheckBox) findViewById(R.id.chk_user_agreements);
						
						EditText idEdit = (EditText) findViewById(R.id.joinIdEdit);
						EditText pwEdit = (EditText) findViewById(R.id.joinPwEdit);
						EditText pwChkEdit = (EditText) findViewById(R.id.joinPwChkEdit);
						EditText emailEdit = (EditText) findViewById(R.id.joinEmailEdit);
						CheckBox getEmail = (CheckBox) findViewById(R.id.joinGetEmail);
						
						String id = idEdit.getText().toString();
						String pw = pwEdit.getText().toString();
						String pwChk = pwChkEdit.getText().toString();
						String email = emailEdit.getText().toString();
						boolean getMail = getEmail.isChecked();
						
						try
						{
							String url = PropertyUtil.getProperty("golaju.dbWork.url") + "/join.jsp?id=" + URLEncoder.encode(id, "UTF-8") + "&pw=" + URLEncoder.encode(pw, "UTF-8") + "&pwChk=" + URLEncoder.encode(pwChk, "UTF-8") + "&email=" + URLEncoder.encode(email, "UTF-8") + "&getMail=" + URLEncoder.encode(String.valueOf(getMail), "UTF-8") + "&isAgreements=" + URLEncoder.encode(String.valueOf(isAgreements.isChecked()), "UTF-8");
					            
				    		Document doc = XmlUtil.readXml(url);
				    		
				    		if (XmlUtil.isXPath(doc, "//Join_Success"))
				    		{
				    			boolean isSuccess = Boolean.parseBoolean(XmlUtil.getXPath(doc, "//Join_Success"));
				    			
				    			if(isSuccess){ //가입을 성공했다면.
		
				    				runOnUiThread(new Runnable(){
										@Override public void run(){
											Toast.makeText(Join.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
										}
				    				});
				    				
				    				Intent intent = new Intent(Join.this, AddJoinInfoGuide.class);
				    				Bundle bundle = new Bundle( ) ;
									bundle.putString("loginId", id) ;
									intent.putExtras(bundle) ;
									startActivity(intent);
									finish();
				    				
				    			}else{ //가입을 실패했다면.
				    				if (XmlUtil.isXPath(doc, "//Error_Message"))
				    				{	
				    					final String errorMsg = XmlUtil.getXPath(doc, "//Error_Message");
				    					runOnUiThread(new Runnable(){
											@Override public void run(){
												//가입 오류 메세지.
												Toast.makeText(Join.this, errorMsg, Toast.LENGTH_SHORT).show();
											}
				    					});
				    				}
				    			}
				    		}
						} catch (Exception e){
							e.printStackTrace();
						}
						LoadingProgress.getHandler().sendEmptyMessage(0);
					}
				}).start();
			} 
        });
		
		Button clauseBtn = (Button) findViewById(R.id.btn_user_agreements);
		clauseBtn.setOnClickListener(new View.OnClickListener() { //회원가입 버튼을 클릭했으면.
			@Override
			public void onClick(final View v)
			{
				runOnUiThread(new Runnable(){
					@Override public void run(){
						//알림 dialog
						mDialog = createDialog(v);
						mDialog.show();
					}
				});
			} 
        });
	}
	
	AlertDialog createDialog(View v) {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        //ab.setTitle("Title");
        ab.setMessage(getResources().getString(R.string.text_clause));
        ab.setCancelable(false);
        ab.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
          
        ab.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            	// 다이얼로그를 화면에서 사라지게 한다.
            	mDialog.dismiss();
            }
        });
        return ab.create();
    }
}
