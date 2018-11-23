package test;




import java.util.List;
import java.util.logging.Logger;

import twitter4j.Paging;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.util.TimeSpanConverter;

//public class UserStream extends HttpServlet{
public class UserStream {
	/**
	 *
	 */
	//private static final long serialVersionUID = 1L;
	private static final String CONSUMER_KEY = "";
	private static final String CONSUMER_SECRET = "";
	private static final String ACCESS_TOKEN = "";
	private static final String ACCESS_TOKEN_SECRET = "";

	private static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	static class MyStatusListener implements StatusListener{
		@Override
		public void onStatus(Status status){
			String tweet = status.getText();
			long twid = status.getUser().getId();
			String user = status.getUser().getName();
			String scrn = status.getUser().getScreenName();
			String client = status.getSource();
			TimeSpanConverter times = new TimeSpanConverter();
			String time = times.toTimeSpanString(status.getCreatedAt());

			logger.info("Tweet:" +tweet + " Id:" + twid + " User:" + user + " Screen:" + scrn + " Client:" + client + " Time:" + time);
		}
		@Override
		public void onDeletionNotice(StatusDeletionNotice sdn){
//			System.out.println("onDeletionNotice.");
		}
		@Override
		public void onTrackLimitationNotice(int i){
//			System.out.println("onTrackLimitationNotice.("+i+")");
		}
		@Override
		public void onScrubGeo(long lat, long lng){
//			System.out.println("onScrubGeo.("+lat+","+lng+")");
		}
		@Override
		public void onException(Exception exception){
			System.out.println("onException.");
		}

		@Override
		public void onStallWarning(StallWarning arg0){

		}
	}
	//JavaServlect_Code
	/*@SuppressWarnings("null")
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		PrintWriter writer;
		Configuration configuration = new ConfigurationBuilder().setOAuthConsumerKey(CONSUMER_KEY)
				.setOAuthConsumerSecret(CONSUMER_SECRET)
				.setOAuthAccessToken(ACCESS_TOKEN)
				.setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET)
				.build();
		TwitterStream twitterStream = new TwitterStreamFactory(configuration).getInstance();
		twitterStream.addListener(new MyStatusListener());
		MyStatusListener myStatusListener = new MyStatusListener();
		Status status = null;
		myStatusListener.onStatus(status);
		String tweet = status.getText();
		try {
			writer = response.getWriter();
			writer.write(tweet);
			System.out.println(tweet);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}


		twitterStream.user();
	}*/

	public static void main(String[] args) throws Exception{
		Configuration configuration = new ConfigurationBuilder().setOAuthConsumerKey(CONSUMER_KEY)
				.setOAuthConsumerSecret(CONSUMER_SECRET)
				.setOAuthAccessToken(ACCESS_TOKEN)
				.setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET)
				.build();
		TwitterStream twitterStream = new TwitterStreamFactory(configuration).getInstance();
		twitterStream.addListener(new MyStatusListener());
		Twitter twitter = new TwitterFactory(configuration).getInstance();
		Paging page = new Paging();
		page.setCount(100);
		List<Status> statuses = twitter.getHomeTimeline(page);
		for(Status status : statuses){
			String tweet = status.getText();
			long twid = status.getUser().getId();
			String user = status.getUser().getName();
			String scrn = status.getUser().getScreenName();
			String client = status.getSource();
			TimeSpanConverter times = new TimeSpanConverter();
			String time = times.toTimeSpanString(status.getCreatedAt());

			logger.info("Tweet:" +tweet + " Id:" + twid + " User:" + user + " Screen:" + scrn + " Client:" + client + " Time:" + time);
		}

		//User Stream
		twitterStream.user();
	}
}
