package composition;

import java.util.ArrayList;

public class AssociationNetwork {
	ArrayList<Node> network = new ArrayList<Node>();
	
	void addNode(Node newNode) {
		for(Node node : network) {
			newNode.linkNodes(node);
		}
		network.add(newNode);
	}
	
	Node deduceNextNode(Node currentNode) {
		Link mostLikelyLink = null;
		ArrayList<Link> equivalentLinks = new ArrayList<Link>();
		for(Link link : currentNode.linkedNodes) {
			if(mostLikelyLink == null || link.weight > mostLikelyLink.weight) {
				equivalentLinks.clear();
				mostLikelyLink = link;
			} else if(link.weight == mostLikelyLink.weight) {
				equivalentLinks.add(link);
			}
		}
		return mostLikelyLink.endNode;
	}
}
