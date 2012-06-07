package bugzilla.models.TO;

import java.sql.Timestamp;

public class BugzillaBugTO
{
	private int 		bug_id;
	private String		assigned_to;
	private String		bug_severity;
	private Timestamp	creation_ts;
	private String		reporter;
	private String		qa_contact;
	private String		keywords;
	
	public BugzillaBugTO()
	{
		super();
	}

	public BugzillaBugTO(int bug_id, String assigned_to, String bug_severity,
			Timestamp creation_ts, String reporter, String qa_contact,
			String keywords)
	{
		super();
		this.bug_id = bug_id;
		this.assigned_to = assigned_to;
		this.bug_severity = bug_severity;
		this.creation_ts = creation_ts;
		this.reporter = reporter;
		this.qa_contact = qa_contact;
		this.keywords = keywords;
	}

	public int getBug_id()
	{
		return bug_id;
	}

	public void setBug_id(int bug_id)
	{
		this.bug_id = bug_id;
	}

	public String getAssigned_to()
	{
		return assigned_to;
	}

	public void setAssigned_to(String assigned_to)
	{
		this.assigned_to = assigned_to;
	}

	public String getBug_severity()
	{
		return bug_severity;
	}

	public void setBug_severity(String bug_severity)
	{
		this.bug_severity = bug_severity;
	}

	public Timestamp getCreation_ts()
	{
		return creation_ts;
	}

	public void setCreation_ts(Timestamp creation_ts)
	{
		this.creation_ts = creation_ts;
	}

	public String getReporter()
	{
		return reporter;
	}

	public void setReporter(String reporter)
	{
		this.reporter = reporter;
	}

	public String getQa_contact()
	{
		return qa_contact;
	}

	public void setQa_contact(String qa_contact)
	{
		this.qa_contact = qa_contact;
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
