import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RetreivalModels {
	/**
	 * Method to sort a hashmap by value
	 * @param map
	 * @return
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> 
    sortByValue(Map<K, V> map) {
    List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
    Collections.sort( list, new Comparator<Map.Entry<K, V>>() {
        public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
            return (o2.getValue()).compareTo( o1.getValue() );
        }
    });

    Map<K, V> result = new LinkedHashMap<K, V>();
    for (Map.Entry<K, V> entry : list) {
        result.put(entry.getKey(), entry.getValue());
    }
    return result;
}

	/**
	 * method to return k top docIDs given query terms.
	 * @param terms
	 * @param compressFlag
	 * @param n
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<Integer> rawCountModel(ArrayList<String> terms,boolean compressFlag,int n)throws IOException{
		ArrayList<Integer> docIds=new ArrayList<>();
		HashMap<Integer,Integer> sortedValues=new HashMap<Integer, Integer>();
		for(String i:terms){
			ArrayList<Integer> vals=LookUpTableBuilder.readFromLookup(i, true);
			HashMap<Integer, ArrayList<Integer>> v=LookUpTableBuilder.readFromFile(vals.get(0), true, vals.get(1));	
			for(int j:v.keySet()){
				if(sortedValues.containsKey(j)){
					sortedValues.put(j, sortedValues.get(j)+ScoringModels.countscore(v.get(j).size()));
				}else{
					sortedValues.put(j, ScoringModels.countscore(v.get(j).size()));
				}
			}
		}
		
		sortedValues=(HashMap<Integer, Integer>) sortByValue(sortedValues);
		
			
		int count=0;
		for(int i:sortedValues.keySet()){
			if(count>n-1){
				break;
			}
			docIds.add(i);
			count++;
		}
		
		return docIds;
	}
	
	
	public static ArrayList<Integer> BM25Model(ArrayList<String> terms,boolean compressFlag,int n,double k1,double k2,double avgdoclen,double b,int N,int querynumber)throws IOException{
		ArrayList<Integer> docIds=new ArrayList<>();
		HashMap<Integer,Double> sortedValues=new HashMap<Integer, Double>();

		HashMap<String,Integer> querycounts=new HashMap<>();
		for(String i:terms){
			if(querycounts.containsKey(i)){
				querycounts.put(i, querycounts.get(i)+1);
				
			}else{
			querycounts.put(i, 1);
		
			}}
		for(String i:querycounts.keySet()){
			ArrayList<Integer> vals=LookUpTableBuilder.readFromLookup(i, true);
			HashMap<Integer, ArrayList<Integer>> v=LookUpTableBuilder.readFromFile(vals.get(0), true, vals.get(1));	
			
			for(int j:v.keySet()){
				double k=k1*((1-b)+b*PostingListBuilder.docLen(j)/avgdoclen);
				if(sortedValues.containsKey(j)){
					sortedValues.put(j, sortedValues.get(j)+ScoringModels.bm25(j,N,v.size(),v.get(j).size(),k1,k2,k,querycounts.get(i)));
				}else{
					sortedValues.put(j, ScoringModels.bm25(j,N,v.size(),v.get(j).size(),k1,k2,k,querycounts.get(i)));
				}
			}
		}
		
		sortedValues=(HashMap<Integer, Double>) sortByValue(sortedValues);
		String filename="saved/bm25_trecrun.txt";
		writetrec(sortedValues,terms,querynumber,"hari-bm25"+k1+"-"+k2,filename);
		int count=0;
		for(int i:sortedValues.keySet()){
			if(count>n-1){
				break;
			}
			docIds.add(i);
			count++;
		}
		
		return docIds;
	}
	
	public static void writetrec(HashMap<Integer, Double> sortedValues, ArrayList<String> terms, int querynumber,
			String identifier,String filename) throws IOException {
		FileOutputStream fos=new FileOutputStream(filename);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		BufferedWriter bw = new BufferedWriter(osw);
		ArrayList<String> scenes=PostingListBuilder.getSceneIds(new ArrayList(sortedValues.keySet()));
		int j=0;
		for(Integer i:sortedValues.keySet()){
			bw.write("Q"+querynumber+" ");
			bw.write(scenes.get(j++)+" ");
			bw.write(j+" ");
			bw.write(sortedValues.get(i)+" ");
			bw.write(identifier+"\n");
		}
		bw.close();
		osw.close();
		fos.close();
		
	}

	public static ArrayList<Integer> QLJM(ArrayList<String> terms,boolean compressFlag,int n,double lambda,double N,int querynumber)throws IOException{
		ArrayList<Integer> docIds=new ArrayList<>();
		HashMap<Integer,Double> sortedValues=new HashMap<Integer, Double>();

	
		double collectioncount;
		for(String i:terms){
			ArrayList<Integer> vals=LookUpTableBuilder.readFromLookup(i, true);
			HashMap<Integer, ArrayList<Integer>> v=LookUpTableBuilder.readFromFile(vals.get(0), true, vals.get(1));	
			collectioncount=0;
			for(int j:v.keySet()){
				collectioncount+=v.get(j).size();
			}
			
			for(int j:v.keySet()){
				if(sortedValues.containsKey(j)){
					sortedValues.put(j, sortedValues.get(j)+ScoringModels.qljm(j,N,collectioncount,v.get(j).size(),lambda));
				}else{
					sortedValues.put(j, ScoringModels.qljm(j,N,collectioncount,v.get(j).size(),lambda));
				}
			}
		}
		
		sortedValues=(HashMap<Integer, Double>) sortByValue(sortedValues);
		String filename="saved/qljm_trecrun.txt";
		writetrec(sortedValues,terms,querynumber,"hari-qljm-lambda"+"-"+lambda,filename);
		
		int count=0;
		for(int i:sortedValues.keySet()){
			if(count>n-1){
				break;
			}
			docIds.add(i);
			count++;
		}
		
		return docIds;
	}
	
	public static ArrayList<Integer> QLDS(ArrayList<String> terms,boolean compressFlag,int n,double mu,double N,int querynumber)throws IOException{
		ArrayList<Integer> docIds=new ArrayList<>();
		HashMap<Integer,Double> sortedValues=new HashMap<Integer, Double>();

		
		double collectioncount=0;
		for(String i:terms){
			ArrayList<Integer> vals=LookUpTableBuilder.readFromLookup(i, true);
			HashMap<Integer, ArrayList<Integer>> v=LookUpTableBuilder.readFromFile(vals.get(0), true, vals.get(1));	
			collectioncount=0;
			for(int j:v.keySet()){
				collectioncount+=v.get(j).size();
			}
			for(int j:v.keySet()){
				if(sortedValues.containsKey(j)){
					sortedValues.put(j, sortedValues.get(j)+ScoringModels.qlds(j,N,collectioncount,1.0*v.get(j).size(),mu));
				}else{
					sortedValues.put(j, ScoringModels.qlds(j,N,collectioncount,1.0*v.get(j).size(),mu));
				}
			}
		}
		
		sortedValues=(HashMap<Integer, Double>) sortByValue(sortedValues);
		String filename="saved/qlds_trecrun.txt";
		writetrec(sortedValues,terms,querynumber,"hari-qlds-mu"+"-"+mu,filename);
		
		int count=0;
		for(int i:sortedValues.keySet()){
			if(count>n-1){
				break;
			}
			docIds.add(i);
			count++;
		}
		
		return docIds;
	}
	
	public static ArrayList<Integer> vectorspacemodel(ArrayList<String> terms,boolean compressFlag,int n,int N,int querynumber)throws IOException{
		ArrayList<Integer> docIds=new ArrayList<>();
		HashMap<Integer,Double> sortedValues=new HashMap<Integer, Double>();

		HashMap<String,Integer> querycounts=new HashMap<>();
		for(String i:terms){
			if(querycounts.containsKey(i)){
				querycounts.put(i, querycounts.get(i)+1);
				
			}else{
			querycounts.put(i, 1);
		
			}
			}
		int collectioncount;
		for(String i:terms){
			ArrayList<Integer> vals=LookUpTableBuilder.readFromLookup(i, true);
			HashMap<Integer, ArrayList<Integer>> v=LookUpTableBuilder.readFromFile(vals.get(0), true, vals.get(1));	
			collectioncount=0;
			for(int j:v.keySet()){
				collectioncount+=v.get(j).size();
			}
			for(int j:v.keySet()){
				if(sortedValues.containsKey(j)){
					sortedValues.put(j, sortedValues.get(j)+ScoringModels.norm(j,N,collectioncount,v.get(j).size(),querycounts.get(i)));
				}else{
					sortedValues.put(j, ScoringModels.norm(j,N,collectioncount,v.get(j).size(),querycounts.get(i)));
				}
			}
		}
		for( Integer i:sortedValues.keySet()){
			sortedValues.put(i,sortedValues.get(i)/PostingListBuilder.docLen(i));
		}
		sortedValues=(HashMap<Integer, Double>) sortByValue(sortedValues);
		
		int count=0;
		for(int i:sortedValues.keySet()){
			if(count>n-1){
				break;
			}
			docIds.add(i);
			count++;
		}
		
		return docIds;
	}
	
}
