package composition;

import java.io.File;
import java.util.ArrayList;

import midiFeatureFinder.MidiWriter;

public class Main {
	static int[] CMAJOR = {60, 62, 64, 65, 67, 69, 71, 72};
	static int[] AMINOR = {57, 59, 60, 62, 64, 65, 67, 69};
	
	public static void main(String[] args) {
		Composer composer = new Composer("midiReaderData");
		composer.composeMelody("bachKaty.mid", 100, true);
		System.out.println();
	}

}
