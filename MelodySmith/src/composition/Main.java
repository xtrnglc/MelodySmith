package composition;

import java.io.File;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		AssociationNetwork network = new AssociationNetwork();
		File folder = new File("midiReaderData");
		File[] midiFiles = folder.listFiles();
		
		for(File file : midiFiles) {
			network.corpus.add(new Song(file, file.getName(), "bach"));
		}
		
		network.calculateNetworkStatistics();
		network.addAllNotes();
		
		System.out.println();
	}

}
