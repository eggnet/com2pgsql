package linker;

import java.util.List;

import models.Commit;
import models.Issue;
import models.Item;

import comm.ComResources;

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
	public void LinkFromThreadItems() { 
		int offset = 0;
		List<Issue> issues = comDb.getIssues(ComResources.DB_LIMIT, offset);
		
		// Batch selecting
		for(;;) {
			for(Issue issue: issues) {
				System.out.println("Linking items for issue: " + issue.getIssueNum());
				List<String> commitIDs = comDb.getCommitIDForIssue(issue);
				List<Item> items = comDb.getItemsInThread(issue.getItemID());
				if(commitIDs.isEmpty() || items.isEmpty())
					continue;
				for(Item item: items) {
					for(String commitID: commitIDs) {
						Commit commit = linkerDb.getCommit(commitID);
						if(commit == null)
							continue;
						if(item.getItemDate().before(commit.getCommit_date())) {
							comDb.insertLink(new models.Link(item.getItemId(), commit.getCommit_id(), 1.0f));
						}
					}
				}
			}
			
			if(issues.size() < ComResources.DB_LIMIT)
				break;
			else {
				offset += ComResources.DB_LIMIT;
				issues = comDb.getIssues(ComResources.DB_LIMIT, offset);
			}
		}
	}
}
