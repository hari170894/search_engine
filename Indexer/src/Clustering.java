import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.json.simple.JSONArray;

public class Clustering {
	
	public static HashMap<Integer, Double[]> buildunitvectors(int N) throws Exception{
		HashMap <Integer,Double[]> unitvectors=new HashMap<>();
		JSONArray data=JSONFileReader.getData();
		HashSet<String> vocab=VocabBuilder.buildVocab(data);
		int p=0;
		for(String term:vocab){
			ArrayList<Integer> vals=LookUpTableBuilder.readFromLookup(term, true);
			HashMap<Integer, ArrayList<Integer>> v=LookUpTableBuilder.readFromFile(vals.get(0), true, vals.get(1));	
			int collectioncount=v.keySet().size();
			for (Integer i:v.keySet()){
				if(unitvectors.containsKey(i)){
					Double[] values=unitvectors.get(i);
					values[p]=ScoringModels.wt(v.get(i).size(), N, collectioncount);
					unitvectors.put(i, values);
				}else{
					Double[] values=new Double[vocab.size()];
					for(int o=0;o<values.length;o++){
						values[o]=0.0;
					}
					values[p]=ScoringModels.wt(v.get(i).size(), N, collectioncount);
					unitvectors.put(i, values);
				}
			}
		p++;
		}
		System.out.println(unitvectors.keySet().size());
	
		return unitvectors;
	}
	
	
	public static void agglomorative(int mode,int N,double threshold,HashMap<Integer, Double[]> unitvectors) throws Exception{
	
	 HashMap<Integer, ArrayList<Integer>> clusters=new HashMap<>();
	 int firstitemflag=1;
	 int clusternumber=1;
	 int finished =0;
		while (finished == 0) {
			finished=1;
			for (Integer docid : unitvectors.keySet()) {

				if (firstitemflag == 1) {

					ArrayList<Integer> docs = new ArrayList<>();
					docs.add(docid);
					clusters.put(clusternumber++, docs);
					firstitemflag = 0;
				} else {
					int correctcluster = -1;
					double min = 99999;
					double max = -99999;
					double umin = 99999;
					double cmin=99999;
					for (int clusterid : clusters.keySet()) {

						if (mode == 1) {
							for (int clusterdocs : clusters.get(clusterid)) {

								double x = 1 - ScoringModels.cosineSimilarity(unitvectors.get(clusterdocs),
										unitvectors.get(docid));
								if (x <= threshold) {
									if (x <= min) {
										correctcluster = clusterid;
										min = x;
									}
								}
							}
						} else if (mode == 2) {
							for (int clusterdocs : clusters.get(clusterid)) {
								double x = 1 - ScoringModels.cosineSimilarity(unitvectors.get(clusterdocs),
										unitvectors.get(docid));
								if (x <= threshold) {
									if (x >= max) {
										correctcluster = clusterid;
										max = x;
									}
								}
							}

						} else if (mode == 3) {
							double score = 0.0;
							for (int clusterdocs : clusters.get(clusterid)) {
								score += 1 - ScoringModels.cosineSimilarity(unitvectors.get(clusterdocs),
										unitvectors.get(docid));

							}
							score /= clusters.get(clusterid).size();
							if (score <= threshold) {
							if (score <= umin) {
								correctcluster = clusterid;
								umin = score;

							}}

						}else if (mode==4){
							Double[] clustercentriod=new Double[unitvectors.get(0).length];
							for(int o=0;o<clustercentriod.length;o++){
								clustercentriod[o]=0.0;
							}
							for(int clusterdocs:clusters.get(clusterid)){
								Double[] vector=unitvectors.get(clusterdocs);
								for(int j=0;j<vector.length;j++){
									clustercentriod[j]+=vector[j];
									
								}
								
							}
							for(int o=0;o<clustercentriod.length;o++){
								clustercentriod[o]/=clusters.get(clusterid).size();
							}
							double score=1-ScoringModels.cosineSimilarity(clustercentriod, unitvectors.get(docid));
							if(score<=threshold){
							if(score<cmin){
								correctcluster=clusterid;
								cmin=score;
							}
							}
						}
					}
					if (correctcluster == -1) {
						ArrayList<Integer> docs = new ArrayList<>();
						docs.add(docid);
						finished=0;
						clusters.put(clusternumber++, docs);
						correctcluster = clusternumber - 1;
					} else {
						ArrayList<Integer> e = clusters.get(correctcluster);
						if (e.contains(docid)){
							
						}else{
						for(int clusterid:clusters.keySet()){
						ArrayList<Integer> docids=clusters.get(clusterid);
						if(docids.contains(docid)){
							docids.remove(new Integer(docid));
							clusters.put(clusterid, docids);
						}
						}
						e.add(docid);
						finished=0;
						}
						clusters.put(correctcluster, e);
					}
				}
			}
		}
		System.out.println(threshold+" "+(clusternumber-1));
		if (mode==4){
		writefiles(clusters,"saved/cluster-"+threshold+".txt");
		}
	}


	private static void writefiles(HashMap<Integer, ArrayList<Integer>> clusters,String filename) throws IOException {
		RandomAccessFile file=new RandomAccessFile(filename, "rw");
		
		for(int i:clusters.keySet()){
			for(int j:clusters.get(i)){
				file.seek(file.length());
				file.writeChars(i+" "+j+"\n");
			}
		}
		file.close();
	}
}
