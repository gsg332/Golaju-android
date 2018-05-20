package com.kcube.golaju.notification;

import com.kcube.golaju.R;
import com.kcube.golaju.login.Login;
import com.kucbe.golaju.common.NormalActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Popup extends NormalActivity implements OnClickListener{
	TextView golagu;
	TextView title;
	
	Button submit;
	Button cancle;
	
	Long itemId;
	String ttitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initSetting(this, false, false); // 공통적인 초기 새팅을 한다.
		
		
		Log.d("----popup onCreate----", "start");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		
		setContentView(R.layout.popup);
		Log.d("popup 액티비티2 ", "end");

		/*// 파워 매니저로 화면 깨우기.
	     PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
	     PowerManager.WakeLock mWakelock = pm.newWakeLock(
	     PowerManager.FULL_WAKE_LOCK |
	     PowerManager.ACQUIRE_CAUSES_WAKEUP |
	     PowerManager.ON_AFTER_RELEASE, "LODU");               
	   mWakelock.acquire();*/
	   
	   Log.d("popup 액티비티1 ", "end");
		
		golagu = (TextView)findViewById(R.id.popup_golagu);
		title = (TextView)findViewById(R.id.popup_title);
		
		submit = (Button)findViewById(R.id.popup_submit);
		cancle = (Button)findViewById(R.id.popup_cancle);
		submit.setOnClickListener(this);
		cancle.setOnClickListener(this);
		
		
		
		
		if(getIntent().hasExtra("title") && getIntent().hasExtra("itemId")){ // 게시글 목록에서 넘겨받은 title, itemId값이 있다면.
			ttitle = getIntent().getExtras().getString("title");
			itemId = getIntent().getExtras().getLong("itemId");
			title.setText(getIntent().getExtras().getString("title"));
		}else if(saveDataAsClosed.getLogin()){ // 메모리 문제로 인해 강제 종료된 경우를 대비하여 sharedData에 저장한 데이터로 값을 세팅한다.
    		String savedTitle = saveDataAsClosed.getTitle();
    		Long savedItemId = saveDataAsClosed.getItemId();
			if(savedTitle != null && savedItemId != null){
				ttitle = savedTitle;
				itemId = savedItemId;
				title.setText(savedTitle);
			}
    	}
		
		
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("----popup onResume----", "start");
				
		
		
		
		if(getIntent().hasExtra("title") && getIntent().hasExtra("itemId")){ // 게시글 목록에서 넘겨받은 title, itemId 값이 있다면.
			ttitle = getIntent().getExtras().getString("title");
			itemId = getIntent().getExtras().getLong("itemId");
			title.setText(getIntent().getExtras().getString("title"));
		}else if(saveDataAsClosed.getLogin()){ // 메모리 문제로 인해 강제 종료된 경우를 대비하여 sharedData에 저장한 데이터로 값을 세팅한다.
    		String savedTitle = saveDataAsClosed.getTitle();
    		Long savedItemId = saveDataAsClosed.getItemId();
			if(savedTitle != null && savedItemId != null){
				ttitle = savedTitle;
				itemId = savedItemId;
				title.setText(savedTitle);
			}
    	}

	}
	

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(arg0 == submit){
			Log.d("----popup submit----", "start");
			Log.d("----pushCheck itemId----", itemId + "");
			Intent itemRead = new Intent(Popup.this, Login.class);
			Bundle bundle = new Bundle() ;
			bundle.putLong("itemId", itemId) ;
			bundle.putString("list", "itemList") ;
			bundle.putString("pushCheck", "item") ;
			bundle.putString("listType", "notClosed");
			itemRead.putExtras(bundle) ;
			startActivity( itemRead ) ;
			Log.d("----popup submit----", "end");
			finish();
			Log.d("----popup finish----", "finish");
		}else if(arg0 == cancle){
			Log.d("----popup cancle----", "end");
			finish();
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent itemRead = new Intent(Popup.this, Login.class);
		Bundle bundle = new Bundle() ;
		bundle.putLong("itemId", itemId) ;
		bundle.putString("list", "itemList") ;
		bundle.putString("listType", "notClosed");
		bundle.putString("pushCheck", "list") ;
		itemRead.putExtras(bundle) ;
		startActivity( itemRead ) ;
		Log.d("----popup submit----", "end");
		finish();
		Log.d("----popup finish----", "finish");
	}
}
