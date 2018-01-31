package composition;

import java.util.HashMap;

public class CorpusAnalyzer {
	
	private HashMap<Integer, Integer> intervalCounts;
	private HashMap<String, Integer> scaleDegreeNGramCounts;
	private HashMap<String, Integer> durationNGramCounts;
	
	public void addToIntervalCount(int interval) {
		if(intervalCounts.containsKey(interval)) {
			intervalCounts.put(interval, intervalCounts.get(interval) + 1);
		}
		else {
			intervalCounts.put(interval, 1);
		}
	}
	
	public void addToScaleDegreeNGramCount(String nGram) {
		if(scaleDegreeNGramCounts.containsKey(nGram)) {
			scaleDegreeNGramCounts.put(nGram, scaleDegreeNGramCounts.get(nGram) + 1);
		}
		else {
			scaleDegreeNGramCounts.put(nGram, 1);
		}
	}
	
	public void addToDurationNGramCount(String nGram) {
		if(durationNGramCounts.containsKey(nGram)) {
			durationNGramCounts.put(nGram, durationNGramCounts.get(nGram) + 1);
		}
		else {
			durationNGramCounts.put(nGram, 1);
		}
	}
	
	/**
	 * Returns 1 if the interval hasn't been seen to allow for playing of intervals that haven't been seen
	 */
	private int getIntervalCount(int interval) {
		if(intervalCounts.containsKey(interval))
			return intervalCounts.get(interval);
		else
			return 1;
	}
	
	/**
	 * Returns 1 if the ngram hasn't been seen to allow for playing of combinations that haven't been seen
	 */
	private int getScaleDegreeNGramCount(String nGram) {
		if(scaleDegreeNGramCounts.containsKey(nGram))
			return scaleDegreeNGramCounts.get(nGram);
		else
			return 1;
	}
	
	/**
	 * Returns 1 if the ngram hasn't been seen to allow for playing of combinations that haven't been seen
	 */
	private int getDurationNGramCount(String nGram) {
		if(durationNGramCounts.containsKey(nGram))
			return durationNGramCounts.get(nGram);
		else
			return 1;
	}
	
	public double getIntervalProbability(int interval) {
		return 1.0 * getIntervalCount(interval) / sumAllCounts(intervalCounts);
	}
	
	public double getScaleDegreeNGramProbability(String key) {
		return 1.0 * getScaleDegreeNGramCount(key) / sumAllCounts(scaleDegreeNGramCounts);
	}
	
	public double getDurationNGramProbability(String key) {
		return 1.0 * getDurationNGramCount(key) / sumAllCounts(durationNGramCounts);
	}
	
	private int sumAllCounts(HashMap map) {
		int total = 0;
		for(Object value : map.values()) {
			if(value instanceof Integer) {
				total += ((Integer) value).intValue();
			}
		}
		return total;
	}
}
