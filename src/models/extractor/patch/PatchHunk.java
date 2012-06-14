package models.extractor.patch;

public class PatchHunk
{
	private String	text;

	public PatchHunk()
	{
		this.text = "";
	}

	public PatchHunk(String text)
	{
		this.text = text;
	}

	public String getText()
	{
		return this.text;
	}
}
