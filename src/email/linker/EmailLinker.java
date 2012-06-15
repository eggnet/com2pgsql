package email.linker;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import db.ComDb;
import db.LinkerDb;
import linker.Linker;
import models.Commit;
import models.CommitFamily;
import models.Item;
import models.Person;

public class EmailLinker extends Linker
{
	public EmailLinker(ComDb comDb, LinkerDb linkerDb)
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
					List<String> filesChanged = linkerDb.getFilesChangedOnCommit(commit);
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
	
	public String findSnippetFile(String snippet, Timestamp date) {
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
		
		// Look for snippet in all the files
		for(String file: files) {
			String rawFile = linkerDb.getRawFileFromDiffTree(file, commit.getCommit_id());
			if(rawFile.contains(snippet))
				return file;
		}
		
		return null;
	}
	
	private List<CommitFamily> reversePath(List<CommitFamily> path) {
		List<CommitFamily> returnPath = new ArrayList<CommitFamily>();
		
		for(CommitFamily CF: path)
			returnPath.add(0, CF);
		
		return returnPath;
	}
}
