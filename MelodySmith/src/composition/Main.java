package composition;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import midiFeatureFinder.MidiWriter;

public class Main {
	static int[] CMAJOR = {60, 62, 64, 65, 67, 69, 71, 72};
	static int[] AMINOR = {57, 59, 60, 62, 64, 65, 67, 69};
	
	public static void main(String[] args) {
		HashMap<String, Double> artistWeights = new HashMap<String, Double>();
		artistWeights.put("Bass", 1.0);
		artistWeights.put("Lead", 1.0);
		Composer composer = new Composer("Midi_Input/Beatles_Rubber_Soul", "CMAJOR", 5, 3, 1.5, 0.5, artistWeights);
		composer.composeMelody("refactored.mid", 500);
		System.out.println();
	}

}
