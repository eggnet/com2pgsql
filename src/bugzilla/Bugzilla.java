package bugzilla;

import http.HTTPRequester;

import java.util.List;

import models.Item;
import models.Person;

import bugzilla.models.Bug;
import bugzilla.models.TO.BugzillaCommentTO;


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
		Bug bug = gson.fromJson(bugJSON, Bug.class);
	}
	
	public void parseBugzilla() {
		int currentBug = -1;
		int currentThread = -1;
		int offset = 0;
		
		List<BugzillaCommentTO> bugs = bugzillaDB.getItems(ComResources.DB_LIMIT, offset);
		
		for(;;) {
			for(BugzillaCommentTO bug: bugs) {
				if(currentBug != bug.getBug_id()) {
					currentBug = bug.getBug_id();
					currentThread = -1;
				}
				
				// Insert the person
				int pID = comDB.insertPerson(new Person(-1, bug.getWho_name(), bug.getWho()));
				
				// Insert the item
				int itemID = comDB.insertItem(new Item(pID, bug.getBug_when(), -1, bug.getThetext(), 
						bug.getTitle(), CommType.BUGZILLA));
				
				// Insert the thread
				if(currentThread == -1) {
					currentThread = comDB.insertThreadUnknownThread(new models.Thread(itemID, -1));
				}
				else {
					comDB.insertThreadKnownThread(new models.Thread(itemID, currentThread));
				}
				
			}
			
			if(bugs.size() < ComResources.DB_LIMIT)
				break;
			else {
				offset += ComResources.DB_LIMIT;
				bugs = bugzillaDB.getItems(ComResources.DB_LIMIT, offset);
			}
		}
	}
}
