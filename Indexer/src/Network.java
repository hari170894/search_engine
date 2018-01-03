

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Stack;

public class Network {
	
	/**
	 * Evaluates a query tree
	 * @param qnode Query tree to evaluate
	 * @param K number of results to return
	 * @return the list of document score pairs
	 */
	public static HashMap <Integer, Double> runQuery(QueryNode qnode, int K) {
		PriorityQueue<Map.Entry<Integer, Double>> result = new PriorityQueue<>(Map.Entry.<Integer,Double>comparingByValue());
		while (qnode.hasMore()) {
			// which doc can we score next?
			int d = qnode.nextCandidate();
			// advance all of the query nodes to the document
			qnode.skipTo(d);
			// score it
			Double curScore = qnode.score(d);
			// filter nodes may return a null score
			if (curScore != null) { 
				result.add(new AbstractMap.SimpleEntry<Integer, Double>(d, curScore));
				// trim the queue if necessary
				if (result.size() > K) {
					result.poll();
				}
			}
			// advance all of the query nodes past the scored document
			qnode.skipTo(d + 1);
		}
		// reverse the queue
		Stack<Map.Entry<Integer, Double>> temp = new Stack<Map.Entry<Integer, Double>>();
		while(!result.isEmpty()){
			 temp.push(result.poll());
		}
		HashMap<Integer, Double> scores=new HashMap<>();
		for (Entry<Integer, Double> i:temp){
			scores.put(i.getKey(),i.getValue());
		}
		return scores;
	}
}
