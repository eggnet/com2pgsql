package jira;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import models.Item;
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
	public int currentIssueCount = 0;
	public int maxIssueCount;
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
				maxIssueCount = jiraQuery.getTotal();
				for (JiraIssue jiraIssue : jiraQuery.getIssues())
				{
					currentIssueCount++;
					ParseIssueIntoDb(jiraIssue);
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
	
	public void ParseIssueIntoDb(JiraIssue jiraIssue)
	{
		
	}
}
