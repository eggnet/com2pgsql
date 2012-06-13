package jira.linker;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;

import comm.ComResources;

import linker.Linker;
import models.Commit;

import db.ComDb;
import db.LinkerDb;

/**
 * <p>
 * This will serve as the controller class for Linking commits to Items.
 * </p>
 * 
 * @author braden
 * 
 */
public class JiraLinker extends Linker
{
	private ComDb		comDb;
	private LinkerDb	linkerDb;

	public JiraLinker(ComDb comDb, LinkerDb linkerDb)
	{
		this.comDb = comDb;
		this.linkerDb = linkerDb;
	}

	@Override
	public void Link()
	{
		// -------------------------------------------------------------------------------------
		// Do the first pass of linking commits -> issues via commit messages
		// and bug numbers
		// -------------------------------------------------------------------------------------
		LinkFromCommitMessagesJira();
	}

	public void LinkFromCommitMessagesJira()
	{
		if (!ComResources.LINK_FROM_COMMIT_MSGS) return;
		
		Set<Commit> commits = linkerDb.getAllCommits();
		for (Commit c : commits)
		{
			// check it's commit message against all our bug numbers
			Set<String> bugNumbers = getBugNumbers(c);
			for(String bugNumber : bugNumbers)
			{
				Set<Integer> matchedIssueIds = comDb.getIssuesMatchingBugNumber(bugNumber);
				for (Integer itemId : matchedIssueIds)
				{
					comDb.insertLink(new models.Link(itemId, c.getCommit_id(), 1.0f));
				}
			}
		}
	}
	
	public Set<String> getBugNumbers(Commit c)
	{
		Set<String> bugNumbers = new HashSet<String>();
		
		Matcher CommitMatcher = ComResources.BUG_NUMBER_REGEX.matcher(c.getComment());
		
		while(CommitMatcher.find())
			bugNumbers.add(CommitMatcher.group());
		
		return bugNumbers;
	}
}
