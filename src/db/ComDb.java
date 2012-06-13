package db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import models.Attachment;
import models.Dependency;
import models.Issue;
import models.Item;
import models.Link;
import models.Person;
import models.Silent;

import comm.ComResources.CommType;

public class ComDb extends DbConnection
{
	public ComDb() {
		super();
	}
	
	public boolean connect(String dbName)
	{
		return super.connect(dbName);
	}
	/**
	 * Creates a db on the current connection.
	 * @param dbName
	 * @return true for success
	 */
	public boolean createDb(String dbName) {
		PreparedStatement s;
		try {
			// Drop the DB if it already exists
			s = conn.prepareStatement("DROP DATABASE IF EXISTS " + dbName + ";");
			s.execute();
			
			// First create the DB.
			s = conn.prepareStatement("CREATE DATABASE " + dbName + ";");
			s.execute();
			
			// Reconnect to our new database.
			connect(dbName.toLowerCase());
			
			// Now load our default schema in.
			sr.runScript(new InputStreamReader(this.getClass().getResourceAsStream("createdb.sql")));
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public int insertItem(Item item) {
		try 
		{
			PreparedStatement s = conn.prepareStatement(
					"INSERT INTO items (p_id, item_date, item_id, body, title, type) VALUES " +
					"(" + item.getPId() + ", ?::timestamp, default, ?, ?, ?)");
			s.setString(1, item.getItemDate().toString());
			s.setString(2, item.getBody());
			s.setString(3, item.getTitle());
			s.setString(4, item.getCommunicationType().toString());
			s.execute();
			
			return getSequenceValue("items_id_seq"); 
		}
		catch(SQLException e) 
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	public int insertPerson(Person person) {
		try 
		{
			String sql = "SELECT * FROM people WHERE " +
					"name=? AND email=?"; 
			String[] parms = {person.getName(), person.getEmail()};
			ResultSet rs = execPreparedQuery(sql, parms);
			if(!rs.next()) {
				// Insert
				PreparedStatement s = conn.prepareStatement(
						"INSERT INTO people (p_id, name, email) VALUES " +
						"(default, ?, ?)");
				s.setString(1, person.getName());
				s.setString(2, person.getEmail());
				s.execute();

				return getSequenceValue("people_id_seq");
			}
			else {
				return rs.getInt("p_id");
			}
		}
		catch(SQLException e) 
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	public List<Integer> insertPeople(List<Person> people) {
		List<Integer> inserts = new ArrayList<Integer>();
		for(Person person: people) {
			inserts.add(insertPerson(person));
		}
		return inserts;
	}
	
	public boolean insertThread(models.Thread thread) {
		try 
		{
			PreparedStatement s = conn.prepareStatement(
					"INSERT INTO threads (item_id, thread_id) VALUES " +
					"(" + thread.getItemID() + ", " + thread.getThreadID() + ")");
			s.execute();
		}
		catch(SQLException e) 
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean insertLink(Link link) {
		try 
		{
			PreparedStatement s = conn.prepareStatement(
					"INSERT INTO replies (item_id, commit_id, confidence) VALUES " +
					"(" + link.getItemID() + ", ?, " + link.getConfidence() + ")");
			s.setString(2, link.getCommitID());
			s.execute();
		}
		catch(SQLException e) 
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean insertIssue(Issue issue) {
		try 
		{
			PreparedStatement s = conn.prepareStatement(
					"INSERT INTO issues (item_id, status, assignee_id, creation_ts, last_modified_ts, " +
					"title, description, creator_id, keywords, issue_num) VALUES " +
					"(" + issue.getItemID() + ", ?, " + issue.getAssignedID() + ", ?::timestamp, ?::timestamp, ?, ?, " +
					issue.getCreatorID() + ", ?, ?)");
			s.setString(1, issue.getStatus());
			s.setString(2, issue.getCreationTS().toString());
			s.setString(3, issue.getLastModifiedTS().toString());
			s.setString(4, issue.getTitle());
			s.setString(5, issue.getDescription());
			s.setString(6, issue.getKeywords());
			s.setString(7, issue.getIssueNum());
			s.execute();
		}
		catch(SQLException e) 
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean insertSilent(Silent silent) {
		try 
		{
			PreparedStatement s = conn.prepareStatement(
					"INSERT INTO silents (p_id, item_id) VALUES " +
					"(" + silent.getpID() + ", " + silent.getItemID() + ")");
			s.execute();
		}
		catch(SQLException e) 
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean insertAttachment(Attachment attachment) {
		try 
		{
			PreparedStatement s = conn.prepareStatement(
					"INSERT INTO attachments (item_id, title, body) VALUES " +
					"(" + attachment.getItemID() + ", ?, ?)");
			s.setString(1, attachment.getTitle());
			s.setString(2, attachment.getBody());
			s.execute();
		}
		catch(SQLException e) 
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean insertDependency(Dependency dependency)
	{
		try {
			PreparedStatement s = conn.prepareStatement(
					"INSERT INTO dependencies (item_id, depends_on_id) VALUES (?, ?)");
			s.setInt(1, dependency.getItemID());
			s.setInt(2, dependency.getDependsOnID());
			s.execute();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public List<Item> getItemsInThread(int ThreadID) {
		try 
		{
			LinkedList<Item> items = new LinkedList<Item>();
			String sql = "SELECT p_id, item_date, item_id, body, title, type" +
					" FROM threads JOIN items ON (threads.item_id = item.item_id)" +
					" WHERE thread_id=?"; 
			String[] parms = {Integer.toString(ThreadID)};
			ResultSet rs = execPreparedQuery(sql, parms);
			while(rs.next())
			{
				items.add(new Item(rs.getInt("p_id"), rs.getTimestamp("item_date"), rs.getInt("item_id"), 
						rs.getString("body"), rs.getString("title"), CommType.valueOf(rs.getString("type"))));
			}
			return items;
		}
		catch(SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private int getSequenceValue(String sequence) {
		try 
		{
			// Get the ID
			String sql = "SELECT currval(?)"; 
			String[] parms = {sequence};
			ResultSet rs = execPreparedQuery(sql, parms);
			if(rs.next())
				return rs.getInt("currval");
			return -1;
		}
		catch(SQLException e) 
		{
			e.printStackTrace();
			return -1;
		}
	}

	public int findItemIDFromJiraKey(String dependsKey)
	{
		try 
		{
			// Get the ID
			String sql = "SELECT item_id from issues where issue_num = ?";
			String[] parms = {dependsKey};
			ResultSet rs = execPreparedQuery(sql, parms);
			if(rs.next())
				return rs.getInt("item_id");
			return -1;
		}
		catch(SQLException e) 
		{
			e.printStackTrace();
			return -1;
		}		
	}
	
	public Set<Integer> getIssuesMatchingBugNumber(String bugNumber)
	{
		Set<Integer> issues = new HashSet<Integer>();
		try {
			String sql = "SELECT item_id FROM issues WHERE issue_num=?";
			String[] parms = {bugNumber};
			ResultSet rs = execPreparedQuery(sql, parms);
			while (rs.next())
			{
				issues.add(rs.getInt(0));
			}
			return issues;
		}
		catch(SQLException e)
		{
			return null;
		}
	}
	
	public int getItemIDFromBugNumber(int bugNum) {
		try {
			String sql = "SELECT item_id FROM issues WHERE issue_num=?";
			String[] parms = {Integer.toString(bugNum)};
			ResultSet rs = execPreparedQuery(sql, parms);
			while (rs.next())
				return rs.getInt("item_id");
			
			return -1;
		}
		catch(SQLException e)
		{
			return -1;
		}
	}
}
