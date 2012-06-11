package comm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

import jira.Jira;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

import bugzilla.Bugzilla;

import db.ComDb;
import db.Resources;

public class Main
{
	public static ComDb db;
	public static Jira jira;
	
	@SuppressWarnings("static-access")
	public static void main(String[] args)
	{
//		TechnicalExtractor te = new TechnicalExtractor();
//		List<String> results = te.getTechnicalInformation(
//				"The code after \"if (callback.isAcceleratorInUse(SWT.ALT | character))\" inside " + 
//				"Eclipse's MenuManager.java removes the mnemonic, but it seems like Eclipse " + 
//				"should be checking \"isAcceleratorInUse\" only for top level menumanagers like " + 
//				"File,Edit,...,Help, etc.  : " + 
//				"/* (non-Javadoc) " + 
//				"* @see org.eclipse.jface.action.IContributionItem#update(java.lang.String) " + 
//				"*/ " + 
//				" public void update(String property) { " + 
//				"IContributionItem items[] = getItems(); " + 
//				" for (int i = 0; i < items.length; i++) { " + 
//				"items[i].update(property); " + 
//        		"}");
//		
//		for(String r: results) {
//			System.out.println("TECHNICAL");
//			System.out.println(r);
//		}
		
		System.out.println("Com2Pgsql tool developed by eggnet at UVic.");
		CommandLineParser parser = new GnuParser();
		try {
			if (args.length < 1)
			{
				throw new ArrayIndexOutOfBoundsException();
			}
			else
			{
				Options options = new Options();
				
				Option bugzilla = OptionBuilder.withArgName("b")
											   .hasArg()
											   .create("b");
				
				Option database = OptionBuilder.withArgName("d")
						   					   .hasArg()
						   					   .create("d");
				
				Option email 	= OptionBuilder.withArgName("e")
											   .hasArg()
											   .create("e");
				
				Option jiraOpt 	= OptionBuilder.withArgName("j")
											   .hasArg()
											   .create("j");
				
				options.addOption(bugzilla);
				options.addOption(database);
				options.addOption(email);
				options.addOption(jiraOpt);
				
				CommandLine line = parser.parse(options, args);
				
				// Check for database
				if(line.hasOption("d")) {
				    String[] values = line.getOptionValues("d");
				    if(values.length != 1) {
				    	System.out.println("-d flag used incorrectly.");
				    	printMan();
				    	return;
				    }
				    else {
				    	// Create the DB
				    	System.out.println("Creating database");
						db = new ComDb();
						db.connect(Resources.EGGNET_DB_NAME);
						db.createDb(line.getOptionValue("d"));
				    }
				}
				
				// Check for bugzilla
				if(line.hasOption("b")) {
				    String[] values = line.getOptionValues("b");
				    if(values.length != 1) {
				    	System.out.println("-b flag used incorrectly.");
				    	printMan();
				    	return;
				    }
				    else {
				    	Bugzilla bugz = new Bugzilla(db, line.getOptionValue("b"));
				    	bugz.parseBugzilla();
				    }
				}
				
				// Check for email
				if(line.hasOption("e")) {
				    String[] values = line.getOptionValues("e");
				    if(values.length != 1) {
				    	System.out.println("-e flag used incorrectly.");
				    	printMan();
				    	return;
				    }
				    else {
				    	
				    }
				}
				
				// Check for jira
				if (line.hasOption("j")) {
					String[] values = line.getOptionValues("j");
					if (values.length != 1) {
						System.out.println("-j flag used incorrectly");
						printMan();
						return;
					}
					else {
						System.out.println("Running Jira parser on " + line.getOptionValue("j"));
						jira = new Jira();
						jira.parseJira(line.getOptionValue("j"), db);
					}
				}
			}
		}
		catch (Exception e)
		{
			printMan();
		}
	}
	
	private static void printMan() {
		try {
			// Print the man page
			BufferedReader in = new BufferedReader(new FileReader("resources/man.txt")); 
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
