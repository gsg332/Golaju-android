package com.kcube.golaju.user;

public class User
{
	private Long userId;
	private int gender;
	private int ageGroup;
	private String thumbPath;
	
	
	public User(Long userId, int gender, int ageGroup, String thumbPath){
		this.userId = userId;
		this.gender = gender;
		this.ageGroup = ageGroup;
		this.thumbPath = thumbPath;
 	}
	
	
	public Long getUserId()
	{
		return userId;
	}
	
	public int getGender()
	{
		return gender;
	}
	
	public int getAgeGroup()
	{
		return ageGroup;
	}
	
	public String getThumbPath()
	{
		return thumbPath;
	}
	
}
