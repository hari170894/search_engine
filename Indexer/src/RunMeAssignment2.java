import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class RunMeAssignment2 {
	
	public static int sumdoclen(){
		int sums=0;		
		for(String i:PostingListBuilder.docToScene.keySet()){
			sums+=PostingListBuilder.docLen(PostingListBuilder.docToScene.get(i));
			
		}
		return sums;
	}
	public static void main(String args[]) throws IOException{

		QueryProcessing.loadFromSavedFiles();
		HashMap<Integer,String> queries=new HashMap<>();
		queries.put(1,"the king queen royalty");
		queries.put(2,"servant guard soldier");
		queries.put(3,"hope dream sleep");
		queries.put(4,"ghost spirit");
		queries.put(5,"fool jester player");
		queries.put(6,"to be or not to be");
		queries.put(7,"alas");
		queries.put(8,"alas poor");
		queries.put(9,"alas poor yorick");
		queries.put(10,"antony strumpet value");
		queries.put(11,"setting the scene");
		int sums=sumdoclen();
		double avgdoclen=sums/PostingListBuilder.docToScene.keySet().size();
		
		System.out.println("Loaded");
		int N=PostingListBuilder.docToScene.keySet().size();
		for(Integer i:queries.keySet()){
			System.out.println("Processing query"+i);
			double k1=1.2;
			double k2=100;
			double b=0.75;
			ArrayList<String> terms=new ArrayList<String>(Arrays.asList(queries.get(i).split(" ")));
			RetreivalModels.BM25Model(terms, true, 1, k1, k2, avgdoclen, b, N, i);
			double lambda;
			lambda=0.1;
			
			int mu=2000;
			RetreivalModels.QLDS(terms, true, 1, mu,sums , i);

			RetreivalModels.QLJM(terms, true, 1, lambda,sums, i);
		}
	}

}
