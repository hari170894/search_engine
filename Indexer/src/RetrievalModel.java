


public interface RetrievalModel {
	/**
	 * @param tf -- term frequency
	 * @param ctf -- collection term frequency
	 * @param docLen -- document length
	 * @return score of the occurrences for the given model
	 */
	public double scoreOccurrence(Integer tf, int ctf, int docLen);
}
