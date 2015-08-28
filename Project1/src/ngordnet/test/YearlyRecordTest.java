package ngordnet.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import ngordnet.troy.YearlyRecord;

import org.junit.Test;

public class YearlyRecordTest {
	@Test
	public void ctor_PassInHashMap_CreatesNewHashMap(){
		//Arrange
		HashMap<String, Integer> test = new HashMap<String, Integer>();
		test.put("original",100);
		test.put("original2",100);
		YearlyRecord sut = new YearlyRecord(test);
		
		//Act
		sut.put("original", 120);
		sut.put("test", 12);
		sut.put("test2", 1212);
		
		//Assert
		assertEquals(2, test.size());
		assertTrue(test.get("original") == 100);
		assertTrue(test.get("original2") == 100);
		assertEquals(4, sut.size());
		assertTrue(sut.count("original") == 120);
		assertTrue(sut.count("original2") == 100);
	}
	
	@Test
	public void putcoutsize_allworkcorrectly(){
		//Arrange
		YearlyRecord sut = new YearlyRecord();
		
		//Act
		sut.put("test", 12);
		
		//Assert
		assertTrue(sut.size() == 1);
		assertTrue(sut.count("test") == 12);
	}
	
	@Test
	public void words_ReturnsWordsInSortedOrder(){
		//Arrange
		YearlyRecord sut = new YearlyRecord();
		sut.put("test1", 123);
		sut.put("test2", 12);
		sut.put("test3", 1234);
		
		//Act
		Collection<String> words = sut.words();
		
		//Assert
		assertEquals("[test2, test1, test3]", words.toString());	
	}
	
	@Test
	public void count_WordNotFound_ReturnsZero(){
		//Arrange
		YearlyRecord sut = new YearlyRecord();
		sut.put("test1", 123);
		sut.put("test2", 12);
		sut.put("test3", 1234);
		
		//Act
		int count = sut.count("nada");
		
		//Assert
		assertEquals(0, count);		
	}
	
	@Test
	public void count_WordFound_ReturnsCount(){
		//Arrange
		YearlyRecord sut = new YearlyRecord();
		sut.put("test1", 123);
		sut.put("test2", 12);
		sut.put("test3", 1234);
		
		//Act
		int count = sut.count("test3");
		
		//Assert
		assertEquals(1234, count);		
	}
	
	@Test
	public void counts_ReturnsCountsInSortedOrder(){
		//Arrange
		YearlyRecord sut = new YearlyRecord();
		sut.put("test1", 123);
		sut.put("test2", 12);
		sut.put("test3", 1234);
		
		//Act
		Collection<Number> counts = sut.counts();
		
		//Assert
		assertEquals("[12, 123, 1234]", counts.toString());		
	}
	
	@Test
	public void rank_ReturnsRankOfWord(){
		//Arrange
		YearlyRecord sut = new YearlyRecord();
		sut.put("test1", 123);
		sut.put("test2", 12);
		sut.put("test3", 1234);
		
		//Act
		int rank1 = sut.rank("test1");
		int rank2 = sut.rank("test2");
		int rank3 = sut.rank("test3");
		//System.out.println("Ranks: " + rank1 + " " + rank2 + " " + rank3);
		
		//Assert
		assertEquals(2, rank1);			
		assertEquals(3, rank2);	
		assertEquals(1, rank3);	
	}
	
	@Test
	public void demo_PassedInHashMap(){
        HashMap<String, Integer> rawData = new HashMap<String, Integer>();
        rawData.put("berry", 1290);
        rawData.put("auscultating", 6);
        rawData.put("temporariness", 20);
        rawData.put("puppetry", 191);
        YearlyRecord yr2 = new YearlyRecord(rawData);
        assertEquals(4, yr2.rank("auscultating")); // should print 4
	}
	
	@Test
	public void demo_RankAndSizeWork(){
        YearlyRecord yr = new YearlyRecord();
        yr.put("quayside", 95);        
        yr.put("surrogate", 340);
        yr.put("merchantman", 181);      
        assertEquals(1, yr.rank("surrogate")); // should print 1
        assertEquals(3, yr.rank("quayside")); // should print 3
        assertEquals(3, yr.size()); // should print 3
	}
	
	@Test
	public void demo_WordsIsInOrder(){
        YearlyRecord yr = new YearlyRecord();
        yr.put("quayside", 95);        
        yr.put("surrogate", 340);
        yr.put("merchantman", 181);  
        Collection<String> words = yr.words();
        ArrayList<String> results = new ArrayList<String>();
        /* The code below should print: 
            
            quayside appeared 95 times.
            merchantman appeared 181 times.
            surrogate appeared 340 times.
        */
        for (String word : words) {
            results.add(word + " appeared " + yr.count(word) + " times.");
        }

        assertEquals("quayside appeared 95 times.", results.get(0));
        assertEquals("merchantman appeared 181 times.", results.get(1));
        assertEquals("surrogate appeared 340 times.", results.get(2));
	}
}
