package com.kucbe.golaju.common;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class LoadingProgress extends Application
{
	static ProgressDialog loadingDialog;
	static Handler handler;
	

	static
	{
		handler = new Handler(){
			public void handleMessage(Message msg){
				loadingDialog.dismiss();
				
				Log.i("로딩 완료", loadingDialog + "");
			}
		};
	}
	
	static public void settingLoadingDialog(Activity act){
		loadingDialog = new ProgressDialog(act) ;
		loadingDialog.setMessage("로딩중 입니다. , 기다려 주세요...") ;
		loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	}

	static public void showLoadingDialog(Context context){
		if(loadingDialog != null){
			loadingDialog.dismiss();
		}
		loadingDialog = ProgressDialog.show(context, null, "로딩 중입니다. 기다려 주세요...");
		
		//loadingDialog.show();
	}
	
	static public void dismissLoadingDialog(){
		loadingDialog.dismiss();
	}
	
	static public Handler getHandler(){
		return handler;
	}
	
}
