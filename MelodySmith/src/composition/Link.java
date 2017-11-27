package composition;

/**
 * Represents a link between two nodes in the association network
 *		Each link has a weight which indicates the likelihood of using this connection
 */
public class Link {
	Node startNode = null;
	Node endNode = null;
	int weight = 0;
	
	Link(Node start, Node end){
		startNode = start;
		endNode = end;
		calculateWeight();
	}
	
	private void calculateWeight() {
		weight = 0;
	}
}
