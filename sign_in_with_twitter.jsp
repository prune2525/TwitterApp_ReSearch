<%@page import="twitter4j.auth.AccessToken"%>
<%@page import="twitter4j.auth.RequestToken"%>
<%@page import="twitter4j.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	//Twitter4j初期化
	Twitter twitter = new TwitterFactory().getInstance();
	twitter.setOAuthConsumer("NyWI17FHybF8Y49nMnwaQ", "G0tJg73uMFqMihoxN7MDQsVkvmr3HA2ePUOgrZTPI");

	String command = request.getParameter("command");

	if("authorize".equals(command)){
		//OAuth認可　まずリクエストトークンを取得
		RequestToken requestToken = twitter.getOAuthRequestToken(request.getRequestURL() + "?command=callback");
		session.setAttribute("requestToken", requestToken);
		response.sendRedirect(requestToken.getAuthenticationURL());

	}else if("callback".equals(command)){
		//Twitterからコールバックを受け取った
		//アクセストークンを取得してセッションに格納
		AccessToken accessToken = twitter.getOAuthAccessToken((RequestToken)session.getAttribute("requestToken")
				,request.getParameter("oauth_verifier"));
		session.setAttribute("accessToken", accessToken);

		//リクエストトークンは不要(無効)になったので破棄
		session.removeAttribute("requestToken");

	}else if("tweet".equals(command)){
		//Tweetする
		twitter.setOAuthAccessToken((AccessToken)session.getAttribute("accessToken"));
		//request.setCharacterEncoding("UTF-8");
		//twitter.updateStatus(request.getParameter("tweet"));
		String tweet = request.getParameter("tweet");
		tweet = new String(tweet.getBytes("8859_1"),"UTF-8");
		twitter.updateStatus(tweet);
		out.print("ツイートしました.");

	}else if("logout".equals(command)){
		//セッションに格納されているアクセストークンを破棄してログアウト
		session.removeAttribute("accessToken");
	}
%>
<%
	if(null != session.getAttribute("accessToken")){
		//アクセストークンがセッションに存在するのでOAuth認可済み
%>
<form action="sign_in_with_twitter.jsp" method="POST">
	いまどうしてる？ <input type="text" name="tweet" size="50"/><br>
	<input type="hidden" name="command" value="tweet"/>
	<input type="submit" value="ツイート"/>
</form>
<a href="?command=logout">サインアウト</a>
<%
	}else{
%>
<a href="?command=authorize">サインインする</a>
<%
	}
%>
</body>
</html>