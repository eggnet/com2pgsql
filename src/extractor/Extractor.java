package extractor;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import comm.ComResources;
import comm.ComResources.TextType;

import models.Extraction;
import models.Issue;
import models.Item;

/**
 * <p>Parses {@link models.Item}'s and extracts relevant technical information on them.</p> 
 * The different pieces are 
 * <ul>
 * 	<li>CommitIDs (SHA-1) -- Best case</li>
 *  <li>Patches (diffs)</li>
 *  <li>Stacktraces</li>
 *  <li>Snippets</li>
 *  <li>Keywords (filenames, classes, methods, annotations, etc.)</li> 
 *</ul>
 *
 * <p>Once the files are found, we try to find clues at which commits may be referenced by the communication<br>
 * then based on the files changed, stacktraces matched, diffs, matching code snippets, and keywords, we <br>
 * determine a link confidence level for the item-commit relationship.</p>
 * 
 * @author braden
 *
 */
public class Extractor
{
	public Map<Integer, List<Extraction>> ItemExtractionsMap;
	
	/**
	 * Extracts the technical information from the given {@link models.Item} and parses it into <br>
	 * the technical extractions {@link models.Extraction}.
	 * @param {@link models.Item} item
	 * @return
	 */
	public List<Extraction> ExtractKeys(Item item)
	{
		List<Extraction> keys = new LinkedList<Extraction>();
		
		// First search through the title.
		List<Extraction> titleKeys = matchSHA1(item.getTitle(), item.getItemDate());
		titleKeys.addAll(matchKeywords(item.getTitle(), item.getItemDate()));
		
		List<Extraction> bodyKeys = matchSHA1(item.getTitle(), item.getItemDate());
		
		// TODO other types
		
		bodyKeys.addAll(matchKeywords(item.getTitle(), item.getItemDate()));
		
		keys.addAll(titleKeys);
		keys.addAll(bodyKeys);
		return keys;
	}

	/**
	 * Extracts the technical information from the given {@link models.Issue} and parses it into <br>
	 * the technical extractions {@link models.Extraction}.
	 * @param {@link models.Issue} item
	 * @return
	 */
	public List<Extraction> ExtractKeys(Issue issue)
	{
		// get all item keys.
		List<Extraction> keys = new LinkedList<Extraction>();
		
		// First search through the title.
		List<Extraction> titleKeys = matchSHA1(issue.getTitle(), issue.getCreationTS());
		titleKeys.addAll(matchBugNumber(issue.getTitle(), issue.getCreationTS()));
		titleKeys.addAll(matchKeywords(issue.getTitle(), issue.getCreationTS()));
		
		List<Extraction> bodyKeys = matchSHA1(issue.getDescription(), issue.getCreationTS());
		bodyKeys.addAll(matchBugNumber(issue.getDescription(), issue.getCreationTS()));
		bodyKeys.addAll(matchKeywords(issue.getDescription(), issue.getCreationTS()));
		
		keys.addAll(titleKeys);
		keys.addAll(bodyKeys);
		return keys;
	}
	
	private List<Extraction> matchBugNumber(String input, Timestamp timestamp)
	{
		List<Extraction> extractions = new LinkedList<Extraction>();
		Matcher matcher = ComResources.BUG_NUMBER_REGEX.matcher(input);
		while (matcher.find())
			extractions.add(new Extraction(TextType.COMMITID, matcher.group(), timestamp));
		return extractions;
	}
	
	private List<Extraction> matchKeywords(String input, Timestamp timestamp)
	{
		List<Extraction> extractions = new LinkedList<Extraction>();
		Matcher matcher = ComResources.KEYWORDS_REGEX.matcher(input);
		while (matcher.find())
			extractions.add(new Extraction(TextType.COMMITID, matcher.group(), timestamp));
		return extractions;
	}
	
	private List<Extraction> matchSHA1(String input, Timestamp timestamp)
	{
		List<Extraction> extractions = new LinkedList<Extraction>();
		Matcher matcher = ComResources.SHA1_REGEX.matcher(input);
		while (matcher.find())
			extractions.add(new Extraction(TextType.COMMITID, matcher.group(), timestamp));
		return extractions;
	}
}
