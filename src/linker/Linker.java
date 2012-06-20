package linker;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.Commit;
import models.CommitFamily;
import models.Extraction;
import models.Issue;
import models.Item;

import comm.ComResources;

import db.ComDb;
import db.LinkerDb;
import db.Resources;
import extractor.Extractor;
import models.extractor.stacktrace.StackTrace;
import models.extractor.patch.Patch;
import models.extractor.sourcecode.CodeRegion;

public abstract class Linker
{
	public class LinkedExtraction {
		public float Confidence;
		public Commit commit;
		public LinkedExtraction(float confidence, Commit commit)
		{
			this.Confidence = confidence;
			this.commit = commit;
		}
	}
	
	protected ComDb		comDb;
	protected LinkerDb	linkerDb;
	protected Extractor extractor;

	public Linker(ComDb comDb, LinkerDb linkerDb)
	{
		this.comDb = comDb;
		this.linkerDb = linkerDb;
		this.extractor = new Extractor();
	}
	
	/**
	 * Entry point for the linker to start running our linking algorithm from items->commits.
	 */
	public void Link() {
		if (ComResources.DEBUG)
			comDb.deleteLinks();
	}

	/**
	 * <p>Links {@link models.Commit} to {@link models.Issue} by parsing commit messages and <br>
	 * finding the bug numbers.</p>
	 */
	public void LinkFromCommitMessages() { }
	
	/**
	 * <p>Links {@link models.Item} to {@link models.Commit} by parsing through the data
	 * <br> in the Item's body and Summary to give {@link models.Extraction} objects that
	 * <br> can be linked to Commits.</p>
	 */
	public void LinkFromItems() 
	{
		List<Item> items = comDb.getAllItems();
		for(Item i : items)
		{
			List<Extraction> keys = extractor.ExtractKeys(i);
			for (Extraction extraction : keys)
			{
				Resources.log(extraction.getClass().toString());
				
				if (extraction instanceof StackTrace)
				{
					Set<LinkedExtraction> relevantCommitsByFiles = GetRelevantCommitsForFiles(((StackTrace) extraction).getFilenames(), i.getItemDate());
					for (LinkedExtraction linkedE : relevantCommitsByFiles)
					{
						comDb.insertLink(new models.Link(i.getItemId(), linkedE.commit.getCommit_id(), linkedE.Confidence));
					}
				}
				else if (extraction instanceof CodeRegion)
				{
					
					//TODO @bradens
				}
				else if (extraction instanceof Patch)
				{
					
					//TODO @bradens
				}
			}
		}
	}
	
	public Set<LinkedExtraction> GetRelevantCommitsByCodeRegion(CodeRegion region, Timestamp date)
	{
		Set<LinkedExtraction> results = new HashSet<LinkedExtraction>();
		
		return results;
	}
	
	public Set<LinkedExtraction> GetRelevantCommitByPatch(Patch patch, Timestamp date)
	{
		Set<LinkedExtraction> results = new HashSet<LinkedExtraction>();
		
		return results;
	}
	
	/**
	 * Returns a list of commits and their confidence that are linked to a given extraction by 
	 * the files given from that extraction.  Uses algorithm (matchCount / totalChangedFiles) to get confidence
	 * @param filenames
	 * @param date
	 * @return
	 */
	public Set<LinkedExtraction> GetRelevantCommitsForFiles(List<String> filenames, Timestamp date)
	{
		Set<LinkedExtraction> results = new HashSet<LinkedExtraction>();
		List<Commit> commitsAroundItem = linkerDb.getCommitsAroundDate(date);
		for (Commit commit : commitsAroundItem)
		{
			List<String> filesChangedAtCommit = linkerDb.getFilesChangedOnCommit(commit);
			int matchCount = 0;
			for (String fileInExtraction: filenames)
			{
				if (filesChangedAtCommit.contains(fileInExtraction))
					matchCount++;
			}
			if (matchCount > 0)
			{
				results.add(new LinkedExtraction((matchCount / filesChangedAtCommit.size()), commit));
				Resources.log("Match found %s: %d", commit.getComment(), (matchCount / filesChangedAtCommit.size()));
			}
		}
		return results;
	}
	
	/**
	 * <p>Links {@link models.Item} to {@link models.Issue} by parsing the date of the item<br>
	 * and the date of commits linked to the Item's thread.  Then after finding a 'correct'<br>
	 * commit, the item->commit link get's placed in the {@link db.LinkerDb}.  This algorithm <br>
	 * is only performed on the items that are children of threads.<p>
	 */
	public void LinkFromIssueThreadItems() { 
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
				String rawFile = linkerDb.getRawFileFromDiffTree(file, commit.getCommit_id(), linkerDb.getCommitPathToRoot(commit.getCommit_id()));
				if(rawFile.contains(snippet))
					return file;
			}
		}
		// Look for snippet in all the files using substring matching and cut off
		else {
			for(String file: files) {
				String rawFile = linkerDb.getRawFileFromDiffTree(file, commit.getCommit_id(), linkerDb.getCommitPathToRoot(commit.getCommit_id()));
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
