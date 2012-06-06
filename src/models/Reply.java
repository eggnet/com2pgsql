package models;

public class Reply
{
	private int	FromItemID;
	private int	ToItemID;
	
	public Reply()
	{
		super();
	}

	public Reply(int fromItemID, int toItemID)
	{
		super();
		FromItemID = fromItemID;
		ToItemID = toItemID;
	}

	public int getFromItemID()
	{
		return FromItemID;
	}

	public void setFromItemID(int fromItemID)
	{
		FromItemID = fromItemID;
	}

	public int getToItemID()
	{
		return ToItemID;
	}

	public void setToItemID(int toItemID)
	{
		ToItemID = toItemID;
	}
}
