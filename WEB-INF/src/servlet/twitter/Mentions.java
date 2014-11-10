package servlet.twitter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import servlet.twitter.UtilitwitClass;
import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;
import twitter4j.auth.AccessToken;
import twitter4j.util.TimeSpanConverter;



@WebServlet(urlPatterns = { "/Mention" })
public class Mentions extends HttpServlet{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		HttpSession session = request.getSession();
		AccessToken accessToken = null;

		//xss対策
		String[] escape = {"&", "<", ">", "\"", "\'", "\n", "\t"};
		String[] replace = {"&amp;", "&lt;", "&gt;", "&quot;", "&#39;", "<br>", "&#x0009;"};


		//アクセストークンが接書に存在するのでOAuth認可済み
		if(null != session.getAttribute("accessToken")){
			PrintWriter writer;
			try {
				//web.xml呼び出し
				ServletContext application = this.getServletContext();
				//AccessToken取得
				accessToken = (AccessToken) session.getAttribute("accessToken");

				String ACCESSTOKEN = accessToken.getToken();
				String ACCESSSECRET = accessToken.getTokenSecret();
				String CONSUMERKEY = application.getInitParameter("CONSUMERKEY");
				String CONSUMERSECRET = application.getInitParameter("CONSUMERSECRET");

				//Twitter
				Twitter twitter = UtilitwitClass.getOAuthedTwitterObject(
						CONSUMERKEY,
						CONSUMERSECRET,
						ACCESSTOKEN,
						ACCESSSECRET
					);

				response.setContentType("text/html; charset=UTF-8");
				writer = response.getWriter();

				Paging page = new Paging();
				page.setCount(50);

				List<Status> statuses = twitter.getMentionsTimeline(page);

				//writer.write("<br/>タイムライン一覧<br />");

				logger.info("リプライ一覧");
				//ツイートごとにスクリーン名とツイート本文を表示
				for(Status status : statuses){
					//ツイート
					String twtext = status.getText();

					//xss対策
					for(int i=0; i<escape.length; i++){
						twtext = twtext.replace(escape[i], replace[i]);
					}

					//ユーザ名
					String user = status.getUser().getName();
					//スクリーン名
					String suser = status.getUser().getScreenName();
					suser = suser.replaceAll(suser, "<a href=https://twitter.com/" + suser + ">" + "@" + suser + "</a>");
					//クライアント
					String client = status.getSource();
					//投稿時間
					TimeSpanConverter tsc = new TimeSpanConverter();
					String min = tsc.toTimeSpanString(status.getCreatedAt());
					//url
					URLEntity[] url = status.getURLEntities();
					//メディア
					MediaEntity[] murl = status.getMediaEntities();
					//hashtag
					HashtagEntity[] urs = status.getHashtagEntities();
					//スクリーン名クリック
					UserMentionEntity[] um = status.getUserMentionEntities();

					//テキスト内のURL
					for(URLEntity link : url){
						String tmp = link.getURL();
						twtext = twtext.replaceAll(tmp, "<a href=" + tmp + ">" + "Linkへアクセス" + "</a>");
					}
					//テキスト内のメディア
					for(MediaEntity media : murl){
						String tmedia = media.getURL();
						twtext = twtext.replaceAll(tmedia, "<a href=" + tmedia + ">" + "画像/動画" + "</a>");
					}
					//テキスト内のハッシュタグ
					for(HashtagEntity hash : urs){
						String has = hash.getText();
						twtext = twtext.replaceAll("#" + has, "<a href=https://twitter.com/search?q=%23"
						+URLEncoder.encode(has, "utf-8") + ">" + "#" + has + "</a>");
					}
					//テキスト内のスクリーン名
					for(UserMentionEntity ume : um){
						String atr = ume.getScreenName();
						twtext = twtext.replaceAll("@" + atr, "<a href=https://twitter.com/"
						+ atr + ">" + "@" + atr + "</a>");
					}
					System.out.println("@" + status.getUser().getScreenName() + "-" + twtext);
					//script
					writer.write("<script type="+"text/javascript"+"src="+"//platform.twitter.com/widgets.js"+"></script>");
					writer.write("<div style='width:100%; border:1px solid #ccc; font-size:16px'>");

					writer.write("<a href=https://twitter.com/" + status.getUser().getScreenName() + "><img src=" + status.getUser().getProfileImageURL() + "></a>"
					+ " " + user + suser + "</br>" + "&nbsp;" + twtext + " for " + client + " " + min +"</br>");
					writer.write("<div align="+"right"+">");
					writer.write("<a href="+"https://twitter.com/intent/tweet?in_reply_to="+status.getId()+"><button>"+"返信"+"</button></a>");
					writer.write("<a href="+"https://twitter.com/intent/retweet?tweet_id="+status.getId()+"><button>"+"RT"+"</button></a>");
					writer.write("<a href="+"https://twitter.com/intent/favorite?tweet_id="+status.getId()+"><button>"+"お気に入り"+"</button></a>");
					writer.write("</div>");
					writer.write("</div>");
				}
				writer.write("</br></br></br></br>");
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (TwitterException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
	}
}
