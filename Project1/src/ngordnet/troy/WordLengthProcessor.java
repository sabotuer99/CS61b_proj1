package ngordnet.troy;

public class WordLengthProcessor implements YearlyRecordProcessor {
    public double process(YearlyRecord yearlyRecord) {
    	if(yearlyRecord == null)
    		return 0;
    	
    	Long runningTotal = 0L;
    	Long wordCount = 0L;
    	
		for (String word : yearlyRecord.words()) {
			Integer count = yearlyRecord.count(word);
			wordCount += count;
			runningTotal += word.length() * count;
		}
    	
    	return runningTotal.doubleValue()/wordCount.doubleValue();
    }
}
