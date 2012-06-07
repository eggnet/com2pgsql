package bugzilla.models;

import java.sql.Timestamp;


public class ChangeSet
{
	private User		changer;
	private Change[]	changes;
	private Timestamp	change_time;
	
	public ChangeSet()
	{
		super();
	}

	public ChangeSet(User changer, Change[] changes, Timestamp change_time)
	{
		super();
		this.changer = changer;
		this.changes = changes;
		this.change_time = change_time;
	}

	public User getChanger()
	{
		return changer;
	}

	public void setChanger(User changer)
	{
		this.changer = changer;
	}

	public Change[] getChanges()
	{
		return changes;
	}

	public void setChanges(Change[] changes)
	{
		this.changes = changes;
	}

	public Timestamp getChange_time()
	{
		return change_time;
	}

	public void setChange_time(Timestamp change_time)
	{
		this.change_time = change_time;
	}
}
