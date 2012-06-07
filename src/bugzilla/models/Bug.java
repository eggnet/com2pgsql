package bugzilla.models;

import java.sql.Timestamp;



public class Bug
{
	private String			alias;
	private User			assigned_to;
	private Attachment[]	attachments;
	private int[]			blocks;
	private User[]			cc;
	private String			classification;
	private Comment[]		comments;
	private String			component;
	private Timestamp		creation_time;
	private User			creator;
	private int[]			depends_on;
	private int				dupe_of;
	private Flag[]			flags;
	private Group[]			groups;
	private ChangeSet[]		history;
	private int				id;
	private boolean			is_cc_accessible;
	private boolean			is_confirmed;
	private boolean			is_creator_accessible;
	private String[]		keywords;
	private Timestamp		last_change_time;
	private String			op_sys;
	private String			platform;
	private String			priority;
	private String			product;
	private User			qa_contact;
	private String			ref;
	private String			resolution;
	private String[]		see_also;
	private String			severity;
	private String			status;
	private String			summary;
	private String			target_milestone;
	private String			update_token;
	private String			url;
	private String			version;
	private String			whiteboard;
	
	public Bug()
	{
		super();
	}

	public Bug(String alias, User assigned_to, Attachment[] attachments,
			int[] blocks, User[] cc, String classification, Comment[] comments,
			String component, Timestamp creation_time, User creator,
			int[] depends_on, int dupe_of, Flag[] flags, Group[] groups,
			ChangeSet[] history, int id, boolean is_cc_accessible,
			boolean is_confirmed, boolean is_creator_accessible,
			String[] keywords, Timestamp last_change_time, String op_sys,
			String platform, String priority, String product, User qa_contact,
			String ref, String resolution, String[] see_also, String severity,
			String status, String summary, String target_milestone,
			String update_token, String url, String version, String whiteboard)
	{
		super();
		this.alias = alias;
		this.assigned_to = assigned_to;
		this.attachments = attachments;
		this.blocks = blocks;
		this.cc = cc;
		this.classification = classification;
		this.comments = comments;
		this.component = component;
		this.creation_time = creation_time;
		this.creator = creator;
		this.depends_on = depends_on;
		this.dupe_of = dupe_of;
		this.flags = flags;
		this.groups = groups;
		this.history = history;
		this.id = id;
		this.is_cc_accessible = is_cc_accessible;
		this.is_confirmed = is_confirmed;
		this.is_creator_accessible = is_creator_accessible;
		this.keywords = keywords;
		this.last_change_time = last_change_time;
		this.op_sys = op_sys;
		this.platform = platform;
		this.priority = priority;
		this.product = product;
		this.qa_contact = qa_contact;
		this.ref = ref;
		this.resolution = resolution;
		this.see_also = see_also;
		this.severity = severity;
		this.status = status;
		this.summary = summary;
		this.target_milestone = target_milestone;
		this.update_token = update_token;
		this.url = url;
		this.version = version;
		this.whiteboard = whiteboard;
	}

	public String getAlias()
	{
		return alias;
	}

	public void setAlias(String alias)
	{
		this.alias = alias;
	}

	public User getAssigned_to()
	{
		return assigned_to;
	}

	public void setAssigned_to(User assigned_to)
	{
		this.assigned_to = assigned_to;
	}

	public Attachment[] getAttachments()
	{
		return attachments;
	}

	public void setAttachments(Attachment[] attachments)
	{
		this.attachments = attachments;
	}

	public int[] getBlocks()
	{
		return blocks;
	}

	public void setBlocks(int[] blocks)
	{
		this.blocks = blocks;
	}

	public User[] getCc()
	{
		return cc;
	}

	public void setCc(User[] cc)
	{
		this.cc = cc;
	}

	public String getClassification()
	{
		return classification;
	}

	public void setClassification(String classification)
	{
		this.classification = classification;
	}

	public Comment[] getComments()
	{
		return comments;
	}

	public void setComments(Comment[] comments)
	{
		this.comments = comments;
	}

	public String getComponent()
	{
		return component;
	}

	public void setComponent(String component)
	{
		this.component = component;
	}

	public Timestamp getCreation_time()
	{
		return creation_time;
	}

	public void setCreation_time(Timestamp creation_time)
	{
		this.creation_time = creation_time;
	}

	public User getCreator()
	{
		return creator;
	}

	public void setCreator(User creator)
	{
		this.creator = creator;
	}

	public int[] getDepends_on()
	{
		return depends_on;
	}

	public void setDepends_on(int[] depends_on)
	{
		this.depends_on = depends_on;
	}

	public int getDupe_of()
	{
		return dupe_of;
	}

	public void setDupe_of(int dupe_of)
	{
		this.dupe_of = dupe_of;
	}

	public Flag[] getFlags()
	{
		return flags;
	}

	public void setFlags(Flag[] flags)
	{
		this.flags = flags;
	}

	public Group[] getGroups()
	{
		return groups;
	}

	public void setGroups(Group[] groups)
	{
		this.groups = groups;
	}

	public ChangeSet[] getHistory()
	{
		return history;
	}

	public void setHistory(ChangeSet[] history)
	{
		this.history = history;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public boolean isIs_cc_accessible()
	{
		return is_cc_accessible;
	}

	public void setIs_cc_accessible(boolean is_cc_accessible)
	{
		this.is_cc_accessible = is_cc_accessible;
	}

	public boolean isIs_confirmed()
	{
		return is_confirmed;
	}

	public void setIs_confirmed(boolean is_confirmed)
	{
		this.is_confirmed = is_confirmed;
	}

	public boolean isIs_creator_accessible()
	{
		return is_creator_accessible;
	}

	public void setIs_creator_accessible(boolean is_creator_accessible)
	{
		this.is_creator_accessible = is_creator_accessible;
	}

	public String[] getKeywords()
	{
		return keywords;
	}

	public void setKeywords(String[] keywords)
	{
		this.keywords = keywords;
	}

	public Timestamp getLast_change_time()
	{
		return last_change_time;
	}

	public void setLast_change_time(Timestamp last_change_time)
	{
		this.last_change_time = last_change_time;
	}

	public String getOp_sys()
	{
		return op_sys;
	}

	public void setOp_sys(String op_sys)
	{
		this.op_sys = op_sys;
	}

	public String getPlatform()
	{
		return platform;
	}

	public void setPlatform(String platform)
	{
		this.platform = platform;
	}

	public String getPriority()
	{
		return priority;
	}

	public void setPriority(String priority)
	{
		this.priority = priority;
	}

	public String getProduct()
	{
		return product;
	}

	public void setProduct(String product)
	{
		this.product = product;
	}

	public User getQa_contact()
	{
		return qa_contact;
	}

	public void setQa_contact(User qa_contact)
	{
		this.qa_contact = qa_contact;
	}

	public String getRef()
	{
		return ref;
	}

	public void setRef(String ref)
	{
		this.ref = ref;
	}

	public String getResolution()
	{
		return resolution;
	}

	public void setResolution(String resolution)
	{
		this.resolution = resolution;
	}

	public String[] getSee_also()
	{
		return see_also;
	}

	public void setSee_also(String[] see_also)
	{
		this.see_also = see_also;
	}

	public String getSeverity()
	{
		return severity;
	}

	public void setSeverity(String severity)
	{
		this.severity = severity;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getSummary()
	{
		return summary;
	}

	public void setSummary(String summary)
	{
		this.summary = summary;
	}

	public String getTarget_milestone()
	{
		return target_milestone;
	}

	public void setTarget_milestone(String target_milestone)
	{
		this.target_milestone = target_milestone;
	}

	public String getUpdate_token()
	{
		return update_token;
	}

	public void setUpdate_token(String update_token)
	{
		this.update_token = update_token;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public String getWhiteboard()
	{
		return whiteboard;
	}

	public void setWhiteboard(String whiteboard)
	{
		this.whiteboard = whiteboard;
	}
}
