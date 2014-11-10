package servlet.twitter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.reduls.igo.Morpheme;
import net.reduls.igo.Tagger;
import servlet.anal.Analysis;
import servlet.db.DataInsert;
import servlet.db.KeyInsert;
import servlet.twitter.UtilitwitClass;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.auth.AccessToken;

@WebServlet(urlPatterns = { "/Search" })
public class Search extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		HttpSession session = request.getSession();
		AccessToken accessToken = null;
		
		response.setContentType("text/html");
		
		//keyword[#タグ]
		String str;
		
		//アクセストークンが接書に存在するのでOAuth認可済み
		if(null != session.getAttribute("accessToken")){
			try {
				//web.xml呼び出し
				ServletContext application = this.getServletContext();
				//AccessToken取得
				accessToken = (AccessToken) session.getAttribute("accessToken");
				
				String ACCESSTOKEN = accessToken.getToken();
				String ACCESSSECRET = accessToken.getTokenSecret();
				String CONSUMERKEY = application.getInitParameter("CONSUMERKEY");
				String CONSUMERSECRET = application.getInitParameter("CONSUMERSECRET");
				
				//Twitter認証部分呼び出し
				Twitter twitter = UtilitwitClass.getOAuthedTwitterObject(
						CONSUMERKEY, 
						CONSUMERSECRET, 
						ACCESSTOKEN, 
						ACCESSSECRET
					);
				
				//キーワード取得
				str = URLDecoder.decode(request.getParameter("query"), "UTF-8");
				
				//DB insert keyword table
				KeyInsert keyInsert = new KeyInsert();
				keyInsert.insert(accessToken, str);
				
				//クエリ宣言
				Query query = new Query();
				
				//クエリに単語と検索数をセット
				query.setQuery(str);
				query.setCount(100);
				
				//デバック用にリスト分け
				ArrayList<String> tweetList = new ArrayList<String>();
				ArrayList<User> userList = new ArrayList<User>();
				ArrayList<Date> dateList = new ArrayList<Date>();

				//検索
				QueryResult qResult = twitter.search(query);
				
				//検索結果
				for(Status tweet : qResult.getTweets()){
					tweetList.add(tweet.getText());
					userList.add(tweet.getUser());
					dateList.add(tweet.getCreatedAt());
				}
				
				//検索件数表示
				System.out.println("hit count : " + tweetList.size());
				
				for(int j=0; j<tweetList.size(); j++){
					if(j%100 == 0){
						System.out.println("----------------検索結果-------------------");
					}
					String twee = tweetList.get(j);
					
					//形態素解析
					Tagger tagger = new Tagger("C:\\workspace\\TomTwitest\\ipadic");
					
					List<Morpheme> list = tagger.parse(twee);
					
					List<Analysis> analList = new ArrayList<Analysis>();
					
					for(Morpheme morpheme : list){
						twee = morpheme.surface + "\t" + morpheme.feature;
						
						Analysis analysis = new Analysis();
						analysis.setHashtag(str);
						analysis.setMorpheme(morpheme);
						analList.add(analysis);
					}
					//DB insert data table
					DataInsert dataInsert = new DataInsert();
					dataInsert.insert(analList);
				}
				logger.info(str);
				System.out.println("検索ヒット数 : " + tweetList.size());
				System.out.println("end");
				//ここまできたら成功。
				response.getWriter().write("success");
			} catch (TwitterException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
	}
}
