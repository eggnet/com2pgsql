package models;

import java.sql.Timestamp;

import com.ComResources.CommType;

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
	private Timestamp Timestamp;
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
		Timestamp = timestamp;
		ItemID = itemId;
		Body = body;
		Title = title;
		CommunicationType = communicationType;
	}
	
	public int getPId()
	{
		return PID;
	}
	public void setPId(int pId)
	{
		PID = pId;
	}
	public Timestamp getTimestamp()
	{
		return Timestamp;
	}
	public void setTimestamp(Timestamp timestamp)
	{
		Timestamp = timestamp;
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
}
