package bugzilla;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import models.Item;

import bugzilla.models.TO.BugzillaBugTO;
import bugzilla.models.TO.BugzillaCCTO;
import bugzilla.models.TO.BugzillaCommentTO;

import comm.ComResources.CommType;

import db.DbConnection;

public class BugzillaDb extends DbConnection
{
	public BugzillaDb() {
		super();
	}
	
	public List<BugzillaCommentTO> getItems(int bLIMIT, int bOFFSET) {
		try 
		{
			LinkedList<BugzillaCommentTO> items = new LinkedList<BugzillaCommentTO>();
			String sql = "SELECT * FROM bugzilla_longdescs JOIN bugzilla_bugs ON " +
					"bugzilla_longdescs.bug_id = bugzilla_bugs.bug_id " +
					"LIMIT ? OFFSET ?"; 
			String[] parms = {Integer.toString(bLIMIT), Integer.toString(bOFFSET)};
			ResultSet rs = execPreparedQuery(sql, parms);
			while(rs.next())
			{
				items.add(new BugzillaCommentTO(rs.getInt("bug_id"), rs.getString("who"), rs.getString("who_name"),
						rs.getTimestamp("bug_when"), rs.getString("thetext"), rs.getString("short_desc")));
			}
			return items;
		}
		catch(SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public List<BugzillaBugTO> getBugs(int bLIMIT, int bOFFSET) {
		try 
		{
			LinkedList<BugzillaBugTO> bugs = new LinkedList<BugzillaBugTO>();
			String sql = "SELECT * FROM bugzilla_bugs" +
					"LIMIT ? OFFSET ?"; 
			String[] parms = {Integer.toString(bLIMIT), Integer.toString(bOFFSET)};
			ResultSet rs = execPreparedQuery(sql, parms);
			while(rs.next())
			{
				bugs.add(new BugzillaBugTO(rs.getInt("bug_id"), rs.getString("assigned_to"), 
						rs.getString("bug_severity"), rs.getTimestamp("creation_ts"), rs.getString("reporter"),
						rs.getString("qa_contact"), rs.getString("keywords")));
			}
			return bugs;
		}
		catch(SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public List<BugzillaCCTO> getCCs(int bLIMIT, int bOFFSET) {
		try 
		{
			LinkedList<BugzillaCCTO> cc = new LinkedList<BugzillaCCTO>();
			String sql = "SELECT * FROM bugzilla_cc" +
					"LIMIT ? OFFSET ?"; 
			String[] parms = {Integer.toString(bLIMIT), Integer.toString(bOFFSET)};
			ResultSet rs = execPreparedQuery(sql, parms);
			while(rs.next())
			{
				cc.add(new BugzillaCCTO(rs.getInt("bug_id"), rs.getString("who")));
			}
			return cc;
		}
		catch(SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
}
