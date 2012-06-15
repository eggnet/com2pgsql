/**
 * Slightly modified version InfoZilla, for use 
 * by the Eggnet Social-Technical Network project.
 * 
 * Credit : InfoZilla Tool
 * <a href='http://groups.csail.mit.edu/pag/pubs/bettenburg-msr-2008.pdf'>
 * Extracting Structural Information From Bug Reports
 * </a>
 * @authors Nicolas Bettenburg, Rahul Premraj, Thomas Zimmermann, Sunghun Kim
 */
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
