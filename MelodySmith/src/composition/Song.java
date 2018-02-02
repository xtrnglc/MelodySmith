package composition;

import java.io.File;
import java.util.ArrayList;

import midiFeatureFinder.MidiReader;
public class Song {
	String name, artist;
	double maxDistanceFromTonic = -1.0;
	double maxDistanceToCadence = -1.0;
	double averageDistanceFromTonic = -1.0;
	double averageDistanceToCadence = -1.0;
	
	Song(File file, String name, String artist){
		
		this.name = name;
		this.artist = artist;
	}
	
	Song(MidiReader mr){
		
	}
}
