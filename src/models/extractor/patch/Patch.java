/**
 * Slightly modified version of InfoZilla's Patch.java, for use 
 * by the Eggnet Social-Technical Network project.
 * 
 * Credit : InfoZilla Tool
 * <a href='http://groups.csail.mit.edu/pag/pubs/bettenburg-msr-2008.pdf'>
 * Extracting Structural Information From Bug Reports
 * </a>
 * @authors Nicolas Bettenburg, Rahul Premraj, Thomas Zimmermann, Sunghun Kim
 */
package models.extractor.patch;

import comm.RegExHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import models.Extraction;

public class Patch extends Extraction
{
	private String			index			= "";
	private String			originalFile	= "";
	private String			modifiedFile	= "";
	private String			header			= "";
	private int				startPosition;
	private int				endPosition;
	private List<PatchHunk>	hunks;

	public Patch()
	{
		this.hunks = new ArrayList();
		this.startPosition = 0;
		this.endPosition = 0;
	}

	public Patch(int s, int e)
	{
		this.hunks = new ArrayList();
		this.startPosition = s;
		this.endPosition = e;
	}

	public Patch(int s)
	{
		this.hunks = new ArrayList();
		this.startPosition = s;
	}

	public void addHunk(PatchHunk hunk)
	{
		this.hunks.add(hunk);
	}

	public String getIndex()
	{
		if (this.index.length() > 7)
		{
			return this.index.substring(7, this.index.length());
		}
		return this.index;
	}

	public void setIndex(String index)
	{
		this.index = index;
	}

	public String PlusMinusLineToFilename(String input)
	{
		String temp = input;
		String pmreg = "([-]{3}|[+]{3})([ \\r\\n\\t](.*?)[ \\t])";
		for (MatchResult r : RegExHelper.findMatches(Pattern.compile(pmreg, 8), input))
		{
			if (r.groupCount() > 1)
				temp = r.group(2).trim();
		}
		return temp;
	}

	public String getOriginalFile()
	{
		return PlusMinusLineToFilename(this.originalFile);
	}

	public void setOriginalFile(String originalFile)
	{
		this.originalFile = originalFile;
	}

	public String getModifiedFile()
	{
		return PlusMinusLineToFilename(this.modifiedFile);
	}

	public void setModifiedFile(String modifiedFile)
	{
		this.modifiedFile = modifiedFile;
	}

	public List<PatchHunk> getHunks()
	{
		return this.hunks;
	}

	public String toString()
	{
		String s = "";
		String lineSep = System.getProperty("line.separator");
		s = s + this.index + lineSep;
		s = s + "ORIGINAL=" + getOriginalFile() + lineSep;
		s = s + "MODIFIED=" + getModifiedFile() + lineSep;
		s = s + "#HUNKS=" + Integer.valueOf(this.hunks.size()) + lineSep;
		return s;
	}

	public String getHeader()
	{
		return this.header;
	}

	public void setHeader(String header)
	{
		this.header = header;
	}

	public int getStartPosition()
	{
		return this.startPosition;
	}

	public void setStartPosition(int startPosition)
	{
		this.startPosition = startPosition;
	}

	public int getEndPosition()
	{
		return this.endPosition;
	}

	public void setEndPosition(int endPosition)
	{
		this.endPosition = endPosition;
	}
}
