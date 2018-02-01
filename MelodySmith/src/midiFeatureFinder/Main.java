package midiFeatureFinder;

import java.io.File;
import java.util.ArrayList;

import composition.*;

public class Main {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CorpusAnalyzer c = new CorpusAnalyzer();
		MidiReader r = new MidiReader(c);
		r.readSequenceRaw(new File("midiReaderData/beatles/Lead2.mid"));
		ArrayList<ArrayList<Node>> test = r.readSequence(new File("midiReaderData/beatles/Lead2.mid"), 5);
		System.out.println();
	}
}
