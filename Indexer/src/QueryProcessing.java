import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.json.simple.JSONArray;

public class QueryProcessing {
	/**
	 * Method for measuring time taken to get index from disk and decompress if necessary
	 * @param s
	 * @param compressflag
	 * @return
	 * @throws IOException
	 */
	public static long timeToFetchInvList(String s,boolean compressflag) throws IOException{
		ArrayList<Integer> list=LookUpTableBuilder.readFromLookup(s, compressflag);
		long start=System.nanoTime();
		LookUpTableBuilder.readFromFile(list.get(0), compressflag, list.get(1));
		long end=System.nanoTime();
		return (end-start);
	}
	
	/**
	 * Method to compare time if inv index fetched from compressed and uncompressed 
	 * @param s
	 * @throws IOException
	 */
	public static void comparetime(String s) throws IOException{
		long uc=timeToFetchInvList(s, false);
		long c=timeToFetchInvList(s, true);
		System.out.println(uc+" "+c);
	}
	
	
	/**
	 * This method returns the closest word given a word. Twowindow has na and nb for all words
	 * @param s
	 * @param twoWindow
	 * @param jsonArray
	 * @return
	 * @throws IOException
	 */
	public static String findClosestWord(String s, HashMap<String, Integer> twoWindow,JSONArray jsonArray) throws IOException{
		
	
		double max=-1;
		String closeterm="none";
		
		HashMap<String, Integer> v=VocabBuilder.findCoOccurences(jsonArray, s);
		for(String word:v.keySet()){
			double val=2.0*v.get(word)/(twoWindow.get(word)+twoWindow.get(s));
			if(max<val){
				max=val;
				closeterm=word;
			}
		}
		return closeterm;
	}
	
	
	

	/**
	 * Method to load everything
	 * @throws IOException
	 */
	public static void loadFromScratch()throws IOException{
		JSONArray jsonArray=JSONFileReader.getData();
		ArrayList<PostingOriginal> postings=PostingListBuilder.buildPostingList(jsonArray);
		LookUpTableBuilder.buildLookupTable(postings,false);
		LookUpTableBuilder.buildLookupTable(postings,true);
		
	}
	/**
	 * Method to run if you have the necessary inverted index and lookup tables built
	 * @throws IOException
	 */
	public static void loadFromSavedFiles()throws IOException{
		PostingListBuilder.loadDocToScene();
		PostingListBuilder.loadDocToPlay();
		
	}
}
