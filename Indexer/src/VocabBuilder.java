import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class VocabBuilder {
	
	static int docId=0;
	private static String[] getTerms(String s){
		String[] termList=s.trim().split("\\s+");
		return termList;
	}
	
	
	public static HashSet<String> buildVocab(JSONArray jsonArray){
		HashSet<String> vocab=new HashSet<String>();
		for(Object o:jsonArray){
        	JSONObject jsonLineItem = (JSONObject) o;
        	String[] termsInDoc=getTerms((String) jsonLineItem.get("text"));
        	for (String term:termsInDoc){
        		vocab.add(term);
        	}
        	
		}
		return vocab;
		
	}
	
	public static HashMap<String,Integer> twoWindow(JSONArray jsonArray){
		HashMap<String,Integer> wordToWindow=new HashMap<>();
		for(Object o:jsonArray){
        	JSONObject jsonLineItem = (JSONObject) o;
        	String[] termsInDoc=getTerms((String) jsonLineItem.get("text"));
        	for (int i=0;i<termsInDoc.length;i++){
        		if(wordToWindow.containsKey(termsInDoc[i])){
        			if(i==0||i==termsInDoc.length-1){
        				wordToWindow.put(termsInDoc[i], wordToWindow.get(termsInDoc[i])+1);
        			}else{
        				wordToWindow.put(termsInDoc[i], wordToWindow.get(termsInDoc[i])+2);
            			
        			}
        		}else{
        			if(i==0||i==termsInDoc.length-1){
        				wordToWindow.put(termsInDoc[i], 1);
            			
        			}else{
        				wordToWindow.put(termsInDoc[i], 2);
            			
        			}
        		}
        	}
        	
		}
		return wordToWindow;
		
	}
	public static ArrayList<String> random(int n,HashSet<String> vocab){
		Random rand=new Random(System.currentTimeMillis());
		ArrayList<String> list=new ArrayList<>();
		String[] array = new String[vocab.size()];
	    vocab.toArray(array);
		for(int i=0;i<n;i++){
			list.add(array[rand.nextInt(vocab.size())]);
		}
		
		return list;
	}
	
	public static ArrayList<String> findComplement(HashSet<String> vocab,HashMap <String,Integer> twoWindow,JSONArray jsonArray) throws IOException{
		ArrayList<String> wordsWithDice=new ArrayList<>();
		ArrayList<String> vals=random(7, vocab);;
		
		
		ArrayList<String> complementwords=new ArrayList<>();
		for(String word:vals){
			complementwords.add(QueryProcessing.findClosestWord(word, twoWindow, jsonArray));
		}
		for(String i:vals){
			wordsWithDice.add(i);
		}
		for(String i: complementwords){
			wordsWithDice.add(i);
		}
		
		
		return wordsWithDice;
	}
	
public static HashMap<String, Integer> findCoOccurences(JSONArray jsonArray,String term){
		
		HashMap<String, Integer> wordToCount=new HashMap<>();
		for (Object o:jsonArray){
			JSONObject jsonLineItem = (JSONObject) o;
			String s=(String)jsonLineItem.get("text");
			String[] termList=s.trim().split("\\s+");
			for (int i=0;i<termList.length-1;i++){
				  if(termList[i].equals(term)){
					  if(termList[i+1]!=""){
						  if(wordToCount.containsKey(termList[i+1])){
							  Integer val=wordToCount.get(termList[i+1]);
							  val+=1;
							  wordToCount.put(termList[i+1],val);
						  }else{
							  wordToCount.put(termList[i+1],0);
						
						  }
					  }
				  }
				  if(termList[i+1].equals(term)){
					  
					  if(termList[i]!=""){
						  if(wordToCount.containsKey(termList[i])){
							  Integer val=wordToCount.get(termList[i]);
							  val+=1;
							  wordToCount.put(termList[i],val);
						  }else{
							  wordToCount.put(termList[i],0);
						
						  }
					  }
				  }
				
			}
		}
		
		return wordToCount;		
	}

	
	

	public static void saveRandomValues() throws IOException{
		JSONArray jsonArray=JSONFileReader.getData();
		HashSet<String> vocab=buildVocab(jsonArray);
		HashMap<String,	Integer> twoWindow=twoWindow(jsonArray);
		RandomAccessFile file=new RandomAccessFile("saved/words.txt", "rw");
		RandomAccessFile file1=new RandomAccessFile("saved/wordsWithDice.txt", "rw");
		
		for(int x=0;x<100;x++){
		ArrayList<String> valsToWrite=findComplement(vocab, twoWindow, jsonArray);
		
		for(int i=0;i<7;i++)
		file.writeChars(valsToWrite.get(i)+" ");
		
		for(int i=0;i<14;i++)
		{
			file1.writeChars(valsToWrite.get(i)+" ");
		}
		
		
		file.writeChars("\n");

		file1.writeChars("\n");
		
		}
		file.close();
		file1.close();
	}	
	
	public static long readRandomValues(boolean flag) throws IOException{
		File file=new File("saved/wordsWithDice.txt");
		FileInputStream fis=new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		String line;
		long sum=0;
		while((line=br.readLine()) != null){
			String[] parts=line.trim().split("\\s+");
			
			for(int i=0;i<parts.length;i++){
				parts[i]=parts[i].replaceAll("\0", "");
				
			}
			//System.out.println(parts.length);
			if (parts.length==0){
				break;
			}
			ArrayList<String> valsToWrite=new ArrayList<>(Arrays.asList(parts));
			long start=System.nanoTime();
			ArrayList<Integer> i=RetreivalModels.rawCountModel(valsToWrite, flag, 1);
			long end=System.nanoTime();
			//System.out.println(" TIME IN NANO SECONDS : "+(end-start));
			sum+=end-start;
			PostingListBuilder.loadDocToScene();
			ArrayList<String> scenes=PostingListBuilder.getSceneIds(i);
			

			for(String scne:scenes){
				System.out.println(scne);
			}
		
		}	
		br.close();
		isr.close();
		fis.close();
		return sum;
	}
	public static void main(String args[]) throws IOException{
		//QueryProcessing.loadFromScratch();
		
		//saveRandomValues();
		//readRandomValues(true);
	}

}
