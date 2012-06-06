package github;

import java.net.MalformedURLException;
import java.net.URL;

public class Github
{
	public URL projectUrl;
	public void parse(String location){
		try
		{
			projectUrl = new URL(location);
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		} 
	}
}
