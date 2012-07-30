package test.linker;

import java.util.List;

import jira.linker.JiraLinker;
import linker.Linker;
import models.Extraction;

import org.junit.Test;

import test.TestData;
import db.SocialDb;
import db.TechnicalDb;
import extractor.Extractor;

public class LinkerTest
{
	Linker l = new JiraLinker(new SocialDb(), new TechnicalDb());
	Extractor extractor = new Extractor();
	TestData data = new TestData();
	
	@Test
	public void TestLinker() {
		List<Extraction> results = extractor.ExtractKeys(data.StackTraceItem);
		
	}
}
