package comm;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellChecker;

public class TechnicalExtractor //implements SpellCheckListener
{
	private SpellChecker spellChecker;
	private ArrayList listOfMisspelledWords;
	
	public TechnicalExtractor()
	{
		File dict = new File("resources/english.0");
	    try {
	      spellChecker = new SpellChecker(new SpellDictionaryHashMap(dict));
	    }
	    catch (FileNotFoundException e) {
	      System.err.println("Dictionary File '" + dict + "' not found! Quitting. " + e);
	      System.exit(1);
	    }
	    catch (IOException ex) {
	      System.err.println("IOException occurred while trying to read the dictionary file: " + ex);
	      System.exit(2);
	    }
	}
	
	public List<String> getTechnicalInformation(String body) {
		List<String> technicalInformation = new ArrayList<String>();
		
		String[] split = body.split("\\s");
		String currentPhrase = "";
		for(String word: split) {
			if(isTechnicalWord(word)) {
				currentPhrase = currentPhrase.concat(" " + word);
			}
			else {
				if(!currentPhrase.equals("")) {
					technicalInformation.add(currentPhrase.trim());
					currentPhrase = "";
				}
			}
		}
		
		return technicalInformation;
	}
	
	private boolean isTechnicalWord(String word) {
		if(isSpellingMistake(word)) {
			return (checkCamelCase(word) || 
					checkKeywords(word) ||
					checkSpecialCharacters(word));
		}
		return false;
	}
	
	private boolean isSpellingMistake(String word) {
		return !spellChecker.isCorrect(word);
	}
	
	private boolean checkCamelCase(String token) {
		return(token.matches(".*(([A-Z_][a-z_0-9]*)+[A-Z_0-9][a-z_0-9]+([A-Z_0-9][a-z_0-9]*)*).*") ||
				token.matches(".*(([a-z_][a-z_0-9]*)+([A-Z_0-9][a-z_0-9]*)+).*") ||
				token.matches(".*(([A-Z_][A-Z_0-9]+)).*") ||
				token.matches(".*((([A-Z_][a-z_0-9]*)+([A-Z_0-9]+))).*"));
	}
	
	private boolean checkKeywords(String token) {
		try{
			FileInputStream fstream = new FileInputStream("resources/keywords.txt");
			
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String keyword;
			
			while ((keyword = br.readLine()) != null)   {
				keyword = keyword.replace(System.getProperty("line.separator"), "");
				if(token.equals(keyword))
					return true;
			}
			//Close the input stream
			in.close();
		}
		catch (Exception e){//Catch exception if any
			System.err.println("Could not check keywords file");
		}
		return false;
	}
	
	private boolean checkSpecialCharacters(String token) {
		int count = 0;
		for (int i=0; i<  token.length(); i++) {
            char ch = token.charAt(i);
            if (!Character.isLetterOrDigit(ch))
                count++;
        }
		return count > 2;
	}
}
