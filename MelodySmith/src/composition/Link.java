package composition;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a link between two nodes in the association network
 *		Each link has a weight which indicates the likelihood of using this connection
 */
public class Link {
	static HashMap<Integer,Double> calculatedIntervals = new HashMap<Integer,Double>();
	Node startNode = null;
	Node endNode = null;
	int weight = 0;
	
	Link(Node start, Node end){
		startNode = start;
		endNode = end;
	}
	
	private void calculateWeight() {
		weight = 0;
	}
	
	/**
	 *  Weight calculated based on the frequencies of each interval in the network
	 */
	private double calculateIntervalWeight() {
		return 0.0;
	}
	
}
