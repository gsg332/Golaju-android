package com.kcube.golaju.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SaveDataAsClosed
{

	
	
	private SharedPreferences pref = null;
	private SharedPreferences.Editor editor;
	
	
	public SaveDataAsClosed(Context activity) {
		pref = activity.getSharedPreferences("golajuAsClosed", Activity.MODE_PRIVATE);
		editor = pref.edit();
	}
	

	
	
	public boolean getLogin(){
		return pref.getBoolean("login", false);
	}
	public void setLogin(boolean login){
		editor.putBoolean("login", login);
	}
	
	public Long getUserId() {
		return pref.getLong("userId", 0);
	}
	public void setUserId(Long userId) {
		if(userId != null){
			editor.putLong("userId", userId);
		}
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

	
	
	public String getUserName() {
		return pref.getString("userName", "");
	}
	public void setUserName(String userName) {
		editor.putString("userName", userName);
	}
	
	public String getJobCode() {
		return pref.getString("jobCode", "");
	}
	public void setJobCode(String jobCode) {
		editor.putString("JobCode", jobCode);
	}
	
	
	public String getThumbPath() {
		return pref.getString("thumbPath", "");
	}
	public void setThumbPath(String thumbPath) {
		editor.putString("thumbPath", thumbPath);
	}
	
	
	
	public String getSavePath() {
		return pref.getString("thumbPath", "");
	}
	public void setSavePath(String savePath) {
		editor.putString("savePath", savePath);
	}
	
	
	
	public boolean getAdmin() {
		return pref.getBoolean("admin", false);
	}
	public void setAdmin(boolean admin) {
		editor.putBoolean("admin", admin);
	}
	
	
	
	public String getEmail() {
		return pref.getString("email", "");
	}
	public void setEmail(String email) {
		editor.putString("email", email);
	}
	
	
	public int getGender() {
		return pref.getInt("gender", 0);
	}
	public void setGender(int gender) {
		editor.putInt("gender", gender);
	}
	
	
	public int getAgeGroup() {
		return pref.getInt("ageGroup", 0);
	}
	public void setAgeGroup(int ageGroup) {
		editor.putInt("ageGroup", ageGroup);
	}
	
	
	
	
	
	
	
	
	
	
	public Long getItemId() {
		return pref.getLong("itemId", 0);
	}
	public void setItemId(Long itemId) {
		editor.putLong("itemId", itemId);
	}
	
	public String getTitle() {
		return pref.getString("title", "");
	}
	public void setTitle(String title) {
		editor.putString("title", title);
	}
	
	
	public String getContent() {
		return pref.getString("content", "");
	}
	public void setContent(String content) {
		editor.putString("content", content);
	}
	
	public String getList() {
		return pref.getString("content", "");
	}
	public void setList(String list) {
		editor.putString("list", list);
	}
	
	
	
	

	public void editCommit() {
		editor.commit();
	}
	
	
	public void editClear(){
		editor.clear();
	}
}
