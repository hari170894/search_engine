


public class Dirichlet implements RetrievalModel {
	private double mu;
	
	public Dirichlet(double mu) {
		this.mu = mu;
	}

	@Override
	public double scoreOccurrence(Integer tf, int ctf, int docLen) {
		double C = Index.getCollectionSize();
		return Math.log((tf + mu * ctf/C)/(mu + docLen));	
	}	
}	
