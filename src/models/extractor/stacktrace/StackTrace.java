/**
 * Slightly modified version of InfoZilla, for use 
 * by the Eggnet Social-Technical Network project.
 * 
 * Credit : InfoZilla Tool
 * <a href='http://groups.csail.mit.edu/pag/pubs/bettenburg-msr-2008.pdf'>
 * Extracting Structural Information From Bug Reports
 * </a>
 * @authors Nicolas Bettenburg, Rahul Premraj, Thomas Zimmermann, Sunghun Kim
 */
package models.extractor.stacktrace;

import java.util.ArrayList;
import java.util.List;

public class StackTrace
{
	private String			exception;
	private String			reason;
	private List<String>	frames;
	private boolean			isCause;
	private int				traceStart;
	private int				traceEnd;

	public int getTraceStart()
	{
		return this.traceStart;
	}

	public void setTraceStart(int traceStart)
	{
		this.traceStart = traceStart;
	}

	public int getTraceEnd()
	{
		return this.traceEnd;
	}

	public void setTraceEnd(int traceEnd)
	{
		this.traceEnd = traceEnd;
	}

	public StackTrace()
	{
		this.traceStart = 0;
		this.traceEnd = 0;
		this.exception = "Not specified";
		this.reason = "No reason given";
		this.frames = new ArrayList();
		this.isCause = false;
	}

	public StackTrace(String exception, String reason, List<String> frames)
	{
		this.traceStart = 0;
		this.traceEnd = 0;
		this.exception = exception;
		this.reason = reason;
		this.frames = frames;
		this.isCause = false;
	}

	public StackTrace(String exception, String reason)
	{
		this.traceStart = 0;
		this.traceEnd = 0;
		this.exception = exception;
		this.reason = reason;
		this.frames = new ArrayList();
		this.isCause = false;
	}

	public String getException()
	{
		return this.exception;
	}

	public String getReason()
	{
		if ((this.reason.startsWith(": ")) || (this.reason.startsWith(" :")))
		{
			return this.reason.substring(2);
		}
		return this.reason;
	}

	public List<String> getFrames()
	{
		return this.frames;
	}

	public void setCause(boolean isCause)
	{
		this.isCause = isCause;
	}

	public boolean isCause()
	{
		return this.isCause;
	}

	public String getFramesText()
	{
		String framesText = "";
		for (String frame : this.frames)
		{
			framesText = framesText + "at " + frame + System.getProperty("line.separator");
		}
		return framesText;
	}
}
