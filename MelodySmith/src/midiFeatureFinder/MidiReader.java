package midiFeatureFinder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class MidiReader {
    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    public static final String[] NOTES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    public static final String[] KEYS = {"Cb", "Gb", "Db", "Ab", "Eb", "Bb", "F", "C", "G", "D", "A", "E", "B", "F#", "C#"};
    public static final float oneMinuteInMicroseconds = 60000000;
    
    public Hashtable<Integer, Hashtable<Long, ArrayList<Note>>> orderedNotes;
    
	Sequence currentSequence;
	private String currentInstrument;
	private String currentKey = "C";
	private String currentTime = "4/4";
	private float currentBPM = 120;
	private String trackName;
	
	public ArrayList<Note> getAllConcurrentNotes(Note note, int channel) {
		return orderedNotes.get(channel).get(note.startTick);
	}
	
	
	public ArrayList<Note> getAllConcurrentNotesAllChannels(Note note) {
		ArrayList<Note> ret = new ArrayList<Note>();
		
		for (Hashtable<Long, ArrayList<Note>> channel : orderedNotes.values()) {
			ret.addAll(channel.get(note.startTick));
		}
		return ret;
	}
	
	public void readSequence(File midiFile) {
		try {
			Hashtable<Integer,Note> currentNotes = new Hashtable<Integer, Note>();
			orderedNotes = new Hashtable<Integer, Hashtable<Long, ArrayList<Note>>>();
			
			currentSequence = MidiSystem.getSequence(midiFile);
			currentSequence.getDivisionType();
			long currentTick = 0;
			for (Track track : currentSequence.getTracks()) {
				for (int i = 0; i < track.size(); i++) {
					MidiEvent event = track.get(i);

					MidiMessage message = event.getMessage();
					if (message instanceof ShortMessage) {
						ShortMessage sMessage = (ShortMessage) message;
						
						if (!orderedNotes.containsKey(sMessage.getChannel())) {
							orderedNotes.put(sMessage.getChannel(), new Hashtable<Long, ArrayList<Note>>());
						}
						
						Hashtable<Long, ArrayList<Note>> notes = orderedNotes.get(sMessage.getChannel());
						
						int key = sMessage.getData1();
						int velocity = sMessage.getData2();
						
						if (sMessage.getCommand() == NOTE_ON && velocity != 0) {
							Note n = new Note(key, velocity, sMessage.getChannel(), currentTick, trackName, currentInstrument, currentKey, currentTime, currentBPM);
							currentNotes.put(key,n);
							
							if (currentTick != event.getTick()) {
								currentTick = event.getTick();
							    notes.put(currentTick, new ArrayList<Note>());
							}
							
							notes.get((int) currentTick).add(n);
							 
						}
						else if  (sMessage.getCommand() == NOTE_OFF || velocity == 0){
							Note n = currentNotes.remove(key);
							
							// This may skip some notes
							if (n != null) {
								n.turnOff(currentTick);
							}
						}
						else {
							//writer.println("Command: " + sMessage.getCommand());
						}
					}
					else {
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
	
	public void readSequenceRaw(File midiFile) {
		try {
//			FileWriter fw = new FileWriter(outputFile);
//			writer = new PrintWriter(fw);
			int trackNum = 1;
			currentSequence = MidiSystem.getSequence(midiFile);

			for (Track track : currentSequence.getTracks()) {
				System.out.println("Track Number: " +  trackNum + " size: " + track.size() );
				for (int i = 0; i < track.size(); i++) {
					MidiEvent event = track.get(i);
					System.out.print(event.getTick() + ", ");
					MidiMessage message = event.getMessage();
					
					if (message instanceof ShortMessage) {
						processShortMessage((ShortMessage) message);
					}
					else {
						//writer.println(processMetaMessage((MetaMessage) message, microToTickRatio));
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

	
	private void processShortMessage(ShortMessage sMessage) {
		System.out.print("Channel " + sMessage.getChannel() + ", ");
		
		if (sMessage.getCommand() == NOTE_ON) {
			processNote(true, sMessage.getData1(), sMessage.getData2());
		}
		else if  (sMessage.getCommand() == NOTE_OFF){
			processNote(false, sMessage.getData1(), sMessage.getData2());
		}
		else {
			System.out.println("Command: " + sMessage.getCommand());
		}
	}
	
	private void processNote(boolean on, int key, int velocity) {
		if (on) {
			System.out.print("On, ");
		}
		else {
			System.out.print("Off, ");
		}
		
		int note = key % 12;
		int octave = (key / 12) - 1;
		
		System.out.println("Key: " + key + ", Note: " + NOTES[note] + ", Octave: " + octave + ", Velocity: " + velocity);
	}
	
	private void processMetaMessage(MetaMessage message) {
		String r =  "Other message: " + message.getType();
		byte[] temp = message.getData();
		
		// MIDI Reference : http://www.somascape.org/midi/tech/mfile.html#meta
		if (message.getType() == 0x59) {
			int key = temp[0];
			if (temp[1] == 1) {
				System.out.println("Key Signature: " + KEYS[key + 7] + " minor");
				currentKey = KEYS[key + 7] + " minor";
			}
			else {
				System.out.println("Key Signature: " + KEYS[key + 7] + " major");
				currentKey = KEYS[key + 7] + " major";
			}
		}
		else if (message.getType() == 0x58) {
			int num = temp[0];
			int den = (int) Math.pow(2, temp[1]);
			int met = temp[2] / ( 24 / (temp[1] - 1));
			System.out.println("Time Signature: " + num + "/" + den +  " Metronome: Every " + met + " 1/" + den + " notes");
			
			currentTime =  num + "/" + den;
		}
		else if (message.getType() == 0x51) {
			int t = ((temp[0] & 0xff) << 16) | ((temp[1] & 0xff) << 8) | (temp[2] & 0xff);
			System.out.println("Tempo: " + (oneMinuteInMicroseconds / t) 
					* ( Integer.parseInt(currentTime.split("/")[1]) / 4.0f));
			
			currentBPM =  (oneMinuteInMicroseconds / t) 
					* (Integer.parseInt(currentTime.split("/")[1]) / 4.0f );
		}
		else if (message.getType() == 0x2F) {
			System.out.println("End of Track");
		}
		else if (message.getType() == 0x04) {
			
			System.out.println("Instrument: " + Arrays.toString(message.getData()));
			
			currentInstrument = Arrays.toString(message.getData());
		}
		else if (message.getType() == 0x03) {
			System.out.println("Track Name: " + Arrays.toString(message.getData()));

			trackName = Arrays.toString(message.getData());
		}
		else {
			System.out.println("Meta Message: " + message.getType());
		}
	}
}
