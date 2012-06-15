package test;

import java.sql.Timestamp;

import models.Issue;
import models.Item;

public class TestData
{
	public Item commitIDItem = createDummyItem("This is a body with a commit id inside of it 67f57daf7583b7f5edff91d975c9dd3702ba1330.",
			"This is referenced in 67f57daf7583b7f5edff91d975c9dd3702ba1330 and includes a commit id.", Timestamp.valueOf("2008-04-28 18:53:28"));
	
	public Item StackTraceItem = createDummyItem(
			 "This is a stack trace\n" +
			 "java.lang.Exception" +
			 "\n\tat java.lang.Throwable.<init>(Throwable.java)" +
			 "\n\tat org.eclipse.ui.actions.DeleteResourceAction.delete" +
			 "\n(DeleteResourceAction.java:325)" +
			 "\n\tat org.eclipse.ui.actions.DeleteResourceAction.access$0" +
			 "\n(DeleteResourceAction.java:305)" +
			 "\n\tat org.eclipse.ui.actions.DeleteResourceAction$2.execute" +
			 "\n(DeleteResourceAction.java:429)" +
			 "\n\tat org.eclipse.ui.actions.WorkspaceModifyOperation$1.run" +
			 "\n(WorkspaceModifyOperation.java:91)" +
			 "\n\tat org.eclipse.core.internal.resources.Workspace.run" +
			 "\n(Workspace.java:1673)" +
			 "\n\tat org.eclipse.ui.actions.WorkspaceModifyOperation.run ..." +
			 "\n and here is some more text",
	         "This is a body with a commit id inside of it 67f57daf7583b7f5edff91d975c9dd3702ba1330.",
	         Timestamp.valueOf("2008-04-28 18:53:28")); 
	
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
