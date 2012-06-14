package bugzilla.linker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

import comm.ComResources;

import linker.Linker;
import models.Commit;
import models.Pair;

import db.ComDb;
import db.LinkerDb;

public class BugzillaLinker extends Linker
{

	public BugzillaLinker(ComDb comDb, LinkerDb linkerDb)
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
		LinkFromCommitMessagesBugzilla();
	}

	public void LinkFromCommitMessagesBugzilla()
	{
		if (!ComResources.LINK_FROM_COMMIT_MSGS) 
			return;
		
		Set<Commit> commits = linkerDb.getAllCommits();
		for (Commit c : commits)
		{
			// check it's commit message against all our bug numbers
			List<Pair<Integer, Float>> bugNumbers = getLinks(c);
			for(Pair<Integer, Float> bugNumber : bugNumbers)
			{
				Set<Integer> matchedIssueIds = comDb.getIssuesMatchingBugNumber(bugNumber.getFirst().toString());
				for (Integer itemId : matchedIssueIds) {
					comDb.insertLink(new models.Link(itemId, c.getCommit_id(), bugNumber.getSecond()));
				}
			}
		}
	}
	
	public List<Pair<Integer, Float>> getLinks(Commit commit) {
		List<Pair<Integer, Float>> bugNumbers = new ArrayList<Pair<Integer, Float>>();
		
		// Find numbers
		Matcher numberMatcher = ComResources.NUMBER.matcher(commit.getComment());
		while(numberMatcher.find()) {
			updateConfidenceList(bugNumbers, Integer.parseInt(numberMatcher.group(1)), 0.0f);
		}
		
		// Find bug numbers
		numberMatcher = ComResources.BUG_NUMBER_BUGZILLA_REGEX.matcher(commit.getComment());
		while(numberMatcher.find()) {
			updateConfidenceList(bugNumbers, Integer.parseInt(numberMatcher.group(1)), 1.0f);
		}
		
		// Find keywords
		if(containsKeyword(commit.getComment())) {
			for(Pair<Integer, Float> link: bugNumbers) {
				link.setSecond(Math.max((float)(link.getSecond() + 0.3), 1.0f));
			}
		}
		
		return bugNumbers;
	}
	
	private void updateConfidenceList(List<Pair<Integer, Float>> list, int number, float addition) {
		for(Pair<Integer, Float> link: list) {
			if(link.getFirst() == number) {
				link.setSecond(Math.max((float)(link.getSecond() + addition), 1.0f));
				return;
			}
		}
		
		list.add(new Pair<Integer, Float>(number, addition));
	}
	
	private boolean containsKeyword(String comment) {
		String[] tokens = comment.split(" ");
		
		for(String token: tokens) {
			Matcher numberMatcher = ComResources.COMMIT_KEYWORDS.matcher(token);
			if(numberMatcher.find())
					return true;
		}
		
		return false;
	}
}
