package ngordnet.test;

import static org.junit.Assert.*;

//import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import ngordnet.troy.TimeSeries;

public class TimeSeriesTest {

	@Test
	public void ctor_CreateEmptyNumberTimeSeries_Success(){
		new TimeSeries<Integer>();
		new TimeSeries<Double>();
		new TimeSeries<Long>();
	}
	
	@Test
	public void ctor_PassExistingTSWithRange_CorrectYearsIncluded(){
		//Arrage
		TimeSeries<Double> ts = new TimeSeries<Double>();
        ts.put(1992, 3.6);
        ts.put(1993, 9.2);
        ts.put(1994, 15.2);
        ts.put(1995, 16.1);
        ts.put(1996, -15.7);
		
		//Act
        TimeSeries<Double> sut = new TimeSeries<Double>(ts, 1993, 1995);
        
        //Assert
        assertEquals(3, sut.size());
        assertTrue(9.2 == sut.get(1993));
        assertTrue(15.2 == sut.get(1994));
        assertTrue(16.1 == sut.get(1995));
	}
	
	@Test
	public void ctor_PassExistingTS_CreateNew(){
		//Arrage
		TimeSeries<Double> ts = new TimeSeries<Double>();
        ts.put(1992, 3.6);
        ts.put(1993, 9.2);
        ts.put(1994, 15.2);
        ts.put(1995, 16.1);
        ts.put(1996, -15.7);
		
		//Act
        TimeSeries<Double> sut = new TimeSeries<Double>(ts);
        ts.put(1992, 99.9);
        
        //Assert
        assertTrue(3.6 == sut.get(1992));
        assertTrue(99.9 == ts.get(1992));
	}
	
	@Test
	public void put_AddValuesToTimeSeries_Success(){
		//Arrage
		TimeSeries<Double> ts = new TimeSeries<Double>();
		
		//Act
        ts.put(1992, 3.6);
        ts.put(1993, 9.2);
        ts.put(1994, 15.2);
        ts.put(1995, 16.1);
        ts.put(1996, -15.7);
        
        //Assert
        assertEquals(5, ts.size());
        assertTrue(3.6 == ts.get(1992));
	}
	
	@Test
	public void CollectionGetters_YearsAndDataReturnInSameOrder(){
		//Arrange
		TimeSeries<Double> ts = new TimeSeries<Double>();
        ts.put(1992, 3.6);
        ts.put(1993, 9.2);
        ts.put(1994, 15.2);
        ts.put(1995, 16.1);
        ts.put(1996, -15.7);

        //Act
        Collection<Number> years = ts.years();
        Collection<Number> data = ts.data();

        //Assert
        ArrayList<Number> yearsOrdered = new ArrayList<Number>(years);
        ArrayList<Number> dataOrdered = new ArrayList<Number>(data);
        assertEquals(years.size(), data.size());
        for(int i = 0; i < 5; i += 1)
        	assertTrue(dataOrdered.get(i) == ts.get(yearsOrdered.get(i)));
	}
	
	@Test
	public void divideBy_CorrectSizeTimeSeries_Success(){
		//Arrange
		TimeSeries<Integer> ts2 = new TimeSeries<Integer>();
        ts2.put(1991, 10);
        ts2.put(1992, -5);
        ts2.put(1993, 1);

        TimeSeries<Double> ts3 = new TimeSeries<Double>();
        ts3.put(1991, 5.0);
        ts3.put(1992, 1.0);
        ts3.put(1993, 100.0);

        //Act
        TimeSeries<Double> tQuotient = ts2.dividedBy(ts3);
        
        //Assert
        assertTrue(2.0 == tQuotient.get(1991));
        assertEquals(3, tQuotient.size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void divideBy_MismatchKeySets_ThrowsException(){
		//Arrange
		TimeSeries<Integer> ts2 = new TimeSeries<Integer>();
        ts2.put(1991, 10);
        ts2.put(1992, -5);
        ts2.put(1993, 1);

        TimeSeries<Double> ts3 = new TimeSeries<Double>();
        ts3.put(1990, 5.0);
        ts3.put(1992, 1.0);
        ts3.put(1993, 100.0);

        //Act
        ts2.dividedBy(ts3);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void divideBy_DifferentSizeKeySets_ThrowsException(){
		//Arrange
		TimeSeries<Integer> ts2 = new TimeSeries<Integer>();
        ts2.put(1991, 10);
        ts2.put(1992, -5);
        ts2.put(1993, 1);

        TimeSeries<Double> ts3 = new TimeSeries<Double>();
        ts3.put(1991, 5.0);
        ts3.put(1992, 1.0);
        ts3.put(1993, 100.0);
        ts3.put(1994, 150.0);

        //Act
        ts2.dividedBy(ts3);
	}
	
	@Test
	public void plus_DifferentSizeKeySets_Success(){
		//Arrange
		TimeSeries<Integer> ts2 = new TimeSeries<Integer>();
        ts2.put(1991, 10);
        ts2.put(1992, -5);
        ts2.put(1993, 1);

        TimeSeries<Double> ts3 = new TimeSeries<Double>();
        ts3.put(1991, 5.0);
        ts3.put(1992, 1.0);
        ts3.put(1993, 100.0);
        ts3.put(1994, 150.0);

        //Act
        TimeSeries<Double> tPlus = ts2.plus(ts3);
        
        //Assert
        assertTrue(15.0 == tPlus.get(1991));
        assertTrue(-4.0 == tPlus.get(1992));
        assertTrue(101.0 == tPlus.get(1993));
        assertTrue(150.0 == tPlus.get(1994));
	}
	
	
}
