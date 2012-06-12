package test.extractor;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import models.Extraction;
import models.Issue;
import models.Item;

import org.junit.Test;

import comm.ComResources;

import extractor.Extractor;
public class ExtractorTest
{
	@SuppressWarnings("deprecation")
	public Item commitIDItem = createDummyItem("This is a body with a commit id inside of it 67f57daf7583b7f5edff91d975c9dd3702ba1330.",
			"This is referenced in 67f57daf7583b7f5edff91d975c9dd3702ba1330 and includes a commit id.", Timestamp.valueOf("2008-04-28 18:53:28")); 
	@SuppressWarnings("deprecation")
	public Issue jiraIssueTest = createDummyIssue("This code appears in HHH-6605.  public void addModifiedFlag(Map<String, Object> data, boolean flagValue) {\n" +
			"\tdata.put(getModifiedFlagPropertyName(), flagValue);\n" +
			"}", 
			"This test references something with @Properties", Timestamp.valueOf("2011-01-24 14:25:09"));
	
	public Extractor extractor = new Extractor();
	
	@Test
	public void TestFindCommitIDInTitle() {
		List<Extraction> results = extractor.ExtractKeys(commitIDItem);
		assertEquals(results.size(), 2);
		assertEquals(results.get(0).getTextType(), ComResources.TextType.COMMITID);
		assertEquals(results.get(1).getTextType(), ComResources.TextType.COMMITID);
	}
	

	@Test
	public void TestFindBugNumber() {
		List<Extraction> results = extractor.ExtractKeys(jiraIssueTest);
		assertEquals(results.size(), 3);
	}
	
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
