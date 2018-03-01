package composition;

import java.util.HashMap;

public class CorpusAnalyzer {
	
	// Calculated from Key2 - Key1
	private HashMap<Integer, Integer> intervalCounts = new HashMap<Integer, Integer>();
	
	// Form 0,1,2,3...
	private HashMap<String, Integer> scaleDegreeNGramCounts = new HashMap<String, Integer>();
	
	// Form A,B,C#,D...
	private HashMap<String, Integer> noteNameNGramCounts = new HashMap<String, Integer>();
	
	// Form 1/2,1/4,1/8,1/16,1/3, ...
	private HashMap<String, Integer> durationNGramCounts = new HashMap<String, Integer>();
	
	
	private HashMap<String,Integer> intervalVocab = new HashMap<String,Integer>();
	private String totalIntervalCount;
	
	private HashMap<String,Integer> scaleDegreeVocab = new HashMap<String,Integer>();
	private HashMap<String,Integer> noteNameVocab = new HashMap<String,Integer>();
	private HashMap<String,Integer> durationVocab = new HashMap<String,Integer>();
	
	public void addToIntervalCount(int interval) {
		collectNewVocabulary(Integer.toString(interval) ,intervalVocab);
		if(intervalCounts.containsKey(interval)) {
			intervalCounts.put(interval, intervalCounts.get(interval) + 1);
		}
		else {
			intervalCounts.put(interval, 1);
		}
	}
	
	public void addToScaleDegreeNGramCount(String nGram) {
		collectNewVocabulary(nGram, scaleDegreeVocab);
		if(scaleDegreeNGramCounts.containsKey(nGram)) {
			scaleDegreeNGramCounts.put(nGram, scaleDegreeNGramCounts.get(nGram) + 1);
		}
		else {
			scaleDegreeNGramCounts.put(nGram, 1);
		}
	}
	
	public void addToNoteNameNGramCount(String nGram) {
		collectNewVocabulary(nGram, noteNameVocab);
		if(noteNameNGramCounts.containsKey(nGram)) {
			noteNameNGramCounts.put(nGram, noteNameNGramCounts.get(nGram) + 1);
		}
		else {
			noteNameNGramCounts.put(nGram, 1);
		}
	}
	
	public void addToDurationNGramCount(String nGram) {
		collectNewVocabulary(nGram, durationVocab);
		if(durationNGramCounts.containsKey(nGram)) {
			durationNGramCounts.put(nGram, durationNGramCounts.get(nGram) + 1);
		}
		else {
			durationNGramCounts.put(nGram, 1);
		}
	}
	
	private void collectNewVocabulary(String nGram, HashMap<String,Integer> vocabList) {
		String[] words = nGram.split(",");
		if(words.length > 2)
			return;
		
		for(String word : words) {
			if(vocabList.containsKey(word))
				vocabList.put(word, vocabList.get(word) + 1);
			else
				vocabList.put(word, 1);
		}
	}
	
	/**
	 * Returns 1 if the interval hasn't been seen to allow for playing of intervals that haven't been seen
	 */
	private int getIntervalCount(int interval) {
		if(intervalCounts.containsKey(interval))
			return intervalCounts.get(interval);
		else
			return 0;
	}
	
	private int getScaleDegreeNGramCount(String nGram) {
		if(scaleDegreeNGramCounts.containsKey(nGram))
			return scaleDegreeNGramCounts.get(nGram);
		else
			return 0;
	}
	
	private int getNoteNameNGramCount(String nGram) {
		if(noteNameNGramCounts.containsKey(nGram))
			return noteNameNGramCounts.get(nGram);
		else
			return 0;
	}
	
	private int getDurationNGramCount(String nGram) {
		if(durationNGramCounts.containsKey(nGram))
			return durationNGramCounts.get(nGram);
		else
			return 0;
	}
	
	public double getIntervalProbability(int interval) {
		return 1.0 * (getIntervalCount(interval)+1) / (sumAllCounts(intervalCounts)+intervalVocab.size()) ;
	}
	
	public double getScaleDegreeNGramProbability(String key) {
		String subKey = constructSubkey(key);
		if(subKey.length() == 1)
			return 1.0 * (getScaleDegreeNGramCount(key)+1) / (scaleDegreeVocab.get(subKey)+scaleDegreeVocab.size());
		return 1.0 * (getScaleDegreeNGramCount(key)+1) / (getScaleDegreeNGramCount(subKey)+scaleDegreeVocab.size());
	}
	
	public double getNoteNameNGramProbability(String key) {
		String subKey = constructSubkey(key);
		if(subKey.length() == 1)
			return 1.0 * (getNoteNameNGramCount(key)+1) / (noteNameVocab.get(subKey)+noteNameVocab.size());
		return 1.0 * (getNoteNameNGramCount(key)+1) / (getNoteNameNGramCount(subKey)+noteNameVocab.size());
	}
	
	public double getDurationNGramProbability(String key) {
		String subKey = constructSubkey(key);
		if(subKey.length() == 1)
			return 1.0 * (getDurationNGramCount(key)+1) / (durationVocab.get(subKey)+durationVocab.size());
		return 1.0 * (getDurationNGramCount(key)+1) / (getDurationNGramCount(subKey)+durationVocab.size());
	}
	
	private int sumAllCounts(HashMap map) {
		if(totalIntervalCount != null)
			return Integer.parseInt(totalIntervalCount);
		int total = 0;
		for(Object value : map.values()) {
			if(value instanceof Integer) {
				total += ((Integer) value).intValue();
			}
		}
		totalIntervalCount = Integer.toString(total);
		return total;
	}
	
	// Returns an nGram which is 1 less than the provided key
	private String constructSubkey(String key) {
		String[] parts = key.split(",");
		StringBuilder subKey = new StringBuilder();
		for(int i = 0; i < parts.length-2; i++) {
			subKey.append(parts[i] + ",");
		}
		subKey.delete(parts.length-2, parts.length-2);
		return subKey.toString();
	}
}
