package test.linker;

import java.util.List;

import jira.linker.JiraLinker;

import db.ComDb;
import db.LinkerDb;
import extractor.Extractor;

import linker.Linker;
import models.Extraction;

import org.junit.Test;
import test.TestData;

public class LinkerTest
{
	Linker l = new JiraLinker(new ComDb(), new LinkerDb());
	Extractor extractor = new Extractor();
	TestData data = new TestData();
	
	@Test
	public void TestLinker() {
		List<Extraction> results = extractor.ExtractKeys(data.StackTraceItem);
		
	}
}
