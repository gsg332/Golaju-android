package com.kcube.golaju.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SaveDataAsLogin
{
	private SharedPreferences pref = null;
	private SharedPreferences.Editor editor;
	
	
	public SaveDataAsLogin(Context activity) {
		pref = activity.getSharedPreferences("golajuAsLogin", Activity.MODE_PRIVATE);
		editor = pref.edit();
	}

	
	public String getLoginId() {
		return pref.getString("loginId", "");
	}
	public void setLoginId(String loginId) {
		editor.putString("loginId", loginId);
	}
	
	public String getLoginPw() {
		return pref.getString("loginPw", "");
	}
	public void setLoginPw(String loginPw) {
		editor.putString("loginPw", loginPw);
	}
	
	public boolean getAutoLogin() {
		return pref.getBoolean("autoLogin", false);
	}
	public void setAutoLogin(boolean autoLogin) {
		editor.putBoolean("autoLogin", autoLogin);
	}
	
	
	//////////알림설정
	public boolean getVibrate() {
		return pref.getBoolean("vibrate", false);
	}
	public void setVibrate(boolean vibrate) {
		editor.putBoolean("vibrate", vibrate);
	}
	
	public boolean getSound() {
		return pref.getBoolean("sound", false);
	}
	public void setSound(boolean sound) {
		editor.putBoolean("sound", sound);
	}
	
	public boolean getPush() {
		return pref.getBoolean("push", false);
	}
	public void setPush(boolean push) {
		editor.putBoolean("push", push);
	}
	
	public boolean getMail() {
		return pref.getBoolean("mail", false);
	}
	public void setMail(boolean mail) {
		editor.putBoolean("mail", mail);
	}
	
	////////처음실행 체크
	public boolean getFirstRun() {
		return pref.getBoolean("firstRun", false);
	}
	public void setFirstRun(boolean firstRun) {
		editor.putBoolean("firstRun", firstRun);
	}
	
	public boolean getClosedList() {
		return pref.getBoolean("closedList", false);
	}
	public void setClosedList(boolean closedList) {
		editor.putBoolean("closedList", closedList);
	}
	
	public void editCommit(){
		editor.commit();
	}
	
	
	
	
}
