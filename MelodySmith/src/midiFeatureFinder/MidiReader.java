package midiFeatureFinder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import composition.*;

public class MidiReader {

	// Setup final variables
	public static final int NOTE_ON = 0x90;
	public static final int NOTE_OFF = 0x80;
	public static final String[] NOTES = { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };
	public static final String[] KEYS = { "Cb", "Gb", "Db", "Ab", "Eb", "Bb", "F", "C", "G", "D", "A", "E", "B", "F#",
			"C#" };
	public static final float oneMinuteInMicroseconds = 60000000;
	public static final int maxChannels = 16;

	// A list of the unordered notes of the last song processed
	public ArrayList<LinkedHashMap<Long, ArrayList<Node>>> unorderedNotes;

	// Need to record current time and interval of song in order to insert
	// information into notes.
	Sequence currentSequence;
	private String currentInstrument;
	private String currentKey = "C";
	private String currentTime = "4/4";
	private float currentBPM = 120;
	private String trackName;
	private int ticksPerQuarterNote = 0;
	private int ticksPerBar = 0;
	private long[] lastTickOfChannel;
	public boolean isPPQ = false;	
	public CorpusAnalyzer analyzer;
	public ArrayList<Node> lastOutputComposition = new ArrayList<Node>();
	
	public ArrayList<Integer> lowestTonicOctave = new ArrayList<Integer>();

	
	public MidiReader(CorpusAnalyzer c) {
		analyzer = c;
	}

	/**
	 * Returns the ordered form of the current unordered notes
	 * 
	 * @return Ordered 2d array of the type [channel[note]]
	 */
	public ArrayList<ArrayList<Node>> getOrderedNotes() {

		ArrayList<ArrayList<Node>> r = new ArrayList<ArrayList<Node>>();
		for (LinkedHashMap<Long, ArrayList<Node>> channel : unorderedNotes) {
			ArrayList<Node> temp = new ArrayList<Node>();
			r.add(temp);

			// Because notesByTick is a LinkedHashMap it retains the correct
			// ordering from when the note arrays were added
			for (Map.Entry<Long, ArrayList<Node>> notesByTick : channel.entrySet()) {
				ArrayList<Node> notes = notesByTick.getValue();

				for (Node note : notes) {
					temp.add(note);
				}
			}
		}
		return r;
	}
	
	/**
	 * Returns the ordered form of the current unordered notes
	 * 
	 * @return Ordered 2d array of the type [channel[note]]
	 */
	public ArrayList<ArrayList<Node>> getOrderedNotesWithoutRests() {

		ArrayList<ArrayList<Node>> r = new ArrayList<ArrayList<Node>>();
		for (LinkedHashMap<Long, ArrayList<Node>> channel : unorderedNotes) {
			ArrayList<Node> temp = new ArrayList<Node>();
			r.add(temp);

			// Because notesByTick is a LinkedHashMap it retains the correct
			// ordering from when the note arrays were added
			for (Map.Entry<Long, ArrayList<Node>> notesByTick : channel.entrySet()) {
				ArrayList<Node> notes = notesByTick.getValue();

				for (Node note : notes) {
					if (note.key != -1)
						temp.add(note);
				}
			}
		}
		return r;
	}

	/**
	 * Returns all notes concurrent to note and in channel
	 * 
	 * @param note
	 * @param channel
	 * @return List containing all the notes started at the same tick as note
	 *         and in the channel
	 */
	public ArrayList<Node> getAllConcurrentNotes(Node note, int channel) {
		return unorderedNotes.get(channel).get(note.startTick);
	}

	/**
	 * Returns all notes concurrent to note across all channels
	 * 
	 * @param note
	 * @return List containing all notes concurrent to note
	 */
	public ArrayList<Node> getAllConcurrentNotesAllChannels(Node note) {
		ArrayList<Node> ret = new ArrayList<Node>();

		for (LinkedHashMap<Long, ArrayList<Node>> channel : unorderedNotes) {
			ret.addAll(channel.get(note.startTick));
		}
		return ret;
	}
	
	public Hashtable<Integer, ArrayList<Node>> getCurrentNotes() {
		// CurrentNotes is used to keep track of all ongoing notes. This is
		// of the form channel -> active notes
		unorderedNotes = new ArrayList<LinkedHashMap<Long, ArrayList<Node>>>();
		
		Hashtable<Integer, ArrayList<Node>> currentNotes = new Hashtable<Integer, ArrayList<Node>>();
		
		for (int i = 0; i < maxChannels; i++) {
			unorderedNotes.add(new LinkedHashMap<Long, ArrayList<Node>>());
			currentNotes.put(i, new ArrayList<Node>());
		}
		return currentNotes;
	}

	/**
	 * 	 * Reads in a sequence from midiFile and processes it tick by tick, storing
	 * the result as unordered notes. Unordered notes is of the form Channel
	 * (ArrayList index) -> Tick (key) -> All notes played on tick
	 * 
	 * @param midiFile
	 * @param markovLength
	 * @param withRests
	 * @return getOrderedNotes()
	 */
	public ArrayList<ArrayList<Node>> readSequence(File midiFile, int markovLength, boolean withRests, int scaleDegreeSize) {
		try {
			// Rest Logic
			lastTickOfChannel = new long[maxChannels];
			Hashtable<Integer, ArrayList<Node>> currentNotes = getCurrentNotes();
			
			
			// Reset the fields
			currentSequence = MidiSystem.getSequence(midiFile);
			
			currentInstrument = "";
			currentKey = "C";
			currentTime = "4/4";
			currentBPM = 120;
			trackName = "";
			ticksPerQuarterNote = 0;
			isPPQ = false;
			
			for (int i = 0; i < maxChannels; i++) {
				lowestTonicOctave.add(Integer.MAX_VALUE);
			}


			// Currently the logic will only handle MIDI with PPQ division type
			// This should not be a problem for the prototype as the SMPTE is
			// for movie sounds
			if (currentSequence.getDivisionType() == Sequence.PPQ) {
				isPPQ = true;
				ticksPerQuarterNote = currentSequence.getResolution();
				ticksPerBar = ticksPerQuarterNote * 4;
			}
			
			for (Track track : currentSequence.getTracks()) {
				for (int i = 0; i < track.size(); i++) {
					processEvent(track, currentNotes, i);
				}
			}
			
			ArrayList<ArrayList<Node>> orderedNotes = getOrderedNotes();

			for (int i = 0; i < orderedNotes.size(); i++) {
				for (int k = 0; k < orderedNotes.get(i).size(); k++) {
					orderedNotes.get(i).get(k).setScaleDegree(lowestTonicOctave.get(i), scaleDegreeSize);
				}
			}
			
			
			if (markovLength > 0) {
				recordIntervals(markovLength, withRests, scaleDegreeSize);
			}

		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("The Midi File is invalid");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Could not locate the MIDI file");
		}
		if (withRests)
			return getOrderedNotes();
		else
			return getOrderedNotesWithoutRests();
	}
	
	/**
	 * Stores interval scale degrees, note names, and durations into the analyzer (CorpusAnalyzer)
	 * currently ignores rests
	 * @param markovLength
	 */
	private void recordIntervals(int markovLength, boolean withRests, int scaleDegreeSize) {	
		ArrayList<ArrayList<Node>> orderedNotes;
		if (withRests) {
			orderedNotes = getOrderedNotes();
		}
		else {
			orderedNotes = getOrderedNotesWithoutRests();
		}
		
		for (ArrayList<Node> channel : orderedNotes) {
			for (int i = 0; i < channel.size() - markovLength; i++) {
				if (i < channel.size() - 1) {
					analyzer.addToIntervalCount(channel.get(i+1).key - channel.get(i).key);
				}
				
				String intervalScaleDegrees = channel.get(i).scaleDegree + "";
				String intervalDurations = channel.get(i).noteDuration;
				String intervalNoteNames = channel.get(i).noteName;

				for (int k = i+1; k < (i + markovLength); k++) {
					intervalScaleDegrees += "," + channel.get(k).scaleDegree;
					intervalDurations += "," + channel.get(k).noteDuration;	
					intervalNoteNames += "," + channel.get(k).noteName;
					
					analyzer.addToScaleDegreeNGramCount(intervalScaleDegrees);
					analyzer.addToDurationNGramCount(intervalDurations);
					analyzer.addToNoteNameNGramCount(intervalNoteNames);
				}
				
			}
			
			// Insert ending intervals into IntervalCount analysis hashmap
			if (markovLength < channel.size()) {
				for (int i = channel.size() - markovLength; i < channel.size(); i++) {
					if (i < channel.size() - 1) {
						analyzer.addToIntervalCount(channel.get(i+1).key - channel.get(i).key);
					}
				}
			}
		}
	}
	
	/**
	 * Helper method to process track information (a single event i)
	 * @param track
	 * @param currentNotes
	 * @param i
	 */
	private Hashtable<Integer, ArrayList<Node>> processEvent(Track track, Hashtable<Integer, ArrayList<Node>> currentNotes, int i) {
		MidiEvent event = track.get(i);
		long currentTick = -1;
		MidiMessage message = event.getMessage();
		if (message instanceof ShortMessage) {
			ShortMessage sMessage = (ShortMessage) message;

			LinkedHashMap<Long, ArrayList<Node>> notes = unorderedNotes.get(sMessage.getChannel());

			int key = sMessage.getData1();
			int velocity = sMessage.getData2();

			if (sMessage.getCommand() == NOTE_ON && velocity != 0) {
				// Rest Logic
				if (lastTickOfChannel[sMessage.getChannel()] == event.getTick()) {
					lastTickOfChannel[sMessage.getChannel()] = -1;
				}
				else if (lastTickOfChannel[sMessage.getChannel()] != -1 && lastTickOfChannel[sMessage.getChannel()] != event.getTick() ) {
					Node n = new Node(sMessage.getChannel(), lastTickOfChannel[sMessage.getChannel()], trackName,
							currentInstrument, currentKey, currentTime, currentBPM);
					n.turnOff(event.getTick(), ticksPerQuarterNote);
					
					if (notes.get(lastTickOfChannel[sMessage.getChannel()]) == null) {
						notes.put(lastTickOfChannel[sMessage.getChannel()], new ArrayList<Node>());
					}
					
					if (n.tickDuration >= 5) {
						notes.get(lastTickOfChannel[sMessage.getChannel()]).add(n);
					}
					lastTickOfChannel[sMessage.getChannel()] = -1;
				}
				
				Node n = new Node(key, velocity, sMessage.getChannel(), event.getTick(), trackName,
						currentInstrument, currentKey, currentTime, currentBPM);
				
				if (n.distanceFromTonic == 0 && n.octave < lowestTonicOctave.get(n.channel)) {
					lowestTonicOctave.set(n.channel, n.octave);
				}
				
				currentNotes.get(sMessage.getChannel()).add(n);
				
				if (notes.get(event.getTick()) == null) {
					currentTick = event.getTick();
					notes.put(currentTick, new ArrayList<Node>());
				}
				
				if (currentTick != -1)
					notes.get(currentTick).add(n);
				

				
			} else if (sMessage.getCommand() == NOTE_OFF || velocity == 0) {
				ArrayList<Node> channelNotes = currentNotes.get(sMessage.getChannel());
				Node n = null;

				// Find the note in the channel in current notes and
				// remove and store it
				for (int k = 0; k < channelNotes.size(); k++) {
					if (channelNotes.get(k).key == key) {
						n = channelNotes.remove(k);
						
						// Rest Logic
						if (channelNotes.size() == 0 && lastTickOfChannel[sMessage.getChannel()] == -1) {
							lastTickOfChannel[sMessage.getChannel()] = event.getTick();
						}

						break;
					}
				}

				if (n != null) {
					n.turnOff(event.getTick(), ticksPerQuarterNote);
				}
			} else {
				// writer.println("Command: " +
				// sMessage.getCommand());
			}
		} else if (message instanceof MetaMessage) {
			processMetaMessage((MetaMessage) message);
		}
		
		return currentNotes;
	}

	/**
	 * DO NOT CALL THIS, this is for personal debugging purposes only. Prints a
	 * readable form of the MIDI file for debugging
	 * 
	 * @param midiFile
	 */
	public void readSequenceRaw(File midiFile) {
		try {
			int trackNum = 1;
			currentSequence = MidiSystem.getSequence(midiFile);

			for (Track track : currentSequence.getTracks()) {
				System.out.println("Track Number: " + trackNum + " size: " + track.size());
				for (int i = 0; i < track.size(); i++) {
					MidiEvent event = track.get(i);
					System.out.print(event.getTick() + ", ");
					MidiMessage message = event.getMessage();

					if (message instanceof ShortMessage) {
						processShortMessage((ShortMessage) message);
					} else if (message instanceof MetaMessage) {
						// writer.println(processMetaMessage((MetaMessage)
						// message, microToTickRatio));
						processMetaMessage((MetaMessage) message);
					}
				}
				trackNum++;
			}

		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("The Midi File is invalid");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Could not locate the MIDI file");
		}
	}

	/**
	 * Helper method for processing short messages (note events), called for
	 * readSequenceRaw
	 * 
	 * @param sMessage
	 */
	private void processShortMessage(ShortMessage sMessage) {
		System.out.print("Channel " + sMessage.getChannel() + ", ");

		if (sMessage.getCommand() == NOTE_ON) {
			processNote(true, sMessage.getData1(), sMessage.getData2());
		} else if (sMessage.getCommand() == NOTE_OFF) {
			processNote(false, sMessage.getData1(), sMessage.getData2());
		} else {
			System.out.println("Command: " + sMessage.getCommand());
		}
	}

	/**
	 * Helper method for processing notes
	 * 
	 * @param sMessage
	 */
	private void processNote(boolean on, int key, int velocity) {
		if (on) {
			System.out.print("On, ");
		} else {
			System.out.print("Off, ");
		}

		int note = key % 12;
		int octave = (key / 12) - 1;

		System.out
				.println("Key: " + key + ", Note: " + NOTES[note] + ", Octave: " + octave + ", Velocity: " + velocity);
	}

	/**
	 * Helper method to process Midi Meta events
	 * 
	 * @param message
	 */
	private void processMetaMessage(MetaMessage message) {
		String r = "Other message: " + message.getType();
		byte[] temp = message.getData();

		// MIDI Reference : http://www.somascape.org/midi/tech/mfile.html#meta

		// Key Signature
		if (message.getType() == 0x59) {
			int key = temp[0];
			if (temp[1] == 1) {
//				System.out.println("Key Signature: " + KEYS[key + 7] + " minor");
				currentKey = KEYS[key + 7] + " minor";
			} else {
//				System.out.println("Key Signature: " + KEYS[key + 7] + " major");
				currentKey = KEYS[key + 7] + " major";
			}
		}

		// Time Signature
		else if (message.getType() == 0x58) {
			int num = temp[0];
			int den = (int) Math.pow(2, temp[1]);
			int met = temp[2] / (24 / (temp[1] - 1));
//			System.out.println(
//					"Time Signature: " + num + "/" + den + " Metronome: Every " + met + " 1/" + den + " notes");

			currentTime = num + "/" + den;
		}

		// Tempo
		else if (message.getType() == 0x51) {
			int t = ((temp[0] & 0xff) << 16) | ((temp[1] & 0xff) << 8) | (temp[2] & 0xff);
//			System.out.println(
//					"Tempo: " + (oneMinuteInMicroseconds / t) * (Integer.parseInt(currentTime.split("/")[1]) / 4.0f));

			currentBPM = (oneMinuteInMicroseconds / t) * (Integer.parseInt(currentTime.split("/")[1]) / 4.0f);
		}

		// End of Track
//		else if (message.getType() == 0x2F) {
//			System.out.println("End of Track");
//		}

		// Instrument
		else if (message.getType() == 0x04) {

			//System.out.println("Instrument: " + Arrays.toString(message.getData()));

			currentInstrument = Arrays.toString(message.getData());
		}

		// Track Name
		else if (message.getType() == 0x03) {
			//System.out.println("Track Name: " + Arrays.toString(message.getData()));

			trackName = Arrays.toString(message.getData());
		} 
//		else {
//			System.out.println("Meta Message: " + message.getType());
//		}
	}
	
	/**
	 * Processes the current sequence using heuristics to determine a confidence level for each channel that represents that chance that that channel contains the melody
	 * @return A predictive array of the form index = channel, value = an integer between 0 and 100 which represents the confidence that that channel is the melody channel
	 * where 0 is a channel that does not contain the melody and 100 is a channel that definitely contains the melody.
	 */
	public int[] predictMelodyChannel() {
		
		
		ArrayList<ArrayList<Node>> orderedNotes = getOrderedNotes();
		int[] predictions = new int[orderedNotes.size()];
		int longestRepeat = 0;
		int longestRepeatOverAllChannels = 0;
		int indexOfRepeat = 0;
		int currentRepeat = 0;
		int currentKey = -1;
		int restCount = 0;
		
		
		for (int i = 0; i < orderedNotes.size(); i++) {
			predictions[i] = 100;
		}
		
		for (int i = 0; i < orderedNotes.size(); i++) {
			
			currentRepeat = 0;
			longestRepeat = 0;
			restCount = 0;
			
			if (orderedNotes.get(i).size() == 0) {
				predictions[i] = 0;
			}
			else {
				// Counting repeats
				for (Node note : orderedNotes.get(i)) {
					if (note.key != -1) {
						if (note.key != currentKey) {
							currentKey = note.key;
							currentRepeat = 1;
						}
						else {
							currentRepeat++;
						}
						
						if (currentRepeat > longestRepeat) {
							longestRepeat = currentRepeat;
							if (longestRepeat > longestRepeatOverAllChannels) {
								indexOfRepeat = i;
								longestRepeatOverAllChannels = longestRepeat;
							}
						}
					}
					else {
						restCount++;
					}
				}
				
				if (longestRepeat >= 10) {
					predictions[i] = predictions[i] - 10;
				}
				
				if ((double) restCount/(double)orderedNotes.get(i).size() > .25) {
					predictions[i] = predictions[i] - 10;
				}
				
			}
			predictions[indexOfRepeat] = predictions[indexOfRepeat] - 10;
			
		}
		
		return predictions;
	}
	
	public ArrayList<ArrayList<Phrase>> stitchPhraseByRests(ArrayList<ArrayList<Node>> input, String minRestSize) {
		ArrayList<ArrayList<Phrase>> ret = new ArrayList<ArrayList<Phrase>>();
		
		for (ArrayList<Node> channel : input) {
			ret.add(new ArrayList<Phrase>());
			Phrase temp = new Phrase();
			for (Node node : channel) {
				temp.nodes.add(node);
				if (node.key == -1 && (!node.noteDuration.contains("/") || Integer.parseInt(node.noteDuration.split("/")[1]) <= Integer.parseInt(minRestSize.split("/")[1]))) {
					ret.get(ret.size()-1).add(temp);
					temp = new Phrase();
				}
			}
			if (temp.nodes.size() != 0)
				ret.get(ret.size()-1).add(temp);
		}
		
		return ret;
		 
		 
	}
	
	public ArrayList<ArrayList<Phrase>> stitchPhraseByTonic(ArrayList<ArrayList<Node>> input) {
		ArrayList<ArrayList<Phrase>> ret = new ArrayList<ArrayList<Phrase>>();
		
		for (ArrayList<Node> channel : input) {
			ret.add(new ArrayList<Phrase>());
			Phrase temp = new Phrase();
			for (int i = 0; i < channel.size(); i++) {
				Node node = channel.get(i);
				temp.nodes.add(node);
				if (node.scaleDegree == 0) {
					while (i+1 < channel.size() && channel.get(i+1).startTick == node.startTick) {
						temp.nodes.add(channel.get(i+1));
						i++;
					}
					ret.get(ret.size()-1).add(temp);
					temp = new Phrase();
				}
			}
			if (temp.nodes.size() != 0)
				ret.get(ret.size()-1).add(temp);
		}
		
		return ret;
		 
		 
	}
	
	public ArrayList<ArrayList<Phrase>> stitchPhraseByBar(ArrayList<ArrayList<Node>> input, double barSize) {
		ArrayList<ArrayList<Phrase>> ret = new ArrayList<ArrayList<Phrase>>();
		
		for (ArrayList<Node> channel : input) {
			ret.add(new ArrayList<Phrase>());
			Phrase temp = new Phrase();
			int bar = 1;
			for (int i = 0; i < channel.size(); i++) {
				Node node = channel.get(i);
				temp.nodes.add(node);
				if (node.startTick + node.tickDuration >= ticksPerBar * bar * barSize) {
					while (i+1 < channel.size() && node.startTick + node.tickDuration >= channel.get(i+1).startTick + channel.get(i+1).tickDuration) {
						temp.nodes.add(channel.get(i+1));
						i++;
					}
					bar = (int) ((node.startTick + node.tickDuration) / (ticksPerBar * barSize)) + 1;
					ret.get(ret.size()-1).add(temp);
					temp = new Phrase();
				}
			}
			if (temp.nodes.size() != 0)
				ret.get(ret.size()-1).add(temp);
		}
		
		return ret;
	}
	
	public ArrayList<Phrase> stitchOutputByBar() {
		ArrayList<Phrase> ret = new ArrayList<Phrase>();

		
		Phrase temp = new Phrase();
		int bar = 1;
		for (int i = 0; i < lastOutputComposition.size(); i++) {
			Node node = lastOutputComposition.get(i);
			temp.nodes.add(node);
			if (node.startTick + node.tickDuration >= ticksPerBar * bar) {
				while (i+1 < lastOutputComposition.size() && node.startTick + node.tickDuration >= lastOutputComposition.get(i+1).startTick + lastOutputComposition.get(i+1).tickDuration) {
					temp.nodes.add(lastOutputComposition.get(i+1));
					i++;
				}
				
				long total = ticksPerBar;
				for (Node n : temp.nodes) {
					if (total > n.tickDuration) 
					{
						total -= n.tickDuration;
					}
					else {
						n.turnOff(n.startTick + total, ticksPerBar / 4);
					}
				}
				
				bar = (int) ((node.startTick + node.tickDuration) / (ticksPerBar)) + 1;
				ret.add(temp);
				temp = new Phrase();
			}
		}
		if (temp.nodes.size() != 0)
			ret.add(temp);
		
		return ret;
		 
		 
	}
	
	// Need to add triplet logic and work on notes across bars
	public String compositionToABCJS() {
		StringBuilder ret = new StringBuilder();
		if (lastOutputComposition.size() != 0) {
			ArrayList<Phrase> temp = stitchOutputByBar();
			ret.append("X: " + temp.size() + '\n');
			ret.append("T: Composition\n");
			ret.append("M: 4/4\n");
			ret.append("L: 1/4\n");
			ret.append("Q: 1/4=120\n");
			ret.append("K: C\n");
			for (int i = 0; i < temp.size(); i++) {
				Phrase phrase = temp.get(i);
				
				ret.append("|");
				for (Node node : phrase.nodes) {
					if (node.noteName.contains("#")) {
						ret.append("^");
					}
					else if (node.noteName.contains("b")) {
						ret.append("_");
					}
					
					if (node.key != -1) 
					{
						ret.append(node.noteName.substring(0, 1));
						if (node.octave > 4) {
							for (int k = 0; k < node.octave - 4; k++) {
								ret.append("'");
							}
						}
						else if (node.octave < 4) {
							for (int k = 0; k < 4 - node.octave; k++) {
								ret.append(",");
							}
						}
					}
					else {
						ret.append(" z");
					}
					
					if (node.noteDuration.contains("32")) {
						ret.append("/8");
					}
					else if (node.noteDuration.contains("16")) {
						ret.append("/4");
					}
					else if (node.noteDuration.contains("8")) {
						ret.append("/2");
					}
					else if (node.noteDuration.contains("/4")) {
						ret.append("1");
					}
					else if (node.noteDuration.contains("/2")) {
						ret.append("2");
					}
					else {
						ret.append("4");
					}
				}
			}
			ret.append("|");
		}
		return ret.toString();
	}

	
//	public ArrayList<ArrayList<Phrase>> stitchPhraseByRythmicCadence(ArrayList<ArrayList<Node>> input) {
//		ArrayList<ArrayList<Phrase>> ret = new ArrayList<ArrayList<Phrase>>();
//		
//		for (ArrayList<Node> channel : input) {
//			ret.add(new ArrayList<Phrase>());
//			Phrase temp = new Phrase();
//			for (Node node : channel) {
//				temp.nodes.add(node);
//				if (node.key == -1 && (!node.noteDuration.contains("/") || Integer.parseInt(node.noteDuration.split("/")[1]) <= Integer.parseInt(minRestSize.split("/")[1]))) {
//					ret.get(ret.size()-1).add(temp);
//					temp = new Phrase();
//				}
//			}
//		}
//		
//		return ret;
//	}
	
}
