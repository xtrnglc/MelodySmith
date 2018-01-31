package composition;

import java.util.ArrayList;

/**
 * Represents a single node in the association network
 * 		Contains information about a single note in a song
 */
public class Node {	
	public static final String[] NOTES = { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };
	public static final String[] SCALEDEGREE = { "C", "D", "E", "F", "G", "A", "B" };
	
	public int key;
	public int note;
	public int octave;
	public int velocity;
	public int scaleDegree;
	public int channel;
	public float bpm = 120f;
	
	public String noteDuration;
	
	public long tickDuration;
	public long startTick;

	
	public int noteLength = 0;
	public int distanceToCadence = 0;
	public int distanceFromTonic = 0;
	public int beat = 0;
	
	public String noteName;
	public String song;
	public String instrument;
	public String keySignature = "C major";
	public String timeSignature = "4/4";

	// You can ignore everything below here
	public int index = 0;
	
	public String pattern = "";
	public String artist = "";
	
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
