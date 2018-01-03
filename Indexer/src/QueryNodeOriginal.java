import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class QueryNodeOriginal {

	public ArrayList<Integer> docIds =new ArrayList<>();
	public ArrayList<Double> scores=new ArrayList<>();
	public ArrayList<ArrayList<Integer>> posLists=new ArrayList<>();
	
	public void belnot(QueryNodeOriginal e){
		for(int i=0;i<scores.size();i++){
			double x=Math.pow(10, scores.get(i));
			scores.set(i,Math.log10(1-x));
		}
	}
	
	public void beland(ArrayList<QueryNodeOriginal> e){
		boolean flag=false;
		ArrayList<Integer> docs=new ArrayList<>();
		for (QueryNodeOriginal node:e){
			if(flag==false){
				docs=node.docIds;
				flag=true;
			}else{
				docs=InfModels.intersection(docs, node.docIds);
			}
		}
		for(int i:docs){
			int idx;
			double score=1;
			for(QueryNodeOriginal q:e){
				if(q.docIds.contains(i)){
				idx=q.docIds.indexOf(i);
				score*=Math.pow(10,q.scores.get(idx));
				}
				this.docIds.add(i);
				this.scores.add(Math.log10(score));
			}
		}
	}
	
	public void belmax(ArrayList<QueryNodeOriginal> e){
		boolean flag=false;
		ArrayList<Integer> docs=new ArrayList<>();
		for (QueryNodeOriginal node:e){
			if(flag==false){
				docs=node.docIds;
				flag=true;
			}else{
				docs=InfModels.intersection(docs, node.docIds);
			}
		}
		for(int i:docs){
			int idx;
			double score=-999999;
			for(QueryNodeOriginal q:e){
				if(q.docIds.contains(i)){
				idx=q.docIds.indexOf(i);
				double val=Math.pow(10,q.scores.get(idx));
				if(val>score){
					score=val;
				}
				}
				this.docIds.add(i);
				this.scores.add(Math.log10(score));
			}
		}
	}
	
	public void belsum(ArrayList<QueryNodeOriginal> e){
		boolean flag=false;
		ArrayList<Integer> docs=new ArrayList<>();
		for (QueryNodeOriginal node:e){
			if(flag==false){
				docs=node.docIds;
				flag=true;
			}else{
				docs=InfModels.intersection(docs, node.docIds);
			}
		}
		for(int i:docs){
			int idx;
			double score=0;
			for(QueryNodeOriginal q:e){
				if(q.docIds.contains(i)){
				idx=q.docIds.indexOf(i);
				score+=Math.pow(10,q.scores.get(idx));
				}
			}
				this.docIds.add(i);
				this.scores.add(Math.log10(score/docs.size()));
			
		}
	}
	
	public void belwsum(ArrayList<QueryNodeOriginal> e,ArrayList<Double> wts){
		boolean flag=false;
		ArrayList<Integer> docs=new ArrayList<>();
		for (QueryNodeOriginal node:e){
			if(flag==false){
				docs=node.docIds;
				flag=true;
			}else{
				docs=InfModels.intersection(docs, node.docIds);
			}
		}
		for(int i:docs){
			int idx;
			double score=0;
			int k=0;
			for(QueryNodeOriginal q:e){
				if(q.docIds.contains(i)){
				idx=q.docIds.indexOf(i);
				score+=Math.pow(10,q.scores.get(idx))*wts.get(k);
				}
				k++;
			}
			double wsum=0;
			for(double q:wts){
				wsum+=q;
			}
				this.docIds.add(i);
				this.scores.add(Math.log10(score/wsum));
			
		}
	}
	
	public void belwand(ArrayList<QueryNodeOriginal> e,ArrayList<Double> wts){
		boolean flag=false;
		ArrayList<Integer> docs=new ArrayList<>();
		for (QueryNodeOriginal node:e){
			if(flag==false){
				docs=node.docIds;
				flag=true;
			}else{
				docs=InfModels.intersection(docs, node.docIds);
			}
		}
		for(int i:docs){
			int idx;
			double score=1;
			int k=0;
			for(QueryNodeOriginal q:e){
				if(q.docIds.contains(i)){
				idx=q.docIds.indexOf(i);
				score*=Math.pow(Math.pow(10,q.scores.get(idx)),wts.get(k));
				}
				k+=1;
			}
			this.docIds.add(i);
			this.scores.add(Math.log10(score));
			
		}
	}
	
	
	public void belor(ArrayList<QueryNodeOriginal> e){
		boolean flag=false;
		ArrayList<Integer> docs=new ArrayList<>();
		for (QueryNodeOriginal node:e){
			if(flag==false){
				docs=node.docIds;
				flag=true;
			}else{
				docs=InfModels.intersection(docs, node.docIds);
			}
		}
		for(int i:docs){
			int idx;
			double score=1;
			for(QueryNodeOriginal q:e){
				if(q.docIds.contains(i)){
				idx=q.docIds.indexOf(i);
				score*=1-Math.pow(10, q.scores.get(idx));
				}
			}
			this.docIds.add(i);
			this.scores.add(Math.log10(1-score));
			
		}
	}
	
	public void orderedwindow(ArrayList<QueryNodeOriginal> e,int windowSize){
		boolean flag=false;
		ArrayList<Integer> docs=new ArrayList<>();
		for (QueryNodeOriginal node:e){
			if(flag==false){
				docs=node.docIds;
				flag=true;
			}else{
				docs=InfModels.intersection(docs, node.docIds);
			}
		}
		for(int i:docs){
			int idx;
			double score;
			ArrayList<ArrayList<Integer>> postings=new ArrayList<>();
			for(QueryNodeOriginal q:e){
				if(q.docIds.contains(i)){
					idx=q.docIds.indexOf(i);
					postings.add(q.posLists.get(idx));
					}
				
			}
			score=InfModels.findorderedcount(postings, windowSize);
			this.docIds.add(i);
			this.scores.add(Math.log10(score));
		}
	}
	public void unorderedwindow(ArrayList<QueryNodeOriginal> e,int windowSize){
		boolean flag=false;
		ArrayList<Integer> docs=new ArrayList<>();
		for (QueryNodeOriginal node:e){
			if(flag==false){
				docs=node.docIds;
				flag=true;
			}else{
				docs=InfModels.intersection(docs, node.docIds);
			}
		}
		for(int i:docs){
			int idx;
			double score;
			ArrayList<ArrayList<Integer>> postings=new ArrayList<>();
			for(QueryNodeOriginal q:e){
				if(q.docIds.contains(i)){
					idx=q.docIds.indexOf(i);
					postings.add(q.posLists.get(idx));
					}
				
			}
			score=InfModels.findunorderedcount(postings, windowSize);
			this.docIds.add(i);
			this.scores.add(Math.log10(score));
		}
	}
	
	
	
	public void term(String t) throws IOException{
		ArrayList<Integer> vals=LookUpTableBuilder.readFromLookup(t, true);
		HashMap<Integer, ArrayList<Integer>> v=LookUpTableBuilder.readFromFile(vals.get(0), true, vals.get(1));	
		double collectioncount=0;
		for(int i:v.keySet()){
			this.docIds.add(i);
			this.posLists.add(v.get(i));
			collectioncount+=v.get(i).size();
		}
		int N=RunMeAssignment2.sumdoclen();
		for(int i:v.keySet()){
			this.scores.add(ScoringModels.qlds(i, N, collectioncount,1.0* v.get(i).size(), 2000));
		}
		
	}


}
