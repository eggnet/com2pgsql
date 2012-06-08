package models;

import java.sql.Date;
import java.sql.Timestamp;

import comm.ComResources.CommType;

/**
 * Stores the record of an Item of communication
 * {@link Thread}
 * {@link Email}
 * {@link Commit}
 * {@link IM}
 * {@link IRC}
 * etc.
 * @author braden
 */
public class Item
{
	private int PID;
	private String person;
	private Timestamp ItemDate;
	private int ItemID;
	private String Body;
	private String Title;
	private CommType CommunicationType;

	/**
	 * @param pId
	 * @param timestamp
	 * @param itemId
	 * @param body
	 * @param title
	 * @param communicationType
	 */
	public Item(int pId, Timestamp timestamp, int itemId, String body, String title, CommType communicationType)
	{
		super();
		PID = pId;
		ItemDate = timestamp;
		ItemID = itemId;
		Body = body;
		Title = title;
		CommunicationType = communicationType;
	}
	
	public Item(String person, Timestamp itemDate, int itemID, String body,
			String title, CommType communicationType)
	{
		super();
		this.person = person;
		ItemDate = itemDate;
		ItemID = itemID;
		Body = body;
		Title = title;
		CommunicationType = communicationType;
	}

	public Item() { }

	public int getPId()
	{
		return PID;
	}
	public void setPId(int pId)
	{
		PID = pId;
	}
	public Timestamp getItemDate()
	{
		return ItemDate;
	}
	public void setItemDate(Timestamp timestamp)
	{
		ItemDate = timestamp;
	}
	public int getItemId()
	{
		return ItemID;
	}
	public void setItemId(int itemId)
	{
		ItemID = itemId;
	}
	public String getBody()
	{
		return Body;
	}
	public void setBody(String body)
	{
		Body = body;
	}
	public String getTitle()
	{
		return Title;
	}
	public void setTitle(String title)
	{
		Title = title;
	}
	public CommType getCommunicationType()
	{
		return CommunicationType;
	}
	public void setCommunicationType(CommType communicationType)
	{
		CommunicationType = communicationType;
	}

	public String getPerson()
	{
		return person;
	}

	public void setPerson(String person)
	{
		this.person = person;
	}
	
	
}
