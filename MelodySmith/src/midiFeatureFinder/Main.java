package midiFeatureFinder;

import java.io.File;
import java.util.ArrayList;

import composition.*;

public class Main {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CorpusAnalyzer c = new CorpusAnalyzer();
		MidiReader r = new MidiReader(c);
		r.readSequenceRaw(new File("simple/1.mid"));
		ArrayList<ArrayList<Node>> test = r.readSequence(new File("simple/1.mid"), 5, true, 8);
		ArrayList<ArrayList<Phrase>> temp = r.stitchPhraseByRests(test, "1/4");
		ArrayList<ArrayList<Phrase>> temp1 = r.stitchPhraseByTonic(test);
		ArrayList<ArrayList<Phrase>> temp2 = r.stitchPhraseByBar(test, .5);
		
		System.out.println();
	}
}
