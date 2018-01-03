import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class InfModels {
	  

    public static <T> ArrayList<T> union(ArrayList<T> list1, ArrayList<T> list2) {
        HashSet<T> set = new HashSet<T>();

        set.addAll(list1);
        set.addAll(list2);

        return new ArrayList<T>(set);
    }
	  public static <T> ArrayList<T> intersection(ArrayList<T> list1, ArrayList<T> list2) {
	        ArrayList<T> list = new ArrayList<T>();

	        for (T t : list1) {
	            if(list2.contains(t)) {
	                list.add(t);
	            }
	        }

	        return list;
	    }
	
	public static void orderedWindow(ArrayList<String> terms,int N,boolean compressFlag,int queryNumber,int windowSize,double mu) throws IOException{
		boolean flag=false;
		ArrayList<Integer> List1=new ArrayList<>();
		for (String i:terms){
			if(flag==false){
				ArrayList<Integer> vals=LookUpTableBuilder.readFromLookup(i, compressFlag);
				HashMap<Integer, ArrayList<Integer>> v=LookUpTableBuilder.readFromFile(vals.get(0), compressFlag, vals.get(1));	
				List1= new ArrayList<Integer>(v.keySet());
				flag=true;
			}else{
				ArrayList<Integer> vals=LookUpTableBuilder.readFromLookup(i, compressFlag);
				HashMap<Integer, ArrayList<Integer>> v=LookUpTableBuilder.readFromFile(vals.get(0), compressFlag, vals.get(1));	
				List1=intersection(List1, new ArrayList<Integer>(v.keySet()));
			}	
		}
		HashMap<Integer,Double> sortedValues=new HashMap<Integer, Double>();
		HashMap<Integer,Double> values=new HashMap<Integer, Double>();
		for(int i:List1){
			ArrayList<ArrayList<Integer>> postings=new ArrayList<>();
			for(String term:terms){
				ArrayList<Integer> vals=LookUpTableBuilder.readFromLookup(term, compressFlag);
				HashMap<Integer, ArrayList<Integer>> v=LookUpTableBuilder.readFromFile(vals.get(0), compressFlag, vals.get(1));	
				postings.add(v.get(i));
				
			}

			int f=findorderedcount(postings,windowSize);
			
			values.put(i, 1.0*f);
		}
		double collectioncount=0;
		for (Integer i:values.keySet()){
		collectioncount+=values.get(i);
		}
		for(Integer i:values.keySet()){
			if(values.get(i)>0){
			sortedValues.put(i, ScoringModels.qlds(i, N, collectioncount, values.get(i), mu));
			}
			}
		sortedValues=(HashMap<Integer, Double>) RetreivalModels.sortByValue(sortedValues);
		String filename="saved/od1_trecrun.txt";
		RetreivalModels.writetrec(sortedValues,terms,queryNumber,"hari-orderedwindow"+"-"+mu,filename);
			
		
	}

	
	public static int findorderedcount(ArrayList<ArrayList<Integer>> postings, int windowSize) {
		Integer [] sizes = new Integer[postings.size()];
		Integer [] pivots=new Integer[postings.size()];
		int j=0;
		for(ArrayList<Integer> i:postings){
			sizes[j++]=i.size();
			pivots[j-1]=0;
		}
		
		int count=0;
		while(!checkZeroSize(sizes)){
			int val=findSize(pivots,postings);
			if(val<windowSize){
				count++;
				for(int i=0;i<pivots.length;i++){
					pivots[i]+=1;
					sizes[i]-=1;
				}
			}else{
				int minpos=findMinLower(pivots, postings);
				pivots[minpos]+=1;
				sizes[minpos]-=1;
			}
		}
		return count;
		
		
	}
	

	private static int findSize(Integer[] pivots, ArrayList<ArrayList<Integer>> postings) {
		
		int x;
		for(int i=0;i<pivots.length-1;i++){
			
			x=postings.get(i+1).get(pivots[i+1])-postings.get(i).get(pivots[i]);
			if(x!=1){
				return 99999;
			}
			
		}
		return 0;
	}
	private static int findMin(Integer[] pivots, ArrayList<ArrayList<Integer>> postings) {
		int minpos=-1;
		int min=99999999;
		for(int i=0;i<pivots.length;i++){
			if(postings.get(i).get(pivots[i])<=min){
				min=postings.get(i).get(pivots[i]);
				minpos=i;
			}
			
		}
		return minpos;
	}
	private static int findMinLower(Integer[] pivots, ArrayList<ArrayList<Integer>> postings) {
		int minpos=0;
		int min=-99999999;
		for(int i=0;i<pivots.length;i++){
			if(postings.get(i).get(pivots[i])>=min){
				min=postings.get(i).get(pivots[i]);
			}else{	
				minpos=i;
				break;
			}
			
		}
		return minpos;
	}


	private static boolean checkZeroSize(Integer[] sizes) {
		
		for(int i:sizes){
			if (i==0){
				return true;
			}
		}
		return false;
	}
	
	
	public static void unorderedWindow(ArrayList<String> terms,int N,boolean compressFlag,int queryNumber,int windowSize,double mu) throws IOException{
		boolean flag=false;
		ArrayList<Integer> List1=new ArrayList<>();
		for (String i:terms){
			if(flag==false){
				ArrayList<Integer> vals=LookUpTableBuilder.readFromLookup(i, compressFlag);
				HashMap<Integer, ArrayList<Integer>> v=LookUpTableBuilder.readFromFile(vals.get(0), compressFlag, vals.get(1));	
				List1= new ArrayList<Integer>(v.keySet());
				flag=true;
			}else{
				ArrayList<Integer> vals=LookUpTableBuilder.readFromLookup(i, compressFlag);
				HashMap<Integer, ArrayList<Integer>> v=LookUpTableBuilder.readFromFile(vals.get(0), compressFlag, vals.get(1));	
				List1=intersection(List1, new ArrayList<Integer>(v.keySet()));
			}	
		}
		HashMap<Integer,Double> sortedValues=new HashMap<Integer, Double>();
		HashMap<Integer,Double> values=new HashMap<Integer, Double>();
		for(int i:List1){
			ArrayList<ArrayList<Integer>> postings=new ArrayList<>();
			for(String term:terms){
				ArrayList<Integer> vals=LookUpTableBuilder.readFromLookup(term, compressFlag);
				HashMap<Integer, ArrayList<Integer>> v=LookUpTableBuilder.readFromFile(vals.get(0), compressFlag, vals.get(1));	
				postings.add(v.get(i));
				
			}
			int f=findunorderedcount(postings,windowSize);
			values.put(i, 1.0*f);
		}
		double collectioncount=0;
		for (Integer i:values.keySet()){
		collectioncount+=values.get(i);
		}
		for(Integer i:values.keySet()){
			if(values.get(i)>0){
			sortedValues.put(i, ScoringModels.qlds(i, N, collectioncount, values.get(i), mu));
			}
		}
		
		sortedValues=(HashMap<Integer, Double>) RetreivalModels.sortByValue(sortedValues);
		String filename="saved/uw_trecrun.txt";
		RetreivalModels.writetrec(sortedValues,terms,queryNumber,"hari-orderedwindow"+"-"+mu,filename);
			
		
	}

	public static int findunorderedcount(ArrayList<ArrayList<Integer>> postings, int windowSize) {
		Integer [] sizes = new Integer[postings.size()];
		Integer [] pivots=new Integer[postings.size()];
		int j=0;
		for(ArrayList<Integer> i:postings){
			sizes[j++]=i.size();
			pivots[j-1]=0;
		}
		
		int count=0;
		while(!checkZeroSize(sizes)){
			int val=findMinandMax(pivots,postings);
			if(val<windowSize){
				count++;
				for(int i=0;i<pivots.length;i++){
					pivots[i]+=1;
					sizes[i]-=1;
				}
			}else{
				int minpos=findMin(pivots, postings);
				pivots[minpos]+=1;
				sizes[minpos]-=1;
			}
		}
		return count;
		
	}
	private static int findMinandMax(Integer[] pivots, ArrayList<ArrayList<Integer>> postings) {
		int max=-1;
		int min=99999999;
		for(int i=0;i<pivots.length;i++){
			if(postings.get(i).get(pivots[i])<min){
				min=postings.get(i).get(pivots[i]);
			}
			if(postings.get(i).get(pivots[i])>max){
				max=postings.get(i).get(pivots[i]);
			}
			
		}
		return max-min;
	}

	

}
