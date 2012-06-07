package bugzilla.models.TO;

public class BugzillaCCTO
{
	private int		bug_id;
	private String	who;
	
	public BugzillaCCTO()
	{
		super();
	}

	public BugzillaCCTO(int bug_id, String who)
	{
		super();
		this.bug_id = bug_id;
		this.who = who;
	}

	public int getBug_id()
	{
		return bug_id;
	}

	public void setBug_id(int bug_id)
	{
		this.bug_id = bug_id;
	}

	public String getWho()
	{
		return who;
	}

	public void setWho(String who)
	{
		this.who = who;
	}
}
