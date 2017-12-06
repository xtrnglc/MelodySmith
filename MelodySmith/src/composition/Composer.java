package composition;

import java.io.File;
import java.util.ArrayList;
import jvst.*;

import midiFeatureFinder.MidiWriter;

public class Composer {
	static int[] CMAJOR = {60, 62, 64, 65, 67, 69, 71, 72};
	static int[] AMINOR = {57, 59, 60, 62, 64, 65, 67, 69};
	
	AssociationNetwork network;
	
	public Composer(String folderName) {
		network = new AssociationNetwork();
		trainNetwork(folderName, network);
	}
	
	public Composer(ArtistGrouping[] artists) {
		network = new AssociationNetwork();
		trainNetwork(artists, network);
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
			if(alreadyChosen.size() > 100)
				alreadyChosen.remove(0);
			
			if(currentNode.linkedNodes.size() > 0) {
				Node nextNode = network.deduceNextNode(currentNode, alreadyChosen);
				
				if(nextNode.scaleDegree == -1) {
					restDuration += decodeDuration(nextNode.duration);
					lastWasRest = true;
					currentNode = nextNode;
					continue;
				}
			
				int duration = decodeDuration(nextNode.duration);
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
					network.corpus.add(new Song(file, file.getName(), artist.artistName));
			}
		}
		network.calculateNetworkStatistics();
		network.addAllNotes();	
	}
	
	private void trainNetwork(String folderName, AssociationNetwork network) {
		File folder = new File(folderName);
		File[] midiFiles = folder.listFiles();
		
		for(File file : midiFiles) {
			if(file.isFile())
				network.corpus.add(new Song(file, file.getName(), folder.getName()));
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
		return 0.0;
	}
}
