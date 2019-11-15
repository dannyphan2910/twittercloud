package edu.brandeis.cs12b.pa11;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import wordcloud.CollisionMode;
import wordcloud.LayeredWordCloud;
import wordcloud.PolarBlendMode;
import wordcloud.PolarWordCloud;
import wordcloud.WordCloud;
import wordcloud.WordFrequency;
import wordcloud.bg.CircleBackground;
import wordcloud.bg.RectangleBackground;
import wordcloud.font.FontWeight;
import wordcloud.font.scale.LinearFontScalar;
import wordcloud.font.scale.SqrtFontScalar;

public class WordCloudMaker {
	private String filename;
	private List<String> tokens;
	
	private Map<String, Integer> frequencyMap;
	private List<WordFrequency> wordFreqList;
	
	/**
	 * Constructor for WordCloudMaker
	 * @param tokens the List of tokens
	 * @param filename the name of the File to write in
	 */
	public WordCloudMaker(List<String> tokens, String filename) {
		// loads in the data
		this.filename = filename;
		this.tokens = tokens;
		
		this.frequencyMap = new HashMap<String, Integer>();		
		this.wordFreqList = new LinkedList<WordFrequency>();
		
		// creates the frequency map and then the List of WordFrequency from the data
		makeFrequencyMap();
		toWordFrequencyList();
	}
	
	/**
	 * Creates the word cloud using Kumo API
	 */
	public void createWordCloud() {
		// This can be used to create your word cloud.
		WordCloud wordCloud = new WordCloud(400, 400, CollisionMode.RECTANGLE);
		wordCloud.setPadding(0);
		wordCloud.setBackground(new CircleBackground(200));
		wordCloud.setFontScalar(new LinearFontScalar(14, 40));
		wordCloud.build(wordFreqList);
		wordCloud.writeToFile(filename);
	}
	
	/**
	 * Returns the frequency map
	 * @return the frequency map
	 */
	public Map<String, Integer> getFrequencyMap() {
		return this.frequencyMap;
	}
	
	/**
	 * Creates the frequency map from the List of tokens
	 */
	private void makeFrequencyMap() {
		for (String token: tokens) {
			if (frequencyMap.containsKey(token)) frequencyMap.put(token, frequencyMap.get(token) + 1);
			else frequencyMap.put(token, 1);
		}
	}
	
	
	/**
	 * Creates the List of WordFrequency from the frequency map
	 */
	private void toWordFrequencyList() {
		for (String word: frequencyMap.keySet()) {
			wordFreqList.add(new WordFrequency(word, frequencyMap.get(word)));
		}
	}
}
