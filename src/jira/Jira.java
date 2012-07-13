package jira;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import jira.linker.JiraLinker;

import models.Dependency;
import models.Issue;
import models.Item;
import models.Person;
import models.jira.JiraComment;
import models.jira.JiraIssue;
import models.jira.JiraIssueLink;
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
import db.SocialDb;
import db.LinkerDb;
import db.TechnicalDb;

public class Jira
{
	public int							currentIssueCount	= 1;
	public int							totalIssueCount		= 2;
	public Gson							gson;
	public String						location;
	public SocialDb						comDb;
	public TechnicalDb						linkerDb;
	private List<TemporaryIssueLink>	tempLinks;
	private JiraLinker					linker;
	private static boolean				IS_INITIALIZED		= false;

	private class TemporaryIssueLink
	{
		private int		ItemID;
		private String	DependsKey;

		public TemporaryIssueLink(int itemID, String dependsKey)
		{
			ItemID = itemID;
			DependsKey = dependsKey;
		}

		public int getItemID()
		{
			return ItemID;
		}

		@SuppressWarnings("unused")
		public void setItemID(int itemID)
		{
			ItemID = itemID;
		}

		@SuppressWarnings("unused")
		public String getDependsKey()
		{
			return DependsKey;
		}

		@SuppressWarnings("unused")
		public void setDependsKey(String dependsKey)
		{
			DependsKey = dependsKey;
		}
	}

	public void initJira(String location, TechnicalDb linkerDb, SocialDb db) throws URISyntaxException
	{
		gson = new Gson();
		this.location = location;
		this.comDb = db;
		this.tempLinks = new LinkedList<TemporaryIssueLink>();
		this.linkerDb = linkerDb;
		this.linker = new JiraLinker(db, linkerDb);
		this.IS_INITIALIZED = true;
	}

	public void parseJira(String location, TechnicalDb linkerDb, SocialDb comDb) throws URISyntaxException
	{
		// initialize the Jira Client
		initJira(location, linkerDb, comDb);
		while (currentIssueCount < totalIssueCount)
			getNextIssues(currentIssueCount);

		// Now resolve our temporary links into hard links.
		for (TemporaryIssueLink tLink : this.tempLinks)
		{
			int dependsID = comDb.findItemIDFromJiraKey(tLink.DependsKey);
			if (dependsID == -1)
				continue;
			comDb.insertDependency(new Dependency(tLink.getItemID(), dependsID));
		}

		// -----------------------------------------------------------------------------
		// Now that the db is imported, start linking
		// -----------------------------------------------------------------------------
		linkJira(location, linkerDb, comDb);
	}

	public void linkJira(String location, TechnicalDb linkerDb, SocialDb comDb) throws URISyntaxException
	{
		if (!IS_INITIALIZED) initJira(location, linkerDb, comDb);
		linker.Link();
	}

	public List<JiraIssue> getNextIssues(int startPosition)
	{
		List<JiraIssue> issues = new LinkedList<JiraIssue>();
		ComResources.log("//---------------------------------------------------------------------------------------//");
		ComResources.log("Getting issues number %d - %s from %s.", startPosition, startPosition
				+ ComResources.JIRA_MAX_RESULTS, location);
		ComResources.log("Timestamp: %s", new Date().toString());
		try
		{
			HttpClient httpClient = new DefaultHttpClient();

			URIBuilder builder = new URIBuilder();
			URI uri = builder.setScheme("https").setHost(location).setPath("/rest/api/latest/search").setParameter(
					"startAt", String.valueOf(startPosition)).setParameter("maxResults",
					String.valueOf(ComResources.JIRA_MAX_RESULTS)).setParameter("fields", "*all").build();

			HttpGet httpget = new HttpGet(uri.toString() + "&jql=project%3D" + ComResources.ISSUE_NUMBER_KEY);
			HttpResponse resp = httpClient.execute(httpget);
			HttpEntity entity = resp.getEntity();
			if (entity != null)
			{
				JiraIssueQuery jiraQuery = gson.fromJson(EntityUtils.toString(entity), JiraIssueQuery.class);
				totalIssueCount = jiraQuery.getTotal();
				for (JiraIssue jiraIssue : jiraQuery.getIssues())
				{
					ComResources.log("Issues done: %d/%d", currentIssueCount, totalIssueCount);
					ComResources.log("Current Issue %s", jiraIssue.getFields().getSummary());
					ParseIssueIntoDb(jiraIssue);
					currentIssueCount++;
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
		ComResources.log("//---------------------------------------------------------------------------------------//");
		return issues;
	}

	/**
	 * <p>
	 * Parse a given JiraIssue into the social technical network schema.
	 * </p>
	 * 
	 * @param jiraIssue
	 */
	public void ParseIssueIntoDb(JiraIssue jiraIssue)
	{
		try
		{
			// -----------------------------------------------------------------------------
			// Create the creator and insert
			// -----------------------------------------------------------------------------
			Person newCreator = new Person(-1, jiraIssue.getFields().getReporter().getDisplayName(), jiraIssue
					.getFields().getReporter().getEmailAddress());
			
			if (newCreator.getEmail() == null || newCreator.getName() == null) return;
			
			int pID = comDb.insertPerson(newCreator);

			// -----------------------------------------------------------------------------
			// Create the assignee if exists and insert it
			// -----------------------------------------------------------------------------
			Person newAssignee = null;
			if (jiraIssue.getFields().getAssignee() != null)
			{
				newAssignee = new Person(-1, jiraIssue.getFields().getAssignee().getDisplayName(), jiraIssue
						.getFields().getAssignee().getEmailAddress());
				newAssignee.setPID(comDb.insertPerson(newAssignee));
			}

			// -----------------------------------------------------------------------------
			// Create the item and insert it
			// -----------------------------------------------------------------------------
			Item newItem = new Item(jiraIssue);
			newItem.setPId(pID);
			newItem.setItemId(comDb.insertItem(newItem));

			// -----------------------------------------------------------------------------
			// Create the issue and insert it
			// -----------------------------------------------------------------------------
			Issue newIssue = new Issue(jiraIssue);
			newIssue.setCreatorID(pID);
			newIssue.setItemID(newItem.getItemId());

			if (newAssignee != null)
			{
				newIssue.setAssignedID(newAssignee.getPID());
				newIssue.setAssignee(newAssignee.getEmail());
			}

			comDb.insertIssue(newIssue);

			// -----------------------------------------------------------------------------
			// Create the issue links and insert them
			// -----------------------------------------------------------------------------

			if (jiraIssue.getFields().getIssueLinks() != null)
			{
				for (JiraIssueLink jLink : jiraIssue.getFields().getIssueLinks())
				{
					if (jLink.getInwardIssue() != null)
						this.tempLinks
								.add(new TemporaryIssueLink(newIssue.getItemID(), jLink.getInwardIssue().getKey()));
					if (jLink.getOutwardIssue() != null)
						this.tempLinks.add(new TemporaryIssueLink(newIssue.getItemID(), jLink.getOutwardIssue()
								.getKey()));
				}
			}
			ParseIssueComments(jiraIssue, newIssue.getItemID());
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * Parse through the comments on each issue and insert them into the
	 * corresponding threads, comments, and persons tables.
	 * </p>
	 * 
	 * @author bradens
	 * @param jiraIssue
	 * @param issueItemId
	 */
	public void ParseIssueComments(JiraIssue jiraIssue, int issueItemId)
	{
		try
		{
			for (JiraComment comment : jiraIssue.getFields().getComment().getComments())
			{
				Person newPerson = new Person(-1, comment.getAuthor().getDisplayName(), comment.getAuthor()
						.getEmailAddress());
				if (newPerson.getEmail() == null || newPerson.getName() == null) return;
				
				newPerson.setPID(comDb.insertPerson(newPerson));
				Item newCommentItem = new Item(comment);
				newCommentItem.setPerson(newPerson.getEmail());
				newCommentItem.setPId(newPerson.getPID());
				newCommentItem.setItemId(comDb.insertItem(newCommentItem));

				models.Thread newThread = new models.Thread(newCommentItem.getItemId(), issueItemId);
				comDb.insertThread(newThread);
			}
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
	}
}
