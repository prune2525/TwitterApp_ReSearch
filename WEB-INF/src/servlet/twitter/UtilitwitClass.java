package servlet.twitter;


import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class UtilitwitClass {

	static public Twitter getOAuthedTwitterObject(String CONSUMERKEY, String CONSUMERSECRET, String ACCESSTOKEN, String ACCESSSECRET){
		Twitter twitter = null;
		ConfigurationBuilder confbuilder = new ConfigurationBuilder();

		confbuilder.setOAuthConsumerKey(CONSUMERKEY);
		confbuilder.setOAuthConsumerSecret(CONSUMERSECRET);
		confbuilder.setOAuthAccessToken(ACCESSTOKEN);
		confbuilder.setOAuthAccessTokenSecret(ACCESSSECRET);

		TwitterFactory twitterfactory = new TwitterFactory(confbuilder.build());

		twitter = twitterfactory.getInstance();
		return twitter;
	}


	static public TwitterStream getOAuthedTwitterStreamObject(String CONSUMERKEY, String CONSUMERSECRET, String ACCESSTOKEN, String ACCESSSECRET){
		TwitterStream twitter = null;
		ConfigurationBuilder confbuilder = new ConfigurationBuilder();

		confbuilder.setOAuthConsumerKey(CONSUMERKEY);
		confbuilder.setOAuthConsumerSecret(CONSUMERSECRET);
		confbuilder.setOAuthAccessToken(ACCESSTOKEN);
		confbuilder.setOAuthAccessTokenSecret(ACCESSSECRET);

		TwitterStreamFactory twitterfactory = new TwitterStreamFactory(confbuilder.build());

		twitter = twitterfactory.getInstance();
		return twitter;
	}

}