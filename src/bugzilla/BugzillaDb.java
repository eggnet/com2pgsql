package bugzilla;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import models.BugzillaTO;
import models.Item;

import com.ComResources.CommType;

import db.DbConnection;

public class BugzillaDb extends DbConnection
{
	public BugzillaDb() {
		super();
	}
	
	public List<BugzillaTO> getItemsFromBugzilla( int bLIMIT, int bOFFSET) {
		try 
		{
			LinkedList<BugzillaTO> bugs = new LinkedList<BugzillaTO>();
			String sql = "SELECT * FROM bugzilla_longdescs " +
					"LIMIT ? OFFSET ?"; 
			String[] parms = {Integer.toString(bLIMIT), Integer.toString(bOFFSET)};
			ResultSet rs = execPreparedQuery(sql, parms);
			while(rs.next())
			{
				bugs.add(new BugzillaTO(rs.getInt("bug_id"), rs.getString("who"), rs.getString("who_name"),
						rs.getTimestamp("bug_when"), rs.getString("thetext")));
			}
			return bugs;
		}
		catch(SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
}
