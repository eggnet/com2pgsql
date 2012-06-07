package bugzilla.models;

public class Change
{
	private String		added;
	private String		file_name;
	private String		removed;
	
	public Change()
	{
		super();
	}

	public Change(String added, String file_name, String removed)
	{
		super();
		this.added = added;
		this.file_name = file_name;
		this.removed = removed;
	}

	public String getAdded()
	{
		return added;
	}

	public void setAdded(String added)
	{
		this.added = added;
	}

	public String getFile_name()
	{
		return file_name;
	}

	public void setFile_name(String file_name)
	{
		this.file_name = file_name;
	}

	public String getRemoved()
	{
		return removed;
	}

	public void setRemoved(String removed)
	{
		this.removed = removed;
	}
}
