import java.io.IOException;

public abstract class ProximityNode implements QueryNode {
	protected int ctf = 0;
	protected int curDocItr = 0;
	protected PostingList postingList = null;
	protected RetrievalModel model = RetrievalModelFactory.getModel();

	protected abstract void generatePostings() throws IOException;

	protected PostingList getPostings() {
		return postingList;
	}

	@Override
	public Integer nextCandidate() {
		if (hasMore()) {
			return postingList.get(curDocItr).getDocId();
		}
		return null;
	}

	@Override
	public Double score(Integer docId) {
		int tf = 0;
		if (hasMore() && postingList.get(curDocItr).getDocId() == docId) {
			tf = postingList.get(curDocItr).getTermFreq();
		}
		return model.scoreOccurrence(tf, ctf, Index.getDocLength(docId));	
	}

	@Override
	public int skipTo(int docId) {
		while (hasMore() && postingList.get(curDocItr).getDocId() < docId) {
			curDocItr++;
		}
		return hasMore() ? postingList.get(curDocItr).getDocId() : 0;
	}


	@Override
	public boolean hasMore() {
		return curDocItr < postingList.documentCount();
	}

}