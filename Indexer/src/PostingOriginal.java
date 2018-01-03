/**
 * Helper class to store postings
 * @author Hari
 *
 */
public class PostingOriginal {
	private String term;
	private Integer docId;
	private Integer postionOfPosting;
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public Integer getDocId() {
		return docId;
	}
	public void setDocId(Integer docId) {
		this.docId = docId;
	}
	public Integer getPostionOfPosting() {
		return postionOfPosting;
	}
	public void setPostionOfPosting(Integer postionOfPosting) {
		this.postionOfPosting = postionOfPosting;
	}
	
	public void AddPosting(String s, int d, int p){
		term=s;
		docId=d;
		postionOfPosting=p;
	}
	public void printPosting(){
		System.out.println(term+" "+docId+" "+ postionOfPosting);
	}
	
}
