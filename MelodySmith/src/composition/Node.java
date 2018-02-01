package composition;

import java.util.ArrayList;

/**
 * Represents a single node in the association network
 * 		Contains information about a single note in a song
 */
public class Node {	
	public static final String[] NOTES = { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };
	public static final String[] SCALEDEGREE = { "C", "D", "E", "F", "G", "A", "B" };
	public static final String[] KEYS = { "Cb", "Gb", "Db", "Ab", "Eb", "Bb", "F", "C", "G", "D", "A", "E", "B", "F#",
	"C#" };
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

	public int index = 0;
	
	public String pattern = "";
	public String artist = "";
	
	ArrayList<Node> concurrentNodes = new ArrayList<Node>();
	ArrayList<Link> linkedNodes = new ArrayList<Link>();
	
	// Code from Note
	/**
	 * A Node with no key is a rest
	 * 
	 * @param channel
	 * @param startTick
	 * @param song
	 * @param instrument
	 * @param keySignature
	 * @param timeSignature
	 * @param bpm
	 */
	public Node(int channel, long startTick, String song, String instrument, String keySignature, String timeSignature,
			float bpm) {
		this.key = -1;
		note = -1;
		octave = -1;
		this.velocity = -1;
		this.channel = channel;
		this.startTick = startTick;
		noteName = "Rest";
		this.song = song;
		this.instrument = instrument;
		if (keySignature != null) {
			this.keySignature = keySignature;
		}
		if (timeSignature != null) {
			this.timeSignature = timeSignature;
		}
		if (bpm != 0) {
			this.bpm = bpm;
		}
		scaleDegree = getScaleDegree();
	}

	/**
	 * Regular Node constructor
	 * 
	 * @param key
	 * @param velocity
	 * @param channel
	 * @param startTick
	 * @param song
	 * @param instrument
	 * @param keySignature
	 * @param timeSignature
	 * @param bpm
	 */
	public Node(int key, int velocity, int channel, long startTick, String song, String instrument, String keySignature,
			String timeSignature, float bpm) {
		this.key = key;
		note = key % 12;
		octave = (key / 12) - 1;
		this.velocity = velocity;
		this.channel = channel;
		this.startTick = startTick;
		noteName = NOTES[note];
		this.song = song;
		this.instrument = instrument;
		if (keySignature != null) {
			this.keySignature = keySignature;
		}
		if (timeSignature != null) {
			this.timeSignature = timeSignature;
		}
		if (bpm != 0) {
			this.bpm = bpm;
		}
		scaleDegree = getScaleDegree();
	}
	
	/**
	 * Compares the keys of this and note
	 * 
	 * @param note
	 * @return this.key - note.key
	 */
	public int compareTo(Node note) {
		return this.key - note.key;
	}

	/**
	 * Turns off this note, sets the tickDuration and the noteDuration
	 * 
	 * @param endTick
	 * @param ticksPerQuarterNote
	 */
	public void turnOff(long endTick, int ticksPerQuarterNote) {
		tickDuration = endTick - startTick;

		double ticksPerQuarterNoteDouble = (double) ticksPerQuarterNote;

		if (ticksPerQuarterNote == 0) {
			noteDuration = "N/A";
		} else if ((tickDuration / (long) (ticksPerQuarterNoteDouble * 4)) >= .9) {
			noteDuration = (int) (Math.round((double) tickDuration / ticksPerQuarterNoteDouble)) + "";
		} else if ((tickDuration / ticksPerQuarterNoteDouble) < 2.5
				&& (tickDuration / ticksPerQuarterNoteDouble) >= 1.6) {
			noteDuration = "1/2";
		} else if ((tickDuration / ticksPerQuarterNoteDouble) < 1.15
				&& (tickDuration / ticksPerQuarterNoteDouble) >= .75) {
			noteDuration = "1/4";
		} else if ((tickDuration / ticksPerQuarterNoteDouble) < .75
				&& (tickDuration / ticksPerQuarterNoteDouble) >= .375) {
			noteDuration = "1/8";
		} else if ((tickDuration / ticksPerQuarterNoteDouble) < .375
				&& (tickDuration / ticksPerQuarterNoteDouble) >= .1875) {
			noteDuration = "1/16";
		} else if ((tickDuration / ticksPerQuarterNoteDouble) < .1875) {
			noteDuration = "1/32";
		}
	}

	/**
	 * Helper method to process the scale degree from the note name and Key
	 * Signature
	 * 
	 * @return
	 */
	private int getScaleDegree() {
		String note = noteName.substring(0, 1);
		String start = keySignature.substring(0, 1);

		int startIndex = 0;
		for (int i = 0; i < SCALEDEGREE.length; i++) {
			if (SCALEDEGREE[i].equals(start)) {
				startIndex = i;
			}
		}

		for (int i = 0; i < SCALEDEGREE.length; i++) {
			if (SCALEDEGREE[(startIndex + i) % 7].equals(note)) {
				return i;
			}
		}

		return 0;
	}
	// end from Note
	
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
