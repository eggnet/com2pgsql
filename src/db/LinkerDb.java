package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import comm.ComResources;

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
	
	public List<Commit> getCommitsAroundDate(Timestamp date) {
		try {
			List<Commit> commits = new ArrayList<Commit>();
			
			String sql = "SELECT commit_id, author, author_email, comments, commit_date, branch_id from commits WHERE" +
					" (branch_id is NULL OR branch_id=?) AND" +
					" commit_date <= ? + interval " + ComResources.COMMIT_DATE_MAX_RANGE + " day";
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
			return commits;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getFilesChangedOnCommit(Commit commit) {
		try {
			List<String> files = new ArrayList<String>();
			String sql = "SELECT distinct file_id FROM file_diffs WHERE" +
					" new_commit_id=?";
			String[] parms = {commit.getCommit_id()};
			ResultSet rs = execPreparedQuery(sql, parms);
			while(rs.next())
			{
				files.add(rs.getString("file_id"));
			}
			return files;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
