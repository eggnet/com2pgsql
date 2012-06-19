package jira.linker;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;

import linker.Linker;
import models.Commit;

import comm.ComResources;

import db.ComDb;
import db.LinkerDb;

/**
 * <p>
 * This will serve as the controller class for Linking commits to Items.
 * </p>
 * @author braden
 */
public class JiraLinker extends Linker
{
	
	public JiraLinker(ComDb comDb, LinkerDb linkerDb)
	{
		super(comDb, linkerDb);
	}

	@Override
	public void Link()
	{	
		// -------------------------------------------------------------------------------------
		// Do the first pass of linking commits -> issues via commit messages
		// and bug numbers
		// -------------------------------------------------------------------------------------
		LinkFromCommitMessages();
		
		// -------------------------------------------------------------------------------------
		// Now get try to link items -> commits by using commitID's
		// -------------------------------------------------------------------------------------
		//LinkFromIssueThreadItems();
		
		// -------------------------------------------------------------------------------------
		// Now get try to link items -> commits by using the item's data.
		// -------------------------------------------------------------------------------------
		LinkFromItems();
	}

	@Override
	public void LinkFromCommitMessages()
	{
		if (!ComResources.LINK_FROM_COMMIT_MSGS)
			return;

		Set<Commit> commits = linkerDb.getAllCommits();
		for (Commit c : commits)
		{
			// check its commit message against all our bug numbers
			Set<String> bugNumbers = getBugNumbers(c);
			for (String bugNumber : bugNumbers)
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

		while (CommitMatcher.find())
			bugNumbers.add(CommitMatcher.group());

		return bugNumbers;
	}
}
