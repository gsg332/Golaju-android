package com.kucbe.golaju.common;

import java.lang.Thread.UncaughtExceptionHandler;

import com.kcube.golaju.login.Login;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

/*
 * try catch처리를 하지 않은 모든 예외처리를 위해 사용.
 */
public class GlobalException implements UncaughtExceptionHandler 
{
	Activity act;
	
	BroadcastReceiver broadcastReceiver = null;
	
	public GlobalException(Activity act){
		this.act = act;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex)
	{
		


		
		/*
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		act.startActivity(intent);
		act.moveTaskToBack(true);
		android.os.Process.killProcess(android.os.Process.myPid());*/
		
		
		/*act.moveTaskToBack(true);

        act.finish();

        android.os.Process.killProcess(android.os.Process.myPid());
*/
		
		
		
		/*Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MAIN); // 실제 ACTION_MAIN로 지정되어 있는 액티비티가 finish()등으로 없어지면 남아 있는 액티비티에서 맨 처음 액티비지가 그 권한을 갖는다??
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
		android.os.Process.killProcess(android.os.Process.myPid());*/
		
		
		/*int sdkVersion = Integer.parseInt(Build.VERSION.SDK);	
		if (sdkVersion < 8)
		{		
			//#2. if we can use restartPackage method, just use it.		
			ActivityManager am = (ActivityManager) act.getSystemService(Context.ACTIVITY_SERVICE);		
			am.restartPackage(act.getPackageName());
		}
		else
		{		
			
			Intent intent = new Intent(act, Login.class) ;
			Bundle bundle = new Bundle( ) ;
			bundle.putString("flag", "finish") ;
			intent.putExtras(bundle) ;
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
			act.startActivity(intent) ;
			//act.finish() ;
			System.exit(0)
			Log.i( "==== Messge === " , "goto killActivity" ) ;
		}*/

		
		
	}
	
	
	
	

}
