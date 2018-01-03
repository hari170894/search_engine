import java.util.ArrayList;

public class Prior implements QueryNode{
	
	String probType;
	public Prior(String s){
		probType=s;
	}
	
	
	@Override
	public Double score(Integer docId) {
		
		return ProbabilityListBuilder.readBinaryfile(probType, docId*8);
	}

	@Override
	public Integer nextCandidate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasMore() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int skipTo(int docId) {
		// TODO Auto-generated method stub
		return 0;
	}

}
