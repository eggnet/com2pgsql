package bugzilla.models;

import java.sql.Timestamp;


public class Comment
{
	private int 		attachment_id;
	private String		attachment_ref;
	private User		creator;
	private Timestamp	creation_time;
	private int 		id;
	private boolean		is_private;
	private String		text;
	public Comment()
	{
		super();
	}
	public Comment(int attachment_id, String attachment_ref, User creator,
			Timestamp creation_time, int id, boolean is_private, String text)
	{
		super();
		this.attachment_id = attachment_id;
		this.attachment_ref = attachment_ref;
		this.creator = creator;
		this.creation_time = creation_time;
		this.id = id;
		this.is_private = is_private;
		this.text = text;
	}
	public int getAttachment_id()
	{
		return attachment_id;
	}
	public void setAttachment_id(int attachment_id)
	{
		this.attachment_id = attachment_id;
	}
	public String getAttachment_ref()
	{
		return attachment_ref;
	}
	public void setAttachment_ref(String attachment_ref)
	{
		this.attachment_ref = attachment_ref;
	}
	public User getCreator()
	{
		return creator;
	}
	public void setCreator(User creator)
	{
		this.creator = creator;
	}
	public Timestamp getCreation_time()
	{
		return creation_time;
	}
	public void setCreation_time(Timestamp creation_time)
	{
		this.creation_time = creation_time;
	}
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public boolean isIs_private()
	{
		return is_private;
	}
	public void setIs_private(boolean is_private)
	{
		this.is_private = is_private;
	}
	public String getText()
	{
		return text;
	}
	public void setText(String text)
	{
		this.text = text;
	}
}
