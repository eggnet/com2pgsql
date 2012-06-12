package extractor;

import java.util.List;
import java.util.Map;

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
public class TechnicalExtractor
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
		// TODO @braden
		return null;
	}

	/**
	 * Extracts the technical information from the given {@link models.Issue} and parses it into <br>
	 * the technical extractions {@link models.Extraction}.
	 * @param {@link models.Issue} item
	 * @return
	 */
	public List<Extraction> ExtractKeys(Issue issue)
	{
		// TODO @braden
		return null;
	}
}
