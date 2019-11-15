package edu.brandeis.cs12b.pa11;

import java.util.ArrayList;
import java.util.List;

import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;

public class TweetCleaner {
	private String tweet;
	private static List<String> emojis = new ArrayList<String>();
	
	/**
	 * Constructor for TweetCleanr
	 * @param tweet the Tweet as a String
	 */
	public TweetCleaner(String tweet) {
		this.tweet = tweet;
	}
	
	public static List<String> getEmojis() {
		return emojis;
	}

	/**
	 * Cleans the Tweet
	 * @return the cleaned Tweet
	 */
	public String clean() {
		// makes an array out of the Tweet
		String[] tweetArr = tweet.split(" ");
		
		// removes "RT", tags (@....), links (http://....), and empty spaces
		for (int i = 0; i < tweetArr.length; i++) {
			String toEval = tweetArr[i].trim();
			if (toEval.contains("RT") || toEval.contains("@") || toEval.contains("https://") || tweetArr[i].equals(" ")) tweetArr[i] = null;
			else {
				emojis.addAll(EmojiParser.extractEmojis(toEval));
				tweetArr[i] = EmojiParser.removeAllEmojis(toEval);
			}
		}
		
		return toNewString(tweetArr);
	}
	
	public String toNewString(String[] tweetArr) {
		// builds the clean Tweet as a new String
		String cleanTweet = "";
		for (String s: tweetArr) {
			if (s != null) cleanTweet += s + " ";
		}
		
		return cleanTweet.trim();
	}

}
