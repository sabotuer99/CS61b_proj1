package ngordnet.test;

import org.junit.Ignore;
import org.junit.Test;

import ngordnet.troy.NGramMap;
import ngordnet.troy.Plotter;
import ngordnet.troy.TimeSeries;

public class PlotterTest {

	@Ignore
	@Test
	public void plotTs_DisplaysCorrect(){
		//Arrange
		NGramMap sut = new NGramMap("/ngordnet/data/ngrams/words_that_start_with_q.csv", "/ngordnet/data/ngrams/total_counts.csv");
		
		//Act
		TimeSeries<Integer> record = sut.countHistory("quality");
		
		//Assert
		Plotter.plotTS(record, "plotTs Test", "year", "count", "count");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Ignore
	@Test
	public void plotCountHistory_DisplaysCorrect(){
		//Arrange
		NGramMap sut = new NGramMap("/ngordnet/data/ngrams/words_that_start_with_q.csv", "/ngordnet/data/ngrams/total_counts.csv");
		
		//Act
		//Assert
		Plotter.plotCountHistory(sut, "quality", 1900, 2008);
		
		//Pause so we can actually see the plot
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Ignore
	@Test
	public void plotWeightHistory_DisplaysCorrect(){
		//Arrange
		NGramMap sut = new NGramMap("/ngordnet/data/ngrams/words_that_start_with_q.csv", "/ngordnet/data/ngrams/total_counts.csv");
		
		//Act
		//Assert
		Plotter.plotWeightHistory(sut, "quality", 1900, 2008);
		
		//Pause so we can actually see the plot
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
