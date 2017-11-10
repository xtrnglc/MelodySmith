package midiFeatureFinder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;

import javax.sound.midi.InvalidMidiDataException;
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
	
	Sequence currentSequence;
	private PrintWriter writer;
	
	public void readSequence(File midiFile, File outputFile) {
		try {
			Hashtable<Integer,Note> currentNotes = new Hashtable<Integer, Note>();
			
			FileWriter fw = new FileWriter(outputFile);
			writer = new PrintWriter(fw);
			int trackNum = 1;
			currentSequence = MidiSystem.getSequence(midiFile);
			for (Track track : currentSequence.getTracks()) {
				writer.println("Track Number: " +  trackNum + " size: " + track.size() );
				for (int i = 0; i < track.size(); i++) {
					MidiEvent event = track.get(i);
					long currentTick = event.getTick();
					
					
					writer.print(currentTick + ", ");
					MidiMessage message = event.getMessage();
					
					if (message instanceof ShortMessage) {
						ShortMessage sMessage = (ShortMessage) message;
						int key = sMessage.getData1();
						int velocity = sMessage.getData2();
						int note = key % 12;
						int octave = (key / 12) - 1;
						
						if (sMessage.getCommand() == NOTE_ON) {
							currentNotes.put(key, new Note(key, velocity, currentTick));
							writer.println("Start Press: " + NOTES[note] + ", Octave: " + octave + ", Velocity: " + velocity);
						}
						else if  (sMessage.getCommand() == NOTE_OFF){
							Note n = currentNotes.remove(key);
							
							// This may skip some notes
							if (n != null) {
								n.turnOff(currentTick);
						
								writer.println("End Press: " + NOTES[note] + ", Octave: " + octave + ", Velocity: " + velocity + " Duration: " + n.duration + " ticks");
							}
						}
						else {
							writer.println("Command: " + sMessage.getCommand());
						}
					}
					else {
						writer.println("Other message: " + message.getClass());
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
	
	public void readSequenceRaw(File midiFile, File outputFile) {
		try {
			FileWriter fw = new FileWriter(outputFile);
			writer = new PrintWriter(fw);
			int trackNum = 1;
			currentSequence = MidiSystem.getSequence(midiFile);
			for (Track track : currentSequence.getTracks()) {
				writer.println("Track Number: " +  trackNum + " size: " + track.size() );
				for (int i = 0; i < track.size(); i++) {
					MidiEvent event = track.get(i);
					writer.print(event.getTick() + ", ");
					MidiMessage message = event.getMessage();
					
					if (message instanceof ShortMessage) {
						processShortMessage((ShortMessage) message);
					}
					else {
						writer.println("Other message: " + message.getClass());
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
		writer.print("Channel " + sMessage.getChannel() + ", ");
		
		if (sMessage.getCommand() == NOTE_ON) {
			processNote(true, sMessage.getData1(), sMessage.getData2());
		}
		else if  (sMessage.getCommand() == NOTE_OFF){
			processNote(false, sMessage.getData1(), sMessage.getData2());
		}
		else {
			writer.println("Command: " + sMessage.getCommand());
		}
	}
	
	private void processNote(boolean on, int key, int velocity) {
		if (on) {
			writer.print("On, ");
		}
		else {
			writer.print("Off, ");
		}
		
		int note = key % 12;
		int octave = (key / 12) - 1;
		
		writer.println("Key: " + key + ", Note: " + NOTES[note] + ", Octave: " + octave + ", Velocity: " + velocity);
	}
}
