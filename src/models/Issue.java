package models;

import java.sql.Timestamp;

public class Issue
{
	private int itemID;
	private String status;
	private int assignedID;
	private Timestamp creationTS;
	private Timestamp lastModifiedTS;
	private String title;
	private String description;
	private int creatorID;
	private String keywords;
	
	public Issue()
	{
		super();
	}

	public Issue(int itemID, String status, int assignedID,
			Timestamp creationTS, Timestamp lastModifiedTS, String title,
			String description, int creatorID, String keywords)
	{
		super();
		this.itemID = itemID;
		this.status = status;
		this.assignedID = assignedID;
		this.creationTS = creationTS;
		this.lastModifiedTS = lastModifiedTS;
		this.title = title;
		this.description = description;
		this.creatorID = creatorID;
		this.keywords = keywords;
	}

	public int getItemID()
	{
		return itemID;
	}

	public void setItemID(int itemID)
	{
		this.itemID = itemID;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public int getAssignedID()
	{
		return assignedID;
	}

	public void setAssignedID(int assignedID)
	{
		this.assignedID = assignedID;
	}

	public Timestamp getCreationTS()
	{
		return creationTS;
	}

	public void setCreationTS(Timestamp creationTS)
	{
		this.creationTS = creationTS;
	}

	public Timestamp getLastModifiedTS()
	{
		return lastModifiedTS;
	}

	public void setLastModifiedTS(Timestamp lastModifiedTS)
	{
		this.lastModifiedTS = lastModifiedTS;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public int getCreatorID()
	{
		return creatorID;
	}

	public void setCreatorID(int creatorID)
	{
		this.creatorID = creatorID;
	}

	public String getKeywords()
	{
		return keywords;
	}

	public void setKeywords(String keywords)
	{
		this.keywords = keywords;
	}
}
