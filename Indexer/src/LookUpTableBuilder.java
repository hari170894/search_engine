
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.HashMap;

import org.json.simple.JSONArray;

public class LookUpTableBuilder {
	/**
	 * Method for building lookup table using the postinglist
	 * @param postings
	 * @param compressflag
	 * @throws IOException
	 */
	public static void buildLookupTable(ArrayList<PostingOriginal> postings,boolean compressflag) throws IOException{
		JSONArray jsonArray=JSONFileReader.getData();
		RandomAccessFile file,file1;
		if (compressflag==true){
			file=new RandomAccessFile("saved/compressed.bin", "rw");	
		
		}else{
			file=new RandomAccessFile("saved/uncompressed.bin", "rw");
		}
		if(compressflag==true){
			
			file1=new RandomAccessFile("saved/compressedlookup.txt", "rw");
		}else{
			
			file1=new RandomAccessFile("saved/uncompressedlookup.txt", "rw");		
		}
		HashMap<String,Long> termToOffset=new HashMap<>();
		HashSet<String> words=VocabBuilder.buildVocab(jsonArray);
		for (String term: words){
			TreeMap<Integer,ArrayList<Integer>> docAndPosList=new TreeMap<>();
			
			for (PostingOriginal p:postings){
				if (term.equals(p.getTerm())){
					if (docAndPosList.containsKey(p.getDocId())){
						ArrayList<Integer> posList=docAndPosList.get(p.getDocId());
						posList.add(p.getPostionOfPosting());
						docAndPosList.put(p.getDocId(), posList);
					}else{
						ArrayList<Integer> posList=new ArrayList<>();
						posList.add(p.getPostionOfPosting());
						docAndPosList.put(p.getDocId(), posList);
					}
				}
			}
			long offset=file.getFilePointer();
			if(compressflag==true){
				int prev=0;
				for (Integer i :docAndPosList.keySet()){
					VByteEncoder.encode(file, i-prev);
				
					prev=i;
					ArrayList<Integer> posList=docAndPosList.get(i);
					VByteEncoder.encode(file, posList.size());
					
					for (Integer j:posList){
						VByteEncoder.encode(file, j);
					}
				}
			}else{
			
			for (Integer i :docAndPosList.keySet()){
				file.writeInt(i);
				ArrayList<Integer> posList=docAndPosList.get(i);
				file.writeInt(posList.size());
				for (Integer j:posList){
					file.writeInt(j);
				}
			}
			}
			
			
			termToOffset.put(term, offset);
			
				file1.writeChars(term);
				file1.writeChars(" ");
				file1.writeChars(String.valueOf(termToOffset.get(term)));
				file1.writeChars(" ");
				file1.writeChars(String.valueOf(docAndPosList.keySet().size()));
				int count=0;
				for (Integer i:docAndPosList.keySet()){
					ArrayList<Integer> list=docAndPosList.get(i);
					count+=list.size();
				}
				file1.writeChars(" ");
				file1.writeChars(String.valueOf(count));
				file1.writeChars("\n");
			
		}
		file.close();
		
		file1.close();
	}
	
	/**
	 * Read the offset dtf and ctf from disk
	 * @param s
	 * @param compressflag
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<Integer> readFromLookup(String s,boolean compressflag) throws IOException{
		File file;
		if (compressflag==true){
		file=new File("saved/compressedlookup.txt");
		}else{
		file=new File("saved/uncompressedlookup.txt");
				
		}
		
		FileInputStream fis=new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		String line;
		s=s.trim();
		while((line=br.readLine()) != null){
			String[] parts=line.trim().split("\\s+");
			
			parts[0]=parts[0].replaceAll("\0", "");
			parts[1]=parts[1].replaceAll("\0", "");
			parts[2]=parts[2].replaceAll("\0", "");
			parts[3]=parts[3].replaceAll("\0", "");
			
		
			
			if(parts[0].equals(s.toString())){

				ArrayList<Integer> retval=new ArrayList<>();
				
				retval.add(Integer.valueOf(parts[1]));
				retval.add(Integer.valueOf(parts[2]));
				retval.add(Integer.valueOf(parts[3]));
				
				return retval;
				
			}
		}
		br.close();
		isr.close();
		fis.close();
		return null;
	}
	/**
	 * Read inverted list from disk using offset and number of docs it occured
	 * @param pos
	 * @param compressflag
	 * @param numberoftimes
	 * @return
	 * @throws IOException
	 */
	public static HashMap<Integer, ArrayList<Integer>> readFromFile(int pos,boolean compressflag,int numberoftimes) throws IOException{
		RandomAccessFile file;
		if (compressflag==true){
		file=new RandomAccessFile("saved/compressed.bin", "rw");
		}else{
		file=new RandomAccessFile("saved/uncompressed.bin", "rw");
		}
		HashMap<Integer, ArrayList<Integer>> postingList=new HashMap<>();
		int docId = 0,times;
		ArrayList<Integer> positions = null;
		file.seek(pos);
		if (compressflag==true){
			int prev=0;
			for(int i=0;i<numberoftimes;i++){
				docId=prev+VByteEncoder.decode(file);
				prev=docId;
				times=VByteEncoder.decode(file);
				positions=new ArrayList<>();
				for(int j=0;j<times;j++){
					int val=VByteEncoder.decode(file);
					positions.add(val);
				}
				postingList.put(docId,positions);
			
				
			}
		}else{
			for (int i=0;i<numberoftimes;i++){
				docId=file.readInt();
				times=file.readInt();
				positions=new ArrayList<>();
				for(int j=0;j<times;j++){
					int val=file.readInt();
					positions.add(val);
				}
				postingList.put(docId,positions);
			
				
		}
			
		}
		
		
		
		file.close();
		return postingList;
	}

}
