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
		return AssociationNetwork.getAverage(getAllDistancesFromTonic());
	}
	
	double getAverageDistanceToCadence() {
		return AssociationNetwork.getAverage(getAllDistancesToCadence());
	}
	
	double getFurthestDistanceFromTonic() {
		return AssociationNetwork.getValueFarthestFromAverage(averageDistanceFromTonic, getAllDistancesFromTonic());
	}
	
	double getFurthestDistanceToCadence() {
		return AssociationNetwork.getValueFarthestFromAverage(averageDistanceToCadence, getAllDistancesToCadence());
	}
	
	private ArrayList<Integer> getAllDistancesFromTonic() {
		ArrayList<Integer> allDistancesFromTonic = new ArrayList<Integer>();
		for(ArrayList<Integer> channel : getAllScaleDegrees()) {
			for(int i = 0; i < channel.size(); i++) {
				int distanceFromTonic = getDistanceFromPreviousTonic(i, channel);
				allDistancesFromTonic.add(distanceFromTonic);
			}
		}
		return allDistancesFromTonic;
	}
	
	private ArrayList<Integer> getAllDistancesToCadence() {
		ArrayList<Integer> allDistancesToCadence = new ArrayList<Integer>();
		for(ArrayList<Integer> channel : getAllScaleDegrees()) {
			for(int i = 0; i < channel.size(); i++) {
				int distanceToCadence = getDistanceToNextTonic(i, channel);
				allDistancesToCadence.add(distanceToCadence);
			}
		}
		return allDistancesToCadence;
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
