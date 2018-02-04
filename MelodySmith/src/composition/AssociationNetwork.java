package composition;

import java.util.ArrayList;
import java.util.HashMap;

public class AssociationNetwork {
	ArrayList<Node> allNodes = new ArrayList<Node>();
	ArrayList<Song> corpus = new ArrayList<Song>();
	
	Link[][] matrix;
	
	HashMap<Integer, Double> intervalProbabilities = new HashMap<>();
	HashMap<String, Double> nextIntervalProbabilities = new HashMap<>();
	HashMap<Integer, Double> durationProbabilities = new HashMap<>();
	HashMap<String, Double> nextDurationProbabilities = new HashMap<>();
	HashMap<String, Double> artistWeightings = new HashMap<>();
	double averageTempo = -1.0;
	double maxTempo = -1.0;
	double averageDistanceFromTonic = -1.0;
	double maxDistanceFromTonic = -1.0;
	double averageDistanceToCadence = -1.0;
	double maxDistanceToCadence = -1.0;
	int size = 0;
	
	double intervalContribution;
	double durationContribution;
	
	CorpusAnalyzer probabilities;
	
	double sameScaleDegreePenalty = 0.5;
	double sameDurationPenalty = 1.0;
	
	public void linkNetwork() {
		matrix = new Link[allNodes.size()][allNodes.size()];
		ArrayList<Node> alreadyLinked = new ArrayList<Node>();
		int count = 0;
		for(Node node : allNodes) {
			node.index = count;
			printProgress(count);
			count++;
			alreadyLinked.add(node);
			for(Node node2 : alreadyLinked) {
				Link forwardLink = weightNodes(node, node2);
				Link backwardLink = weightNodes(node2, node);
				matrix[node.index][node2.index] = forwardLink;
				matrix[node2.index][node.index] = backwardLink;
			}
		}
	}
	
	private void printProgress(int count) {
		if((count % (allNodes.size()/10)) == 0)
			System.out.println(roundUp(((double)count/allNodes.size())*100));
	}
	
	private int roundUp(double d) {
		int percent = (int)Math.round(d);
		while(percent % 10 != 0)
			percent++;
		return percent;
	}
	
	Link weightNodes(Node start, Node end) {
		int interval = end.scaleDegree-start.scaleDegree;
		String scaleDegreeNGram = start.scaleDegree + "," + end.scaleDegree;
		String durationNGram = start.noteDuration + "," + end.noteDuration;
		

		double weight = 
		  getEquivalentValueWeight(start.song == end.song)
		+ getEquivalentValueWeight(start.keySignature == end.keySignature)
		+ artistWeightings.get(end.artist)
		+ (probabilities.getIntervalProbability(interval) * intervalContribution)
		+ (probabilities.getScaleDegreeNGramProbability(scaleDegreeNGram) * intervalContribution)
		+ (probabilities.getDurationNGramProbability(durationNGram) * durationContribution);
		
		if(start.scaleDegree == end.scaleDegree)
			weight -= sameScaleDegreePenalty;
		if(start.noteDuration == end.noteDuration)
			weight -= sameDurationPenalty;
		
		return new Link(start, end, weight);
	}
	
	Node getTonic() {
		for(Node node : allNodes) {
			if(node.scaleDegree == 0) {
				return node;
			}
		}
		return null;
	}
	
	Node getTonic(int i) {
		for(Node node : allNodes) {
			if(node.scaleDegree == 0) {
				i--;
				if(i <= 0)
					return node;
			}
		}
		return null;
	}
	
	ArrayList<Node> deduceBestNextNodes(Node currentNode, int numberToCollect, ArrayList<Node> alreadyChosen) {
		ArrayList<Node> bestNodes = new ArrayList();
		ArrayList<Node> chosen = new ArrayList();
		chosen.addAll(alreadyChosen);
		for(int i = 0; i < numberToCollect; i++) {
			Node bestNode = deduceNextNode(currentNode, bestNodes, chosen);
			bestNodes.add(bestNode);
			chosen.add(bestNode);
		}
		return bestNodes;
	}
	
	Node deduceNextNode(Node currentNode, ArrayList<Node> bestNodes, ArrayList<Node> alreadyChosenNodes) {
		Link mostLikelyLink = null;
		for(int i = 0; i < allNodes.size(); i++) {
			Link currentLink = matrix[currentNode.index][i];
			if(alreadyChosenNodes.contains(currentLink.endNode) || equivalentNodeInList(currentLink.endNode, bestNodes))
				continue;
			if(mostLikelyLink == null || currentLink.weight > mostLikelyLink.weight) {
				mostLikelyLink = currentLink;
			}
		}
		if(mostLikelyLink == null)
			return getTonic();
		else
			return mostLikelyLink.endNode;
	}
	
	boolean equivalentNodeInList(Node node, ArrayList<Node> listOfNodes) {
		for(Node node2 : listOfNodes) {
			if(node.noteName == node2.noteName)
				return true;
		}
		return false;
	}
	
	void calculateNetworkStatistics() {
		calculateAllIntervalProbabilities();
		calculateAllNextIntervalProbabilities();
		calculateDistanceFromTonicStats();
		calculateDistanceToCadenceStats();
	}
	
	private double getNextIntervalProbability(String key) {
		if(nextIntervalProbabilities.containsKey(key))
			return nextIntervalProbabilities.get(key);
		else
			return 0;
	}
	
	private void calculateAllIntervalProbabilities() {
		ArrayList<Double> allIntervalProbabilities = new ArrayList<Double>();
		addBlanks(allIntervalProbabilities, 12);
		
		for(Song song : corpus) {			
			for(int i=0; i < 12; i++) {
				//double intervalProbability = 1.0*getSingleIntervalFrequencyFromSong(i, song)/song.countNotes();
				//allIntervalProbabilities.set(i, allIntervalProbabilities.get(i) + intervalProbability);
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
					//double intervalProbability = 1.0*getIntervalFrequencyAfterScaleDegreeFromSong(scaleDegree, interval, song)/song.countScaleDegreeOccurences(scaleDegree);
					String key = scaleDegree+"->"+interval;
					//double intervalTotal = map.get(key) + intervalProbability;
					//if(!Double.isNaN(intervalTotal))
						//map.put(key, intervalTotal);
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
		for(Node currentNode : allNodes) {
			allTempos.add((int) currentNode.bpm);
		}
		return allTempos;
	}
	
	private int getIntervalFrequencyAfterScaleDegreeFromSong(int scaleDegree, int interval, Song song) {
		int count = 0;
		//for(ArrayList<Integer> channel : song.getAllScaleDegrees()) {
			//count += getIntervalFrequencyAfterScaleDegree(scaleDegree, interval, channel);
		//}
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
		//for(ArrayList<Integer> channel : song.getAllScaleDegrees()) {
			//count += getSingleIntervalFrequencyFromChannel(interval, channel);
		//}
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
		double average = Composer.getAverage(list);
		double maxValue = Composer.getValueFarthestFromAverage(average, list);
		return getNormalizedDistanceFromAverage(value, average, maxValue);
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
