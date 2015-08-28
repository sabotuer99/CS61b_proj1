package ngordnet.troy;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class NGramMap {
	private HashMap<Integer, YearlyRecord> wordCounts;
	private TimeSeries<Long> yearCounts;
	
    /** Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME. */
    public NGramMap(String wordsFilename, String countsFilename){
    	try{
    		
	    	String sCurrentLine; 
	    	wordCounts = new HashMap<Integer, YearlyRecord>();
	    	yearCounts = new TimeSeries<Long>();
	    	
	    	//process wordsFile
	    	BufferedReader wordsr = getReader(wordsFilename);		
			while ((sCurrentLine = wordsr.readLine()) != null) {
				
				String[] pieces = sCurrentLine.split("\t");
				String word = pieces[0];
				Integer year = Integer.decode(pieces[1]);
				Integer matchCount = Integer.decode(pieces[2]);
				//per spec we ignore the fourth column
				//Integer volumeCount = Integer.decode(pieces[3]);
				
				//add word and count to appropriate TimeSeries
				YearlyRecord thisYear = wordCounts.get(year);
				if(thisYear == null){
					thisYear = new YearlyRecord();
					wordCounts.put(year, thisYear);
				}
				thisYear.put(word, matchCount);	
			}
			wordsr.close();
			sCurrentLine = "";
			
	    	//process countsFile
	    	BufferedReader cntsr = getReader(countsFilename);		
			while ((sCurrentLine = cntsr.readLine()) != null) {
				
				String[] pieces = sCurrentLine.split(",");
				Integer year = Integer.decode(pieces[0]);
				Long wordCount = Long.decode(pieces[1]);

				//per spec we ignore the fourth column
				//Integer volumeCount = Integer.decode(pieces[3]);
				
				//add wordcount to yearCounts
				yearCounts.put(year, wordCount);	
			}
			cntsr.close();
			sCurrentLine = "";

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    //returns an empty (not null) reader if file not found
    //using the getReader array introduces seam to allow for non filesystem
    //based handling of WordNet constructor (requires subclassing and overriding)
    public BufferedReader getReader(String fileName){
    	try {
    		File f = new File(getClass().getResource(fileName).getFile());
			return new BufferedReader(new FileReader(f));
		} catch (Exception e) {
			System.out.println("Oops, problem with file!");
			e.printStackTrace();
			return new BufferedReader(new InputStreamReader(new ByteArrayInputStream("".getBytes())));
		}
    }
    
    /** Returns the absolute count of WORD in the given YEAR. If the word
      * did not appear in the given year, return 0. */
    public int countInYear(String word, int year){
		return year;
    	
    }

    /** Returns a defensive copy of the YearlyRecord of YEAR. */
    public YearlyRecord getRecord(int year){
		return null;
    	
    }

    /** Returns the total number of words recorded in all volumes. */
    public TimeSeries<Long> totalCountHistory(){
		return null;
    	
    }

    /** Provides the history of WORD between STARTYEAR and ENDYEAR. */
    public TimeSeries<Integer> countHistory(String word, int startYear, int endYear){
		return null;
    	
    }

    /** Provides a defensive copy of the history of WORD. */
    public TimeSeries<Integer> countHistory(String word){
		return null;
    	
    }

    /** Provides the relative frequency of WORD between STARTYEAR and ENDYEAR. */
    public TimeSeries<Double> weightHistory(String word, int startYear, int endYear){
		return null;
    	
    }

    /** Provides the relative frequency of WORD. */
    public TimeSeries<Double> weightHistory(String word){
		return null;
    	
    }

    /** Provides the summed relative frequency of all WORDS between
      * STARTYEAR and ENDYEAR. If a word does not exist, ignore it rather
      * than throwing an exception. */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words, 
                              int startYear, int endYear){
    	return null;
                              	
    }

    /** Returns the summed relative frequency of all WORDS. */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words){
		return null;
    	
    }

    /** Provides processed history of all words between STARTYEAR and ENDYEAR as processed
      * by YRP. */
    public TimeSeries<Double> processedHistory(int startYear, int endYear,
                                               YearlyRecordProcessor yrp){
												return null;
                                               	
    }

    /** Provides processed history of all words ever as processed by YRP. */
    public TimeSeries<Double> processedHistory(YearlyRecordProcessor yrp) {
		return null;
    	
    }
}
