package com.kcube.golaju.splash;

import com.kcube.golaju.R;
import com.kcube.golaju.item.ItemList;
import com.kcube.golaju.login.Login;
import com.kucbe.golaju.common.NormalActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

public class Splash extends NormalActivity
{
	protected void onCreate(android.os.Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		initSetting(this, false, false); // 공통적인 초기 새팅을 한다.
		
		
		// 2초 이후 Main 화면으로 이동한 뒤 Splash화면을 Finish 한다.
		Handler handler = new Handler();
		
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				//startActivity(new Intent(getApplication(), ItemList.class));
				Intent splash = new Intent(getApplication(), ItemList.class);
				Bundle bundle = new Bundle() ;
				bundle.putString("listType", "notClosed") ;
				splash.putExtras(bundle) ;
				startActivity( splash ) ;
				Splash.this.finish();
			}
		}, 1500);
	};

	
	public boolean onKeyDown( int KeyCode, KeyEvent event )
	{
		if( event.getAction() == KeyEvent.ACTION_DOWN ){ // 이 부분은 특정 키를 눌렀을때 실행 된다.
			if( KeyCode == KeyEvent.KEYCODE_BACK ){ // back버튼을 눌렀다면.
				
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_MAIN); // 실제 ACTION_MAIN로 지정되어 있는 액티비티가 finish()등으로 없어지면 남아 있는 액티비티에서 맨 처음 액티비지가 그 권한을 갖는다??
				intent.addCategory(Intent.CATEGORY_HOME);
				startActivity(intent);
				android.os.Process.killProcess(android.os.Process.myPid());
				
				return true;
			}
		}
	return super.onKeyDown( KeyCode, event );
	}
}
