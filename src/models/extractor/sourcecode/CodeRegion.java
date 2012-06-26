/**
 * Slightly modified version of InfoZilla, for use 
 * by the Eggnet Social-Technical Network project.
 * 
 * Credit : InfoZilla Tool
 * <a href='http://groups.csail.mit.edu/pag/pubs/bettenburg-msr-2008.pdf'>
 * Extracting Structural Information From Bug Reports
 * </a>
 * @authors Nicolas Bettenburg, Rahul Premraj, Thomas Zimmermann, Sunghun Kim
 */
package models.extractor.sourcecode;

import models.Extraction;

public class CodeRegion extends Extraction implements Comparable<CodeRegion>
{
	public int		start	= 0;

	public int		end		= 0;
	public String	keyword;

	public CodeRegion(int start, int end, String keyword, String text)
	{
		super();
		this.start = start;
		this.end = end;
		this.keyword = keyword;
		this.text = text;
	}

	public CodeRegion(CodeRegion that)
	{
		super();
		this.start = Integer.valueOf(that.start).intValue();
		this.end = Integer.valueOf(that.end).intValue();
		this.keyword = new String(that.keyword);
		this.text = that.text;
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
