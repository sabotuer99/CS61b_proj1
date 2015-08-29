package ngordnet.test;

import static org.junit.Assert.*;
import ngordnet.troy.WordLengthProcessor;
import ngordnet.troy.YearlyRecord;

import org.junit.Test;

public class WordLengthProcessorTest {

	@Test
	public void process_CalculatesCorrectly() {
		//Arrange
		YearlyRecord sut = new YearlyRecord();
		sut.put("test", 10);
		sut.put("testicle", 12);
		sut.put("weasle", 5);
		sut.put("ass", 21);
		
		//Act
		double result = new WordLengthProcessor().process(sut);
		double expected = (40.0 + 96.0 + 30.0 + 63.0)/(10.0 + 12.0 + 5.0 + 21.0);
		
		//
		assertEquals(expected, result, 0.00001);
	}
	
	@Test
	public void process_NullYearlyRecord_returnZero() {
		//Arrange
		YearlyRecord sut = null;
		
		//Act
		double result = new WordLengthProcessor().process(sut);
		double expected = 0;
		
		//
		assertEquals(expected, result, 0.00001);
	}

}
