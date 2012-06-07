package bugzilla.models;

public class User
{
	private String 	email;
	private int		id;
	private String	name;
	private String	real_name;
	private String	ref;
	
	public User()
	{
		super();
	}

	public User(String email, int id, String name, String real_name, String ref)
	{
		super();
		this.email = email;
		this.id = id;
		this.name = name;
		this.real_name = real_name;
		this.ref = ref;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
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

	public String getReal_name()
	{
		return real_name;
	}

	public void setReal_name(String real_name)
	{
		this.real_name = real_name;
	}

	public String getRef()
	{
		return ref;
	}

	public void setRef(String ref)
	{
		this.ref = ref;
	}
}
