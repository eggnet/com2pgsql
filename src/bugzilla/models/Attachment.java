package bugzilla.models;

import java.sql.Timestamp;



public class Attachment
{
	private User 		attacher;
	private int 		bug_id;
	private String		bug_ref;
	private Comment[]	comments;
	private Timestamp 	creation_time;
	private String		data;
	private String		description;
	private String		encoding;
	private String		file_name;
	private Flag[]		flags;
	private int			id;
	private boolean		is_obsolete;
	private boolean		is_patch;
	private boolean		is_private;
	private String		ref;
	private int			size;
	private String		update_token;
	private String 		content_type;
	
	public Attachment()
	{
		super();
	}

	public Attachment(User attacher, int bug_id, String bug_ref,
			Comment[] comments, Timestamp creation_time, String data,
			String description, String encoding, String file_name,
			Flag[] flags, int id, boolean is_obsolete, boolean is_patch,
			boolean is_private, String ref, int size, String update_token,
			String content_type)
	{
		super();
		this.attacher = attacher;
		this.bug_id = bug_id;
		this.bug_ref = bug_ref;
		this.comments = comments;
		this.creation_time = creation_time;
		this.data = data;
		this.description = description;
		this.encoding = encoding;
		this.file_name = file_name;
		this.flags = flags;
		this.id = id;
		this.is_obsolete = is_obsolete;
		this.is_patch = is_patch;
		this.is_private = is_private;
		this.ref = ref;
		this.size = size;
		this.update_token = update_token;
		this.content_type = content_type;
	}

	public User getAttacher()
	{
		return attacher;
	}

	public void setAttacher(User attacher)
	{
		this.attacher = attacher;
	}

	public int getBug_id()
	{
		return bug_id;
	}

	public void setBug_id(int bug_id)
	{
		this.bug_id = bug_id;
	}

	public String getBug_ref()
	{
		return bug_ref;
	}

	public void setBug_ref(String bug_ref)
	{
		this.bug_ref = bug_ref;
	}

	public Comment[] getComments()
	{
		return comments;
	}

	public void setComments(Comment[] comments)
	{
		this.comments = comments;
	}

	public Timestamp getCreation_time()
	{
		return creation_time;
	}

	public void setCreation_time(Timestamp creation_time)
	{
		this.creation_time = creation_time;
	}

	public String getData()
	{
		return data;
	}

	public void setData(String data)
	{
		this.data = data;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getEncoding()
	{
		return encoding;
	}

	public void setEncoding(String encoding)
	{
		this.encoding = encoding;
	}

	public String getFile_name()
	{
		return file_name;
	}

	public void setFile_name(String file_name)
	{
		this.file_name = file_name;
	}

	public Flag[] getFlags()
	{
		return flags;
	}

	public void setFlags(Flag[] flags)
	{
		this.flags = flags;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public boolean isIs_obsolete()
	{
		return is_obsolete;
	}

	public void setIs_obsolete(boolean is_obsolete)
	{
		this.is_obsolete = is_obsolete;
	}

	public boolean isIs_patch()
	{
		return is_patch;
	}

	public void setIs_patch(boolean is_patch)
	{
		this.is_patch = is_patch;
	}

	public boolean isIs_private()
	{
		return is_private;
	}

	public void setIs_private(boolean is_private)
	{
		this.is_private = is_private;
	}

	public String getRef()
	{
		return ref;
	}

	public void setRef(String ref)
	{
		this.ref = ref;
	}

	public int getSize()
	{
		return size;
	}

	public void setSize(int size)
	{
		this.size = size;
	}

	public String getUpdate_token()
	{
		return update_token;
	}

	public void setUpdate_token(String update_token)
	{
		this.update_token = update_token;
	}

	public String getContent_type()
	{
		return content_type;
	}

	public void setContent_type(String content_type)
	{
		this.content_type = content_type;
	}
}
