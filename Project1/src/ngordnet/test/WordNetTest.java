package ngordnet.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

import ngordnet.troy.WordNet;

public class WordNetTest {
	@Test
	public void synsetAndHyponymsShouldLoadInUnder5Seconds(){
		//Arrange
		
		//Act
		Stopwatch sw = new Stopwatch();
		@SuppressWarnings("unused")
		WordNet wn2 = new WordNet("/ngordnet/data/wordnet/synsets.txt", "/ngordnet/data/wordnet/hyponyms.txt");
		long time = (long)(sw.elapsedTime() * 1000);
		
		//Assert
		//System.out.println("Elapsed time: " + time);
		assertTrue(time < 5000);
	}
	
	@Test
	public void isNoun_DefinedWords_ReturnsTrue(){
		//Arrange
        WordNet wn = new WordNet("/ngordnet/data/wordnet/synsets11.txt", "/ngordnet/data/wordnet/hyponyms11.txt");

        //Act
        boolean result1 = wn.isNoun("jump");
        boolean result2 = wn.isNoun("leap");
        boolean result3 = wn.isNoun("nasal_decongestant");
        
        //Assert
        assertTrue(result1 && result2 && result3);
	}
	
	@Test
	public void nounsContainsKnownWords(){
		//Arrange
        WordNet wn = new WordNet("/ngordnet/data/wordnet/synsets11.txt", "/ngordnet/data/wordnet/hyponyms11.txt");
        Collection<String> known = new ArrayList<String>(Arrays.asList(
        	"augmentation",
            "nasal_decongestant",
            "change",
            "action",
            "actifed",
            "antihistamine",
            "increase",
            "descent",
            "parachuting",
            "leap",
            "demotion",
            "jump"
        ));
        
        //Act
        boolean result = wn.nouns().containsAll(known);
        
        //Assert
        assertTrue(result);
        assertTrue(wn.nouns().size() == 12);
	}
	
	@Test
	public void hyponymsContainsKnownWords(){
		//Arrange
        WordNet wn = new WordNet("/ngordnet/data/wordnet/synsets11.txt", "/ngordnet/data/wordnet/hyponyms11.txt");
        Collection<String> known = new ArrayList<String>(Arrays.asList(
        	"augmentation",
            "increase",
            "leap",
            "jump"
        ));
        
        //Act
        boolean result = wn.hyponyms("increase").containsAll(known);
        
        //Assert
        assertTrue(result);
        assertTrue(wn.hyponyms("increase").size() == 4);
	}
	
	@Test
	public void hyponymsDoesNotContainsExtraWords(){
		//Arrange
        WordNet wn = new WordNet("/ngordnet/data/wordnet/synsets11.txt", "/ngordnet/data/wordnet/hyponyms11.txt");
        Collection<String> known = new ArrayList<String>(Arrays.asList(
        	"augmentation",
            "increase",
            "leap",
            "jump",
            "parachuting",
            "demotion"
        ));
        
        //Act
        boolean result = wn.hyponyms("increase").containsAll(known);
        
        //Assert
        assertFalse(result);
	}
	
}
