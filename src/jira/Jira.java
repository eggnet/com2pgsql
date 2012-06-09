package jira;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import models.Issue;
import models.Item;
import models.Person;
import models.jira.JiraComment;
import models.jira.JiraIssue;
import models.jira.JiraIssueQuery;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import comm.ComResources;
import db.ComDb;

public class Jira
{
	public int currentIssueCount = 1;
	public int totalIssueCount = 2; // initial dummy value
	public Gson gson;
	public String location;
	public ComDb db;
	
	public void initJira(String location, ComDb db) throws URISyntaxException
	{
		gson = new Gson();
		this.location = location;
		this.db = db;
	}
	
	public void parseJira(String location, ComDb db) throws URISyntaxException
	{
		// initialize the Jira Client
		initJira(location, db);
		//https://hibernate.onjira.com/rest/api/latest/search?jql=null&startAt=0&fields=*all
		while(currentIssueCount < totalIssueCount)
			getNextIssues(currentIssueCount);
	}
	
	public List<JiraIssue> getNextIssues(int startPosition)
	{
		List<JiraIssue> issues = new LinkedList<JiraIssue>();
		ComResources.log("//----------------------------------------------------------------------------------------------------------//");
		ComResources.log("Getting issues number %d - %s from %s.", startPosition, startPosition+ComResources.JIRA_MAX_RESULTS, location);		
		ComResources.log("Timestamp: %s", new Date().toString());
		try {
			HttpClient httpClient = new DefaultHttpClient();
			
			URIBuilder builder = new URIBuilder();
			URI uri = builder.setScheme("https").setHost(location).setPath("/rest/api/latest/search")
					    .setParameter("startAt", String.valueOf(startPosition))
					    .setParameter("maxResults", String.valueOf(ComResources.JIRA_MAX_RESULTS))
					    .setParameter("fields", "*all").build();
			
			HttpGet httpget = new HttpGet(uri);
			HttpResponse resp = httpClient.execute(httpget);
			HttpEntity entity = resp.getEntity();
			if (entity != null)
			{
				JiraIssueQuery jiraQuery = gson.fromJson(EntityUtils.toString(entity), JiraIssueQuery.class);
				totalIssueCount = jiraQuery.getTotal();
				for (JiraIssue jiraIssue : jiraQuery.getIssues())
				{
					ComResources.log("Issues done: %d/%d", currentIssueCount, totalIssueCount);
					currentIssueCount++;
//					ParseIssueIntoDb(jiraIssue);
					// TODO @braden insert into db
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		ComResources.log("Timestamp: %s", new Date().toString());
		ComResources.log("Completed request.");
		ComResources.log("//----------------------------------------------------------------------------------------------------------//");
		return issues;
	}
	
	/**
	 * <p>Parse a given JiraIssue into the social technical network schema.</p>
	 * @param jiraIssue
	 */
	public void ParseIssueIntoDb(JiraIssue jiraIssue)
	{
		//-----------------------------------------------------------------------------
		// Create the creator and insert
		//-----------------------------------------------------------------------------
		Person newCreator = new Person(-1,
					jiraIssue.getFields().getReporter().getDisplayName(),
					jiraIssue.getFields().getReporter().getEmailAddress());
		int pID = db.insertPerson(newCreator);

		//-----------------------------------------------------------------------------
		// Create the assignee if exists and insert it
		//-----------------------------------------------------------------------------
		Person newAssignee = null;
		if (jiraIssue.getFields().getAssignee() != null)
		{
			newAssignee = new Person(-1, 
					jiraIssue.getFields().getAssignee().getDisplayName(),
					jiraIssue.getFields().getAssignee().getEmailAddress());
			newAssignee.setPID(db.insertPerson(newAssignee));
		}
		
		//-----------------------------------------------------------------------------
		// Create the item and insert it
		//-----------------------------------------------------------------------------
		Item newItem = new Item(jiraIssue);
		newItem.setPId(pID);
		int itemID = db.insertItem(newItem);
		
		//-----------------------------------------------------------------------------
		// Create the issue and insert it
		//-----------------------------------------------------------------------------
		Issue newIssue = new Issue(jiraIssue);
		newIssue.setCreatorID(pID);
		
		if (newAssignee != null) {
			newIssue.setAssignedID(newAssignee.getPID());
			newIssue.setAssignee(newAssignee.getEmail());
		}
		
		db.insertIssue(newIssue);
		ParseIssueComments(jiraIssue);
	}
	
	/**
	 * <p>Parse through the comments on each issue and insert them into the corresponding 
	 * threads, comments, and persons tables.</p>
	 * @author bradens
	 * @param jiraIssue
	 */
	public void ParseIssueComments(JiraIssue jiraIssue)
	{
		for (JiraComment comment : jiraIssue.getFields().getComment().getComments())
		{
			Person newPerson = new Person(-1,
					comment.getAuthor().getDisplayName(),
					comment.getAuthor().getEmailAddress());
			newPerson.setPID(db.insertPerson(newPerson));
			Item newCommentItem = new Item(comment);
			newCommentItem.setItemId(db.insertItem(newCommentItem));
			
			// TODO @braden insert into threads table and finish.
			// after the schema change.
		}
	}
}
