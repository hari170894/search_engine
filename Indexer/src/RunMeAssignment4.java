import java.io.IOException;
import java.util.HashMap;

public class RunMeAssignment4 {
	public static void main(String args[]) throws Exception {
		QueryProcessing.loadFromSavedFiles();
		HashMap<Integer, Double[]> unitvectors = Clustering
				.buildunitvectors(PostingListBuilder.docToScene.keySet().size());
		for (int i = 4; i <= 4; i++) {
			System.out.println("MODE" + i);
			for (int j = 1; j <= 19; j++) {
				Clustering.agglomorative(i, PostingListBuilder.docToScene.keySet().size(), j*0.05, unitvectors);
			
			}
		}
	}

}
