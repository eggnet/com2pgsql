package linker;

import db.ComDb;
import db.LinkerDb;

public abstract class Linker
{
	protected ComDb		comDb;
	protected LinkerDb	linkerDb;

	public Linker(ComDb comDb, LinkerDb linkerDb)
	{
		this.comDb = comDb;
		this.linkerDb = linkerDb;
	}
	
	/**
	 * Entry point for the linker to start running our linking algorithm from items->commits.
	 */
	public void Link() { }

	/**
	 * <p>Links {@link models.Commit} to {@link models.Issue} by parsing commit messages and <br>
	 * finding the bug numbers.</p>
	 */
	public void LinkFromCommitMessages() { }
	
	/**
	 * <p>Links {@link models.Item} to {@link models.Issue} by parsing the date of the item<br>
	 * and the date of commits linked to the Item's thread.  Then after finding a 'correct'<br>
	 * commit, the item->commit link get's placed in the {@link db.LinkerDb}.  This algorithm <br>
	 * is only performed on the items that are children of threads.<p>
	 */
	public void LinkFromThreadItems() { }
}
