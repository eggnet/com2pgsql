package test.extractor;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import models.Extraction;
import models.Issue;
import models.Item;

import org.junit.Test;

import test.TestData;

import comm.ComResources;

import extractor.Extractor;
public class ExtractorTest
{
	public Extractor extractor = new Extractor();
	public TestData data = new TestData();
	@Test
	public void TestFindCommitIDInTitle() {
		List<Extraction> results = extractor.ExtractKeys(data.commitIDItem);
		assertEquals(results.size(), 2);
		assertEquals(results.get(0).getTextType(), ComResources.TextType.COMMITID);
		assertEquals(results.get(1).getTextType(), ComResources.TextType.COMMITID);
	}
	
	@Test
	public void TestFindBugNumber() {
		List<Extraction> results = extractor.ExtractKeys(data.jiraIssueTest);
		assertEquals(results.size(), 3);
	}
}
