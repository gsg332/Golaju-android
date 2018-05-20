package com.kcube.golaju.item;

import android.view.View;

public class LimitOption
{
	private View _timeLimit;
	private View _peopleLimit;
	private View _genderLimit;
	private View _ageGroupLimit;
	
	
	public View getTimeLimit()
	{
		return _timeLimit;
	}
	public void setTimeLimit(View timeLimit)
	{
		_timeLimit = timeLimit;
	}
	
	public View getPeopleLimit()
	{
		return _peopleLimit;
	}
	public void setPeopleLimit(View peopleLimit)
	{
		_peopleLimit = peopleLimit;
	}
	
	public View getGenderLimit()
	{
		return _genderLimit;
	}
	public void setGenderLimit(View genderLimit)
	{
		_genderLimit = genderLimit;
	}
	
	public View getAgeGroupLimit()
	{
		return _ageGroupLimit;
	}
	public void setAgeGroupLimit(View ageGroupLimit)
	{
		_ageGroupLimit = ageGroupLimit;
	}
	
}
