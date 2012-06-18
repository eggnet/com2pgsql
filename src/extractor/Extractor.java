package extractor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import comm.ComResources;
import comm.RegExHelper;
import comm.ComResources.TextType;
import extractor.filters.FilterPatches;
import extractor.filters.FilterSourceCodeJAVA;
import extractor.filters.FilterStackTraceJAVA;

import models.Extraction;
import models.Issue;
import models.Item;
import models.extractor.patch.Patch;
import models.extractor.patch.PatchHunk;
import models.extractor.sourcecode.CodeRegion;
import models.extractor.stacktrace.StackTrace;

/**
 * <p>
 * Parses {@link models.Item}'s and extracts relevant technical information on
 * them.
 * </p>
 * The different pieces are
 * <ul>
 * <li>CommitIDs (SHA-1) -- Best case</li>
 * <li>Patches (diffs)</li>
 * <li>Stacktraces</li>
 * <li>Snippets</li>
 * <li>Keywords (filenames, classes, methods, annotations, etc.)</li>
 *</ul>
 * 
 * <p>
 * Once the files are found, we try to find clues at which commits may be
 * referenced by the communication<br>
 * then based on the files changed, stacktraces matched, diffs, matching code
 * snippets, and keywords, we <br>
 * determine a link confidence level for the item-commit relationship.
 * </p>
 * 
 * @author braden
 * 
 */
public class Extractor
{
	public Map<Integer, List<Extraction>>	ItemExtractionsMap;

	/**
	 * Extracts the technical information from the given {@link models.Item} and
	 * parses it into <br>
	 * the technical extractions {@link models.Extraction}.
	 * 
	 * @param {@link models.Item} item
	 * @return
	 */
	public List<Extraction> ExtractKeys(Item item)
	{
		FilterPatches patchFilter = new FilterPatches();
		FilterStackTraceJAVA stacktraceFilter = new FilterStackTraceJAVA();
		FilterSourceCodeJAVA sourcecodeFilter = new FilterSourceCodeJAVA(ComResources.class.getResource("Java_CodeDB.txt"));

		List<Extraction> keys = new LinkedList<Extraction>();

		keys.addAll(extractItemBody(item, patchFilter, stacktraceFilter, sourcecodeFilter));
		keys.addAll(extractItemSummary(item));
		return keys;
	}

	public List<Extraction> extractItemSummary(Item item)
	{
		List<Extraction> titleKeys = new ArrayList<Extraction>();
		titleKeys.addAll(matchSHA1(item.getBody(), item.getItemDate()));
		titleKeys.addAll(matchBugNumber(item.getBody(), item.getItemDate()));
		titleKeys.addAll(matchKeywords(item.getBody(), item.getItemDate()));
		return titleKeys;
	}
	
	public List<Extraction> extractItemBody(Item item, FilterPatches patchFilter, FilterStackTraceJAVA stacktraceFilter, FilterSourceCodeJAVA sourcecodeFilter)
	{
		List<Extraction> bodyKeys = matchSHA1(item.getTitle(), item.getItemDate());

		// get the links, keywords, and commit ids
		bodyKeys.addAll(matchSHA1(item.getBody(), item.getItemDate()));
		bodyKeys.addAll(matchBugNumber(item.getBody(), item.getItemDate()));
		bodyKeys.addAll(matchKeywords(item.getBody(), item.getItemDate()));
		
		String outputBodyText = RegExHelper.makeLinuxNewlines(item.getBody());
		
		List<Patch> patches = patchFilter.runFilter(outputBodyText);
		outputBodyText = patchFilter.getOutputText();
		
		List<StackTrace> traces = stacktraceFilter.runFilter(outputBodyText);
		outputBodyText = stacktraceFilter.getOutputText();
		
		List<CodeRegion> regions = sourcecodeFilter.runFilter(outputBodyText);
		outputBodyText = stacktraceFilter.getOutputText();
		
		bodyKeys.addAll(traces);
		bodyKeys.addAll(regions);
		bodyKeys.addAll(patches);
		return bodyKeys;
	}
	
	/**
	 * Extracts the technical information from the given {@link models.Issue}
	 * and parses it into <br>
	 * the technical extractions {@link models.Extraction}.
	 * 
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
