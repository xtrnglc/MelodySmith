package midiFeatureFinder;

public class Note {
	
    public static final String[] NOTES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    public static final String[] SCALEDEGREE = {"C", "D", "E", "F", "G",  "A", "B"};

    public int key;
	public int note;
	public int octave;
	public int velocity;
	public int scaleDegree;
	public int channel;
	public float bpm = 120;
	
	public double noteDuration;
	
	public long duration;
	public long startTick;
	
	public String noteName;
	public String song;
	public String instrument;
    public String keySignature = "C major";
    public String timeSignature = "4/4";
	
	/**
	 * 
	 * @param key
	 * @param velocity
	 * @param startTick
	 */
	public Note(int key, int velocity, int channel, long startTick, 
			String song, String instrument, String keySignature, 
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
		scaleDegree = setScaleDegree();
	}
	
	public int compareTo(Note note) {
		return this.key - note.key;
	}
	
	public void turnOff(long endTick) {
		duration = endTick - startTick;
		noteDuration = duration / bpm;
	}
	
	private int setScaleDegree() {
		String note = noteName.substring(0, 1);
		String start = keySignature.substring(0, 1);
		
		int startIndex = 0;
		for (int i = 0;  i < SCALEDEGREE.length; i++) {
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
}
