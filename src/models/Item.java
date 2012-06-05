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
	private int PId;
	private Timestamp Timestamp;
	private int ItemId;
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
		PId = pId;
		Timestamp = timestamp;
		ItemId = itemId;
		Body = body;
		Title = title;
		CommunicationType = communicationType;
	}
	
	public int getPId()
	{
		return PId;
	}
	public void setPId(int pId)
	{
		PId = pId;
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
		return ItemId;
	}
	public void setItemId(int itemId)
	{
		ItemId = itemId;
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
