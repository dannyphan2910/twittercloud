package edu.brandeis.cs12b.pa11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsAnalyzer {
	private static List<String> location = new ArrayList<String>();
	private static Map<String, Integer> emojisFreq;
	private static int count = 0;
	private static int followersCount = 0;
	private static int tweetsCount = 0;

	public static void addLocation(String loc) { 
		location.add(loc); 
	}
	
	public static void setCount(int num) {
		count = num;
	}
	
	public static void addFollowersCount(int follow) {
		followersCount += follow;
	}
	
	public static void addTweetsCount(int tweets) {
		tweetsCount += tweets;
	}
	
	public static String getTotalFollowersCount() {
		return "The total numbers of followers of the users: " + followersCount;
	}
	
	public static String getTotalTweetsCount() {
		return "The total numbers of tweets of the users: " + tweetsCount;
	}
	
	/**
	 * Evaluates the locations to estimate the percentage of US citizens from the tweets 
	 * @return the String with information about the percentage of US citizens from the tweets 
	 */
	public static String evalLocation() {
		String[] states = {"AA","AE","AP","AL","AK","AS","AZ","AR","CA","CO","CT","DE","DC","FM","FL","GA","GU","HI","ID","IL","IN","IA","KS","KY","LA","ME","MH","MD","MA","MI","MN","MS","MO","MT","NE","NV","NH","NJ","NM","NY","NC","ND","MP","OH","OK","OR","PW","PA","PR","RI","SC","SD","TN","TX","UT","VT","VI","VA","WA","WV","WI","WY"};
		
		int USCount = 0;
		for (String loc: location) {
			String toEval = loc.trim();
			// valid: location contains "US"/"us"/"America"/"america" or ends with an abbreviation of a US State
			if (toEval.contains("US") || toEval.contains("us") || toEval.contains("America") || toEval.contains("america")) USCount++;
			else {
				for (String state: states) {
					if (toEval.endsWith(state)) {
						USCount++;
						break;
					}
				}
			}
		}
		
		return "Out of " + count + " tweets, estimated percentage of users from US States: " + (double) USCount * 100 / count + " %";
	}
	
	/**
	 * Evaluates the list of emojis to report the top most used ones
	 * @param topNum the top number of emojis wanted to report
	 * @return the String with information about the top most used emojis from the tweets
	 */
	public static String evalEmoji(int topNum) {
		String result = "Most popular emojis used: \n";
		emojisFreq = emojiFreq();
		
		List<Map.Entry<String, Integer>> entries = sortByValue();
		// if there are no entries, there're no emojis in the tweets
		if (entries.size() == 0) {
			return "No emojis detected";
		}
		// get the top most used emojis by looping through the SORTED list of map entries
		int i = 0;
		while (i < topNum && i < entries.size()) {
			Map.Entry<String, Integer> thisEntry = entries.get(i);
			result += thisEntry.getKey() + " => " + thisEntry.getValue() + " times \n";
			i++;
		}
		
		return result;
	}
	
	/**
	 * Sort the map by its value (in descending order)
	 * @return the List of map entries
	 */
	private static List<Map.Entry<String, Integer>> sortByValue() {
		List<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>(emojisFreq.entrySet());
		// Using comparator to compare the values of each map entry
		Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
				return e2.getValue().compareTo(e1.getValue());
			}
		});
		
		return entries;
	}
	
	/**
	 * Get a frequency map of each emoji by looping through the list of Emojis gathered by the TweetCleaner
	 * @return a frequency map of each emoji
	 */
	private static Map<String, Integer> emojiFreq() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		// loops through each emoji to get its frequency
		for (String emoji: TweetCleaner.getEmojis()) {
			if (map.containsKey(emoji)) map.put(emoji, map.get(emoji) + 1);
			else map.put(emoji, 1);
		}

		return map;
	}
}
