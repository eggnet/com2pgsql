package test.extractor;

import static org.junit.Assert.assertEquals;

import java.util.List;

import models.Extraction;

import org.junit.Test;

import test.TestData;

import comm.ComResources;

import extractor.Extractor;

public class ExtractorTest
{
	public Extractor	extractor	= new Extractor();
	public TestData		data		= new TestData();

	@Test
	public void TestFindCommitIDInTitle()
	{
		List<Extraction> results = extractor.ExtractKeys(data.commitIDItem);
		assertEquals(results.size(), 2);
		assertEquals(results.get(0).getTextType(), ComResources.TextType.COMMITID);
		assertEquals(results.get(1).getTextType(), ComResources.TextType.COMMITID);
	}

	@Test
	public void TestStacktrace()
	{
		List<Extraction> results = extractor.ExtractKeys(data.StackTraceItem);
		assertEquals(results.size(), 1);
	}

	@Test
	public void TestFindBugNumber()
	{
		List<Extraction> results = extractor.ExtractKeys(data.jiraIssueTest);
		assertEquals(results.size(), 3);
	}
}
