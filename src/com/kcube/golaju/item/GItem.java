package com.kcube.golaju.item;

import java.util.Date;
import java.util.List;

public class GItem
{
	private Long userId;
	private String userName;
	private String loginId;
	private String userThumb;
	
	private String voteThumb; //목록에 보여줄 이미지.
	
	private Long itemId;
	private String title;
	private String content;
	private List<Vote> voteList;
	
	private Date timeLimit;
	private Integer peopleLimit;
	private Integer genderLimit;
	private String ageLimit;
	
	private boolean isClose;

	
	public GItem(){
		
	}
	 
	public GItem(Long itemId, String title, String voteThumb){
		this.voteThumb = voteThumb;
		this.itemId = itemId;
		this.title = title;
	}
	
	
	public Long getUserId()
	{
		return userId;
	}
	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getLoginId()
	{
		return loginId;
	}
	public void setLoginId(String loginId)
	{
		this.loginId = loginId;
	}

	public String getUserThumb()
	{
		return userThumb;
	}
	public void setUserThumb(String userThumb)
	{
		this.userThumb = userThumb;
	}

	public String getVoteThumb() {
		return voteThumb;
	}
	public void setVoteThumb(String voteThumb)
	{
		this.voteThumb = voteThumb;
	}

	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId)
	{
		this.itemId = itemId;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}

	public List<Vote> getVoteList()
	{
		return voteList;
	}
	public void setVoteList(List<Vote> voteList)
	{
		this.voteList = voteList;
	}

	public Date getTimeLimit()
	{
		return timeLimit;
	}
	public void setTimeLimit(Date timeLimit)
	{
		this.timeLimit = timeLimit;
	}

	public Integer getPeopleLimit()
	{
		return peopleLimit;
	}
	public void setPeopleLimit(Integer peopleLimit)
	{
		this.peopleLimit = peopleLimit;
	}

	public Integer getGenderLimit()
	{
		return genderLimit;
	}
	public void setGenderLimit(Integer genderLimit)
	{
		this.genderLimit = genderLimit;
	}

	public String getAgeLimit()
	{
		return ageLimit;
	}
	public void setAgeLimit(String ageLimit)
	{
		this.ageLimit = ageLimit;
	}

	public boolean isClose()
	{
		return isClose;
	}
	public void setClose(boolean isClose)
	{
		this.isClose = isClose;
	}
	
}
