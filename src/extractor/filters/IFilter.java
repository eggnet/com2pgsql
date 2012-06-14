package extractor.filters;

import java.util.List;

public abstract interface IFilter
{
  public abstract List<?> runFilter(String paramString);

  public abstract String getOutputText();
}