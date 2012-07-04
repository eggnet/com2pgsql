package email.linker;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import linker.Linker;
import models.Commit;
import models.Item;
import models.Person;
import db.LinkerDb;
import db.SocialDb;

public class EmailLinker extends Linker
{
	public EmailLinker(SocialDb comDb, LinkerDb linkerDb)
	{
		super(comDb, linkerDb);
	}
	
	@Override
	public void Link()
	{
				
	}
	
	public void linkItemsByMentionedFiles(Item item, List<Person> people, List<String> files) {
		List<Commit> commits = linkerDb.getCommitsAroundDate(item.getItemDate());
		List<models.Link> links = new ArrayList<models.Link>();
		
		for(Commit commit: commits) {
			for(Person person: people) {
				if(matchCommitToPerson(commit, person)) {
					models.Link link = new models.Link(item.getItemId(), commit.getCommit_id(), 0.0f);
					
					// Get confidence up if possible
					Set<String> fileSet = linkerDb.getChangesetForCommit(commit.getCommit_id());
					List<String> filesChanged = new ArrayList<String>();
					filesChanged.addAll(fileSet);					
					link.setConfidence(fileMatchConfidence(files, filesChanged));
					
					// Insert
					if(link.getConfidence() > 0)
						links.add(link);
				}
			}
		}
		
		// Insert the links
		if(!links.isEmpty()) {
			for(models.Link link: links) {
				comDb.insertLink(link);
			}
		}
	}
	
	private boolean matchCommitToPerson(Commit commit, Person person) {
		return (person.getEmail().equals(commit.getAuthor_email()) ||
				person.getName().equals(commit.getAuthor()));
	}
	
	/**
	 * This is the main confidence calculation for email items to 
	 * commits in the social network.
	 * @param files
	 * @param filesChanged
	 * @return
	 */
	private float fileMatchConfidence(List<String> files, List<String> filesChanged) {
		float confidence = 0.0f;
		
		for(String file: files) {
			if(filesChanged.contains(file))
				confidence++;
		}
		
		confidence /= files.size();
		
		return confidence;
	}
}
