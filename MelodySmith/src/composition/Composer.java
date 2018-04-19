package composition;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

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
	double repetitionPenalty;
	CorpusAnalyzer analyzer;
	double intervalWeight;
	double durationWeight;
	MidiReader reader;
	int speedWeight;
	double syncopationWeight;
	int restAmount;
	boolean constructiveRests;
	boolean extractPhrases;
	Random random;
	HashMap<String, Integer> durationCounts;
	
	public Composer(String corpusFolderName, String keySig, double intervalWeight, double durationWeight, String restType, int restAmount, int syncopation, int nGramLength, int choicesToCompare, int speed, boolean phrases, HashMap<String, Double> artistWeightings) {
		analyzer = new CorpusAnalyzer();
		random = new Random();
		reader = new MidiReader(analyzer);
		network = new AssociationNetwork();
		durationCounts = new HashMap<>();
		network.artistWeightings = artistWeightings;
		network.probabilities = analyzer;
		network.intervalContribution = intervalWeight;
		network.durationContribution = durationWeight;
		network.noteLengthWeight = speed;
		this.intervalWeight = intervalWeight;
		this.durationWeight = durationWeight;
		this.choicesToCompare = choicesToCompare;
		this.nGramLength = nGramLength;
		this.speedWeight = speed;
		this.syncopationWeight = 100/syncopation;
		this.constructiveRests = true;//(restType.toUpperCase().trim().equals("CONSTRUCTIVE"));
		this.restAmount = restAmount;
		this.repetitionPenalty = 1.5;
		this.extractPhrases = phrases;
		keySignature = keySig;
		trainNetwork(corpusFolderName, network);
	}
	
	public void composeWithPhrases(String outputFilename, int numberOfPhrases) {
		MidiWriter mw = new MidiWriter();
		int[] scale = getScaleMidiKeys(keySignature);
		int tonic = scale[0];
		ArrayList<Node> composition = new ArrayList<>();
		ArrayList<Phrase> recentPhrases = new ArrayList<>();
		
		if(recentPhrases.size() > 5)
			recentPhrases.remove(0);
		
		Phrase currentPhrase = network.allPhrases.get(0);
		
		for(int i = 0; i < numberOfPhrases; i++) {
			ArrayList<Phrase> choices = network.deduceBestNextPhrases(currentPhrase, choicesToCompare);
			
			Phrase bestPhrase = choices.get(0);
			ArrayList<Node> nGram = getMostRecentNGram(composition, nGramLength);
			double bestWeight = 0;
			for(Phrase phrase : choices) {
				double choiceWeight = weightNodeInCurrentComposition(phrase.nodes.get(0), nGram, composition, 0, 22)- (10 - phrase.nodes.size());
				for(Phrase playedPhrase : recentPhrases) {
					if(samePhrase(phrase, playedPhrase))
						choiceWeight -= 5;
				}
				if(choiceWeight > bestWeight) {
					bestWeight = choiceWeight;
					bestPhrase = phrase;
				}
			}
			
			for(Node phraseNode : bestPhrase.nodes) {
				writeNodeToMidi(phraseNode, mw, tonic, scale, 10000);
			}
			composition.addAll(bestPhrase.nodes);
			currentPhrase = bestPhrase;
			recentPhrases.add(bestPhrase);
		}		
		writeMidi(mw, reader, outputFilename);
	}
	
	private boolean samePhrase(Phrase phrase1, Phrase phrase2) {
		if(phrase1.nodes.size() != phrase2.nodes.size())
			return false;
		else {
			int i = 0;
			while(i < phrase1.nodes.size()) {
				if(!phrase1.nodes.get(i).equivalentNodes(phrase2.nodes.get(i)))
					return false;
				i++;
			}
		}
		return true;
	}
	
	public void composeNBars(String outputFilename, int n) {
		MidiWriter mw = new MidiWriter();
		int[] scale = getScaleMidiKeys(keySignature);
		int tonic = scale[0];
		ArrayList<Node> alreadyChosen = new ArrayList<>();
		ArrayList<Node> composition = new ArrayList<>();
		
		int lengthToEnd = 64*n;
		int distanceToEndOfBar = 63;
		int distanceFromRest = 0;
		
		Node currentNode = network.getTonic();
		writeNodeToMidi(currentNode, mw, tonic, scale);
		lengthToEnd -= decodeDuration(currentNode.noteDuration);
		distanceToEndOfBar = (distanceToEndOfBar - decodeDuration(currentNode.noteDuration))%64;
		
		while(lengthToEnd > 0) {
			ArrayList<Node> choices = network.deduceBestNextNodes(currentNode, choicesToCompare, composition);
			
			Node bestNode = choices.get(0);
			ArrayList<Node> nGram = getMostRecentNGram(composition, nGramLength);
			double bestWeight = weightNodeInCurrentComposition(bestNode, nGram, alreadyChosen, distanceToEndOfBar, distanceFromRest);
			for(Node choice : choices) {
				double choiceWeight = weightNodeInCurrentComposition(choice, nGram, alreadyChosen, distanceToEndOfBar, distanceFromRest);
				if(choiceWeight > bestWeight) {
					bestWeight = choiceWeight;
					bestNode = choice;
				}
			}
			
			boolean foundEquivalentNode = false;
			for(Node nodeInComposition : alreadyChosen) {
				if(nodeInComposition.equivalentNodes(bestNode)) {
					nodeInComposition.timesPlayed++;
					foundEquivalentNode = true;
				}
			}
			
			if(!foundEquivalentNode) {
				for(Node nodeInComposition : alreadyChosen) {
					nodeInComposition.timesPlayed--;
				}
				bestNode.timesPlayed += 1;
				alreadyChosen.add(bestNode);
			}
			
			if(writeNodeToMidi(bestNode, mw, tonic, scale, lengthToEnd)) {
				distanceFromRest++;
			} else {
				distanceFromRest = 0;
			}
			
			currentNode = bestNode;
			lengthToEnd -= decodeDuration(currentNode.noteDuration);
			distanceToEndOfBar = (distanceToEndOfBar - decodeDuration(currentNode.noteDuration))%64;
			composition.add(bestNode);
		}
		writeMidi(mw, reader, outputFilename);
	}
	
	public void composeMelody(String outputFilename, int lengthInNotes) {
		Node startNode = network.getTonic();
		MidiWriter mw = new MidiWriter();
		int[] scale = getScaleMidiKeys(keySignature);
		
		int midiOffset = scale[0]; // This ensures that the notes are being played relative to the correct tonic
		
		mw.noteOnOffNow(decodeDuration(startNode.noteDuration), startNode.scaleDegree+midiOffset, smoothVelocity(startNode.velocity));
		
		ArrayList<Node> alreadyChosen = new ArrayList<Node>();	// I maintain a list of previous choices to avoid repetition
		ArrayList<Node> composition = new ArrayList<Node>();
		Node currentNode = startNode;
		int distanceToEndOfBar = 15;
		int distanceFromRest = 0;

		for(int i = 0; i < lengthInNotes; i++) {
			composition.add(currentNode);
			if(alreadyChosen.size() > nGramLength * 5)
				alreadyChosen.remove(0);
			
			ArrayList<Node> choices = network.deduceBestNextNodes(currentNode, choicesToCompare, alreadyChosen);
			ArrayList<Node> nGram = getMostRecentNGram(alreadyChosen, nGramLength-1);
			
			Node nextNode = getBestSelection(distanceToEndOfBar, distanceFromRest, nGram, choices);
			
			if(constructiveRests) {
				if(writeNodeToMidi(nextNode, mw, midiOffset, scale))
					distanceFromRest = 0;
				else
					distanceFromRest++;
			} else {
				if (distanceFromRest < restAmount) {
					writeNodeToMidi(nextNode, mw, midiOffset, scale);
					distanceFromRest++;
				} else {
					distanceFromRest = restAmount - random.nextInt(2*restAmount);
				}
			}
			
			int duration = decodeDuration(nextNode.noteDuration);
			distanceToEndOfBar = (distanceToEndOfBar - duration) % 16;
			currentNode = nextNode;
		}
		composition.add(currentNode);
		writeMidi(mw, reader, outputFilename);
	}
	
	/**
	 * Writes a note/rest using the provided MidiWriter
	 * Returns true if it was a rest
	 */
	private boolean writeNodeToMidi(Node node, MidiWriter mw, int midiOffset, int[] scale) {
		int duration = decodeDuration(node.noteDuration);
		int midiKey = getTransposedScaleDegree(node.scaleDegree+midiOffset, scale);
		int velocity = smoothVelocity(node.velocity);
		
		if(isRest(node)){
			if (duration < 8)
				duration = 8;
			mw.addRest(duration);
			return true;
		} 
		else{
			if(midiKey > 200)
				System.out.println("MidiKey: " + midiKey);
			mw.noteOnOffNow(duration, midiKey, velocity);
			return false;
		}	
	}
	
	private boolean writeNodeToMidi(Node node, MidiWriter mw, int midiOffset, int[] scale, int lengthTillEnd) {
		int duration = decodeDuration(node.noteDuration);
		if(duration > lengthTillEnd)
			duration = lengthTillEnd;
		int midiKey = getTransposedScaleDegree(node.scaleDegree+midiOffset, scale);
		int velocity = smoothVelocity(node.velocity);
		
		if(isRest(node)){
			if (duration < 8) {
				duration = 8;
				node.noteDuration = "1/8";
			}
			mw.addRest(duration);
			return true;
		} 
		else{
			mw.noteOnOffNow(duration, midiKey, velocity);
			return false;
		}	
	}
	
	private ArrayList<Node> getMostRecentNGram(ArrayList<Node> melodySoFar, int n){
		ArrayList<Node> nGram = new ArrayList<>();
		int bound = Math.min(n, melodySoFar.size());
		for(int i = 0; i < bound; i++) {
			nGram.add(melodySoFar.get(melodySoFar.size()-i-1));
		}
		return nGram;
	}
	
	private void writeMidi(MidiWriter mw, MidiReader mr, String fileName) {
		try {
			mw.writeToFile(fileName);
			System.out.println("100");
			System.out.println("JS:" + mr.readSequenceForABCJS(new File(fileName), 5, true, 8));
		} catch(Exception e) {
			System.out.println("Error writing to output file: " + fileName + "\nError Message: " + e.getMessage());
		}
	}
	
	private double weightNodeInCurrentComposition(Node node, ArrayList<Node> nGram, ArrayList<Node> nodesInComposition, int distanceToEndOfBar, int distanceFromRest) {
		double weight = 0.0;
		for(String scaleDegreeNGram : getScaleDegreeNGramKeys(nGram, node)) {
			weight += (intervalWeight * analyzer.getScaleDegreeNGramProbability(scaleDegreeNGram));
		}
		
		for(String noteNameNGram : getNoteNameNGramKeys(nGram, node)) {
			weight += (intervalWeight * analyzer.getNoteNameNGramProbability(noteNameNGram));
		}
		
		for(String durationNGram : getDurationNGramStrings(nGram, node)) {
			weight += (durationWeight * analyzer.getDurationNGramProbability(durationNGram));
		}
		
		for(Node alreadyPlayed : nodesInComposition) {
			if(alreadyPlayed.equivalentNodes(node))
				weight -= repetitionPenalty*alreadyPlayed.timesPlayed;
		}
		
		if(distanceToEndOfBar == 63 && isRest(node))
			weight -= 20;
		if(distanceFromRest < restAmount && isRest(node))
			weight -= 20;
		
		int duration = decodeDuration(node.noteDuration);
		if(distanceToEndOfBar - duration == 0) {
			weight += syncopationWeight;
		}
		return weight;
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
				weight += (intervalWeight * analyzer.getNoteNameNGramProbability(noteNameNGram));
			}
			
			for(String durationNGram : getDurationNGramStrings(nGram, choice)) {
				weight += (durationWeight * analyzer.getDurationNGramProbability(durationNGram));
			}
			
			if(distanceToEndOfBar == 63 && isRest(choice))
				weight -= 20;
			if(distanceFromRest < restAmount && isRest(choice))
				weight -= 20;
			
			int duration = decodeDuration(choice.noteDuration);
			if(distanceToEndOfBar - duration == 0) {
				weight += syncopationWeight;
			}
			
			if(weight > bestWeight) {
				bestWeight = weight;
				bestNode = choice;
			}
		}
		return bestNode;
	}
	
	private int[] getScaleMidiKeys(String scale) {
		switch(scale.toUpperCase()) {
		case "CGYPSY": return CGYPSY;
		case "AMINOR": return AMINOR;
		case "DBLUES": return DBLUES;
		case "FHARMONIC" : return FHARMONIC;
		case "CMAJOR" : return CMAJOR;
		default : return GCHROMATIC;
		}
	}
	
	public ArrayList<String> getScaleDegreeNGramKeys(ArrayList<Node> nGram, Node currentNode){
		ArrayList<String> nGrams = new ArrayList<>();
		StringBuilder nGramString = new StringBuilder();
		nGramString.append(currentNode.scaleDegree);
		for(int i = nGram.size()-1; i >= 0; i--) {
			nGramString.insert(0, nGram.get(i).scaleDegree + ",");
			nGrams.add(nGramString.toString());
		}
		
		return nGrams;
	}
	
	public ArrayList<String> getNoteNameNGramKeys(ArrayList<Node> nGram, Node currentNode){
		ArrayList<String> nGrams = new ArrayList<>();
		StringBuilder nGramString = new StringBuilder();
		nGramString.append(currentNode.noteName);
		for(int i = nGram.size()-1; i >= 0; i--) {
			nGramString.insert(0, nGram.get(i).noteName + ",");
			nGrams.add(nGramString.toString());
		}	
		return nGrams;
	}
	
	public ArrayList<String> getDurationNGramStrings(ArrayList<Node> nGram, Node currentNode){
		ArrayList<String> nGrams = new ArrayList<>();
		StringBuilder nGramString = new StringBuilder();
		nGramString.append(currentNode.noteDuration);
		for(int i = nGram.size()-1; i >= 0; i--) {
			nGramString.insert(0, nGram.get(i).noteDuration + ",");
			nGrams.add(nGramString.toString());
		}
		
		return nGrams;
	}
	
	private boolean isRest(Node note) {
		return note.key == -1;
	}
	
	private void trainNetwork(String corpusFolder, AssociationNetwork network) {
		File corp = new File(corpusFolder);
		File[] artistFolders = corp.listFiles();
		
		for(File folder : artistFolders) {
			if(folder.getName().startsWith("."))
				continue;
			for(File midiFile : folder.listFiles()) {
				if(midiFile.getName().startsWith("."))
					continue;
				if(extractPhrases) {
					ArrayList<ArrayList<Phrase>> channels = reader.stitchPhraseByBar(reader.readSequence(midiFile, nGramLength, constructiveRests, 16), 2);
					for(ArrayList<Phrase> channel : channels) {
						for(Phrase phrase : channel) {
							for(Node node : phrase.nodes) {
								node.artist = folder.getName();
								node.song = midiFile.getName();
							}
						}
						network.allPhrases.addAll(channel);
					}
				}
				else {
					for(ArrayList<Node> channel : reader.readSequence(midiFile, nGramLength, constructiveRests, 16)) {
						
						for(Node note : channel) {
							note.artist = folder.getName();
							note.song = midiFile.getName();
						}
						network.allNodes.addAll(channel);
					}
				}
			}
		}
		printBigrams();
		if(extractPhrases) {
			network.linkPhraseNetwork();
		} else {
			network.linkNetwork();
		}
	}
	
	private void printBigrams() {
		HashMap<String,Double> bigramProbabilities = analyzer.getNoteNameBigramProbabilities();
		Set<String> noteNameVocab = analyzer.getNoteNameVocab();
		StringBuilder json = new StringBuilder();
		json.append("{");
		for(String noteName : noteNameVocab) {
			StringBuilder builder = new StringBuilder();
			builder.append("'" + noteName +"':[");
			for(String note2Name : noteNameVocab) {
				String key = noteName+","+note2Name;
				builder.append(bigramProbabilities.get(key)+",");
			}
			json.append(builder.substring(0, builder.length()-1) + "], ");
		}
		System.out.println(json.substring(0, json.length()-2) + "}");
	}
	
	static int decodeDuration(String duration) {
		if(duration == null)
			return 8;
		switch(duration) {
		case "4":return 256;
		case "3":return 192;
		case "2":return 128;
		case "1":return 64;
		case "1/2":return 32;
		case "1/4":return 16;
		case "1/8":return 8;
		case "1/16":return 4;
		case "1/32":return 2;
		default:return 8;
		}
	}
	
	static int getTransposedScaleDegree(int octavePosition, int[] scale) {
		
		if(octavePosition < scale[0])
			return scale[0];
		
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
