# Twitter Cloud

This is my final assignment for COSI 12B (Advanced Programming Techniques) where I can study further about using *API* and *Object-Oriented Programming in Java*. The task is to design a program that:
1. Collects Tweets from Twitter
2. Parses those tweets for tokens
3. Uses those tokens to calculate how frequently certain terms appear in the tweets 
4. Creates a word cloud with that data, and saves it to an image file

In this project, I use 4 APIs:
1. [Hosebird](https://github.com/twitter/hbc), a Java API for interacting with Twitter that will allow me to pull down recent tweets containing certain terms.
2. [JSON.org's JSON parser](https://www.json.org/json-en.html), which will process what I get from Twitter through the Hosebird API.
3. [Apache Lucene](https://lucene.apache.org/), a Java search and indexing service that provides many low-level tools for analyzing natural language. I use it to tokenize each tweet.
4. [Kumo](https://github.com/kennycason/kumo), a Java API for generating word clouds.

I took a creative step by making:
1. **TweetCleaner**: removes "RT", tags (@....), links (http://....), and empty spaces from each tweet content 
2. **StatisticsAnalyzer**: uses the tweets, the *location*, *followers_count*, and *statuses_count* to estimate the percentage of US citizens from the tweets, to report the total followers count and the total statuses count, and to evaluate the list of emojis to report the top most used ones.

**A Twitter Cloud example:**

![example twitter cloud](https://github.com/dannyphan2910/twittercloud/blob/master/test.png)

To replicate/experiment with this project:
1. Get your CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET from Twitter: developer.twitter.com then replace it (or set environment variables for the JUnit test file (in src/test/java) - I used Eclipse for this Java project) in the TweetRetriever.java file (in src/main/java)
2. Experiment by changing the line *tc.makeCloud(new String[] {"miley", "cyrus" }, "test.png", 4000);* in the JUnit test file to whatever *key words*, *name of .png file*, and/or *number of tweets* you desire.
