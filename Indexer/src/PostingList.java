import java.util.ArrayList;

/**
 * Inverted List interface
 * NB: the interface is minimalist and could present more support for iteration
 *
 */
public class PostingList {
	ArrayList<Posting> postingsForTerm=new ArrayList<>();
	/**
	 * @param index the index of the Posting to return
	 * @return the index'th Posting of the list
	 */
	public Posting get(int index){
		return postingsForTerm.get(index);
	}
	/**
	 * @return the number of Postings in the list
	 */
	public int documentCount(){
		return postingsForTerm.size();
	}
	/**
	 * @param posting the Posting to add
	 */
	public void add(Posting posting){
		postingsForTerm.add(posting);
	}
}
