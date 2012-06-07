package bugzilla.models;


public class Flag
{
	private int		id;
	private String	name;
	private String	requestee;
	private User	setter;
	private String	status;
	private int		type_id;
	
	public Flag()
	{
		super();
	}

	public Flag(int id, String name, String requestee, User setter,
			String status, int type_id)
	{
		super();
		this.id = id;
		this.name = name;
		this.requestee = requestee;
		this.setter = setter;
		this.status = status;
		this.type_id = type_id;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getRequestee()
	{
		return requestee;
	}

	public void setRequestee(String requestee)
	{
		this.requestee = requestee;
	}

	public User getSetter()
	{
		return setter;
	}

	public void setSetter(User setter)
	{
		this.setter = setter;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public int getType_id()
	{
		return type_id;
	}

	public void setType_id(int type_id)
	{
		this.type_id = type_id;
	}
}
