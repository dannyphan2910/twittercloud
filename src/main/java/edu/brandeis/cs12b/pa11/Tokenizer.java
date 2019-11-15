package edu.brandeis.cs12b.pa11;

import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class Tokenizer {
	private List<String> tweets;
	private List<String> tokens;
	
	/**
	 * Constructor for Tokenizer
	 * @param tweets the List of Tweets
	 */
	public Tokenizer(List<String> tweets) {
		this.tweets = tweets;
		this.tokens = new LinkedList<String>();
		tokenize();
	}
	
	/**
	 * Returns the List of tokens
	 * @return the List of tokens
	 */
	public List<String> getTokens() {
		return this.tokens;
	}
	
	/**
	 * Tokenizes all the Tweets
	 */
	private void tokenize() {
		for (String tweet: tweets) {
			tokenizeTweet(tweet);
		}
	}
	
	/**
	 * Tokenizes the Tweet using Lucene API and adds it to the List of tokens
	 * @param tweet a Tweet (in the form of a String)
	 */
	private void tokenizeTweet(String tweet) {
		try (EnglishAnalyzer an = new EnglishAnalyzer()) {
			TokenStream sf = an.tokenStream(null, tweet);
			try {
				sf.reset();
				while (sf.incrementToken()) {
					CharTermAttribute cta = sf.getAttribute(CharTermAttribute.class);
					//Add finished tokens to the list.
					tokens.add(cta.toString());
				}
			} catch (Exception e) {
				System.err.println("Could not tokenize string: " + e);
			}
		}
	}
}
