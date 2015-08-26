package ngordnet.troy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.NavigableMap;
//import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeMap;

@SuppressWarnings("serial")
public class TimeSeries<T extends Number> extends TreeMap<Integer, T> {    

	/** Constructs a new empty TimeSeries. */
    public TimeSeries(){
    	
    }

    /** Returns the years in which this time series is valid. Doesn't really
      * need to be a NavigableSet. This is a private method and you don't have 
      * to implement it if you don't want to. */
//    private NavigableSet<Integer> validYears(int startYear, int endYear){
//		return null;
//    	
//    }

    /** Creates a copy of TS, but only between STARTYEAR and ENDYEAR. 
     * inclusive of both end points. */
    public TimeSeries(TimeSeries<T> ts, int startYear, int endYear){
    	NavigableMap<Integer, T> subtree = ts.subMap(startYear, true, endYear, true);
    	for (Integer key : subtree.keySet()) {
			this.put(key, ts.get(key));
		}
    }

    /** Creates a copy of TS. */
    public TimeSeries(TimeSeries<T> ts){
    	for (Integer key : ts.keySet()) {
			this.put(key, ts.get(key));
		}
    }

    /** Returns the quotient of this time series divided by the relevant value in ts.
      * If ts is missing a key in this time series, return an IllegalArgumentException. */
    public TimeSeries<Double> dividedBy(TimeSeries<? extends Number> ts){
    	//compare years
    	if(!keySetsMatch(ts))
    		throw new IllegalArgumentException("KeySets Do Not Match!");
    	
    	TimeSeries<Double> divTree = new TimeSeries<Double>();
    	for (Integer key : ts.keySet()) {
    		Double value = this.get(key).doubleValue() / ts.get(key).doubleValue();
    		divTree.put(key, value);
		}
    	
    	return divTree;
    	
    }

    private boolean keySetsMatch(TimeSeries<? extends Number> ts) {
		boolean match = true;
		
    	if(ts == null || ts.size() != this.size()) {
    		match = false;
    	} 
    	else {
    		for(Number item: ts.keySet()){
    			if(!this.containsKey(item))
    				match = false;
    		}
    	}
    	
    	return match;	
	}

	/** Returns the sum of this time series with the given ts. The result is a 
      * a Double time series (for simplicity). */
    public TimeSeries<Double> plus(TimeSeries<? extends Number> ts) {
    	TimeSeries<Double> plusTree = new TimeSeries<Double>();
    	HashSet<Integer> allKeys = new HashSet<Integer>(ts.keySet());
    	allKeys.addAll(this.keySet());
    	
    	for (Integer key : allKeys) {
    		Number a = this.get(key);
    		Number b = ts.get(key);
    		
    		//check for nulls
    		Double orig = (a == null) ? 0 : a.doubleValue();
    		Double value = (b == null) ? 0 : b.doubleValue();
    		
    		plusTree.put(key, orig + value);
		}
    	
    	return plusTree;
    	
    }

    /** Returns all years for this time series (in any order). */
    public Collection<Number> years() {
		return new ArrayList<Number>(this.keySet());
    	
    }

    /** Returns all data for this time series. 
      * Must be in the same order as years(). */
    public Collection<Number> data() {
		return new ArrayList<Number>(this.values());
    	
    }
}
