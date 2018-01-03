import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class PostingListBuilder {
	static int docId=0;
	
	static HashMap<String , Integer> docToScene=new HashMap<>();
	static HashMap<Integer , String> docToPlay=new HashMap<>();
	
	public static HashMap<String , ArrayList<Integer>> playToDoc(){
		HashMap<String,ArrayList<Integer>> plDoc=new HashMap<>();
		for(Integer i:docToPlay.keySet()){
			if(plDoc.containsKey(docToPlay.get(i))){
				ArrayList<Integer> vals=plDoc.get(docToPlay.get(i));
				vals.add(i);
				plDoc.put(docToPlay.get(i), vals);
			}else{
				ArrayList<Integer> vals=new ArrayList<>();
				vals.add(i);
				plDoc.put(docToPlay.get(i), vals);
				
			}
		}
		
		return plDoc;
	}
	
	public static int docLen(int docId){
		JSONArray jsonArray=JSONFileReader.getData();
		for (Object o:jsonArray){
			
			JSONObject jsonLineItem = (JSONObject) o;
			
				//System.out.println((String)jsonLineItem.get("sceneId")+"  :  "+i);
				
				if((docToScene.get((String)jsonLineItem.get("sceneId")))==(docId)){
			
					String s=(String)jsonLineItem.get("text");
					//System.out.println(s);
					String[] termList=s.trim().split("\\s+");
					//System.out.println(termList.length);
					return termList.length;
				
			}
			
		}
		return 0;
	}
	
	/**
	 * Method to map docid for a scene
	 * @param sceneId
	 * @return
	 */
	public static int getDocIdfromSceneId(String sceneId){
		if (docToScene.containsKey(sceneId)){
			return docToScene.get(sceneId);
		}else{
			docToScene.put(sceneId, docId++);
			return docId-1;
		}
		
	}
	/**
	 * method to map docid to playid
	 * @param playID
	 * @param docID
	 * @return
	 */
	public static String getDocIdfromPlayId(String playID,int docID){
		if (docToPlay.containsKey(docID)){
			return docToPlay.get(docID);
		}else{
			docToPlay.put(docID, playID);
			return playID;
		}
		
	}
	
	/**
	 * method to get the name of the scene names for given set of docids
	 * @param docIds
	 * @return
	 */
	public static ArrayList<String> getSceneIds(ArrayList<Integer> docIds){
		ArrayList<String> scenes=new ArrayList<>();
		for(int i:docIds){
			for(String s:docToScene.keySet()){
				if(docToScene.get(s)==i){
					scenes.add(s);
					break;
				}
			}
		}
		return scenes;
	}
	/**
	 * saves the doc to scene hash to disk
	 * @throws IOException
	 */
	public static void saveDocToScene() throws IOException{
		RandomAccessFile file=new RandomAccessFile("saved/doctoscene.txt", "rw");
		for(String i:docToScene.keySet()){
			file.writeChars(i);
			file.writeChars(" ");
			
			file.writeChars(docToScene.get(i).toString());
			file.writeChars("\n");
			
		}
		file.close();
		
	}
	
	/**
	 * saves the doc to play hash to disk
	 * @throws IOException
	 */
	public static void saveDocPlay() throws IOException{
		RandomAccessFile file=new RandomAccessFile("saved/doctoplay.txt", "rw");
		for(Integer i:docToPlay.keySet()){
			file.writeChars(i.toString());
			file.writeChars(" ");
			
			file.writeChars(docToPlay.get(i));
			file.writeChars("\n");
			
		}
		file.close();
		
	}
	
	/**
	 * loads the doc to scence from disk
	 * @throws IOException
	 */
	public static void loadDocToScene() throws IOException{
		File file=new File("saved/doctoscene.txt");
		FileInputStream fis=new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		String line;
		while((line=br.readLine()) != null){
			String[] parts=line.trim().split("\\s+");
			
			parts[0]=parts[0].replaceAll("\0", "");
			parts[1]=parts[1].replaceAll("\0", "");
			docToScene.put(parts[0],Integer.valueOf(parts[1]) );
		}	
		br.close();
		isr.close();
		fis.close();
		
	}
	/**
	 * load doc to play
	 * @throws IOException
	 */
	public static void loadDocToPlay() throws IOException{
		File file=new File("saved/doctoplay.txt");
		FileInputStream fis=new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		String line;
		while((line=br.readLine()) != null){
			String[] parts=line.trim().split("\\s+");
			
			parts[0]=parts[0].replaceAll("\0", "");
			parts[1]=parts[1].replaceAll("\0", "");
			docToPlay.put(Integer.valueOf(parts[0]),parts[1] );
		}	
		br.close();
		isr.close();
		fis.close();
		
	}
	
	/**
	 * Method to build a posting list from jsonArray
	 * @param jsonArray
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<PostingOriginal> buildPostingList(JSONArray jsonArray) throws IOException{
		ArrayList<PostingOriginal> postingList=new ArrayList<PostingOriginal>();
		
		for (Object o:jsonArray){
			JSONObject jsonLineItem = (JSONObject) o;
			String s=(String)jsonLineItem.get("text");
			String[] termList=s.trim().split("\\s+");

			int docId=getDocIdfromSceneId((String)jsonLineItem.get("sceneId"));
			getDocIdfromPlayId((String) jsonLineItem.get("playId"), docId);
			int i=0;
			for (String term:termList){
				PostingOriginal p=new PostingOriginal();
				p.AddPosting(term, docId, i);
				i+=1;

				postingList.add(p);
			}
		}
		
		saveDocToScene();
		saveDocPlay();
		return postingList;		
	}
	
	

}
