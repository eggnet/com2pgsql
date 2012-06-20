package test;

import java.sql.Timestamp;

import models.Commit;
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
			 "\n(Ejb3Configuration.java:325)" +
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
	         Timestamp.valueOf("2009-04-21 13:15:48")); 
		
	public Item CodeRegionItem = createDummyItem(
			"When using tabs to format, they should be used only for leading indents and" + 
			"\nnot to line up columns of parameters. For example:" +
			"\npublic class SomeClass {"+
			"\n\tpublic void someMethod() {"+
			"\n\t\tSystem.out.println(\"This is a test\""+
            "\n\t\t\t\t+ \"of the formatter\");"+
    		"\n\t}"+
			"\n}"+
			"In this code the second line of the println statement would be indented using"+ 
			"two tabs and then 19 spaces. This would make sure that the code lines up no"+ 
			"matter what users set their tabs to. This is a REALLY important thing for ...",		
			"This is the commit message of a commit with a snippet.",
			Timestamp.valueOf("2008-04-28 18:53:28"));
	
	public Item PatchItem = createDummyItem(
			"Index: PrecisionRectangle.java" +
			"\n===================================================================" +
			"\nRCS file: /home/tools/org.eclipse.draw2d/src/org/.../PrecisionRectangle.java,v" +
			"\nretrieving revision 1.10" +
			"\ndiff -u -r1.10 PrecisionRectangle.java" +
			"\n--- PrecisionRectangle.java\t21 Jun 2004 19:57:55 -0000\t\t1.10" +
			"\n+++ PrecisionRectangle.java\t23 Jun 2004 20:27:25 -0000" +
			"\n@@ -182,6 +182,31 @@" +
			"\n\treturn this;" +
			"\n}" +
			"\n" +
			"\n+/**" +
			"\n+ * Unions the given PrecisionRectangle with this rectangle and returns ...",
			"This is a commit message of a commit with a patch inside",
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
