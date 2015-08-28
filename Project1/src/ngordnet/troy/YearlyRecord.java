package ngordnet.troy;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;

public class YearlyRecord {
	HashMap<String, Integer> yearlyRecord;
	TreeMap<String, Number> yrSorted;
	
    /** Creates a new empty YearlyRecord. */
    public YearlyRecord(){
    	yearlyRecord = new HashMap<String, Integer>();
    	yrSorted = new TreeMap<String, Number>(new yrComparator());
    }

    /** Creates a YearlyRecord using the given data. */
    public YearlyRecord(HashMap<String, Integer> otherCountMap){
    	yearlyRecord = new HashMap<String, Integer>(otherCountMap);
    	yrSorted = new TreeMap<String, Number>(new yrComparator());
    	yrSorted.putAll(yearlyRecord);
    }

    /** Returns the number of times WORD appeared in this year. */
    public int count(String word) {
    	Integer count = yearlyRecord.get(word);
		return count == null ? 0 : count;
    }

    /** Records that WORD occurred COUNT times in this year. */
    public void put(String word, int count) {
    	yearlyRecord.put(word, count);
    	yrSorted.put(word, count);
    }

    /** Returns the number of words recorded this year. */
    public int size(){
		return yearlyRecord.size();
    }

    /** Returns all words in ascending order of count. */
    public Collection<String> words() {	
		return yrSorted.keySet();	
    }

    /** Returns all counts in ascending order of count. */
    public Collection<Number> counts() {
		return yrSorted.values();
    }

    /** Returns rank of WORD. Most common word is rank 1. 
      * If two words have the same rank, break ties arbitrarily. 
      * No two words should have the same rank.
      */
    public int rank(String word) {
		return yrSorted.subMap(word, true, yrSorted.lastKey(), true).size() + 1;
		//return yrSorted.subMap(yrSorted.firstKey(), true, word, true).size();
    }
    
    private class yrComparator implements Comparator<String> {

		@Override
		public int compare(String a, String b) {
			if(yearlyRecord.get(b) >= yearlyRecord.get(a)) {
				return -1;
			} else {
				return 1;
			}
		}
    	
    }
    
} 
