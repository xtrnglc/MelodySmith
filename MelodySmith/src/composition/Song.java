package composition;

import java.io.File;
import java.util.ArrayList;

import midiFeatureFinder.MidiReader;
import midiFeatureFinder.Note;

public class Song {
	ArrayList<ArrayList<Note>> channels;
	String name, artist;
	double maxDistanceFromTonic = -1.0;
	double maxDistanceToCadence = -1.0;
	double averageDistanceFromTonic = -1.0;
	double averageDistanceToCadence = -1.0;
	
	Song(File file, String name, String artist){
		MidiReader r = new MidiReader();
		r.readSequenceRaw(file);
		r.readSequence(file);
		channels = r.getOrderedNotes();
		
		averageDistanceFromTonic = getAverageDistanceFromTonic();
		maxDistanceFromTonic = getFurthestDistanceFromTonic();
		
		averageDistanceToCadence = getAverageDistanceToCadence();
		maxDistanceToCadence = getFurthestDistanceToCadence();
		
		this.name = name;
		this.artist = artist;
	}
	
	ArrayList<ArrayList<Integer>> getAllScaleDegrees(){
		ArrayList<ArrayList<Integer>> allScaleDegrees = new ArrayList<>();
		for(ArrayList<Note> channel : channels) {
			allScaleDegrees.add(getScaleDegrees(channel));
		}
		return allScaleDegrees;
	}
	
	public static ArrayList<Integer> getScaleDegrees(ArrayList<Note> notes){
		ArrayList<Integer> scaleDegrees = new ArrayList<Integer>();
		for(Note note : notes) {
			scaleDegrees.add(note.scaleDegree);
		}
		return scaleDegrees;
	}
	
	int countNotes() {
		int total = 0;
		for(ArrayList<Note> notes : channels) {
			total += notes.size();
		}
		return total;
	}
	
	int countScaleDegreeOccurences(int scaleDegree) {
		int total = 0;
		for(ArrayList<Note> notes : channels) {
			for(Note note : notes) {
				if(note.scaleDegree == scaleDegree)
					total++;
			}
		}
		return total;
	}
	
	double getAverageDistanceFromTonic() {
		return Composer.getAverage(getAllDistancesFromTonic());
	}
	
	double getAverageDistanceToCadence() {
		return Composer.getAverage(getAllDistancesToCadence());
	}
	
	double getFurthestDistanceFromTonic() {
		return Composer.getValueFarthestFromAverage(averageDistanceFromTonic, getAllDistancesFromTonic());
	}
	
	double getFurthestDistanceToCadence() {
		return Composer.getValueFarthestFromAverage(averageDistanceToCadence, getAllDistancesToCadence());
	}
	
	private ArrayList<Integer> getAllDistancesFromTonic() {
		ArrayList<Integer> allDistancesFromTonic = new ArrayList<Integer>();
		for(ArrayList<Integer> channel : getAllScaleDegrees()) {
			for(int i = 0; i < channel.size(); i++) {
				int distanceFromTonic = Composer.getDistanceFromPreviousTonic(i, channel);
				allDistancesFromTonic.add(distanceFromTonic);
			}
		}
		return allDistancesFromTonic;
	}
	
	private ArrayList<Integer> getAllDistancesToCadence() {
		ArrayList<Integer> allDistancesToCadence = new ArrayList<Integer>();
		for(ArrayList<Integer> channel : getAllScaleDegrees()) {
			for(int i = 0; i < channel.size(); i++) {
				int distanceToCadence = Composer.getDistanceToNextTonic(i, channel);
				allDistancesToCadence.add(distanceToCadence);
			}
		}
		return allDistancesToCadence;
	}
}
