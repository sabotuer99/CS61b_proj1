package ngordnet.test;

import static org.junit.Assert.assertTrue;
import ngordnet.troy.NGramMap;
import ngordnet.troy.WordNet;

import org.junit.Ignore;
import org.junit.Test;

public class NGramMapTest {
	//@Ignore
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
}
