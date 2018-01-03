import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Map.Entry;

public class RunMeAssignment5 {

	public static void main(String args[]) throws Exception{
		String mode="uniform";//change to uniform for random
		QueryProcessing.loadFromSavedFiles();
		String[] query={"the","king","queen","royalty"};
		ArrayList<QueryNode> e=new ArrayList<>();
		for(String term:query){
			TermNode t=new TermNode(term);
			e.add(t);
		}
		Prior node=new Prior(mode);
		e.add(node);	
		AndNode aNode=new AndNode(e);
		
		HashMap<Integer, Double> result =Network.runQuery(aNode, 1000);
		result=(HashMap<Integer, Double>) RetreivalModels.sortByValue(result);
		RetreivalModels.writetrec(result, null, 1, mode, "saved/"+mode+"_trecrun.txt");
		
	}
	
}
