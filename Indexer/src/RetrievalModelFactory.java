

public class RetrievalModelFactory {
	public static RetrievalModel getModel() {
		return new Dirichlet(2000);
	}
}
