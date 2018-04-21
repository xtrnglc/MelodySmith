package composition;

import java.util.HashMap;

/**
 * Represents a link between two nodes in the association network
 *		Each link has a weight which indicates the likelihood of using this connection
 */
public class Link {
	static HashMap<Integer,Double> calculatedIntervals = new HashMap<Integer,Double>();
	Node startNode = null;
	Node endNode = null;
	double weight = 0;
	
	Link(Node start, Node end, double weight){
		startNode = start;
		endNode = end;
		this.weight = weight;
	}
}
