/**
 * Slightly modified version of InfoZilla's PatchParser.java, for use 
 * by the Eggnet Social-Technical Network project.
 * 
 * Credit : InfoZilla Tool
 * <a href='http://groups.csail.mit.edu/pag/pubs/bettenburg-msr-2008.pdf'>
 * Extracting Structural Information From Bug Reports
 * </a>
 * @authors Nicolas Bettenburg, Rahul Premraj, Thomas Zimmermann, Sunghun Kim
 */
package extractor.filters;

import models.extractor.patch.Patch;
import models.extractor.patch.PatchHunk;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PatchParser
{
	private static final boolean	debug	= false;

	private int findNextIndex(String[] lines, int start)
	{
		int found = -1;
		for (int i = start; i < lines.length - 1; i++)
		{
			if (!lines[i].startsWith("Index: "))
				continue;
			if (lines[(i + 1)].startsWith("===="))
			{
				found = i;
				break;
			}

		}

		return found;
	}

	private int findNextHunkHeader(String[] lines, int start)
	{
		int found = -1;
		for (int i = start; i < lines.length; i++)
		{
			if (lines[i].matches("^@@\\s\\-\\d+,\\d+\\s\\+\\d+,\\d+\\s@@$"))
			{
				found = i;
				break;
			}
		}
		return found;
	}

	private List<String> partitionByIndex(String text)
	{
		List<String> indexPartition = new ArrayList<String>();

		String[] lines = text.split("[\n\r]");

		boolean hasMore = true;

		int idxStart = -1;

		while (hasMore)
		{
			idxStart = findNextIndex(lines, idxStart + 1);
			if (idxStart == -1)
			{
				hasMore = false;
			}
			else
			{
				int idxEnd = findNextIndex(lines, idxStart + 1);
				if (idxEnd == -1)
				{
					String range = "";
					for (int i = idxStart; i < lines.length - 1; i++)
					{
						range = range + lines[i] + System.getProperty("line.separator");
					}
					range = range + lines[(lines.length - 1)];
					indexPartition.add(range);
				}
				else
				{
					String range = "";
					for (int i = idxStart; i < idxEnd; i++)
					{
						range = range + lines[i] + System.getProperty("line.separator");
					}
					indexPartition.add(range);

					idxStart = idxEnd - 1;
				}
			}
		}

		return indexPartition;
	}

	private int findFirstLineBeginningWith(String text, String[] lines, int start)
	{
		int found = -1;
		for (int i = start; i < lines.length; i++)
		{
			if (lines[i].startsWith(text))
			{
				found = i;
				break;
			}
		}
		return found;
	}

	private String findFirstLineBeginningWithS(String text, String[] lines, int start)
	{
		String found = "";
		for (int i = start; i < lines.length; i++)
		{
			if (lines[i].startsWith(text))
			{
				found = lines[i];
				break;
			}
		}
		return found;
	}

	private boolean isHunkLine(String line)
	{
		boolean isHunkLine = (line.startsWith("+")) || (line.startsWith("-")) || (line.startsWith(" "));
		return isHunkLine;
	}

	private List<PatchHunk> findAllHunks(String[] lines, int start)
	{
		List<PatchHunk> foundHunks = new ArrayList<PatchHunk>();
		String lineSep = System.getProperty("line.separator");
		int hStart = start - 1;
		boolean hasMore = true;
		while (hasMore)
		{
			hStart = findNextHunkHeader(lines, hStart + 1);

			if (hStart == -1)
			{
				hasMore = false;
			}
			else
			{
				int nextHunkStart = findNextHunkHeader(lines, hStart + 1);
				int searchEnd = 0;
				if (nextHunkStart == -1)
				{
					searchEnd = lines.length;
					hasMore = false;
				}
				else
				{
					searchEnd = nextHunkStart - 1;
				}

				String hunktext = "";
				for (int i = hStart + 1; i < searchEnd; i++)
				{
					if (isHunkLine(lines[i]))
					{
						hunktext = hunktext + lines[i] + lineSep;
					}
					else
						if (i < searchEnd - 1)
						{
							if (isHunkLine(lines[(i + 1)]))
							{
								hunktext = hunktext + lines[i] + lineSep;
							}
							else
							{
								searchEnd = i;
							}
						}

				}

				if (hunktext.length() > 1)
					hunktext = hunktext.substring(0, hunktext.length() - 1);
				foundHunks.add(new PatchHunk(hunktext));
				hStart = nextHunkStart - 1;
			}
		}
		return foundHunks;
	}

	public List<Patch> parseForPatches(String text)
	{
		List<Patch> foundPatches = new ArrayList<Patch>();

		List<String> indexPartition = partitionByIndex(text);

		for (String potentialPatch : indexPartition)
		{
			String[] lines = potentialPatch.split("[\n\r]");

			Patch patch = new Patch();

			String pIndex = findFirstLineBeginningWithS("Index: ", lines, 0);
			patch.setIndex(pIndex);
			String pOrig = findFirstLineBeginningWithS("--- ", lines, 0);
			patch.setOriginalFile(pOrig);
			String pModi = findFirstLineBeginningWithS("+++ ", lines, 0);
			patch.setModifiedFile(pModi);

			int pModiNum = findFirstLineBeginningWith("+++ ", lines, 0);
			int firstHunkLine = findNextHunkHeader(lines, pModiNum + 1);

			if (firstHunkLine == -1)
			{
				break;
			}
			String header = "";
			for (int i = 0; i < firstHunkLine - 1; i++)
			{
				header = header + lines[i] + System.getProperty("line.separator");
			}
			header = header + lines[(firstHunkLine - 1)];
			patch.setHeader(header);

			List<PatchHunk> hunks = findAllHunks(lines, firstHunkLine);
			PatchHunk h;
			for (Iterator<PatchHunk> localIterator2 = hunks.iterator(); localIterator2.hasNext(); patch.addHunk(h))
				h = (PatchHunk) localIterator2.next();
			foundPatches.add(patch);
		}

		for (Patch p : foundPatches)
		{
			int patchStart = text.indexOf(p.getHeader());

			int patchEnd = text.lastIndexOf(((PatchHunk) p.getHunks().get(p.getHunks().size() - 1)).getText())
					+ ((PatchHunk) p.getHunks().get(p.getHunks().size() - 1)).getText().length();

			p.setStartPosition(patchStart);
			p.setEndPosition(patchEnd);
		}

		return foundPatches;
	}
}
