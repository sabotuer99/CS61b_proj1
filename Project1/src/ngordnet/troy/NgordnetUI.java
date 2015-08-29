package ngordnet.troy;

import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.introcs.StdIn;
import edu.princeton.cs.introcs.In;

/** Provides a simple user interface for exploring WordNet and NGram data.
 *  @author [yournamehere mcjones]
 */
public class NgordnetUI {
    public static void main(String[] args) {
    	UiState cs = new NgordnetUI().new UiState();
    	
        In config = new In("/ngordnet/troy/ngordnetui.config");
        System.out.println("Reading ngordnetui.config...");
  
        cs.wordFile = config.readString();
        cs.countFile = config.readString();
        cs.synsetFile = config.readString();
        cs.hyponymFile = config.readString();
        System.out.println("\nBased on ngordnetui.config, using the following: \n"
                           + cs.wordFile + "\n" + cs.countFile + "\n" + cs.synsetFile +
                           "\n" + cs.hyponymFile);
        System.out.println("Loading, please wait...\n");
        cs.ngm = new NGramMap(cs.wordFile, cs.countFile);
        cs.wn = new WordNet(cs.synsetFile, cs.hyponymFile);
        cs.firstYearInWords = cs.ngm.processedHistory(new DummyProcessor()).firstKey();
        cs.firstYearInCounts = cs.ngm.totalCountHistory().firstKey();
        cs.lastYearInWords = cs.ngm.processedHistory(new DummyProcessor()).lastKey();
        cs.lastYearInCounts = cs.ngm.totalCountHistory().lastKey(); 
        cs.lineCountCounts = cs.ngm.totalCountHistory().size();
        for(int i = cs.firstYearInWords; i <= cs.lastYearInWords; i++){
        	YearlyRecord yr = cs.ngm.getRecord(i);
        	if(yr != null)
        		cs.lineCountWords += yr.size();
        }
        cs.synsetCount = cs.wn.nouns().size();
        cs.startDate = Math.max(cs.firstYearInCounts, cs.firstYearInWords);
        cs.endDate = Math.min(cs.lastYearInCounts, cs.lastYearInWords);
        cs.currentWords = new String[]{"unicorn"};
        cs.function = "counts";
        
        String[] funArray = {"counts", "weights", "wordLength", "categories", "zipf"};
        ArrayList<String> functions = new ArrayList<String>(Arrays.asList(funArray));
       
        System.out.println("Done!\n");

        System.out.println("\nPress \"Enter\" to continue....");
        StdIn.readLine();
        resetTerminal(cs);
        
        while (true) {
            System.out.print("> ");
            String line = StdIn.readLine();
            String[] rawTokens = line.split(" ");
            String command = rawTokens[0];
            String[] tokens = new String[rawTokens.length - 1];
            System.arraycopy(rawTokens, 1, tokens, 0, rawTokens.length - 1);
            switch (command) {
                case "quit": 
                    return;
                case "range": 
                	cs.startDate = Integer.parseInt(tokens[0]); 
                	cs.endDate = Integer.parseInt(tokens[1]);
                    System.out.println("Start date: " + cs.startDate);
                    System.out.println("End date: " + cs.endDate);
                    break;
                case "clear":
                	resetTerminal(cs);
                	break;
                case "function":
                	if(!functions.contains(tokens[0])){
                		System.out.println("Invalid function name!"); 
                	} else {
                		cs.function = tokens[0];
                	}
                	break;	
                case "word":
                	cs.currentWords = tokens;
                	break;
                case "plot":
                	plot(cs);
                	break;
                default:
                    System.out.println("Invalid command.");  
                    break;
            }
        }
    }
    
    private static void resetTerminal(UiState cs){
    	System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    	System.out.println("\nBased on ngordnetui.config, using the following: \n"
                + cs.wordFile + "\n" + cs.countFile + "\n" + cs.synsetFile +
                "\n" + cs.hyponymFile + "\n");
    	System.out.println("===============================COMMANDS================================");
    	System.out.println("range [startYear] [endYear]      - set working time range");
    	System.out.println("function [option]                - set function to plot (counts, weights,");
    	System.out.println("                                   wordLength, categories, zipf)");
    	System.out.println("word [word1] [word2] ...         - single word or space seperated list");
    	System.out.println("plot                             - plot with current settings");
    	System.out.println("clear                            - clean the console display");
    	System.out.println("quit                             - exit program");
    	System.out.println("");
    	System.out.println("================================STATS==================================");
    	System.out.println("Date range in word file: " + cs.firstYearInWords + " - " + cs.lastYearInWords +
    			"     Line Count: " + cs.lineCountWords);
    	System.out.println("Date range in count file: " + cs.firstYearInCounts + " - " + cs.lastYearInCounts+
    			"     Line Count: " + cs.lineCountCounts);
    	System.out.println("Total synset count: " + cs.synsetCount);
    	System.out.println("");
    	System.out.println("===========================CURRENT SETTINGS============================");
    	System.out.println("Current date range: " + cs.startDate + " - " + cs.endDate);
    	System.out.println("Current function: " + cs.function);
    	System.out.println("Current word(s): " + Arrays.toString(cs.currentWords));
    	System.out.println("");
    	System.out.println("=======================================================================\n");
    	
    }
    
    private static void plot(UiState cs){
    	switch(cs.function){
    	case "counts":
    		Plotter.plotAllWords(cs.ngm, cs.currentWords, cs.startDate, cs.endDate);
    		break;
    	case "weights":
    		Plotter.plotWeightHistory(cs.ngm, cs.currentWords[0], cs.startDate, cs.endDate);
    		if(cs.currentWords.length > 1)
    			System.out.println("Only first word was plotted");
    		break;
    	case "wordLength":
    		Plotter.plotProcessedHistory(cs.ngm, cs.startDate, cs.endDate, new WordLengthProcessor());
    		break;
    	case "categories":
    		Plotter.plotCategoryWeights(cs.ngm, cs.wn, cs.currentWords, cs.startDate, cs.endDate);
    		break;
    	case "zipf":
    		System.out.println("Plotting Zipfs law for year: " + cs.endDate);
    		Plotter.plotZipfsLaw(cs.ngm, cs.endDate);
    		break;
    	default:
    		System.out.println("Hmm... couldn't plot... =(");
    	}
    }
    
    private class UiState{
    	public String wordFile;
    	public String countFile;
    	public String synsetFile;
    	public String hyponymFile;
    	public NGramMap ngm;
        public WordNet wn;
        public int startDate;
        public int endDate;
        public int firstYearInWords;
        public int lastYearInWords;
        public int firstYearInCounts;
        public int lastYearInCounts;
        public int lineCountWords;
        public int lineCountCounts;
        public int synsetCount;
        public String function;
        public String[] currentWords;
        
    }
}
