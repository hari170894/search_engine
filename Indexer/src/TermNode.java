import java.io.IOException;

public class TermNode extends ProximityNode {

	private String term;
	TermNode(String t) throws IOException{
		term = t;
		generatePostings();
	}

	@Override
	protected void generatePostings() throws IOException {
		postingList = Index.getPostings(term);
		ctf = Index.getTermFreq(term);
	}
}
