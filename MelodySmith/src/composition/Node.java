package composition;

import java.util.ArrayList;

/**
 * Represents a single node in the association network
 * 		Contains information about a single note in a song
 *
 */
public class Node {
	int scaleDegree = 0;
	int pitch = 0;
	String duration = "";
	int noteLength = 0;
	int distanceToCadence = 0;
	int distanceFromTonic = 0;
	int beat = 0;
	int velocity = 0;
	int tempo = 0;
	int channel = 0;
	String key = "";
	String pattern = "";
	String song = "";
	String artist = "";
	Node destinationNode = null;
	Node previousNode = null;
	ArrayList<Node> concurrentNodes = new ArrayList<Node>();
	ArrayList<Link> linkedNodes = new ArrayList<Link>();
	
	void addConcurrentNode(Node concurrentNode) {
		concurrentNodes.add(concurrentNode);
	}
	
	void linkNodes(Node otherNode, double weight, double reverseWeight) {
		Link link = new Link(this, otherNode, weight);
		Link otherLink = new Link(otherNode, this, reverseWeight);
		
		linkedNodes.add(link);
		otherNode.linkedNodes.add(otherLink);
	}
	
}
