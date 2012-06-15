/**
 * Slightly modified version of InfoZilla's IFilter.java, for use 
 * by the Eggnet Social-Technical Network project.
 * 
 * Credit : InfoZilla Tool
 * <a href='http://groups.csail.mit.edu/pag/pubs/bettenburg-msr-2008.pdf'>
 * Extracting Structural Information From Bug Reports
 * </a>
 * @authors Nicolas Bettenburg, Rahul Premraj, Thomas Zimmermann, Sunghun Kim
 */
package extractor.filters;

import java.util.List;

public abstract interface IFilter
{
  public abstract List<?> runFilter(String paramString);

  public abstract String getOutputText();
}