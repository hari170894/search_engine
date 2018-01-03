import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class RunMeAssignment3 {
	public static void main(String args[])throws IOException{


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
		queries.put(10,"antony strumpet");
		int sums=RunMeAssignment2.sumdoclen();
		
		System.out.println("Loaded");
	
		for(Integer i:queries.keySet()){
			
			ArrayList<String> terms=new ArrayList<String>(Arrays.asList(queries.get(i).split(" ")));
			
			int mu=2000;
			InfModels.orderedWindow(terms, sums, true, i,1 , mu);
			InfModels.unorderedWindow(terms, sums, true, i, queries.get(i).length(), mu);
		}
	
	}

}
