package jira.linker;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;

import linker.Linker;
import models.Commit;

import comm.ComResources;

import db.SocialDb;
import db.TechnicalDb;

/**
 * <p>
 * This serves as the controller class for Linking commits to Items.
 * </p>
 * @author braden
 */
public class JiraLinker extends Linker
{
	
	public JiraLinker(SocialDb comDb, TechnicalDb linkerDb)
	{
		super(comDb, linkerDb);
	}

	/**
	 * Runs the linker process, first links from commit messages, 
	 * then issue thread items, then directly from the text anaylsis 
	 * on issues.
	 */
	@Override
	public void Link()
	{	
		super.Link();
		// -------------------------------------------------------------------------------------
		// Do the first pass of linking commits -> issues via commit messages
		// and bug numbers
		// -------------------------------------------------------------------------------------
		LinkFromCommitMessages();
		
		// -------------------------------------------------------------------------------------
		// Now get try to link items -> commits by using commitID's
		// -------------------------------------------------------------------------------------
		LinkFromIssueThreadItems();
		
		// -------------------------------------------------------------------------------------
		// Now get try to link items -> commits by using the item's data.
		// -------------------------------------------------------------------------------------
		LinkFromItems();
	}

	/**
	 * Links to the communications artifacts from the commit comments by searching for 
	 * bug numbers using {@link ComResources.BUG_NUMBER_REGEX}.
	 */
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
	
	/**
	 * Matches all the bugs numbers as defined in {@link ComResources.BUG_NUMBER_REGEX}
	 * in a given commit message
	 * @param c
	 * @return Set of Bug number matches
	 */
	public Set<String> getBugNumbers(Commit c)
	{
		Set<String> bugNumbers = new HashSet<String>();
		
		Matcher CommitMatcher = ComResources.BUG_NUMBER_REGEX.matcher(c.getComment());

		while (CommitMatcher.find())
			bugNumbers.add(CommitMatcher.group());

		return bugNumbers;
	}
}
