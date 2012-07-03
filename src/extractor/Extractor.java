package extractor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import models.Extraction;
import models.Issue;
import models.Item;
import models.extractor.patch.Patch;
import models.extractor.sourcecode.CodeRegion;
import models.extractor.stacktrace.StackTrace;

import comm.ComResources;
import comm.RegExHelper;

import db.Resources.TextType;
import extractor.filters.FilterPatches;
import extractor.filters.FilterSourceCodeJAVA;
import extractor.filters.FilterStackTraceJAVA;

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

		if (item.getBody() != null)
			keys.addAll(extractItemBody(item, patchFilter, stacktraceFilter, sourcecodeFilter));
		if (item.getTitle() != null)
			keys.addAll(extractItemSummary(item));
		return keys;
	}

	private List<Extraction> extractItemSummary(Item item)
	{
		List<Extraction> titleKeys = new ArrayList<Extraction>();
		titleKeys.addAll(matchSHA1(item.getBody(), item.getItemDate()));
		titleKeys.addAll(matchBugNumber(item.getBody(), item.getItemDate()));
		return titleKeys;
	}
	
	private List<Extraction> extractItemBody(Item item, FilterPatches patchFilter, FilterStackTraceJAVA stacktraceFilter, FilterSourceCodeJAVA sourcecodeFilter)
	{
		List<Extraction> bodyKeys = matchSHA1(item.getBody(), item.getItemDate());

		// get the links, keywords, and commit ids
		bodyKeys.addAll(matchSHA1(item.getBody(), item.getItemDate()));
		bodyKeys.addAll(matchBugNumber(item.getBody(), item.getItemDate()));
		
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
	 * <h3>UNIMPLEMENTED</h3>
	 * @param {@link models.Issue} item
	 * @return
	 */
	public List<Extraction> ExtractKeys(Issue issue)
	{
		// This is currently unused because we aren't using any extra logic for an issue,
		// write this code in the future if the issues need more logic.
		return null;
	}

	private List<Extraction> matchBugNumber(String input, Timestamp timestamp)
	{
		List<Extraction> extractions = new LinkedList<Extraction>();
		Matcher matcher = ComResources.BUG_NUMBER_REGEX.matcher(input);
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
