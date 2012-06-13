package comm;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import db.Resources;

public class ComResources extends Resources
{
	public enum CommType {
		EMAIL, BUGZILLA, JIRA, ISSUE, GITHUB, FORUM
	}
	public enum TextType {
		PATCH, SOURCE, TRACE, NAME, KEYWORD, COMMITID
	}
	
	public static final boolean LINK_FROM_COMMIT_MSGS = true;
	public static final SimpleDateFormat JiraDateFormat = new SimpleDateFormat("yyyy-mm-dd kk:mm:ss.SSSZ");  
	public static final Pattern SHA1_REGEX = Pattern.compile("[0-9a-f]{5,40}");
	public static final Pattern BUG_NUMBER_REGEX = Pattern.compile("([A-Z]{2,4}-[0-9]{2,4})|(\\[([A-Z]{2,4}-[0-9]{2,4})\\])"); // TODO add cases for bugzilla
	public static final Pattern KEYWORDS_REGEX = Pattern.compile("^$"); // TODO @braden waiting on the JAR from adrian
	public static final int JIRA_MAX_RESULTS = 50;
	public static int DB_LIMIT = 2000;
}
