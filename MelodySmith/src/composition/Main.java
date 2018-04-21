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
		double intervalWeight = Double.parseDouble(args[3])/100;
		double durationWeight = Double.parseDouble(args[4])/100;
		String restType = args[5];
		int restAmount = 20 - Integer.parseInt(args[6]);
		int syncopation = Integer.parseInt(args[7]);
		int phraseLength = Integer.parseInt(args[8]);
		int creativity = Integer.parseInt(args[9]);
		int speed = 20-Integer.parseInt(args[10]);
		boolean extractPhrase = false;
		String algorithm = args[11];

		
		HashMap<String, Double> artistWeights = initializeArtists(args, 12);
		
		if(algorithm.toUpperCase().equals("EXTRACT"))
			extractPhrase = true;
		
		Composer composer = new Composer(corpusFolder, keySig, intervalWeight, durationWeight, restType, restAmount, syncopation, phraseLength, creativity, speed, extractPhrase, artistWeights);
		
		if(algorithm.toUpperCase().equals("PHRASED"))
			composer.composeNBars(outputFileName, 16);
		else if(algorithm.toUpperCase().equals("EXTRACT"))
			composer.composeWithPhrases(outputFileName, 50);
		else
			composer.composeMelody(outputFileName, 500);
		
		}catch(Exception e) {
			System.out.println("Command arguments are improperly formatted.");
			System.out.println("Your arguments:");
			for(String arg : args)
				System.out.println(arg);
			
			e.printStackTrace();
			System.out.println(e.getLocalizedMessage());
			System.out.println(e.getCause());
			System.out.println(e.getClass());
		}
	}
	
	/**
	 * This is a good method for testing the composition logic. Just choose a corpus, and construct a corpus
	 */
	public static void composeLocalCorpus() {
		boolean extractPhrases = false;
		HashMap<String, Double> artistWeights = new HashMap<String, Double>();
		artistWeights.put("bach", 1.0);
		artistWeights.put("beatles", 92.0);
		artistWeights.put("blackburn", 1.0);
		artistWeights.put("beiber", 1.5);
		artistWeights.put("bass", 7.0);
		artistWeights.put("lead", 2.0);
		artistWeights.put("simpleMelodies", 5.0);
		artistWeights.put("simple", 1.0);
		artistWeights.put("folkSongs", 2.0);
		artistWeights.put("fourBarLoops", 1.0);
		artistWeights.put("folkMelodies", 1.0);
		artistWeights.put("boys", 2.0);
		
		Composer composer = new Composer("corpus5", "cMajor", 0.1, 0.1, "CONSTRUCTIVE", 0, 10, 10, 20, 100, extractPhrases, artistWeights);
		
		if(extractPhrases)
			composer.composeWithPhrases("phrased.mid", 50);
		else
			composer.composeNBars("eightBars.mid", 8);
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
