

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Abstract interface of an information retrieval inverted index
 */
public class Index {
	/**
	 * @param term the term to lookup
	 * @return the posting list for the given term
	 * @throws IOException 
	 */
	public static PostingList getPostings(String term) throws IOException{
		ArrayList<Integer> vals=LookUpTableBuilder.readFromLookup(term, true);
		HashMap<Integer, ArrayList<Integer>> v=LookUpTableBuilder.readFromFile(vals.get(0), true, vals.get(1));	
		PostingList postingList=new PostingList();
		for (Integer docId:v.keySet()){
			Posting p=new Posting(v.get(docId).get(0), docId);
			int j=1;
			while (j<v.get(docId).size()){
				p.add(v.get(docId).get(j));
				j+=1;
			}
			postingList.add(p);			
		}
		return postingList;
	}
	/**
	 * @param term the term to lookup
	 * @return the frequency of the term in the collection
	 * @throws IOException 
	 */
	public static int getTermFreq(String term) throws IOException{
		ArrayList<Integer> vals=LookUpTableBuilder.readFromLookup(term, true);
		HashMap<Integer, ArrayList<Integer>> v=LookUpTableBuilder.readFromFile(vals.get(0), true, vals.get(1));	
		int count=0;
		for (int docId:v.keySet()){
			count+=v.get(docId).size();
		}
		return count;
	}
	/**
	 * @return the size of the collection
	 */
	public static int getCollectionSize(){
		int val= 897268;
		return val;
	}
	/**
	 * @param docId the document id to look up
	 * @return the length of the document
	 */
	public static int getDocLength(int docId){
		return PostingListBuilder.docLen(docId);
	}
}
