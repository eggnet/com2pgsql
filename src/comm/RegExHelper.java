/**
 * Slightly modified version of InfoZilla's FilterPatches.java, for use 
 * by the Eggnet Social-Technical Network project.
 * 
 * Credit : InfoZilla Tool
 * <a href='http://groups.csail.mit.edu/pag/pubs/bettenburg-msr-2008.pdf'>
 * Extracting Structural Information From Bug Reports
 * </a>
 * @authors Nicolas Bettenburg, Rahul Premraj, Thomas Zimmermann, Sunghun Kim
 */

package comm;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExHelper
{
	public static Iterable<MatchResult> findMatches(Pattern p, CharSequence s)
	{
		List<MatchResult> results = new ArrayList<MatchResult>();

		for (Matcher m = p.matcher(s); m.find();)
		{
			results.add(m.toMatchResult());
		}
		return results;
	}

	public static String makeLinuxNewlines(String input)
	{
		String output = input.replaceAll("(([\r][\n])|([\r]))", "\n");
		return output;
	}
}
