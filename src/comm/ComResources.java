package comm;

import java.text.SimpleDateFormat;

import db.Resources;

public class ComResources extends Resources
{
	public enum CommType {
		EMAIL, BUGZILLA, JIRA, ISSUE, GITHUB, FORUM
	}
	public static final SimpleDateFormat JiraDateFormat = new SimpleDateFormat("yyyy-mm-dd kk:mm:ss.SSSZ");
	public enum TextType {
		PATCH, SOURCE, TRACE, NAME
	}
	
	public static final int JIRA_MAX_RESULTS = 50;
	public static int DB_LIMIT = 2000;
}
