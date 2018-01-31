package composition;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import jvst.*;

import midiFeatureFinder.MidiWriter;

public class Composer {
	static int[] CMAJOR = {60, 62, 64, 65, 67, 69, 71, 72};
	static int[] AMINOR = {57, 59, 60, 62, 64, 65, 67, 69};
	static ArtistGrouping[] artists;
	
	AssociationNetwork network;
	String keySignature;
	ArrayList<Song> corpus;
	int nGramLength;
	int choicesToCompare;
	CorpusAnalyzer analyzer;
	double intervalWeight;
	double durationWeight;
	
	public Composer(String folderName) {
		network = new AssociationNetwork();
		trainNetwork(folderName, network);
		artists = null;
	}
	
	public Composer(String corpusFolderName, String keySig, int nGramLength, int choicesToCompare, double intervalWeight, double durationWeight, HashMap<String, Double> artistWeightings) {
		network = new AssociationNetwork();
		network.intervalContribution = intervalWeight;
		network.durationContribution = durationWeight;
		this.intervalWeight = intervalWeight;
		this.durationWeight = durationWeight;
		this.choicesToCompare = choicesToCompare;
		this.nGramLength = nGramLength;
		keySignature = keySig;
		corpus = new ArrayList();
	}
	
	public Composer(ArtistGrouping[] artists) {
		this.artists = artists;
		network = new AssociationNetwork();
		trainNetwork(artists, network);
	}
	
	public void composeMelody(String outputFilename, int lengthInNotes) {
		Node startNode = network.getTonic();
		MidiWriter mw = new MidiWriter();
		int[] scale = CMAJOR;
		
		int midiOffset = scale[0]; // This ensures that the notes are being played relative to the correct tonic
		
		mw.noteOnOffNow(8, startNode.scaleDegree+midiOffset, decodeDuration(startNode.noteDuration));
		
		ArrayList<Node> alreadyChosen = new ArrayList<Node>();	// I maintain a list of previous choices to avoid repetition
		Node currentNode = startNode;
		int distanceToEndOfBar = 63;
		int distanceFromRest = 0;

		for(int i = 0; i < lengthInNotes; i++) {
			alreadyChosen.add(currentNode);
			if(alreadyChosen.size() > network.size/2)
				alreadyChosen.remove(0);
			
			ArrayList<Node> choices = network.deduceBestNextNodes(currentNode, choicesToCompare);
			ArrayList<Node> nGram = getMostRecentNGram(alreadyChosen, nGramLength);
			
			Node nextNode = getBestSelection(distanceToEndOfBar, distanceFromRest, nGram, choices);
			
			int duration = decodeDuration(nextNode.noteDuration);
			int midiKey = getTransposedScaleDegree(nextNode.key, scale);
			int velocity = smoothVelocity(nextNode.velocity);
			
			mw.noteOnOffNow(duration, midiKey, velocity);
			
			distanceFromRest++;
			distanceToEndOfBar = (distanceToEndOfBar - duration) % 64;
		}
		
		writeMidi(mw, outputFilename);
	}
	
	private ArrayList<Node> getMostRecentNGram(ArrayList<Node> melodySoFar, int n){
		ArrayList<Node> nGram = new ArrayList();
		for(int i = 0; i < n; i++) {
			nGram.add(melodySoFar.get(melodySoFar.size()-i-1));
		}
		return nGram;
	}
	
	private void writeMidi(MidiWriter mw, String fileName) {
		try {
			mw.writeToFile(fileName);
		} catch(Exception e) {
			System.out.println("Error writing to output file: " + fileName + "\nError Message: " + e.getMessage());
		}
	}
	
	private Node getBestSelection(int distanceToEndOfBar, int distanceFromRest, ArrayList<Node> nGram, ArrayList<Node> choices) {
		double bestWeight = 0.0;
		Node bestNode = choices.get(0);
		for(Node choice : choices) {
			double weight = 0.0;
			for(String intervalNGram : getIntervalNGramStrings(nGram, choice)) {
				weight += (intervalWeight * analyzer.getScaleDegreeNGramProbability(intervalNGram));
			}
			
			for(String durationNGram : getDurationNGramStrings(nGram, choice)) {
				weight += (durationWeight * analyzer.getDurationNGramProbability(durationNGram));
			}
			
			// If distance from rest == avg distance from rest, weight++		
			
			int duration = decodeDuration(choice.noteDuration);
			if(distanceToEndOfBar - duration == 0) {
				weight += 1;
			}
			
			if(weight > bestWeight) {
				bestWeight = weight;
				bestNode = choice;
			}
		}
		return bestNode;
	}
	
	public ArrayList<String> getIntervalNGramStrings(ArrayList<Node> nGram, Node currentNode){
		ArrayList<String> nGrams = new ArrayList();
		for(int i = nGram.size()-1; i >= 0; i--) {
			
		}
		
		return nGrams;
	}
	
	public ArrayList<String> getDurationNGramStrings(ArrayList<Node> nGram, Node currentNode){
		ArrayList<String> nGrams = new ArrayList();
		for(int i = nGram.size()-1; i >= 0; i--) {
			
		}
		
		return nGrams;
	}
	
	
	public void composeMelody(String outputFilename, int lengthInNotes, boolean major) {
		Node startNode = network.getTonic();
		MidiWriter mw = new MidiWriter();
		
		int[] scale;
		if(major)
			scale = CMAJOR;
		else
			scale = AMINOR;
		
		int midiOffset = scale[0]; // This ensures that the notes are being played relative to the correct tonic
		
		mw.noteOnOffNow(8, startNode.scaleDegree+midiOffset, 120);
		
		ArrayList<Node> alreadyChosen = new ArrayList<Node>();	// I maintain a list of previous choices to avoid repetition
		Node currentNode = startNode;
		int restDuration = 0;
		boolean lastWasRest = false;
		for(int i = 0; i < lengthInNotes; i++) {
			alreadyChosen.add(currentNode);
			if(alreadyChosen.size() > network.size/2)
				alreadyChosen.remove(0);
			
			if(currentNode.linkedNodes.size() > 0) {
				Node nextNode = network.deduceNextNode(currentNode, alreadyChosen);
				
				if(nextNode.scaleDegree == -1) {
					restDuration += decodeDuration(nextNode.noteDuration);
					lastWasRest = true;
					currentNode = nextNode;
					continue;
				}
			
				int duration = decodeDuration(nextNode.noteDuration);
				int midiKey = getTransposedScaleDegree(nextNode.scaleDegree+midiOffset, scale);
				int velocity = smoothVelocity(nextNode.velocity);
				
				if(lastWasRest) {
					lastWasRest = false;
					mw.noteOn(restDuration, midiKey, velocity);
					mw.noteOff(duration, midiKey);
					restDuration = 0;
				}
				else
					mw.noteOnOffNow(duration, midiKey, velocity);
				
				
				currentNode = nextNode;
			}
			else {
				currentNode = network.getTonic(3);
			}
		}
		
		currentNode = network.getTonic();
		mw.noteOnOffNow(16, startNode.scaleDegree+midiOffset, 120);
		
		try {
			mw.writeToFile(outputFilename);
		}catch(Exception e) {
			System.out.println("Error writing to output file: " + outputFilename + "\nError Message: " + e.getMessage());
		}
			
	}
	
	private void trainNetwork(ArtistGrouping[] artists, AssociationNetwork network) {
		for(ArtistGrouping artist : artists) {
			for(File file : artist.artistMIDIFiles) {
				if(file.isFile())
					corpus.add(new Song(file, file.getName(), artist.artistName));
			}
		}
		network.calculateNetworkStatistics();
		network.addAllNotes();	
	}
	
	private void trainNetwork(String corpusFolder, AssociationNetwork network) {
		File corp = new File(corpusFolder);
		File[] artistFolders = corp.listFiles();
		
		for(File folder : artistFolders) {
			for(File midiFile : folder.listFiles())
				corpus.add(new Song(midiFile, midiFile.getName(), folder.getName()));
		}
		
		network.calculateNetworkStatistics();
		network.addAllNotes();	
	}
	
	static int decodeDuration(String duration) {
		if(duration == null)
			return 8;
		switch(duration) {
		case "1/4":return 16;
		case "1/8":return 8;
		case "1/16":return 4;
		//case "1/32":return 2;
		default:return 8;
		}
	}
	
	static int getTransposedScaleDegree(int octavePosition, int[] scale) {
		if(octavePosition < scale[0])
			return -1;
		
		for(int i : scale) {
			if(octavePosition == i || octavePosition < i)
				return i;
		}
		return scale[0];
	}
	
	static int smoothVelocity(int velocity) {
		if(velocity > 70)
			return velocity;
		else
			return 75;
	}
	
	public static double getValueFarthestFromAverage(double average, ArrayList<Integer> list) {
		double maxValue = average;
		for(int i : list) {
			if(Math.abs(average-i) > Math.abs(average-maxValue))
				maxValue = i;
		}
		return maxValue;
	}
	
	public static double getAverage(ArrayList<Integer> list) {
		double total = 0;
		for(int i : list)
			total += i;
		return total/list.size();
	}
	
	/**
	 * Returns the distance in notes from the previously played tonic, or -1 if none exists
	 */
	public static int getDistanceFromPreviousTonic(int index, ArrayList<Integer> scaleDegrees) {
		int i = index, distance = 0;
		while(i > 0) {
			if(scaleDegrees.get(i) == 0)
				return distance;
			i--; distance++;
		}
		return -1;
	}
	
	/**
	 * Returns the distance in notes from the next played tonic, or -1 if none exists
	 */
	public static int getDistanceToNextTonic(int index, ArrayList<Integer> scaleDegrees) {
		int i = index, distance = 0;
		while(i < scaleDegrees.size()) {
			if(scaleDegrees.get(i) == 0)
				return distance;
			i++; distance++;
		}
		return -1;
	}
	
	public static double getArtistWeight(String artist) {
		if(artists != null) {
			for(ArtistGrouping artistGrouping : artists) {
				if(artistGrouping.artistName == artist)
					return artistGrouping.artistInfluence;
			}
		}
		return 0.0;
	}
}
