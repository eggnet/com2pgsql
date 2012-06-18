package linker;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import models.Commit;
import models.CommitFamily;
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
	
	public String findSnippetFile(String snippet, Timestamp date, boolean exactMatchOnly) {
		Commit commit = linkerDb.getCommitAroundDate(date);
		List<CommitFamily> commits = linkerDb.getCommitPathToRoot(commit.getCommit_id());
		List<CommitFamily> path = reversePath(commits);
		List<String> files = new LinkedList<String>();
		
		// Build all file names in project at this point
		if(path.isEmpty()) {
			// It's the initial commit
			files.addAll(linkerDb.getFilesAdded(commit.getCommit_id()));
			files.removeAll(linkerDb.getFilesDeleted(commit.getCommit_id()));
		}
		
		for(CommitFamily commitF: path) {
			files.addAll(linkerDb.getFilesAdded(commitF.getChildId()));
			files.removeAll(linkerDb.getFilesDeleted(commitF.getChildId()));
		}
		
		// Look for snippet in all the files using exact matches
		if(exactMatchOnly) {
			for(String file: files) {
				String rawFile = linkerDb.getRawFileFromDiffTree(file, commit.getCommit_id());
				if(rawFile.contains(snippet))
					return file;
			}
		}
		// Look for snippet in all the files using substring matching and cut off
		else {
			for(String file: files) {
				String rawFile = linkerDb.getRawFileFromDiffTree(file, commit.getCommit_id());
				int match = longestSubstr(file, snippet);
				float matchPercent = (float)((float)match/(float)snippet.length());
				
				if(matchPercent > ComResources.STRING_MATCHING_THRESHOLD)
					return file;
			}
		}
		
		return null;
	}
	
	private List<CommitFamily> reversePath(List<CommitFamily> path) {
		List<CommitFamily> returnPath = new ArrayList<CommitFamily>();
		
		for(CommitFamily CF: path)
			returnPath.add(0, CF);
		
		return returnPath;
	}
	
	private int longestSubstr(String first, String second) {
	    if (first == null || second == null || first.length() == 0 || second.length() == 0)
	        return 0;
	 
	    int maxLen = 0;
	    int fl = first.length();
	    int sl = second.length();
	    int[][] table = new int[fl][sl];
	 
	    for (int i = 0; i < fl; i++) {
	        for (int j = 0; j < sl; j++) {
	            if (first.charAt(i) == second.charAt(j)) {
	                if (i == 0 || j == 0) {
	                    table[i][j] = 1;
	                }
	                else {
	                    table[i][j] = table[i - 1][j - 1] + 1;
	                }
	                if (table[i][j] > maxLen) {
	                    maxLen = table[i][j];
	                }
	            }
	        }
	    }
	    return maxLen;
	}
}
