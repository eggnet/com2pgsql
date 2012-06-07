package models;

import java.sql.Timestamp;

public class BugzillaTO
{
	private int			bug_id;
	private String		who;
	private String		who_name;
	private Timestamp 	bug_when;
	private String		thetext;
	private String		title;
	
	public BugzillaTO()
	{
		super();
	}

	public BugzillaTO(int bug_id, String who, String who_name,
			Timestamp bug_when, String thetext, String title)
	{
		super();
		this.bug_id = bug_id;
		this.who = who;
		this.who_name = who_name;
		this.bug_when = bug_when;
		this.thetext = thetext;
		this.title = title;
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

	public String getWho_name()
	{
		return who_name;
	}

	public void setWho_name(String who_name)
	{
		this.who_name = who_name;
	}

	public Timestamp getBug_when()
	{
		return bug_when;
	}

	public void setBug_when(Timestamp bug_when)
	{
		this.bug_when = bug_when;
	}

	public String getThetext()
	{
		return thetext;
	}

	public void setThetext(String thetext)
	{
		this.thetext = thetext;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}
}
