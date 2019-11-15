package edu.brandeis.cs12b.pa11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

public class TweetRetriever {
	private String[] toSearch;
	private int numberTweets;
	private List<String> tweets;
	
	/**
	 * Constructor for TweetRetriever
	 * @param toSearch the search terms
	 * @param numberTweets the number of Tweets to retrieve
	 */
	public TweetRetriever(String[] toSearch, int numberTweets) {
		this.toSearch = toSearch;
		this.numberTweets = numberTweets;
		this.tweets = new LinkedList<String>();
		retrieveTweets();
	}
	
	/**
	 * Gets the Tweets
	 * @return the List of tweets
	 */
	public List<String> getTweets() {
		return this.tweets;
	}
	
	/**
	 * Retrieves the Tweets from Twitter
	 */
	private void retrieveTweets() {
		disableDebugInfo();

		// This would select specific tweets from twitter with any of the words given
		List<String> terms = new ArrayList<String>();
		for (String s: toSearch) terms.add(s);

		// Stores incoming tweet data in a BlockingQueue
		// A BlockingQueue handles waiting for requests (from the internet, in this case)
		BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(100000);

		Client hosebirdClient = buildClient(terms, msgQueue);
		
		// Connects to the HoseBird API to retrieve Tweets from Twitter
		hosebirdClient.connect();
		addTweets(msgQueue);
		hosebirdClient.stop();
	}
	
	/**
	 * Builds the client using HoseBird API
	 * @param terms the search terms
	 * @param msgQueue the BlockingQueue to stores incoming tweet data
	 * @return the client (after authentication and more)
	 */
	private Client buildClient(List<String> terms, BlockingQueue<String> msgQueue) {
		// Directs HoseBird to the right place
		Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
		StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
		
		hosebirdEndpoint.trackTerms(terms);

		// Adds authentication
		Authentication hosebirdAuth = new OAuth1(
				System.getenv("CONSUMER_KEY"),
				System.getenv("CONSUMER_SECRET"),
				System.getenv("TOKEN"),
				System.getenv("TOKEN_SECRET"));
		
		// Connects to service and start watching for the terms of interest
		ClientBuilder builder = new ClientBuilder()
				.hosts(hosebirdHosts)
				.authentication(hosebirdAuth)
				.endpoint(hosebirdEndpoint)
				.processor(new StringDelimitedProcessor(msgQueue));

		Client hosebirdClient = builder.build();
		
		return hosebirdClient;
	}
	
	/**
	 * Disables HoseBird API debugging information printing
	 */
	private void disableDebugInfo() {
		// HoseBird uses a logging system that (by default) will print out debugging information. 
		// This loop will turn that off
		BasicConfigurator.configure();
		List<Logger> loggers = Collections.<Logger>list(LogManager.getCurrentLoggers());

		loggers.add(LogManager.getRootLogger());
		for ( Logger logger : loggers ) {
			logger.setLevel(Level.OFF);
		}
	}
	
	/**
	 * Adds the cleaned tweets to the List
	 * @param msgQueue the BlockingQueue containing incoming tweets data
	 */
	private void addTweets(BlockingQueue<String> msgQueue) {
		// Until we've gather enough tokens, parses each tweet from Twitter's JSONObject, cleans it, and adds it to the List
		while (tweets.size() < numberTweets) {
			String msg = "";
			try {
				msg = msgQueue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (msg != "" && msg != null) {
				evalTweet(msg);
			}
			
		}
	}
	
	/**
	 * Extract the Tweet and User Info
	 * @param msg the Twitter's JSON representation of a Tweet
	 */
	private void evalTweet(String msg) {
		String token = "";
		// Parses the String from Twitter's JSONObject, adds the cleaned version to the List
		try {
			token = new JSONObject(msg).getString("text");
			tweets.add(new TweetCleaner(token).clean());
		} catch (JSONException e) {
			// for {limit} statements
		}
		
		// Passes user information to the Statistics method
		try {
			JSONObject user = new JSONObject(msg).getJSONObject("user");
			searchUserStat(user);
		} catch (JSONException e) {
			// for {limit} statements
		}	
	}
	
	/**
	 * Gathers User Info 
	 * @param user the JSONObject with user info
	 */
	private void searchUserStat(JSONObject user) {
		// location info
		try {
			String location = user.getString("location");
			StatisticsAnalyzer.addLocation(location);
		} catch (JSONException e) {
			// for "null" locations
		} 
		
		// numbers info
		StatisticsAnalyzer.addFollowersCount(user.getInt("followers_count"));
		StatisticsAnalyzer.addTweetsCount(user.getInt("statuses_count"));
	}
}