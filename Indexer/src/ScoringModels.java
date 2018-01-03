
public class ScoringModels {

	public static Integer countscore(int size) {
		
		return size;
	}

	public static Double bm25(int docId,int N,int n, int f, double k1, double k2, double k, Integer qf) {
		return Math.log10(((N-n+0.5)/(n+0.5)))*(k1+1)*f*(k2+1)*qf/((k+f)*(k2+qf));
	}

	public static Double qljm(int docId, double n, double collectioncount, int f, double lambda) {
		return Math.log10((1.0-lambda)*(f*1.0/PostingListBuilder.docLen(docId) )+ lambda*collectioncount*1.0/n);
	}

	public static Double qlds(int docId, double n, double collectioncount, Double f, double mu) {
		return Math.log10((f*1.0+mu*collectioncount*1.0/n)/(PostingListBuilder.docLen(docId)+mu));
	}
	
	public static double tf(int n){
		return Math.log10(n+1);
	}

	public static double idf(int N,int n){
		return Math.log10(N/n);
	}

	public static double wt(int f,int N,int n){
		
		return tf(f)*idf(N,n);
	}
	public static Double norm(int docId, int N, int n, int f, Integer qf) {
		return wt(f,N,n)*wt(qf,N,n);
	}
	public static double cosineSimilarity(Double[] vectorA, Double[] vectorB) {
	    double dotProduct = 0.0;
	    double normA = 0.0;
	    double normB = 0.0;
	    for (int i = 0; i < vectorA.length; i++) {
	        dotProduct += vectorA[i] * vectorB[i];
	        normA += Math.pow(vectorA[i], 2);
	        normB += Math.pow(vectorB[i], 2);
	    }   
	    return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
	}
	
	
	
}
