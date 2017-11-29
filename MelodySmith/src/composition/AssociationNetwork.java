package composition;

import java.util.ArrayList;
import java.util.HashMap;

public class AssociationNetwork {
	ArrayList<Node> network = new ArrayList<Node>();
	
	HashMap<Integer, Double> intervalProbabilities = new HashMap<>();
	HashMap<String, Double> nextIntervalProbabilities = new HashMap<>();
	HashMap<Integer, Double> durationProbabilities = new HashMap<>();
	HashMap<String, Double> nextDurationProbabilities = new HashMap<>();
	double averageTempo = 0.0;
	
	
	void addNode(Node newNode) {
		for(Node node : network) {
			newNode.linkNodes(node);
		}
		network.add(newNode);
	}
	
	void calculateNetworkStatistics() {
		
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
	
	/**
	 * Returns the distance in notes from the previously played tonic, or -1 if none exists
	 */
	private int getDistanceFromPreviousTonic(int index, ArrayList<Integer> scaleDegrees) {
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
	private int getDistanceFromNextTonic(int index, ArrayList<Integer> scaleDegrees) {
		int i = index, distance = 0;
		while(i < scaleDegrees.size()) {
			if(scaleDegrees.get(i) == 0)
				return distance;
			i++; distance++;
		}
		return -1;
	}
	
	private ArrayList<Integer> getDistancesFromTonic(){
		ArrayList<Integer> allDistancesFromTonic = new ArrayList<Integer>();
		for(Node currentNode : network) {
			allDistancesFromTonic.add(currentNode.distanceFromTonic);
		}
		return allDistancesFromTonic;
	}
	
	private ArrayList<ArrayList<Integer>> getScaleDegrees(){
		ArrayList<ArrayList<Integer>> allSongScaleDegrees = new ArrayList<ArrayList<Integer>>();
		for(Node currentNode : network) {
			
		}
		
		return allSongScaleDegrees;
	}
	
	private ArrayList<Integer> getTempos(){
		ArrayList<Integer> allTempos = new ArrayList<Integer>();
		for(Node currentNode : network) {
			allTempos.add(currentNode.tempo);
		}
		return allTempos;
	}
	
	private int getSingleIntervalFrequencyFromSong(int interval, ArrayList<ArrayList<Integer>> song) {
		int count = 0;
		for(ArrayList<Integer> channel : song) {
			count += getSingleIntervalFrequencyFromChannel(interval, channel);
		}
		return count;
	}
	
	private int getSingleIntervalFrequencyFromChannel(int interval, ArrayList<Integer> channelIntervals) {
		int count = 0, prev = -100;
		for(int i : channelIntervals) {
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
	
	private double getValueFarthestFromAverage(double average, ArrayList<Integer> list) {
		double maxValue = average;
		for(int i : list) {
			if(Math.abs(average-i) > Math.abs(average-maxValue))
				maxValue = i;
		}
		return maxValue;
	}
	
	private double getAverage(ArrayList<Integer> list) {
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
