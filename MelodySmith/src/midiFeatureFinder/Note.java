package midiFeatureFinder;

public class Note {
	
    public static final String[] NOTES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    
    public int key;
	public int note;
	public int octave;
	public int velocity;
	public long duration;
	public long startTick;
	public String noteName;
	
	/**
	 * 
	 * @param key
	 * @param velocity
	 * @param startTick
	 */
	public Note(int key, int velocity, long startTick) {
		this.key = key;
		note = key % 12;
		octave = (key / 12) - 1;
		this.velocity = velocity;
		this.startTick = startTick;
		noteName = NOTES[note];
	}
	
	public int compareTo(Note note) {
		return this.key - note.key;
	}
	
	public void turnOff(long endTick) {
		duration = endTick - startTick;
	}
}
