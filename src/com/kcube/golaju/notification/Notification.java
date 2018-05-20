package com.kcube.golaju.notification;

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
import com.kcube.golaju.data.Global;
import com.kcube.golaju.data.SaveDataAsLogin;
import com.kcube.golaju.util.PropertyUtil;
import com.kucbe.golaju.common.NormalActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.kcube.golaju.data.SaveDataAsLogin;

public class Notification extends NormalActivity{
	private ArrayList<NotiItem> notiList = null;
	private NotiAdapter nAdapter = null;
	String posturl =  null;
	
	SaveDataAsLogin saveDataAsLogin = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification);
		
		initSetting(this, true, false); // 공통적인 초기 새팅을 한다.
		
		ListView mListView = (ListView) findViewById(R.id.notiListView);
		notiList = new ArrayList<NotiItem>();
		notiList.add(new NotiItem("골라쥬 메일 알림",0));
		notiList.add(new NotiItem("골라쥬 PUSH 알림",1));
		notiList.add(new NotiItem("PUSH 진동 여부",2));
		
		nAdapter = new NotiAdapter(this, notiList);
		
		
		saveDataAsLogin = new SaveDataAsLogin(Notification.this);
		
		if(saveDataAsLogin.getMail()){
        	nAdapter.setChecked(0);
        }
        
        if(saveDataAsLogin.getPush()){
        	nAdapter.setChecked(1);
        }
        
        if(saveDataAsLogin.getVibrate()){
        	nAdapter.setChecked(2);
        }
		
        mListView.setAdapter(nAdapter);
		mListView.setOnItemClickListener(mItemClickListener);
	}
	
	// ListView 안에 Item을 클릭시에 호출되는 Listener
    private AdapterView.OnItemClickListener mItemClickListener = new
            AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub 
					nAdapter.setChecked(position);
					if(position == 0 && nAdapter.getChecked(position).equals("true")){
						saveDataAsLogin.setMail(true);
						MailCheck("1");
					}else if(position == 0 && nAdapter.getChecked(position).equals("false")){
						saveDataAsLogin.setMail(false);
						MailCheck("0");
					}else if(position == 1 && nAdapter.getChecked(position).equals("true")){
						saveDataAsLogin.setPush(true);
					}else if(position == 1 && nAdapter.getChecked(position).equals("false")){
						saveDataAsLogin.setPush(false);
						if(nAdapter.getChecked(2).equals("true")){
							nAdapter.setChecked(2);
							saveDataAsLogin.setVibrate(false);
						}
					}else if(position == 2 && nAdapter.getChecked(position).equals("true")){
						if(nAdapter.getChecked(1).equals("false")){
							nAdapter.setChecked(1);
						}
						saveDataAsLogin.setPush(true);
						saveDataAsLogin.setVibrate(true);
					}else if(position == 2 && nAdapter.getChecked(position).equals("false")){
						saveDataAsLogin.setVibrate(false);
					}
					saveDataAsLogin.editCommit();
		            // Data 변경시 호출 Adapter에 Data 변경 사실을 알려줘서 Update 함.
					nAdapter.notifyDataSetChanged();
				}
    };
    
    public void MailCheck(String check){
		try {//이메일 체크 http연결
			HttpClient client = new DefaultHttpClient();
			posturl = PropertyUtil.getProperty("golaju.gcm.url") + "/MailCheck.jsp";
			HttpPost post = new HttpPost(posturl);
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			
		    params.add(new BasicNameValuePair("userId", Long.toString(userInfo.getUserId())));
		    params.add(new BasicNameValuePair("emailCheck", check));
							 
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			post.setEntity(ent);
			 
			HttpResponse responsePost = client.execute(post);
			final HttpEntity resEntity = responsePost.getEntity();
			String err = EntityUtils.toString(resEntity);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
