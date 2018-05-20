package com.kcube.golaju.data;

import android.app.Application;

public class Global extends Application
{
	boolean login = false ;
	
	Long userId;
	String loginId;
	String loginPw;
	String userName;
	String jobCode;
	String thumbPath;
	String savePath;
	boolean admin;
	String email;
	int gender;
	int ageGroup;
	


	// app 실행 시 가장먼저 실행되는 부분 "데이터의 초기화 담당"
	@Override
	public void onCreate() {
		super.onCreate();
	}

	// app 종료 시 가장 마지막에 실행 "데이터 삭제 및 해제 담당"
	@Override
	public void onTerminate() {
	    super.onTerminate();
	}

	
	public boolean isLogin()
	{
		return login;
	}
	public void setLogin(boolean login)
	{
		this.login = login;
	}

	public Long getUserId()
	{
		return userId;
	}
	public void setUserId(Long userId)
	{
		this.userId = userId;
	}
	
	public String getLoginId()
	{
		return loginId;
	}
	public void setLoginId(String loginId)
	{
		this.loginId = loginId;
	}

	public String getLoginPw()
	{
		return loginPw;
	}
	public void setLoginPw(String loginPw)
	{
		this.loginPw = loginPw;
	}

	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getJobCode()
	{
		return jobCode;
	}
	public void setJobCode(String jobCode)
	{
		this.jobCode = jobCode;
	}

	public String getThumbPath()
	{
		return thumbPath;
	}
	public void setThumbPath(String thumbPath)
	{
		this.thumbPath = thumbPath;
	}

	public String getSavePath()
	{
		return savePath;
	}
	public void setSavePath(String savePath)
	{
		this.savePath = savePath;
	}

	public boolean isAdmin()
	{
		return admin;
	}
	public void setAdmin(boolean admin)
	{
		this.admin = admin;
	}

	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}

	public int getGender()
	{
		return gender;
	}
	public void setGender(int gender)
	{
		this.gender = gender;
	}

	public int getAgeGroup()
	{
		return ageGroup;
	}
	public void setAgeGroup(int ageGroup)
	{
		this.ageGroup = ageGroup;
	}
	
}
