package composition;

import java.util.ArrayList;
import java.util.HashMap;

public class AssociationNetwork {
	ArrayList<Node> allNodes = new ArrayList<Node>();
	
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
	double noteLengthWeight = 0;
	
	double intervalContribution;
	double durationContribution;
	
	CorpusAnalyzer probabilities;
	
	double sameScaleDegreePenalty = 1.5;
	double sameDurationPenalty = 1.5;
	
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
		if((count % (allNodes.size()/10)) == 0) {
			int progress = roundUp(((double)count/allNodes.size())*100);
			if(progress != 0 && progress != 100)
				System.out.println(progress);
		}
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
		
		weight += noteLengthWeight/Composer.decodeDuration(end.noteDuration);
		
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
		return allNodes.get(0);
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
		ArrayList<Node> bestNodes = new ArrayList<>();
		ArrayList<Node> chosen = new ArrayList<>();
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
		if(mostLikelyLink == null) {
			alreadyChosenNodes.remove(0);
			alreadyChosenNodes.remove(0);
			return getTonic();
		}else
			return mostLikelyLink.endNode;
	}
	
	boolean equivalentNodeInList(Node node, ArrayList<Node> listOfNodes) {
		for(Node node2 : listOfNodes) {
			if(node.noteName == node2.noteName)
				return true;
		}
		return false;
	}
	
	private double getEquivalentValueWeight(boolean expression) {
		if(expression)
			return 1.0;
		else
			return 0.0;
	}
}
