package com.kcube.golaju.service;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.kcube.golaju.R;
import com.kcube.golaju.data.SaveDataAsLogin;
import com.kcube.golaju.util.PropertyUtil;
import com.kucbe.golaju.common.LoadingProgress;
import com.kucbe.golaju.common.NormalActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Service extends NormalActivity implements OnClickListener{

	WebView m_wv ;
	
	EditText mailContent;
	EditText mailTitle;
	EditText mailAddress;
	Button button;
	
	//메일 등록 url
	String posturl = null;
	//String posturl =  "http://125.140.114.140/App/DBLink/mail.jsp";
	//String posturl =  "http://125.140.114.107/App/DBLink/mail.jsp";
	
	private AlertDialog mDialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView( R.layout.service ) ;
		
		
		
		initSetting(this, true, false); // 공통적인 초기 새팅을 한다.
		
		
		
		
		
		
		button = (Button)findViewById( R.id.serviceSendButton ) ;
		mailTitle = (EditText)findViewById( R.id.email_title) ;
		mailContent = (EditText)findViewById( R.id.email_content) ;
		mailAddress = (EditText)findViewById( R.id.email_address) ;
		
		button.setOnClickListener( this ) ;
	}
	
	private AlertDialog createDialog(final View v) {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        //ab.setTitle("Title");
        ab.setMessage("메일이 성공적으로 발송되었습니다");
        ab.setCancelable(false);
        ab.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
          
        ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            	//Intent intent = new Intent(v.getContext(), ItemList.class);
            	//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				//startActivity(intent);
            	onBackPressed();
				finish();
            }
        });
        return ab.create();
    }
	
	@Override
	public void onClick(final View v) {

		LoadingProgress.showLoadingDialog(v.getContext());
		new Thread(new Runnable(){
			@Override
			public void run()
			{
				if(v == button){
					String title = mailTitle.getText().toString();
					String contents = mailContent.getText().toString();
					String mail = mailAddress.getText().toString();
					SaveDataAsLogin data = new SaveDataAsLogin(Service.this);
					
					if(title == null || "".equals(title)){
						runOnUiThread(new Runnable(){
							@Override public void run(){
								Toast.makeText(getBaseContext(), "메일 제목을 입력해주세요", Toast.LENGTH_SHORT).show();
							}
						});
					}else if(contents == null || "".equals(contents)){
						runOnUiThread(new Runnable(){
							@Override public void run(){
								Toast.makeText(getBaseContext(), "메일 내용을 입력해주세요", Toast.LENGTH_SHORT).show();
							}
						});
					}else if(mail == null || "".equals(mail)){
						runOnUiThread(new Runnable(){
							@Override public void run(){
								Toast.makeText(getBaseContext(), "메일을 입력해주세요", Toast.LENGTH_SHORT).show();
							}
						});
					}else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
						runOnUiThread(new Runnable(){
							@Override public void run(){
								Toast.makeText(getBaseContext(), "이메일 형식이 잘못되었습니다", Toast.LENGTH_SHORT).show();
							}
						});
					}
					else{
						try {//이메일 발송 http연결
							HttpClient client = new DefaultHttpClient();
							posturl = PropertyUtil.getProperty("golaju.dbWork.url") + "/mail.jsp";
							HttpPost post = new HttpPost(posturl);
							List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
							
							params.add(new BasicNameValuePair("title", title));
						    params.add(new BasicNameValuePair("content", contents));
						    params.add(new BasicNameValuePair("email", mail));
						    params.add(new BasicNameValuePair("loginid", data.getLoginId()));
						    params.add(new BasicNameValuePair("loginpw", data.getLoginPw()));
											 
							UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
							post.setEntity(ent);
							 
							HttpResponse responsePost = client.execute(post);
							final HttpEntity resEntity = responsePost.getEntity();
							String err = EntityUtils.toString(resEntity);
						}catch (Exception e) {
							e.printStackTrace();
						}
						
						runOnUiThread(new Runnable(){
							@Override public void run(){
								//알림 dialog
								mDialog = createDialog(v);
								mDialog.show();
							}
						});
					}
				}
				LoadingProgress.getHandler().sendEmptyMessage(0);
			}
		}).start();
	}
}
