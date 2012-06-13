package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import models.Commit;

public class LinkerDb extends DbConnection
{
	public LinkerDb() { }
	
	public Set<Commit> getAllCommits()
	{
		Set<Commit> commits = new HashSet<Commit>();
		
		try {
			String sql = "SELECT commit_id, author, author_email, comments, commit_date, branch_id from commits where (branch_id is NULL OR branch_id=?)";
			String[] parms = {branchID};
			ResultSet rs = execPreparedQuery(sql, parms);
			while(rs.next())
			{
				commits.add(new Commit(
						rs.getString("commit_id"),
						rs.getString("author"),
						rs.getString("author_email"),
						rs.getString("comments"),
						rs.getTimestamp("commit_date"),
						rs.getString("branch_id")
				));
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return commits;
	}
}
