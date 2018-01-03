import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class RunMe {
	
	public static void docStats(){
		ArrayList<Integer> docIds=new ArrayList<>();
		ArrayList<Integer> docLen=new ArrayList<>();
		int mins=999999999;
		int minp=999999999;
		int maxp=-99999999;
		String MaxPlay=null,MinPlay=null,MinScene=null;
		double sums=0.0;		
		for(String i:PostingListBuilder.docToScene.keySet()){
			docIds.add(PostingListBuilder.docToScene.get(i));
			docLen.add(PostingListBuilder.docLen(PostingListBuilder.docToScene.get(i)));
			//System.out.println(PostingListBuilder.docLen(PostingListBuilder.docToScene.get(i)));
			sums+=PostingListBuilder.docLen(PostingListBuilder.docToScene.get(i));
			if(PostingListBuilder.docLen(PostingListBuilder.docToScene.get(i))<mins){
				mins=PostingListBuilder.docLen(PostingListBuilder.docToScene.get(i));
				MinScene=i;
			}
		}
		sums/=PostingListBuilder.docToScene.keySet().size();
		HashMap<String, ArrayList<Integer>> playToDocId=PostingListBuilder.playToDoc();
		//System.out.println(playToDocId.values());
		for(String i:playToDocId.keySet()){
			int xsum=0;
			ArrayList<Integer> Ids=playToDocId.get(i);
			for(Integer j:Ids){
			xsum+=PostingListBuilder.docLen(j);
			}
			if(xsum>maxp){
				maxp=xsum;
				MaxPlay=i;
			}
			if(xsum<minp){
				minp=xsum;
				MinPlay=i;
			}
			
		}
		System.out.println("AVG LEN OF SCENE"+sums);

		System.out.println("MIN  SCENE"+MinScene);

		System.out.println("MAX PLAY"+MaxPlay);

		System.out.println("MIN PLAY"+MinPlay);
	}
	
	public static void main(String args[])throws IOException{
		boolean flag;
		//QueryProcessing.loadFromScratch();
		ArrayList<Integer> vals=LookUpTableBuilder.readFromLookup("to", false);
		HashMap<Integer, ArrayList<Integer>> v=LookUpTableBuilder.readFromFile(vals.get(0), false, vals.get(1));	
		for (int i:v.get(543)){
			System.out.println(i);
		}
		//QueryProcessing.loadFromSavedFiles();
		//docStats();
		//VocabBuilder.saveRandomValues();
		/**if(args[0].equals("0")){
			flag=false;
		}else{
			flag=true;
		}**/
		//flag=true;
		//long c=(VocabBuilder.readRandomValues(flag));
		//flag=false;
		//long uc=(VocabBuilder.readRandomValues(flag));
		//System.out.println(c+" "+uc);
	}
}
