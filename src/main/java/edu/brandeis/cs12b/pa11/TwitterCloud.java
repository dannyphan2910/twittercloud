package edu.brandeis.cs12b.pa11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TwitterCloud {
	private Map<String, Integer> frequencyMap;

	/**
	 * Create a word cloud!
	 * 
	 * @param args an array of strings to use as a filter for incoming Tweets
	 * @param filename the filename of the image file you should create for your word cloud
	 * @param numberTokens the number of tokens you should extract from the tweets
	 */
	public void makeCloud(String[] args, String filename, int numberTokens) {
		// Retrieves Tweets from Twitter and store them into a List of tweets
		TweetRetriever retriever = new TweetRetriever(args, numberTokens);
		List<String> tweets = retriever.getTweets();
		
		// Tokenizes the tweets and store them into a List of tokens
		Tokenizer tokenizer = new Tokenizer(tweets);
		List<String> tokens = tokenizer.getTokens();
		
		// Make a WordCloud from the tokens
		WordCloudMaker WCMaker = new WordCloudMaker(tokens, filename);
		this.frequencyMap = WCMaker.getFrequencyMap();
		WCMaker.createWordCloud();
		
		viewStats(numberTokens, 5);
	}
	
	private void viewStats(int numberTokens, int maxEmoji) {
		StatisticsAnalyzer.setCount(numberTokens);
		System.out.println(StatisticsAnalyzer.evalEmoji(maxEmoji));
		System.out.println(StatisticsAnalyzer.evalLocation());
		System.out.println(StatisticsAnalyzer.getTotalFollowersCount());
		System.out.println(StatisticsAnalyzer.getTotalTweetsCount());
	}

	/**
	 * This method will only be called after makeCloud
	 * @return a list of all tokenized words from your word cloud
	 */
	public List<String> getWords(){
		return new ArrayList<String>(frequencyMap.keySet());
	}
}
