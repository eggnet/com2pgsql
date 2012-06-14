package extractor.filters;

import models.extractor.patch.Patch;
import java.util.List;

import extractor.RelaxedPatchParser;

public class FilterPatches implements IFilter
{
	private FilterTextRemover	textRemover;
	private boolean				relaxed	= false;

	private List<Patch> getPatches(String text)
	{
		this.textRemover = new FilterTextRemover(text);

		List<Patch> foundPatches = null;
		if (isRelaxed())
		{
			RelaxedPatchParser pp = new RelaxedPatchParser();
			foundPatches = pp.parseForPatches(text);
		}
		else
		{
			PatchParser pp = new PatchParser();
			foundPatches = pp.parseForPatches(text);
		}

		for (Patch patch : foundPatches)
		{
			this.textRemover.markForDeletion(patch.getStartPosition(), patch.getEndPosition());
		}
		return foundPatches;
	}

	public String getOutputText()
	{
		return this.textRemover.doDelete();
	}

	public List<Patch> runFilter(String inputText)
	{
		return getPatches(inputText);
	}

	public boolean isRelaxed()
	{
		return this.relaxed;
	}

	public void setRelaxed(boolean relaxed)
	{
		this.relaxed = relaxed;
	}
}
