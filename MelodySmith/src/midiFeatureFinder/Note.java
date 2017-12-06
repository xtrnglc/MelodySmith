package midiFeatureFinder;

public class Note {
	
    public static final String[] NOTES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    public static final String[] SCALEDEGREE = {"C", "D", "E", "F", "G",  "A", "B"};

    // -1 is a rest
    public int key;
	public int note;
	public int octave;
	public int velocity;
	public int scaleDegree;
	public int channel;
	public float bpm = 120;
	
	public String noteDuration;
	
	public long tickDuration;
	public long startTick;
	
	public String noteName;
	public String song;
	public String instrument;
    public String keySignature = "C major";
    public String timeSignature = "4/4";
	

    /**
     * A Note with no key is a rest
     * @param channel
     * @param startTick
     * @param song
     * @param instrument
     * @param keySignature
     * @param timeSignature
     * @param bpm
     */
    public Note(int channel, long startTick, 
			String song, String instrument, String keySignature, 
			String timeSignature, float bpm) {
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
		scaleDegree = setScaleDegree();
    }
    
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
	
	public void turnOff(long endTick, int ticksPerQuarterNote) {
		tickDuration = endTick - startTick;
		
		double ticksPerQuarterNoteDouble = (double) ticksPerQuarterNote;
		
		if (ticksPerQuarterNote == 0) {
			noteDuration = "N/A";
		}
		else if ((tickDuration / (long) (ticksPerQuarterNoteDouble * 4)) >= .9) {
			noteDuration = (int) (Math.round((double)tickDuration / ticksPerQuarterNoteDouble)) + "";
		}
		else if ((tickDuration / ticksPerQuarterNoteDouble) < 1.5 && (tickDuration / ticksPerQuarterNoteDouble) >= .75) {
			noteDuration = "1/4";
		}
		else if ((tickDuration / ticksPerQuarterNoteDouble) < 2.5 && (tickDuration / ticksPerQuarterNoteDouble) >= 1.5) {
			noteDuration = "1/2";
		}
		else if ((tickDuration / ticksPerQuarterNoteDouble) < .75 && (tickDuration / ticksPerQuarterNoteDouble) >= .375) {
			noteDuration = "1/8";
		}
		else if ((tickDuration / ticksPerQuarterNoteDouble) < .375 && (tickDuration / ticksPerQuarterNoteDouble) >= .1875) {
			noteDuration = "1/16";
		}
		else if ((tickDuration / ticksPerQuarterNoteDouble) < .1875) {
			noteDuration = "1/32";
		}
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
