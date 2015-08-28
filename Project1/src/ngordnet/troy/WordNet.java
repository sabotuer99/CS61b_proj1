package ngordnet.troy;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class WordNet {
	private HashMap<Long, Synset> synsets;
	private HashMap<String, List<Synset>> words;
	private HashMap<Long, List<Long>> hypernyms;
	
	private class Synset{
		public Long id;
		public List<String> synonyms;
		@SuppressWarnings("unused")
		public String definition;
	}
	
    /** Creates a WordNet using files form SYNSETFILENAME and HYPONYMFILENAME 
     **/
    public WordNet(String synsetFilename, String hyponymFilename) {
    	try {
        	String sCurrentLine; 
        	synsets = new HashMap<Long, Synset>();
        	words = new HashMap<String, List<Synset>>();
        	hypernyms = new HashMap<Long, List<Long>>();
        	
        	BufferedReader ssr = getReader(synsetFilename);		
			while ((sCurrentLine = ssr.readLine()) != null) {
				
				String[] pieces = sCurrentLine.split(",");
				Synset synset = new Synset();
				synset.id = Long.decode(pieces[0]);
				synset.synonyms = Arrays.asList(pieces[1].split(" "));
				synset.definition = pieces[2];
				
				//add synset to synset map
				synsets.put(synset.id, synset);
				
				//add words to words map
				for (String word : synset.synonyms) {
					List<Synset> ssets = words.get(word);
					if(ssets == null)
						ssets = new ArrayList<Synset>();
					ssets.add(synset);
					words.put(word, ssets);
					//System.out.println(word);
				}
			}
			ssr.close();
			sCurrentLine = "";
			
			// process hypernyms
        	BufferedReader hypr = getReader(hyponymFilename);   		
			while ((sCurrentLine = hypr.readLine()) != null) {
				
				String[] pieces = sCurrentLine.split(",");
				Long id = Long.decode(pieces[0]);
				List<Long> hypIds = new ArrayList<Long>();
				for(int i = 1; i < pieces.length; i += 1 ){
					hypIds.add(Long.decode(pieces[i]));
				}
				hypernyms.put(id, hypIds);
			}
			hypr.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    //returns an empty (not null) reader if file not found
    //using the getReader array introduces seam to allow for non filesystem
    //based handling of WordNet constructor (requires subclassing and overriding)
    public BufferedReader getReader(String fileName){
    	try {
    		File f = new File(getClass().getResource(fileName).getFile());
			return new BufferedReader(new FileReader(f));
		} catch (Exception e) {
			System.out.println("Oops, problem with file!");
			e.printStackTrace();
			return new BufferedReader(new InputStreamReader(new ByteArrayInputStream("".getBytes())));
		}
    }

    /* Returns true if NOUN is a word in some synset. */
    public boolean isNoun(String noun){
		return words.containsKey(noun);
    }

    /* Returns the set of all nouns. */
    public Set<String> nouns(){
		return words.keySet();	
    }

    /** Returns the set of all hyponyms of WORD as well as all synonyms of
      * WORD. If WORD belongs to multiple synsets, return all hyponyms of
      * all of these synsets. See http://goo.gl/EGLoys for an example.
      * Do not include hyponyms of synonyms.
      */
    public Set<String> hyponyms(String word){
    	Set<String> hyponyms = new HashSet<String>();
    	
		//get synsets
    	List<Synset> ssets = words.get(word);
    	
    	//get hyponym synsets
    	for (Synset synset : ssets) {
    		hyponyms.addAll(synset.synonyms);
    		
			List<Long> hypIds = hypernyms.get(synset.id);
			if(hypIds != null){
				for (Long id : hypIds) {
					Synset hypset = synsets.get(id);
					hyponyms.addAll(hypset.synonyms);
				}
			}
		}
    	
    	return hyponyms;  	
    }
}

//buffered input code came from here
//http://stackoverflow.com/questions/20196211/read-input-from-text-file-in-java
//code for csv processing
//http://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/