package com;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import db.ComDb;

public class Main
{
	public static ComDb db;
	public static void main(String[] args)
	{
		System.out.println("Com2Pgsql tool developed by eggnet at UVic.");
		try {
			if (args.length < 1)
			{
				throw new ArrayIndexOutOfBoundsException();
			}
			else
			{
				// Create the DB
				//db = new ComDb();
				//db.connect("");
				//db.createDb(args[args.length-1]);
				
				// Run Bugzilla importer
				String[] bugzillaArgs = bugzillaOptionCheck(args);
				if(bugzillaArgs != null) {
					
				}
				
				
			}
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			try {
				// Print the man page
				BufferedReader in = new BufferedReader(new FileReader("src/com/man.txt")); 
				String line;
				while ((line = in.readLine()) != null) {
					System.out.println(line);          
				}         
				in.close();
			}
			catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("There was an error printing the man page");
			}
		}
	}
	
	private static String[] bugzillaOptionCheck(String args[]) {
		for(int i = 0; i < args.length; i++) {
			if(args[i].equals("-B")) {
				String[] bugzillaArgs = {args[i+1], args[i+2]};
				return bugzillaArgs;
			}
		}
		return null;
	}
}
