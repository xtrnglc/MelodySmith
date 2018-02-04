package composition;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.DEFAULT;

import midiFeatureFinder.MidiReader;
import midiFeatureFinder.MidiWriter;

public class Composer {
	static int[] CMAJOR = {60, 62, 64, 65, 67, 69, 71, 72};
	
	static int[] CGYPSY = {60, 62, 63, 66, 67, 68, 70, 72, 74, 75, 78, 79, 80, 82, 84};
	static int[] AMINOR = {57, 59, 60, 62, 64, 65, 67, 69, 71, 72, 74, 76, 77, 79, 81};
	static int[] DBLUES = {62, 64, 65, 66, 69, 71, 74, 76, 77, 78, 81, 83, 85};
	static int[] FHARMONIC = {65, 67, 68, 70, 72, 73, 76, 77, 79, 80, 82, 84, 85, 88, 89};
	static int[] GCHROMATIC = {55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79};
	
	AssociationNetwork network;
	String keySignature;
	int nGramLength;
	int choicesToCompare;
	CorpusAnalyzer analyzer;
	double intervalWeight;
	double durationWeight;
	MidiReader reader;
	
	public Composer(String corpusFolderName, String keySig, int nGramLength, int choicesToCompare, double intervalWeight, double durationWeight, HashMap<String, Double> artistWeightings) {
		analyzer = new CorpusAnalyzer();
		reader = new MidiReader(analyzer);
		network = new AssociationNetwork();
		network.artistWeightings = artistWeightings;
		network.probabilities = analyzer;
		network.intervalContribution = intervalWeight;
		network.durationContribution = durationWeight;
		this.intervalWeight = intervalWeight;
		this.durationWeight = durationWeight;
		this.choicesToCompare = choicesToCompare;
		this.nGramLength = nGramLength;
		keySignature = keySig;
		trainNetwork(corpusFolderName, network);
	}
	
	public void composeMelody(String outputFilename, int lengthInNotes) {
		Node startNode = network.getTonic();
		MidiWriter mw = new MidiWriter();
		int[] scale = getScaleMidiKeys(keySignature);
		
		int midiOffset = scale[0]; // This ensures that the notes are being played relative to the correct tonic
		
		mw.noteOnOffNow(8, startNode.scaleDegree+midiOffset, decodeDuration(startNode.noteDuration));
		
		ArrayList<Node> alreadyChosen = new ArrayList<Node>();	// I maintain a list of previous choices to avoid repetition
		Node currentNode = startNode;
		int distanceToEndOfBar = 63;
		int distanceFromRest = 0;

		for(int i = 0; i < lengthInNotes; i++) {
			alreadyChosen.add(currentNode);
			
			ArrayList<Node> choices = network.deduceBestNextNodes(currentNode, choicesToCompare, alreadyChosen);
			ArrayList<Node> nGram = getMostRecentNGram(alreadyChosen, nGramLength-1);
			
			Node nextNode = getBestSelection(distanceToEndOfBar, distanceFromRest, nGram, choices);
			
			int duration = decodeDuration(nextNode.noteDuration);
			int midiKey = getTransposedScaleDegree(nextNode.scaleDegree+midiOffset, scale);
			int velocity = smoothVelocity(nextNode.velocity);
			
			mw.noteOnOffNow(duration, midiKey, velocity);
			
			distanceFromRest++;
			distanceToEndOfBar = (distanceToEndOfBar - duration) % 64;
			currentNode = nextNode;
		}
		
		writeMidi(mw, outputFilename);
	}
	
	private ArrayList<Node> getMostRecentNGram(ArrayList<Node> melodySoFar, int n){
		ArrayList<Node> nGram = new ArrayList();
		int bound = Math.min(n, melodySoFar.size());
		for(int i = 0; i < bound; i++) {
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
			for(String scaleDegreeNGram : getScaleDegreeNGramKeys(nGram, choice)) {
				weight += (intervalWeight * analyzer.getScaleDegreeNGramProbability(scaleDegreeNGram));
			}
			
			for(String noteNameNGram : getNoteNameNGramKeys(nGram, choice)) {
				System.out.println(noteNameNGram);
				weight += (intervalWeight * analyzer.getNoteNameNGramProbability(noteNameNGram));
			}
			
			for(String durationNGram : getDurationNGramStrings(nGram, choice)) {
				weight += (durationWeight * analyzer.getDurationNGramProbability(durationNGram));
			}
			
			if(choice.noteName == nGram.get(nGram.size()-1).noteName)
				weight -= 0;
			
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
	
	private int[] getScaleMidiKeys(String scale) {
		switch(scale) {
		case "cGypsy": return CGYPSY;
		case "aMinor": return AMINOR;
		case "DBlues": return DBLUES;
		case "fHarmonic" : return FHARMONIC;
		default : return GCHROMATIC;
		}
	}
	
	public ArrayList<String> getScaleDegreeNGramKeys(ArrayList<Node> nGram, Node currentNode){
		ArrayList<String> nGrams = new ArrayList();
		StringBuilder nGramString = new StringBuilder();
		nGramString.append(currentNode.scaleDegree);
		for(int i = nGram.size()-1; i >= 0; i--) {
			nGramString.insert(0, nGram.get(i).scaleDegree + ",");
			nGrams.add(nGramString.toString());
		}
		
		return nGrams;
	}
	
	public ArrayList<String> getNoteNameNGramKeys(ArrayList<Node> nGram, Node currentNode){
		ArrayList<String> nGrams = new ArrayList();
		StringBuilder nGramString = new StringBuilder();
		nGramString.append(currentNode.noteName);
		for(int i = nGram.size()-1; i >= 0; i--) {
			nGramString.insert(0, nGram.get(i).noteName + ",");
			nGrams.add(nGramString.toString());
		}	
		return nGrams;
	}
	
	public ArrayList<String> getDurationNGramStrings(ArrayList<Node> nGram, Node currentNode){
		ArrayList<String> nGrams = new ArrayList();
		StringBuilder nGramString = new StringBuilder();
		nGramString.append(currentNode.noteDuration);
		for(int i = nGram.size()-1; i >= 0; i--) {
			nGramString.insert(0, nGram.get(i).noteDuration + ",");
			nGrams.add(nGramString.toString());
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
				Node nextNode = network.deduceNextNode(currentNode, new ArrayList(), alreadyChosen);
				
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
	
	private void trainNetwork(String corpusFolder, AssociationNetwork network) {
		File corp = new File(corpusFolder);
		File[] artistFolders = corp.listFiles();
		
		for(File folder : artistFolders) {
			for(File midiFile : folder.listFiles()) {
				for(ArrayList<Node> channel : reader.readSequence(midiFile, nGramLength)) {
					for(Node note : channel) {
						note.artist = folder.getName();
						note.song = midiFile.getName();
					}
					network.allNodes.addAll(channel);
				}
			}
		}
		network.linkNetwork();
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
}
