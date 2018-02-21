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
		String outputFileName = args[1];
		String keySig = args[2];
		double intervalWeight = Double.parseDouble(args[3])/10000;
		double durationWeight = Double.parseDouble(args[4])/1000;
		int nGramLength = Integer.parseInt(args[5]);
		int numberOfComparisons = Integer.parseInt(args[6]);
		
		HashMap<String, Double> artistWeights = initializeArtists(args, 7);
		
		Composer composer = new Composer(corpusFolder, keySig, nGramLength, numberOfComparisons, intervalWeight, durationWeight, artistWeights);
		composer.composeMelody(outputFileName, 500);
		
		}catch(Exception e) {
			System.out.println("Command arguments are improperly formatted\n\tCorrect format =\n\tcorpusPath outputPath keySignature intervalWeight durationWeight nGramLength numberOfComparisons artist1Name:artist1Weight");
			System.out.println("Your arguments:");
			for(String arg : args)
				System.out.println(arg);
			
			e.printStackTrace();
			System.out.println(e.getLocalizedMessage());
			System.out.println(e.getCause());
			System.out.println(e.getClass());
		}
	}
	
	public static void composeLocalCorpus() {
		HashMap<String, Double> artistWeights = new HashMap<String, Double>();
		//artistWeights.put("bach", 1.0);
		//artistWeights.put("beiber", 1.5);
		//artistWeights.put("bass", 1.0);
		//artistWeights.put("lead", 2.0);
		//artistWeights.put("simpleMelodies", 1.0);
		artistWeights.put("simple", 1.0);
		Composer composer = new Composer("corpus", "CMAJOR", 4, 3, 1, 1, artistWeights);
		
		composer.composeMelody("output.mid", 100);
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
