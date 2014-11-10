<%@page import="twitter4j.conf.Configuration"%>
<%@page import="twitter4j.conf.ConfigurationBuilder"%>
<%@page import="servlet.db.UserSession"%>
<%@page import="servlet.db.DBOP"%>
<%@page import="twitter4j.auth.AccessToken"%>
<%@page import="twitter4j.auth.RequestToken"%>
<%@page import="twitter4j.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ja">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>MuteSearcher verson.β</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="description" content="">
  <meta name="author" content="">
  	<!--link rel="stylesheet/less" href="less/bootstrap.less" type="text/css" /-->
	<!--link rel="stylesheet/less" href="less/responsive.less" type="text/css" /-->
	<!--script src="js/less-1.3.3.min.js"></script-->
	<!--append ‘#!watch’ to the browser URL, then refresh the page. -->

  <link href="index/css/bootstrap.min.css" rel="stylesheet">
  <link href="index/css/style.css" rel="stylesheet">

  <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
  <!--[if lt IE 9]>
    <script src="js/html5shiv.js"></script>
  <![endif]-->

  <!-- Fav and touch icons -->
  <link rel="apple-touch-icon-precomposed" sizes="144x144" href="index/img/apple-touch-icon-144-precomposed.png">
  <link rel="apple-touch-icon-precomposed" sizes="114x114" href="index/img/apple-touch-icon-114-precomposed.png">
  <link rel="apple-touch-icon-precomposed" sizes="72x72" href="index/img/apple-touch-icon-72-precomposed.png">
  <link rel="apple-touch-icon-precomposed" href="index/img/apple-touch-icon-57-precomposed.png">
  <link rel="shortcut icon" href="index/img/favicon1.png" >

  <script type="text/javascript" src="index/js/jquery.min.js"></script>
  <script type="text/javascript" src="index/js/bootstrap.min.js"></script>
  <script type="text/javascript" src="index/js/scripts.js"></script>

</head>
<body>
	<div class="container">
		<div class="row clearfix">
			<div class="col-md-12 column">
			<h1>
				MuteSearcher verson.βにようこそ
			</h1>
				<h3>
					本Webアプリケーションは番組(アニメとかアニメとか)のネタバレツイートを自分のTimeLineから
					非表示させることを目的としたものです。
				</h3>
				<h3>
					本Webアプリケーションを利用するに当たり
					<li>不定期の更新が多々あります．</li>
					<li>タイムラインを閲覧するのが基本的な機能となっております．</li>
					<li>スマートフォンブラウザでも使えますが基本的にPCブラウザ向けです．</li>
					<li>返信、リツイート、お気に入りの各機能を付けました．</li>
					<li>タイムラインUIの変更</br>　①ツイートのurlを「Linkへ」に．</br>　②画像や動画のurlを「画像/動画」に．</li>
					<li>UI変更その2</br>　①非表示機能の枠を右上に移しました．</br>　②Reply専用の画面も作りました．</li>
					<li>その他は仕様ですのでご理解ください．</li>
					以上の件をご理解の上、利用してください．
				</h3>
				<h3>
					<a href="./src/howtouse.html">How to Use　←　簡単にですがよかったらご覧ください．</a>
				</h3>
<%
	//Twitter4j初期化
	Twitter twitter = new TwitterFactory().getInstance();
	twitter.setOAuthConsumer("Hl01c6RWUt9NcoFb8wsDLg", "XtSPHaiT7e0byAE7H2zseIeaKG0kEztdn7he4qo4V0");

	String command = request.getParameter("command");

	if("authorize".equals(command)){
		//OAuth認可.リクエストトークンを取得
		RequestToken requestToken = twitter.getOAuthRequestToken(request.getRequestURL() + "?command=callback");
		session.setAttribute("requestToken", requestToken);
		response.sendRedirect(requestToken.getAuthenticationURL());

	}else if("callback".equals(command)){
		//Twitterからコールバックを受け取った
		//アクセストークンを取得してセッションに格納
		AccessToken accessToken = twitter.getOAuthAccessToken((RequestToken)session.getAttribute("requestToken")
									,request.getParameter("oauth_verifier"));
		session.setAttribute("accessToken", accessToken);

		//DBOP
		UserSession us = new UserSession();
		us.insert(accessToken);

		//リクエストトークンは不要になったので破棄
		session.removeAttribute("requestToken");
	}
	if(null != session.getAttribute("accessToken")){
		//アクセストークンがセッションに存在するのでOAuth認可済み
		response.sendRedirect("./top.jsp");
	}else{
%>
				<button type="button" onclick="location.href='?command=authorize'" class="btn btn-lg btn-primary">
					SignIn
				</button>
<%
	}
%>
			</div>
		</div>
		<a class="navbar-brand">MuteSearcher verson.β開発者アカウント
			<a href="https://twitter.com/mli_space" class="navbar-brand">@mli_space</a>
		</a>
	</div>
</body>
</html>