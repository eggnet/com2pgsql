/**
 * Slightly modified version of InfoZilla's FilterSourceCodeJAVA.java, for use 
 * by the Eggnet Social-Technical Network project.
 * 
 * Credit : InfoZilla Tool
 * <a href='http://groups.csail.mit.edu/pag/pubs/bettenburg-msr-2008.pdf'>
 * Extracting Structural Information From Bug Reports
 * </a>
 * @authors Nicolas Bettenburg, Rahul Premraj, Thomas Zimmermann, Sunghun Kim
 */
package extractor.filters;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import models.extractor.sourcecode.CodeRegion;
import com.Ostermiller.util.*;

import comm.RegExHelper;

public class FilterSourceCodeJAVA implements IFilter
{
	private HashMap<String, Pattern>	codePatterns;
	private HashMap<String, String>		codePatternOptions;
	private FilterTextRemover			textRemover;

	public FilterSourceCodeJAVA()
	{
		this.codePatterns = new HashMap<String, Pattern>();
		this.codePatternOptions = new HashMap<String, String>();
	}

	public FilterSourceCodeJAVA(String filename)
	{
		this.codePatterns = new HashMap<String, Pattern>();
		this.codePatternOptions = new HashMap<String, String>();
		try
		{
			readCodePatterns(filename);
		}
		catch (Exception e)
		{
			System.err.println("Error while reading Java Source Code Patterns!");
			e.printStackTrace();
		}
	}

	public FilterSourceCodeJAVA(URL fileurl)
	{
		this.codePatterns = new HashMap<String, Pattern>();
		this.codePatternOptions = new HashMap<String, String>();
		try
		{
			readCodePatterns(fileurl.openStream());
		}
		catch (Exception e)
		{
			System.err.println("Error while reading Java Source Code Patterns!");
			e.printStackTrace();
		}
	}

	private List<CodeRegion> getCodeRegions(String s, boolean minimalSet)
	{
		List<CodeRegion> codeRegions = new ArrayList<CodeRegion>();

		for (String keyword : this.codePatterns.keySet())
		{
			Pattern p = (Pattern) this.codePatterns.get(keyword);
			String patternOptions = (String) this.codePatternOptions.get(keyword);
			if (patternOptions.contains("MATCH"))
			{
				for (MatchResult r : RegExHelper.findMatches(p, s))
				{
					int offset = findMatch(s, '{', '}', r.end());
					CodeRegion foundRegion = new CodeRegion(r.start(), r.end() + offset, keyword, s.substring(
							r.start(), r.end() + offset));
					codeRegions.add(foundRegion);
				}
			}
			else
			{
				for (MatchResult r : RegExHelper.findMatches(p, s))
				{
					CodeRegion foundRegion = new CodeRegion(r.start(), r.end(), keyword, r.group());
					codeRegions.add(foundRegion);
				}
			}

		}

		if (minimalSet)
		{
			return makeMinimalSet(codeRegions);
		}
		return codeRegions;
	}

	private int findMatch(String where, char opening, char closing, int start)
	{
		String region = where.substring(start);
		int level = 0;
		int position = 0;
		for (char c : region.toCharArray())
		{
			position++;
			if (c == opening)
				level++;
			if (c == closing)
			{
				if (level == 0)
					return position;
				level--;
			}
		}
		return 0;
	}

	private void readCodePatterns(String filename) throws Exception
	{
		BufferedReader fileInput = new BufferedReader(new FileReader(filename));

		String inputLine = null;
		while ((inputLine = fileInput.readLine()) != null)
		{
			if (inputLine.substring(0, 2).equalsIgnoreCase("//"))
				continue;
			String[][] parsedLine = CSVParser.parse(inputLine);
			String keyword = parsedLine[0][0];
			String pattern = parsedLine[0][1];

			if (parsedLine[0].length == 3)
			{
				String options = parsedLine[0][2];
				this.codePatternOptions.put(keyword, options);
			}
			else
			{
				this.codePatternOptions.put(keyword, "");
			}
			Pattern somePattern = Pattern.compile(pattern);
			this.codePatterns.put(keyword, somePattern);
		}
	}

	private void readCodePatterns(InputStream instream) throws Exception
	{
		BufferedReader fileInput = new BufferedReader(new InputStreamReader(instream));

		String inputLine = null;
		while ((inputLine = fileInput.readLine()) != null)
		{
			if (inputLine.substring(0, 2).equalsIgnoreCase("//"))
				continue;
			String[][] parsedLine = CSVParser.parse(inputLine);
			String keyword = parsedLine[0][0];
			String pattern = parsedLine[0][1];

			if (parsedLine[0].length == 3)
			{
				String options = parsedLine[0][2];
				this.codePatternOptions.put(keyword, options);
			}
			else
			{
				this.codePatternOptions.put(keyword, "");
			}
			Pattern somePattern = Pattern.compile(pattern);
			this.codePatterns.put(keyword, somePattern);
		}
	}

	public static List<CodeRegion> makeMinimalSet(List<CodeRegion> regionList)
	{
		List<CodeRegion> sortedRegionList = new ArrayList<CodeRegion>(regionList);

		Collections.sort(sortedRegionList);

		List<CodeRegion> minimalSet = new ArrayList<CodeRegion>();

		for (int i = 0; i < sortedRegionList.size(); i++)
		{
			CodeRegion thisRegion = (CodeRegion) sortedRegionList.get(i);
			boolean contained = false;
			for (int j = 0; j < i; j++)
			{
				CodeRegion thatRegion = (CodeRegion) sortedRegionList.get(j);
				if (thatRegion.end >= thisRegion.end)
				{
					contained = true;
				}
			}
			if (!contained)
			{
				minimalSet.add(thisRegion);
			}
		}
		return minimalSet;
	}

	public String getOutputText()
	{
		return this.textRemover.doDelete();
	}

	public List<CodeRegion> runFilter(String inputText)
	{
		this.textRemover = new FilterTextRemover(inputText);

		List<CodeRegion> codeRegions = getCodeRegions(inputText, true);

		for (CodeRegion region : codeRegions)
		{
			this.textRemover.markForDeletion(region.start, region.end);
		}

		return codeRegions;
	}
}
