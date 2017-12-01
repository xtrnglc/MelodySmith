package composition;

import java.util.ArrayList;
import java.util.HashMap;

import midiFeatureFinder.Note;

public class AssociationNetwork {
	ArrayList<Node> network = new ArrayList<Node>();
	ArrayList<Song> corpus = new ArrayList<Song>();
	
	HashMap<Integer, Double> intervalProbabilities = new HashMap<>();
	HashMap<String, Double> nextIntervalProbabilities = new HashMap<>();
	HashMap<Integer, Double> durationProbabilities = new HashMap<>();
	HashMap<String, Double> nextDurationProbabilities = new HashMap<>();
	double averageTempo = -1.0;
	double maxTempo = -1.0;
	double averageDistanceFromTonic = -1.0;
	double maxDistanceFromTonic = -1.0;
	double averageDistanceToCadence = -1.0;
	double maxDistanceToCadence = -1.0;
	
	void addAllNotes() {
		for(Song song : corpus) {
			for(ArrayList<Note> channel : song.channels) {
				int index = 0;
				ArrayList<Integer> scaleDegrees = song.getScaleDegrees(channel);
				for(Note note : channel) {
					Node nodeToAdd = new Node();
					nodeToAdd.song = song.name;
					nodeToAdd.artist = song.artist;
					nodeToAdd.scaleDegree = note.scaleDegree;
					nodeToAdd.distanceFromTonic = Song.getDistanceFromPreviousTonic(index, scaleDegrees);
					nodeToAdd.distanceToCadence = Song.getDistanceToNextTonic(index, scaleDegrees);
					nodeToAdd.key = note.keySignature;
					
					addNode(nodeToAdd);
					index++;
				}
			}
		}
	}
	
	void addNode(Node newNode) {
		for(Node node : network) {
			int interval =  Math.abs(newNode.scaleDegree-node.scaleDegree);
			String nextIntervalKey = newNode.scaleDegree + "->" + interval;
			double normal1 = getNormalizedDistanceFromAverage(node.distanceFromTonic, averageDistanceFromTonic, maxDistanceFromTonic);
			double normal2 = getNormalizedDistanceFromAverage(node.distanceToCadence, averageDistanceToCadence, maxDistanceToCadence);
			
			
			double weight = getNormalizedDistanceFromAverage(node.distanceFromTonic, averageDistanceFromTonic, maxDistanceFromTonic)
							+ getNormalizedDistanceFromAverage(node.distanceToCadence, averageDistanceToCadence, maxDistanceToCadence) 
							+ getEquivalentValueWeight(node.song == newNode.song)
							+ getEquivalentValueWeight(node.key == newNode.key)
							+ intervalProbabilities.get(interval)
							+ nextIntervalProbabilities.get(nextIntervalKey);
			
			String reverseKey = node.scaleDegree + "->" + interval;
			double reverseWeight = getNormalizedDistanceFromAverage(newNode.distanceFromTonic, averageDistanceFromTonic, maxDistanceFromTonic)
					+ getNormalizedDistanceFromAverage(newNode.distanceToCadence, averageDistanceToCadence, maxDistanceToCadence) 
					+ getEquivalentValueWeight(node.song == newNode.song)
					+ getEquivalentValueWeight(node.key == newNode.key)
					+ intervalProbabilities.get(interval)
					+ nextIntervalProbabilities.get(reverseKey);
			
			newNode.linkNodes(node, weight, reverseWeight);
		}
		network.add(newNode);
	}
	
	Node deduceNextNode(Node currentNode) {
		Link mostLikelyLink = null;
		ArrayList<Link> equivalentLinks = new ArrayList<Link>();
		for(Link link : currentNode.linkedNodes) {
			if(mostLikelyLink == null || link.weight > mostLikelyLink.weight) {
				equivalentLinks.clear();
				mostLikelyLink = link;
			} else if(link.weight == mostLikelyLink.weight) {
				equivalentLinks.add(link);
			}
		}
		return mostLikelyLink.endNode;
	}
	
	void calculateNetworkStatistics() {
		calculateAllIntervalProbabilities();
		calculateAllNextIntervalProbabilities();
		calculateDistanceFromTonicStats();
		calculateDistanceToCadenceStats();
	}
	
	private void calculateAllIntervalProbabilities() {
		ArrayList<Double> allIntervalProbabilities = new ArrayList<Double>();
		addBlanks(allIntervalProbabilities, 12);
		
		for(Song song : corpus) {			
			for(int i=0; i < 12; i++) {
				double intervalProbability = 1.0*getSingleIntervalFrequencyFromSong(i, song)/song.countNotes();
				allIntervalProbabilities.set(i, allIntervalProbabilities.get(i) + intervalProbability);
			}
		}
		
		for(int i = 0; i < allIntervalProbabilities.size(); i++) {
			intervalProbabilities.put(i, allIntervalProbabilities.get(i)/corpus.size());
		}
	}
	
	private void calculateAllNextIntervalProbabilities() {
		HashMap<String,Double> map = new HashMap<>();
		addBlanks(map);
		for(Song song : corpus) {
			for(int scaleDegree = 1; scaleDegree < 13; scaleDegree++) {
				for(int interval = 0; interval < 12; interval++) {
					double intervalProbability = 1.0*getIntervalFrequencyAfterScaleDegreeFromSong(scaleDegree, interval, song)/song.countScaleDegreeOccurences(scaleDegree);
					String key = scaleDegree+"->"+interval;
					double intervalTotal = map.get(key) + intervalProbability;
					if(!Double.isNaN(intervalTotal))
						map.put(key, intervalTotal);
				}
			}
		}
		
		for(int scaleDegree = 0; scaleDegree < 13; scaleDegree++) {
			for(int interval = 0; interval < 12; interval++) {
				String key = scaleDegree+"->"+interval;
				nextIntervalProbabilities.put(key, map.get(key)/corpus.size());
			}
		}
	}	
	
	private void calculateDistanceFromTonicStats() {
		double total = 0.0, max = 0.0;
		for(Song song : corpus) {
			total += song.averageDistanceFromTonic;
			if(song.maxDistanceFromTonic > max)
				max = song.maxDistanceFromTonic;
		}
		averageDistanceFromTonic = total/corpus.size();
		maxDistanceFromTonic = max;
	}
	
	private void calculateDistanceToCadenceStats() {
		double total = 0.0, max = 0.0;
		for(Song song : corpus) {
			total += song.averageDistanceToCadence;
			if(song.maxDistanceToCadence > max)
				max = song.maxDistanceToCadence;
		}
		averageDistanceToCadence = total/corpus.size();
		maxDistanceToCadence = max;
	}
	
	private double getEquivalentValueWeight(boolean expression) {
		if(expression)
			return 1.0;
		else
			return 0.0;
	}
	
	private void addBlanks(ArrayList<Double> list, int size) {
		for(int i = 0; i < size; i++) {
			list.add(0.0);
		}
	}
	
	private void addBlanks(HashMap<String, Double> map) {
		for(int scaleDegree = 0; scaleDegree < 13; scaleDegree++) {
			for(int interval = 0; interval < 12; interval++) {
				map.put(scaleDegree+"->"+interval, 0.0);
			}
		}
	}
	
	private ArrayList<Integer> getTempos(){
		ArrayList<Integer> allTempos = new ArrayList<Integer>();
		for(Node currentNode : network) {
			allTempos.add(currentNode.tempo);
		}
		return allTempos;
	}
	
	private int getIntervalFrequencyAfterScaleDegreeFromSong(int scaleDegree, int interval, Song song) {
		int count = 0;
		for(ArrayList<Integer> channel : song.getAllScaleDegrees()) {
			count += getIntervalFrequencyAfterScaleDegree(scaleDegree, interval, channel);
		}
		return count;
	}
	
	private int getIntervalFrequencyAfterScaleDegree(int scaleDegree, int interval, ArrayList<Integer> channel) {
		int count = 0, prev = -100;
		for(int i : channel) {
			if(prev == scaleDegree && Math.abs(prev-i) == interval) {
				count++;
			}
			prev = i;
		}
		return count;
	}
	
	private int getSingleIntervalFrequencyFromSong(int interval, Song song) {
		int count = 0;
		for(ArrayList<Integer> channel : song.getAllScaleDegrees()) {
			count += getSingleIntervalFrequencyFromChannel(interval, channel);
		}
		return count;
	}
	
	private int getSingleIntervalFrequencyFromChannel(int interval, ArrayList<Integer> channel) {
		int count = 0, prev = -100;
		for(int i : channel) {
			if(Math.abs(prev - i) == interval)
				count++;
			prev = i;
		}
		return count;
	}
	
	private double getNormalizedDistanceFromAverage(int value, ArrayList<Integer> list) {
		double average = getAverage(list);
		double maxValue = getValueFarthestFromAverage(average, list);
		return getNormalizedDistanceFromAverage(value, average, maxValue);
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
	
	private double getNormalizedDistanceFromAverage(double value, double average, double maxValue) {
		return Math.abs(value-maxValue) / Math.abs(average-maxValue);
	}
	
	private <T> int getItemFrequencyFromArray(T item, ArrayList<T> array) {
		int count = 0;
		for(T it : array) {
			if(it == item)
				count++;
		}
		return count;
	}
}