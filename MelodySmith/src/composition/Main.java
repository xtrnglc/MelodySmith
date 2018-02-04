package composition;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import midiFeatureFinder.MidiWriter;

public class Main {
	static int[] CMAJOR = {60, 62, 64, 65, 67, 69, 71, 72};
	static int[] AMINOR = {57, 59, 60, 62, 64, 65, 67, 69};
	
	public static void main(String[] args) {
		//composeLocalCorpus();
		
		try {
		String corpusFolder = args[0];
		String keySig = args[1];
		double intervalWeight = Double.parseDouble(args[2]);
		double durationWeight = Double.parseDouble(args[3]);
		int nGramLength = Integer.parseInt(args[4]);
		int numberOfComparisons = Integer.parseInt(args[5]);
		
		HashMap<String, Double> artistWeights = initializeArtists(args, 6);
		
		Composer composer = new Composer(corpusFolder, keySig, nGramLength, numberOfComparisons, intervalWeight, durationWeight, artistWeights);
		composer.composeMelody("output.mid", 500);
		
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getClass());
			System.out.println(e.getLocalizedMessage());
			System.out.println(e.getCause());
			System.out.println(e.getMessage());
			System.out.println("Command arguments improperly formatted\n\tCorrect format =\n\tcorpusPath keySignature intervalWeight durationWeight nGramLength numberOfComparisons artist1Name:artist1Weight artist2Name:artist2Weight");
			System.out.println("Your arguments:");
			for(String arg : args)
				System.out.println(arg + " ");
		}
	}
	
	public static void composeLocalCorpus() {
		HashMap<String, Double> artistWeights = new HashMap<String, Double>();
		artistWeights.put("beatles", 1.0);
		Composer composer = new Composer("corpus", "a", 2, 1, 0.0, 0.0, artistWeights);
		
		composer.composeMelody("output.mid", 500);
	}
	
	private static HashMap<String, Double> initializeArtists(String[] args, int currentPosition){
		HashMap<String, Double> artistWeights = new HashMap<String, Double>();
		for(int i = currentPosition; i < args.length; i++) {
			String[] artistKey = args[i].split(":");
			artistWeights.put(artistKey[0], Double.parseDouble(artistKey[1]));
		}
		return artistWeights;
	}

}
