package midiFeatureFinder;

import java.io.File;
import java.util.ArrayList;

import composition.*;

public class Main {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CorpusAnalyzer c = new CorpusAnalyzer();
		MidiReader r = new MidiReader(c);
		r.readSequenceRaw(new File("IWantItThatWay.mid"));
		ArrayList<ArrayList<Node>> test = r.readSequence(new File("IWantItThatWay.mid"), 5, true, 8);
		ArrayList<ArrayList<Phrase>> temp = r.stitchPhraseByRests(test, "1/4");
		ArrayList<ArrayList<Phrase>> temp1 = r.stitchPhraseByTonic(test);

		
		System.out.print(r.readSequenceForABCJS(new File("output(2).mid"), 5, true, 8));
		
		System.out.println();
	}
}
