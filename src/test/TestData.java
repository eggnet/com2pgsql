package test;

import java.sql.Timestamp;

import models.Issue;
import models.Item;

public class TestData
{
	public Item commitIDItem = createDummyItem("This is a body with a commit id inside of it 67f57daf7583b7f5edff91d975c9dd3702ba1330.",
			"This is referenced in 67f57daf7583b7f5edff91d975c9dd3702ba1330 and includes a commit id.", Timestamp.valueOf("2008-04-28 18:53:28")); 
	public Issue jiraIssueTest = createDummyIssue("This code appears in HHH-6605.  public void addModifiedFlag(Map<String, Object> data, boolean flagValue) {\n" +
			"\tdata.put(getModifiedFlagPropertyName(), flagValue);\n" +
			"}", 
			"This test references something with @Properties", Timestamp.valueOf("2011-01-24 14:25:09"));

	public TestData() { } 

	public Item createDummyItem(String Description, String Title, Timestamp ts)
	{
		Item newItem = new Item();
		newItem.setBody(Description);
		newItem.setTitle(Title);
		return newItem;
	}

	private Issue createDummyIssue(String description, String title, Timestamp ts)
	{
		Issue newItem = new Issue();
		newItem.setDescription(description);
		newItem.setTitle(title);
		return newItem;
	}
}
