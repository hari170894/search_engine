

import java.util.ArrayList;

public class BandNode extends UnorderedWindow {

	public BandNode(ArrayList<ProximityNode> termNodes) {
		super(0, termNodes);
	}
	
	@Override
	public Integer getWindowSize(Integer d){
		return Index.getDocLength(d);
	}
}
