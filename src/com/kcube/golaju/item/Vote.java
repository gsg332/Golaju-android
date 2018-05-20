package com.kcube.golaju.item;

public class Vote
{
	private Long pollId;
	private String voteText;
	private String originalPath;
	private String thumbPath;
	private Long size;
	private int voteCnt;
	private boolean selection;
	
	
	public Long getPollId()
	{
		return pollId;
	}
	public void setPollId(Long pollId)
	{
		this.pollId = pollId;
	}
	
	public String getVoteText()
	{
		return voteText;
	}
	public void setVoteText(String voteText)
	{
		this.voteText = voteText;
	}
	
	public String getOriginalPath()
	{
		return originalPath;
	}
	public void setOriginalPath(String originalPath)
	{
		this.originalPath = originalPath;
	}
	
	public String getThumbPath()
	{
		return thumbPath;
	}
	public void setThumbPath(String thumbPath)
	{
		this.thumbPath = thumbPath;
	}
	
	public Long getSize()
	{
		return size;
	}
	public void setSize(Long size)
	{
		this.size = size;
	}
	
	public int getVoteCnt()
	{
		return voteCnt;
	}
	public void setVoteCnt(int voteCnt)
	{
		this.voteCnt = voteCnt;
	}

	public boolean isSelection()
	{
		return selection;
	}
	public void setSelection(boolean selection)
	{
		this.selection = selection;
	}
	
}
