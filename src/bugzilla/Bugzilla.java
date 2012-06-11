package bugzilla;

import http.HTTPRequester;

import java.util.List;

import models.Attachment;
import models.Issue;
import models.Item;
import models.Person;
import models.Silent;

import com.google.gson.*;
import comm.ComResources;
import comm.ComResources.CommType;

import db.ComDb;

public class Bugzilla
{
	private ComDb comDB;
	private BugzillaDb bugzillaDB;

	public Bugzilla(ComDb comDB) {
		this.comDB = comDB;
	}
	
	public Bugzilla(ComDb comDB, String dbName)
	{
		this.comDB = comDB;
		
		this.bugzillaDB = new BugzillaDb();
		bugzillaDB.connect(dbName);
	}
	
	public void parseBugzillaFromHTTP(String http) {
		System.out.println("Retrieving Bugzilla information from: " + http);
		
		String bugJSON = HTTPRequester.sendGetRequest(http, "bug/35");
		
		Gson gson = new Gson();
	}
	
	public void parseBugzilla() {
		int currentBug = -1;
		int currentThread = -1;
		int offset = 0;
		
		List<Issue> issues = bugzillaDB.getIssues(ComResources.DB_LIMIT, offset);
		
		for(;;) {
			for(Issue issue: issues) {
				// Insert creator
				int creatorID = comDB.insertPerson(new Person(-1, "", issue.getCreator()));
				issue.setCreatorID(creatorID);
				
				// Insert assignee
				int assignedID = comDB.insertPerson(new Person(-1, "", issue.getAssignee()));
				issue.setAssignedID(assignedID);
				
				// Insert the item
				int itemID = comDB.insertItem(new Item(creatorID, issue.getCreationTS(), -1, "", 
						"", CommType.ISSUE));
				issue.setItemID(itemID);
				
				// Insert the thread
				comDB.insertThread(new models.Thread(itemID, itemID));
				currentThread = itemID;
				
				// Insert the issue
				comDB.insertIssue(issue);
				
				// Insert the CCs
				List<Person> CCs = bugzillaDB.getCCForIssue(issue);
				for(Person cc: CCs) {
					int ccID = comDB.insertPerson(cc);
					comDB.insertSilent(new Silent(ccID, itemID));
				}
				
				// Insert the attachments
				List<Attachment> attachments = bugzillaDB.getAttachmentsForIssue(issue);
				for(Attachment attachment: attachments) {
					comDB.insertAttachment(attachment);
				}
				
				// Insert the comment items
				parseIssueComments(issue, currentThread);
			}
			
			if(issues.size() < ComResources.DB_LIMIT)
				break;
			else {
				offset += ComResources.DB_LIMIT;
				issues = bugzillaDB.getIssues(ComResources.DB_LIMIT, offset);
			}
		}
	}
	
	private void parseIssueComments(Issue issue, int threadID) {
		List<Item> items = bugzillaDB.getItemsForIssue(issue);
		for(Item item: items) {
			// Insert person
			int pID = comDB.insertPerson(new Person(-1, "", item.getPerson()));
			item.setPId(pID);
			
			// Insert item
			int itemID = comDB.insertItem(item);
			item.setItemId(itemID);
			
			// Insert thread
			comDB.insertThread(new models.Thread(itemID, threadID));
		}
	}
}
