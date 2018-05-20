package com.kcube.golaju;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
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

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.kcube.golaju.data.Global;
import com.kcube.golaju.data.SaveDataAsLogin;
import com.kcube.golaju.login.Login;
import com.kcube.golaju.notification.Popup;
import com.kcube.golaju.util.PropertyUtil;

public class GCMIntentService extends GCMBaseIntentService {
 
	SaveDataAsLogin saveDataAsLogin = null;
	
	static String PROJECT_ID="804630958109";
    /**
     * GCM Server로부터 발급받은 Project ID를 통해 SuperClass인
     * GCMBaseIntentService를 생성해야한다. 
     */
    public GCMIntentService() {
        super(PROJECT_ID);
        // TODO Auto-generated constructor stub
    }
 
    @Override
    protected void onError(Context arg0, String arg1) {
        // TODO Auto-generated me
        /**
         * GCM 오류 발생 시 처리해야 할 코드를 작성한다.
         * ErrorCode에 대해선 GCM 홈페이지와 GCMConstants 내 static variable 참조한다. 
         */
    	Log.d("---GCM onError---",arg1);
 
    }
 
    @Override
    protected void onMessage(Context arg0, Intent arg1) {
        // TODO Auto-generated method stub
        /**
         * GCMServer가 전송하는 메시지가 정상 처리 된 경우 구현하는 메소드이다.
         * Notification, 앱 실행 등등 개발자가 하고 싶은 로직을 해당 메소드에서 구현한다.
         * 전달받은 메시지는 Intent.getExtras().getString(key)를 통해 가져올 수 있다.
         */
    	saveDataAsLogin = new SaveDataAsLogin(GCMIntentService.this);
    	
    	if(saveDataAsLogin.getPush()){
    		Log.d("---GCM onMessage---","start");
        	Bundle b = arg1.getExtras();

            Iterator<String> iterator = b.keySet().iterator();
            while(iterator.hasNext()) {
                String key = iterator.next();
                String value = b.get(key).toString();
                Log.d("---GCM onMessage 123---", "onMessage. "+key+" : "+value);
            }
            
            Log.d("메시지가 왔습니다 : ", arg1.getExtras().getString("ticker"));
            Log.d("메시지itemId : ", arg1.getExtras().getString("itemId"));

    		try {
				showMessage(arg0, arg1);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
 
    @Override
    protected void onRegistered(Context arg0, String regId) {
        // TODO Auto-generated method stub
        /**
         * GCMRegistrar.getRegistrationId(context)가 실행되어 registrationId를 발급받은 경우 해당 메소드가 콜백된다.
         * 메시지 발송을 위해 regId를 서버로 전송하도록 하자.
         */
    	Log.d("---GCM onRegistered---", "start");
    	//String posturl =  "http://www.oaasys.com/App/GCM/GCMrgstId.jsp";
    	String posturl = null;
    	Global userInfo = (Global) getApplication();
    	try {//이메일 체크 http연결
			HttpClient client = new DefaultHttpClient();
			posturl = PropertyUtil.getProperty("golaju.gcm.url") + "/GCMrgstId.jsp";
			HttpPost post = new HttpPost(posturl);
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			
			Log.d("---GCM onRegistered---", "userId : "+Long.toString(userInfo.getUserId()));
			
		    params.add(new BasicNameValuePair("userId", Long.toString(userInfo.getUserId())));
		    params.add(new BasicNameValuePair("regId", regId));
							 
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			post.setEntity(ent);
			 
			HttpResponse responsePost = client.execute(post);
			HttpEntity resEntity = responsePost.getEntity();
			String err = EntityUtils.toString(resEntity);
			Log.d("---GCM onRegistered---", "err : "+err);
		}catch (Exception e) {
			e.printStackTrace();
		}
    	
    	Log.d("---GCM onRegistered---", "regId : "+regId);
 
    }
 
    @Override
    protected void onUnregistered(Context arg0, String arg1) {
        // TODO Auto-generated method stub
        /**
         * GCMRegistrar.unregister(context) 호출로 해당 디바이스의 registrationId를 해지요청한 경우 해당 메소드가 콜백된다.
         */
    	Log.d("---GCM onUnregistered 삭제---", "삭제");
 
    }
    
    public class CommonUtils {
        /**
         * 인자값이 null이거나 trim의 결과가 ""인 경우 true를 리턴한다.
         * @param String
         * @return boolean
         */
        public boolean isEmpty(String value){
             
            boolean isEmpty = false;
             
            if((value == null) || value == null)
                isEmpty = true;
             
            return isEmpty;
        }
     
    }
    
    private void showMessage(Context context, Intent intent) throws UnsupportedEncodingException{
    	Log.d("----showMessage----", "start");
    	String title = URLDecoder.decode(intent.getStringExtra("title"), "UTF-8") ;
    	String msg = URLDecoder.decode(intent.getStringExtra("content"), "UTF-8") ;
    	String ticker = URLDecoder.decode(intent.getStringExtra("ticker"), "UTF-8") ;
    	String itemId = URLDecoder.decode(intent.getStringExtra("itemId"), "UTF-8") ;
    	
    	Log.d("인코딩된 제목", title);
    	Log.d("인코딩된 메세지", msg);
    	Log.d("인코딩된 ticker", ticker);
    	Log.d("인코딩된 itemId", itemId);
    	
    	saveDataAsLogin = new SaveDataAsLogin(GCMIntentService.this);

    	NotificationManager notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);

    	// 해당 어플을 실행하는 이벤트를 하고싶을 때 아래 주석을 풀어주세요
    	
    	Intent itemRead = new Intent(context, Login.class);
		
    	Bundle bundle = new Bundle() ;
		bundle.putLong("itemId", Long.parseLong(itemId));
		bundle.putString("list", "itemList");
		bundle.putString("pushCheck", "item");
		bundle.putString("listType", "notClosed");
		itemRead.putExtras(bundle) ;
		itemRead.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				|Intent.FLAG_ACTIVITY_NEW_TASK);
		/*itemRead.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				|Intent.FLAG_ACTIVITY_SINGLE_TOP
				|Intent.FLAG_ACTIVITY_NEW_TASK);*/
		
		int randomId = (int)(Math.random() * 1000000000 + 1);
		
		PendingIntent pendingIntent = PendingIntent.getActivity(context, randomId, 
				itemRead, PendingIntent.FLAG_ONE_SHOT);
		/*
    	PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, 
    	new Intent(context, ItemList.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), 0);
    	//PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(), 0);
		*/
    	Notification notification = new Notification();
    	notification.icon = R.drawable.ic_launcher;
    	notification.tickerText = ticker;
    	notification.when = System.currentTimeMillis();
    	
    	if(saveDataAsLogin.getVibrate()){
    		notification.vibrate = new long[] { 500, 100, 500, 100 };
    	} else if(saveDataAsLogin.getSound()){
    		notification.sound = Uri.parse("/system/media/audio/notifications/20_Cloud.ogg");
    	}
    	
    	notification.flags = Notification.FLAG_AUTO_CANCEL;
    	notification.setLatestEventInfo(context, title, msg, pendingIntent);

    	
    	notificationManager.notify(randomId, notification);
    	//notificationManager.notify(Integer.parseInt(MESSAGE_ID), notification);
    	
    	KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
    	
    	Intent itemRead2 = new Intent(context, Popup.class);
    	Bundle bundle2 = new Bundle() ;
		bundle2.putLong("itemId", Long.parseLong(itemId));
		bundle2.putString("title", ticker) ;
		itemRead2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				|Intent.FLAG_ACTIVITY_NEW_TASK);
		itemRead2.putExtras(bundle2) ;
		
		if(km.inKeyguardRestrictedInputMode()){
			startActivity(itemRead2);
    	}
    }
}
