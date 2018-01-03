

import java.util.ArrayList;

public abstract class Window extends ProximityNode {
	ArrayList<ProximityNode> children;

	public Window(ArrayList<ProximityNode> termNodes) {
		children = termNodes;
		generatePostings();
	}

	private boolean allListsNotFinished(Integer[] it, ArrayList<PostingList> childPostings) {
		for (int i = 0; i < it.length; i++) {
			if (it[i] >= childPostings.get(i).documentCount()) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected void generatePostings() {
		//Conjunctive processing of child node postings and populate the posting for uw node
		postingList = IndexFactory.createPostingList();

		ArrayList<PostingList> childPostings = new ArrayList<PostingList>();
		//Get inverted lists for all query terms
		for (ProximityNode q: children) {
			childPostings.add(q.getPostings());
		}

		Integer d = -1;
		ArrayList<Posting> matchingPostings = new ArrayList<Posting>();

		//stores the array index of the posting
		Integer[] it = new Integer[childPostings.size()];
		for (int x = 0; x < childPostings.size(); x++) {
			it[x] = 0;
		}
		while (allListsNotFinished(it, childPostings)) {
			for (int i = 0; i < childPostings.size(); i++ ) {
				Integer tempDocId = childPostings.get(i).get(it[i]).getDocId();
				if (tempDocId > d) {
					d =  tempDocId;
				}
			}
			for (int i = 0; i < childPostings.size(); i++ ) {
				PostingList p = childPostings.get(i);
				while (it[i] < p.documentCount() && p.get(it[i]).getDocId() < d) {
					it[i]++;
				}
				if (it[i] >= p.documentCount()) {
					continue;
				}
				Posting curPosting = p.get(it[i]);
				int curDocId = curPosting.getDocId();
				if (curDocId > d ) {
					continue;
				}
				if (curDocId == d) {
					//Get posting for doc d
					matchingPostings.add(curPosting);
					it[i]++;	
				} else {
					d = -1;
					break;
				}
			}
			if (d > -1 && !matchingPostings.isEmpty() && matchingPostings.size() == childPostings.size()) {
				Posting p = calculateWindows(matchingPostings);
				if (p != null) {
					postingList.add(p);
					ctf += p.getTermFreq();
				}
			}
			matchingPostings.clear();
		}
	}

	abstract protected Posting calculateWindows(ArrayList<Posting> matchingPostings);
}
