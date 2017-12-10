package midiFeatureFinder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class MidiReader {

	// Setup final variables
	public static final int NOTE_ON = 0x90;
	public static final int NOTE_OFF = 0x80;
	public static final String[] NOTES = { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };
	public static final String[] KEYS = { "Cb", "Gb", "Db", "Ab", "Eb", "Bb", "F", "C", "G", "D", "A", "E", "B", "F#",
			"C#" };
	public static final float oneMinuteInMicroseconds = 60000000;
	public static final int maxChannels = 16;

	//
	public ArrayList<LinkedHashMap<Long, ArrayList<Note>>> unorderedNotes;

	// Need to record current time and interval of song in order to insert
	// information into notes.
	Sequence currentSequence;
	private String currentInstrument;
	private String currentKey = "C";
	private String currentTime = "4/4";
	private float currentBPM = 120;
	private String trackName;
	public boolean isPPQ = false;
	private int ticksPerQuarterNote = 0;

	/**
	 * Returns the ordered form of the current unordered notes
	 * 
	 * @return Ordered 2d array of the type [channel[note]]
	 */
	public ArrayList<ArrayList<Note>> getOrderedNotes() {

		ArrayList<ArrayList<Note>> r = new ArrayList<ArrayList<Note>>();
		for (LinkedHashMap<Long, ArrayList<Note>> channel : unorderedNotes) {
			ArrayList<Note> temp = new ArrayList<Note>();
			r.add(temp);

			// Because notesByTick is a LinkedHashMap it retains the correct
			// ordering from when the note arrays were added
			for (Map.Entry<Long, ArrayList<Note>> notesByTick : channel.entrySet()) {
				ArrayList<Note> notes = notesByTick.getValue();

				for (Note note : notes) {
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
	public ArrayList<Note> getAllConcurrentNotes(Note note, int channel) {
		return unorderedNotes.get(channel).get(note.startTick);
	}

	/**
	 * Returns all notes concurrent to note across all channels
	 * 
	 * @param note
	 * @return List containing all notes concurrent to note
	 */
	public ArrayList<Note> getAllConcurrentNotesAllChannels(Note note) {
		ArrayList<Note> ret = new ArrayList<Note>();

		for (LinkedHashMap<Long, ArrayList<Note>> channel : unorderedNotes) {
			ret.addAll(channel.get(note.startTick));
		}
		return ret;
	}

	/**
	 * Reads in a sequence from midiFile and processes it tick by tick, storing
	 * the result as unordered notes. Unordered notes is of the form Channel
	 * (ArrayList index) -> Tick (key) -> All notes played on tick
	 * 
	 * @param midiFile
	 */
	public void readSequence(File midiFile) {
		try {
			// CurrentNotes is used to keep track of all ongoing notes. This is
			// of the form channel -> active notes
			Hashtable<Integer, ArrayList<Note>> currentNotes = new Hashtable<Integer, ArrayList<Note>>();
			unorderedNotes = new ArrayList<LinkedHashMap<Long, ArrayList<Note>>>();

			// Rest Logic commented out for now because it doesn't work well
			// with the association network
			// long[] lastTickOfChannel = new long[maxChannels];

			for (int i = 0; i < maxChannels; i++) {
				unorderedNotes.add(new LinkedHashMap<Long, ArrayList<Note>>());
				currentNotes.put(i, new ArrayList<Note>());
			}

			currentSequence = MidiSystem.getSequence(midiFile);

			long currentTick = -1;

			// Currently the logic will only handle MIDI with PPQ division type
			// This should not be a problem for the prototype as the SMPTE is
			// for movie sounds
			if (currentSequence.getDivisionType() == Sequence.PPQ) {
				isPPQ = true;
				ticksPerQuarterNote = currentSequence.getResolution();
			}

			for (Track track : currentSequence.getTracks()) {
				for (int i = 0; i < track.size(); i++) {
					MidiEvent event = track.get(i);

					MidiMessage message = event.getMessage();
					if (message instanceof ShortMessage) {
						ShortMessage sMessage = (ShortMessage) message;

						LinkedHashMap<Long, ArrayList<Note>> notes = unorderedNotes.get(sMessage.getChannel());

						int key = sMessage.getData1();
						int velocity = sMessage.getData2();

						if (sMessage.getCommand() == NOTE_ON && velocity != 0) {
							// Only record half note rests or lower
							// Rest Logic
							// if (event.getTick() -
							// lastTickOfChannel[sMessage.getChannel()] <
							// (ticksPerQuarterNote * 2)) {
							// Note rest = new Note(sMessage.getChannel(),
							// lastTickOfChannel[sMessage.getChannel()],
							// trackName, currentInstrument, currentKey,
							// currentTime, currentBPM);
							// rest.turnOff(event.getTick(),
							// ticksPerQuarterNote);
							//
							// if
							// (notes.get(lastTickOfChannel[sMessage.getChannel()])
							// == null) {
							// notes.put(lastTickOfChannel[sMessage.getChannel()],
							// new ArrayList<Note>());
							// }
							//
							// //notes.get(lastTickOfChannel[sMessage.getChannel()]).add(rest);
							//
							// //lastTickOfChannel[sMessage.getChannel()] = 0;
							// }

							// On a note_on event We add the same note object n
							// to both currentNotes and unordered notes.
							Note n = new Note(key, velocity, sMessage.getChannel(), event.getTick(), trackName,
									currentInstrument, currentKey, currentTime, currentBPM);
							currentNotes.get(sMessage.getChannel()).add(n);

							if (notes.get(event.getTick()) == null) {
								currentTick = event.getTick();
								notes.put(currentTick, new ArrayList<Note>());
							}

							notes.get(currentTick).add(n);
						} else if (sMessage.getCommand() == NOTE_OFF || velocity == 0) {
							ArrayList<Note> channelNotes = currentNotes.get(sMessage.getChannel());
							Note n = null;

							// Find the note in the channel in current notes and
							// remove and store it
							for (int k = 0; k < channelNotes.size(); k++) {
								if (channelNotes.get(k).key == key) {
									n = channelNotes.remove(k);

									// Rest Logic
									// if (channelNotes.size() == 0) {
									// lastTickOfChannel[sMessage.getChannel()]
									// = event.getTick();
									// }

									break;
								}
							}

							// This may skip some notes, but needed to prevent
							// NullPointerExeptions if we are
							// given a note_off event for a note that has never
							// been turned on
							if (n != null) {
								// Because we inserted the same object into
								// unorderedNotes and currentNotes
								// we can now turn off the correct note without
								// having to find it in unordered notes
								n.turnOff(event.getTick(), ticksPerQuarterNote);
							}
						} else {
							// writer.println("Command: " +
							// sMessage.getCommand());
						}
					} else if (message instanceof MetaMessage) {
						processMetaMessage((MetaMessage) message);
					}
				}
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
				System.out.println("Key Signature: " + KEYS[key + 7] + " minor");
				currentKey = KEYS[key + 7] + " minor";
			} else {
				System.out.println("Key Signature: " + KEYS[key + 7] + " major");
				currentKey = KEYS[key + 7] + " major";
			}
		}

		// Time Signature
		else if (message.getType() == 0x58) {
			int num = temp[0];
			int den = (int) Math.pow(2, temp[1]);
			int met = temp[2] / (24 / (temp[1] - 1));
			System.out.println(
					"Time Signature: " + num + "/" + den + " Metronome: Every " + met + " 1/" + den + " notes");

			currentTime = num + "/" + den;
		}

		// Tempo
		else if (message.getType() == 0x51) {
			int t = ((temp[0] & 0xff) << 16) | ((temp[1] & 0xff) << 8) | (temp[2] & 0xff);
			System.out.println(
					"Tempo: " + (oneMinuteInMicroseconds / t) * (Integer.parseInt(currentTime.split("/")[1]) / 4.0f));

			currentBPM = (oneMinuteInMicroseconds / t) * (Integer.parseInt(currentTime.split("/")[1]) / 4.0f);
		}

		// End of Track
		else if (message.getType() == 0x2F) {
			System.out.println("End of Track");
		}

		// Instrument
		else if (message.getType() == 0x04) {

			System.out.println("Instrument: " + Arrays.toString(message.getData()));

			currentInstrument = Arrays.toString(message.getData());
		}

		// Track Name
		else if (message.getType() == 0x03) {
			System.out.println("Track Name: " + Arrays.toString(message.getData()));

			trackName = Arrays.toString(message.getData());
		} else {
			System.out.println("Meta Message: " + message.getType());
		}
	}
}
