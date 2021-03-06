package bugzilla;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import models.Attachment;
import models.Issue;
import models.Item;
import models.Pair;
import models.Person;

import comm.ComResources;

import db.DbConnection;
import db.util.ISetter;
import db.util.PreparedStatementExecutionItem;

public class BugzillaDb extends DbConnection
{
	public BugzillaDb() {
		super();
	}
	
	public List<Issue> getIssues(int iLIMIT, int iOFFSET) {
		try 
		{
			LinkedList<Issue> issues = new LinkedList<Issue>();
			String sql = "SELECT * FROM bugzilla_bugs " +
					"ORDER BY bug_id " +
					"LIMIT " + iLIMIT + " OFFSET " + iOFFSET; 
			
			ISetter[] params = {};
			PreparedStatementExecutionItem ei = new PreparedStatementExecutionItem(sql, params);
			addExecutionItem(ei);
			ei.waitUntilExecuted();
			ResultSet rs = ei.getResult();
			
			while(rs.next())
			{
				issues.add(new Issue(-1, rs.getString("bug_status"), rs.getString("assigned_to"), 
						rs.getTimestamp("creation_ts"), rs.getTimestamp("delta_ts"), rs.getString("short_desc"),
						"", rs.getString("reporter"), rs.getString("keywords"), Integer.toString(rs.getInt("bug_id"))));
			}
			return issues;
		}
		catch(SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Person> getCCForIssue(Issue issue) {
		try 
		{
			LinkedList<Person> people = new LinkedList<Person>();
			String sql = "SELECT * FROM bugzilla_cc " +
					"WHERE bug_id=" + issue.getIssueNum();
			
			ISetter[] params = {};
			PreparedStatementExecutionItem ei = new PreparedStatementExecutionItem(sql, params);
			addExecutionItem(ei);
			ei.waitUntilExecuted();
			ResultSet rs = ei.getResult();
			
			while(rs.next())
			{
				people.add(new Person(-1, "", rs.getString("who")));
			}
			return people;
		}
		catch(SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Item> getItemsForIssue(Issue issue) {
		try 
		{
			LinkedList<Item> items = new LinkedList<Item>();
			String sql = "SELECT * FROM bugzilla_longdescs " +
					"WHERE bug_id=" + issue.getIssueNum();
			ISetter[] params = {};
			PreparedStatementExecutionItem ei = new PreparedStatementExecutionItem(sql, params);
			addExecutionItem(ei);
			ei.waitUntilExecuted();
			ResultSet rs = ei.getResult();
			while(rs.next())
			{
				items.add(new Item(rs.getString("who"), rs.getTimestamp("bug_when"), 
						-1, rs.getString("thetext"), issue.getTitle(), ComResources.CommType.BUGZILLA));
			}
			return items;
		}
		catch(SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Attachment> getAttachmentsForIssue(Issue issue) {
		try 
		{
			LinkedList<Attachment> attachments = new LinkedList<Attachment>();
			String sql = "SELECT * FROM bugzilla_attachments " +
					"WHERE bug_id=" + issue.getIssueNum();
			
			ISetter[] params = {};
			PreparedStatementExecutionItem ei = new PreparedStatementExecutionItem(sql, params);
			addExecutionItem(ei);
			ei.waitUntilExecuted();
			ResultSet rs = ei.getResult();
			
			while(rs.next())
			{
				attachments.add(new Attachment(issue.getItemID(), rs.getString("filename"), rs.getString("thedata")));
			}
			return attachments;
		}
		catch(SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Pair<Integer,Integer>> getDependencies(int iLIMIT, int iOFFSET) {
		try 
		{
			LinkedList<Pair<Integer, Integer>> depends = new LinkedList<Pair<Integer, Integer>>();
			String sql = "SELECT * FROM bugzilla_dependencies " +
					"ORDER BY bug_id " +
					"LIMIT " + iLIMIT + " OFFSET " + iOFFSET; 
			
			ISetter[] params = {};
			PreparedStatementExecutionItem ei = new PreparedStatementExecutionItem(sql, params);
			addExecutionItem(ei);
			ei.waitUntilExecuted();
			ResultSet rs = ei.getResult();
			
			while(rs.next())
			{
				depends.add(new Pair<Integer, Integer>(rs.getInt("blocked"), rs.getInt("dependson")));
			}
			return depends;
		}
		catch(SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Pair<Integer,Integer>> getDuplicates(int iLIMIT, int iOFFSET) {
		try 
		{
			LinkedList<Pair<Integer, Integer>> depends = new LinkedList<Pair<Integer, Integer>>();
			String sql = "SELECT * FROM bugzilla_duplicates " +
					"ORDER BY bug_id " +
					"LIMIT " + iLIMIT + " OFFSET " + iOFFSET; 
			
			ISetter[] params = {};
			PreparedStatementExecutionItem ei = new PreparedStatementExecutionItem(sql, params);
			addExecutionItem(ei);
			ei.waitUntilExecuted();
			ResultSet rs = ei.getResult();
			
			while(rs.next())
			{
				depends.add(new Pair<Integer, Integer>(rs.getInt("dupe_of"), rs.getInt("dupe")));
			}
			return depends;
		}
		catch(SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
}
