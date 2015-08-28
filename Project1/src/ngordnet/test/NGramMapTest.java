package ngordnet.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import ngordnet.troy.NGramMap;
import ngordnet.troy.TimeSeries;
import ngordnet.troy.YearlyRecord;

import org.junit.Ignore;
import org.junit.Test;

public class NGramMapTest {
	@Ignore
	@Test
	public void filesShouldLoadInUnder25Seconds(){
		//Arrange
		
		//Act
		Stopwatch sw = new Stopwatch();
		@SuppressWarnings("unused")
		NGramMap ng = new NGramMap("/ngordnet/data/ngrams/all_words.csv", "/ngordnet/data/ngrams/total_counts.csv");
		long time = (long)(sw.elapsedTime() * 1000);
		
		//Assert
		System.out.println("Elapsed time: " + time);
		assertTrue(time < 25000);
	}
	
	@Test
	public void smallFilesShouldLoadSuperFast(){
		//Arrange
		
		//Act
		Stopwatch sw = new Stopwatch();
		@SuppressWarnings("unused")
		NGramMap ng = new NGramMap("/ngordnet/data/ngrams/very_short.csv", "/ngordnet/data/ngrams/total_counts.csv");
		long time = (long)(sw.elapsedTime() * 1000);
		
		//Assert
		//System.out.println("Elapsed time: " + time);
		assertTrue(time < 1000);
	}
	
	@Test
	public void countInYear_NoDataForYear_ReturnZero(){
		//Arrange
		NGramMap sut = new NGramMap("/ngordnet/data/ngrams/very_short.csv", "/ngordnet/data/ngrams/total_counts.csv");
		
		//Act
		int count = sut.countInYear("airport", 1960);
		
		//Assert
		assertEquals(0, count);
	}
	
	@Test
	public void countInYear_WordNotInYear_ReturnZero(){
		//Arrange
		NGramMap sut = new NGramMap("/ngordnet/data/ngrams/very_short.csv", "/ngordnet/data/ngrams/total_counts.csv");
		
		//Act
		int count = sut.countInYear("invalid", 2005);
		
		//Assert
		assertEquals(0, count);
	}
	
	@Test
	public void countInYear_GoodWordAndYear_ReturnCount(){
		//Arrange
		NGramMap sut = new NGramMap("/ngordnet/data/ngrams/very_short.csv", "/ngordnet/data/ngrams/total_counts.csv");
		
		//Act
		int count = sut.countInYear("wandered", 2005);
		
		//Assert
		assertEquals(83769, count);
	}
	
	@Test
	public void getRecord_GoodYear_ReturnsData(){
		//Arrange
		NGramMap sut = new NGramMap("/ngordnet/data/ngrams/very_short.csv", "/ngordnet/data/ngrams/total_counts.csv");
		
		//Act
		YearlyRecord record = sut.getRecord(2007);
		
		//Assert
		assertEquals(3, record.size());
	}
	
	@Test
	public void getRecord_NoDataYear_ReturnsEmptyRecord(){
		//Arrange
		NGramMap sut = new NGramMap("/ngordnet/data/ngrams/very_short.csv", "/ngordnet/data/ngrams/total_counts.csv");
		
		//Act
		YearlyRecord record = sut.getRecord(1960);
		
		//Assert
		assertEquals(0, record.size());
	}
	
	@Test
	public void getRecord_RecordModified_OriginalUnaffected(){
		//Arrange
		NGramMap sut = new NGramMap("/ngordnet/data/ngrams/very_short.csv", "/ngordnet/data/ngrams/total_counts.csv");
		
		//Act
		YearlyRecord record1 = sut.getRecord(2007);
		YearlyRecord record2 = sut.getRecord(2007);
		record1.put("test", 123456);
		
		//Assert
		assertEquals(4, record1.size());
		assertEquals(3, record2.size());
	}
	
	@Test
	public void countHistory_TimeRange_CorrectData(){
		//Arrange
		NGramMap sut = new NGramMap("/ngordnet/data/ngrams/very_short.csv", "/ngordnet/data/ngrams/total_counts.csv");
		
		//Act
		TimeSeries<Integer> record = sut.countHistory("wandered", 2006, 2009);
		
		//Assert
		assertEquals(4, record.size());
		assertEquals(87688, record.get(2006).intValue());
		assertEquals(171015, record.get(2008).intValue());
		assertEquals(0, record.get(2009).intValue());
	}
	
	@Test
	public void countHistory_FullHistory_CorrectData(){
		//Arrange
		NGramMap sut = new NGramMap("/ngordnet/data/ngrams/very_short.csv", "/ngordnet/data/ngrams/total_counts.csv");
		
		//Act
		TimeSeries<Integer> record = sut.countHistory("airport");
		
		//Assert
		assertEquals(4, record.size());
		assertEquals(0, record.get(2006).intValue());
		assertEquals(173294, record.get(2008).intValue());
	}
	
	@Test
	public void weightHistory_FullHistory_CorrectData(){
		//Arrange
		NGramMap sut = new NGramMap("/ngordnet/data/ngrams/very_short.csv", "/ngordnet/data/ngrams/total_counts.csv");
		
		//Act
		TimeSeries<Double> record = sut.weightHistory("airport");
		
		//Assert
		assertEquals(4, record.size());
		assertEquals(0, record.get(2006).intValue());
		assertTrue((173294.0/19482936409.0) == record.get(2008));
	}
	
//	2007,16206118071,82969746,155472
//	2008,19482936409,108811006,206272
	
	@Test
	public void summedWeightHistory_FullHistory_CorrectData(){
		//Arrange
		NGramMap sut = new NGramMap("/ngordnet/data/ngrams/very_short.csv", "/ngordnet/data/ngrams/total_counts.csv");
		Collection<String> words = new ArrayList<String>();
		words.add("airport");
		words.add("wandered");
		
		//Act
		TimeSeries<Double> record = sut.summedWeightHistory(words);
		
		//Assert
		assertEquals(4, record.size());
		assertTrue(((0.0 + 87688.0)/15310495914.0) == record.get(2006));
		assertTrue(((173294.0 + 171015.0)/19482936409.0) == record.get(2008));
	}
	
	@Test
	public void summedWeightHistory_FullHistoryWithUnknownWords_CorrectData(){
		//Arrange
		NGramMap sut = new NGramMap("/ngordnet/data/ngrams/very_short.csv", "/ngordnet/data/ngrams/total_counts.csv");
		Collection<String> words = new ArrayList<String>();
		words.add("airport");
		words.add("wandered");
		words.add("unknown");
		
		//Act
		TimeSeries<Double> record = sut.summedWeightHistory(words);
		
		//Assert
		assertEquals(4, record.size());
		assertTrue(((0.0 + 87688.0)/15310495914.0) == record.get(2006));
		assertTrue(((173294.0 + 171015.0)/19482936409.0) == record.get(2008));
	}
	
	@Test
	public void demo_AllExpectedValues(){
		NGramMap ngm = new NGramMap("/ngordnet/data/ngrams/words_that_start_with_q.csv", 
                "/ngordnet/data/ngrams/total_counts.csv");
		
		assertEquals(139, ngm.countInYear("quantity", 1736)); // should print 139
		YearlyRecord yr = ngm.getRecord(1736);
		assertEquals(139, yr.count("quantity")); // should print 139
		
		TimeSeries<Integer> countHistory = ngm.countHistory("quantity");
		assertEquals(new Integer(139), countHistory.get(1736)); // should print 139
		
		TimeSeries<Long> totalCountHistory = ngm.totalCountHistory();
		/* In 1736, there were 8049773 recorded words. Note we are not counting
		* distinct words, but rather the total number of words printed that year. */
		assertEquals(new Long(8049773), totalCountHistory.get(1736)); // should print 8049773
		
		TimeSeries<Double> weightHistory = ngm.weightHistory("quantity");
		
		Double weighted = ((double) countHistory.get(1736) 
		       / (double) totalCountHistory.get(1736)); 
		
		assertEquals(weighted, weightHistory.get(1736));  // should print roughly 1.7267E-5
		
		ArrayList<String> words = new ArrayList<String>();
		words.add("quantity");
		words.add("quality");        
		
		TimeSeries<Double> sum = ngm.summedWeightHistory(words);
		assertEquals(new Double((139.0 + 173.0)/8049773.0), sum.get(1736)); // should print roughly 3.875E-5
	}
}
