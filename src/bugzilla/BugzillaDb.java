package bugzilla;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import models.Item;

import comm.ComResources.CommType;

import db.DbConnection;

public class BugzillaDb extends DbConnection
{
	public BugzillaDb() {
		super();
	}
	/*
	public List<BugzillaCommentTO> getItems(int bLIMIT, int bOFFSET) {
		try 
		{
			LinkedList<BugzillaCommentTO> items = new LinkedList<BugzillaCommentTO>();
			String sql = "SELECT * FROM bugzilla_longdescs JOIN bugzilla_bugs ON " +
					"bugzilla_longdescs.bug_id = bugzilla_bugs.bug_id " +
					"LIMIT " + bLIMIT + " OFFSET " + bOFFSET; 
			String[] parms = {};
			ResultSet rs = execPreparedQuery(sql, parms);
			while(rs.next())
			{
				items.add(new BugzillaCommentTO(rs.getInt("bug_id"), rs.getString("who"), "FAKE",
						rs.getTimestamp("bug_when"), rs.getString("thetext"), rs.getString("short_desc")));
			}
			return items;
		}
		catch(SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}*/
}
