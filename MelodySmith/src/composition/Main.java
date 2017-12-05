package composition;

import java.io.File;
import java.util.ArrayList;

import midiFeatureFinder.MidiWriter;

public class Main {
	static int[] CMAJOR = {60, 62, 64, 65, 67, 69, 71, 72};
	
	public static void main(String[] args) {
		AssociationNetwork network = new AssociationNetwork();
		File folder = new File("midiReaderData");
		File[] midiFiles = folder.listFiles();
		
		for(File file : midiFiles) {
			network.corpus.add(new Song(file, file.getName(), "bach"));
		}
		
		network.calculateNetworkStatistics();
		network.addAllNotes();
		
		Node startNode = network.getTonic();
		MidiWriter mw = new MidiWriter();
		mw.noteOnOffNow(8, startNode.scaleDegree+60, 120);
		
		ArrayList<Node> alreadyChosen = new ArrayList<Node>();
		ArrayList<Integer> recentScaleDegrees = new ArrayList<Integer>();
		Node currentNode = startNode;
		for(int i = 0; i < 240; i++) {
			alreadyChosen.add(currentNode);
			if(recentScaleDegrees.size() > 2)
				recentScaleDegrees.remove(0);
			if(alreadyChosen.size() > 100)
				alreadyChosen.remove(0);
			//recentScaleDegrees.add(currentNode.scaleDegree);
			if(currentNode.linkedNodes.size() > 0) {
				Node nextNode = network.deduceNextNode(currentNode, alreadyChosen, recentScaleDegrees);
			
				int duration = decodeDuration(nextNode.duration);
				int midiKey = getScaleDegree(nextNode.scaleDegree+60, CMAJOR);
				
				mw.noteOnOffNow(duration, midiKey, nextNode.velocity);
				currentNode = nextNode;
			}
			else {
				currentNode = network.getTonic(3);
			}
		}
		
		try {
		mw.writeToFile("eleventhComposition.mid");
		}catch(Exception e) {
			
		}
		
		System.out.println();
	}
	
	static int decodeDuration(String duration) {
		switch(duration) {
		case "1/4":return 16;
		case "1/8":return 8;
		case "1/16":return 4;
		case "1/32":return 2;
		default:return 8;
		}
	}
	
	static int getScaleDegree(int octavePosition, int[] scale) {
		for(int i : scale) {
			if(octavePosition == i || octavePosition < i)
				return i;
		}
		return scale[0];
	}

}
