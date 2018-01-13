package midiFeatureFinder;

import java.io.File;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MidiReader r = new MidiReader();
		r.readSequenceRaw(new File("midiReaderData/lead/lead_10.mid"));
		r.readSequence(new File("midiReaderData/lead/lead_10.mid"), 5);
		ArrayList<ArrayList<Note>> test = r.getOrderedNotes();
		System.out.println();
	}
}
