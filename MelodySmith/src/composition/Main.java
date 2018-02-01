package composition;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import midiFeatureFinder.MidiWriter;

public class Main {
	static int[] CMAJOR = {60, 62, 64, 65, 67, 69, 71, 72};
	static int[] AMINOR = {57, 59, 60, 62, 64, 65, 67, 69};
	
	public static void main(String[] args) {
		String corpusFolder = args[0];
		String keySig = args[1];
		HashMap<String, Double> artistWeights = new HashMap<String, Double>();
		for(int i = 2; i < args.length; i++) {
			String[] artistKey = args[i].split(":");
			artistWeights.put(artistKey[0], Double.parseDouble(artistKey[1]));
		}
		Composer composer = new Composer(corpusFolder, keySig, 5, 3, 1.5, 0.5, artistWeights);
		composer.composeMelody("output.mid", 500);
		System.out.println();
	}

}
