package models.extractor.sourcecode;

public class CodeRegion implements Comparable<CodeRegion>
{
	public int		start	= 0;

	public int		end		= 0;
	public String	text;
	public String	keyword;

	public CodeRegion(int start, int end, String keyword, String text)
	{
		this.start = start;
		this.end = end;
		this.keyword = keyword;
		this.text = text;
	}

	public CodeRegion(CodeRegion that)
	{
		this.start = Integer.valueOf(that.start).intValue();
		this.end = Integer.valueOf(that.end).intValue();
		this.keyword = new String(that.keyword);
		this.text = new String(that.text);
	}

	public int compareTo(CodeRegion that)
	{
		if (this.start < that.start)
			return -1;
		if (this.start > that.start)
			return 1;
		return 0;
	}
}
