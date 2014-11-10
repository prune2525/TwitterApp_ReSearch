<%@page import="twitter4j.Status"%>
<%@page import="servlet.twitter.UtilitwitClass"%>
<%@page import="twitter4j.TwitterFactory"%>
<%@page import="twitter4j.Twitter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ja">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Mute Searcher version.β</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="description" content="">
  <meta name="author" content="">

	<!--link rel="stylesheet/less" href="less/bootstrap.less" type="text/css" /-->
	<!--link rel="stylesheet/less" href="less/responsive.less" type="text/css" /-->
	<!--script src="js/less-1.3.3.min.js"></script-->
	<!--append ‘#!watch’ to the browser URL, then refresh the page. -->

	<link href="top/css/bootstrap.min.css" rel="stylesheet">
	<link href="top/css/style.css" rel="stylesheet">

  <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
  <!--[if lt IE 9]>
    <script src="js/html5shiv.js"></script>
  <![endif]-->

  <!-- Fav and touch icons -->
  <link rel="apple-touch-icon-precomposed" sizes="144x144" href="top/img/apple-touch-icon-144-precomposed.png">
  <link rel="apple-touch-icon-precomposed" sizes="114x114" href="top/img/apple-touch-icon-114-precomposed.png">
  <link rel="apple-touch-icon-precomposed" sizes="72x72" href="top/img/apple-touch-icon-72-precomposed.png">
  <link rel="apple-touch-icon-precomposed" href="top/img/apple-touch-icon-57-precomposed.png">
  <link rel="shortcut icon" href="top/img/favicon1.png">

	<script type="text/javascript" src="top/js/jquery.min.js"></script>
	<script type="text/javascript" src="top/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="top/js/scripts.js"></script>
	<script type="text/javascript" src="//platform.twitter.com/widgets.js"></script>


<!-- Ajaxにはこれが必要 -->
	<script type="text/javascript" src="./js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript">

var timer1;
var normal;
var normal2;
var normal3;
var count;

//非同期通信[ホームタイムライン]
$(function(){
	$('#htimeline').load('../TimeLine');
	//180秒(3分)に一回更新 90秒(1分30秒)に変更
	normal = setInterval(function(){
		$('#htimeline').load('../TimeLine');
		console.log("タイムラインの更新");
	}, 90 * 1000);
});

//ボタン処理と文字エンコード
$(function(){
	$('#key_sendB').click(function(){
		var keyword  = "" + $("#keyid").val();
		var enc = encodeURIComponent(keyword);
		count = 0;
		tag_search(enc);

		//指定した時間ごとに関数を呼ぶ
		timer1 = setInterval("tag_search("+"\""+enc+"\""+");", 60 * 1000);
	})
});

//Searchしたあと成功したらmTimeLineを動かす処理
function select(enc){
	$.ajax({
		//通信方法
		type: 'get',
		//通信をするURL（相対でも絶対でもOK）
		url: '../mTimeLine',
		data: {
			'query': enc
		},
		success: function(data){
			console.log(data);
			clearInterval(normal);
			//通信が成功した時の処理
			$("#htimeline").html(data);
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
            console.log("XMLHttpRequest : " + XMLHttpRequest.status);
            console.log("textStatus : " + textStatus);
            console.log("errorThrown : " + errorThrown.message);
		}
	});
}
<!-- 検索するための関数 -->
function tag_search(enc){
	//これはAjaxのための呪文
	$.ajax({
		//通信方法
	    type: 'get',
	    //通信をするURL（相対でも絶対でもOK）
	    url: '../Search',
	    data: {
			'query': enc
		},
	    success: function(data){
	    	//通信が成功した時の処理
	    	//alert(data);
	    	if(data == "success"){
	    		//SELECT文を実行
	    		select(enc);
	    	}
	    },
	    error: function(XMLHttpRequest, textStatus, errorThrown) {
            console.log("XMLHttpRequest : " + XMLHttpRequest.status);
            console.log("textStatus : " + textStatus);
            console.log("errorThrown : " + errorThrown.message);
            //通信が失敗した時の処理
	    	if(XMLHttpRequest.status != 200) alert("検索が失敗しました");
         }
	});
	count++;
	if(count >= 30){
		//alert("処理が終わりました。");
		alert("ネタばらし防止が終わったよ。");
		clearInterval(timer1);

		$('#htimeline').load('../TimeLine');
		//180秒(3分)に一回更新 90秒(1分30秒)に変更
		normal3 = setInterval(function(){
			$('#htimeline').load('../TimeLine');
			console.log("タイムラインの更新");
		}, 90 * 1000);
	}
}

//非同期通信[リプライ]
$(function(){
	$('#mtimeline').load('../Mention');
	//180秒(3分)に一回更新 90秒(1分30秒)に変更
	normal2 = setInterval(function(){
		$('#mtimeline').load('../Mention');
		console.log("リプライの更新");
	}, 90 * 1000);
});

	</script>

</head>

<body>
	<div class="container">
		<div class="row clearfix">
			<div class="col-md-12 column">
				<nav class="navbar navbar-default navbar-fixed-top navbar-inverse" role="navigation">



					<div class="navbar-header">
						<div>
							<a href="../Logout">
								<button type="button" class="btn btn-xs btn-danger">ログアウト</button>
							</a>
						</div>
						<a class="navbar-brand">Mute Searcher version.β</a>
						<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
							<span class="sr-only">Toggle navigation</span><span class="icon-bar"></span>
							<span class="icon-bar"></span><span class="icon-bar"></span>
						</button>
					</div>
					<br>
					<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
						<div class="form-group" align="right">
	 						<input id="keyid" type="text" value = "#" name="keyword" >
	 						<a href="javascript: void(0)" id="key_sendB" >
			 				<button type="submit" class="btn btn-default">Search</button>
			 				</a>
						</div>
					</div>
				</nav>
			</div>
		</div>
		<br><br><br><br>
<%
	//アクセストークンが接書に存在するのでOAuth認可済み
	if(null != session.getAttribute("accessToken")){
%>
		<div class="row clearfix">
			<div class="col-md-12 column">
			<!--
				<h3>
					<a href="https://docs.google.com/forms/d/1dKR-TqOC3CHRFi32oFAAy0DnEebwhAtU98_ez0GLJoc/viewform">
						アンケートにご協力ください
					</a>
				</h3>
			-->
				<!-- Twitter TweetButton -->
				<div class="row clearfix">
					<div class="col-md-4 column">
						<a href="https://twitter.com/intent/tweet?text" class="navbar-brand">
						<button type="button" class="btn btn-primary">Tweet</button>
						</a>
					</div>
					<div class="col-md-4 column">
					<!-- 画像<img alt="75x75" src="http://lorempixel.com/75/75/" class="img-thumbnail" align="right"> -->
					</div>
					<div class="col-md-4 column">
						<h3>
							<!-- 名前 -->
						</h3>
					</div>
				</div>
				<br><br>
				<div class="tabbable" id="tabs-832833">
					<ul class="nav nav-tabs">
						<li class="active">
							<a href="#panel-404795" data-toggle="tab">Time Line</a>
						</li>
						<li>
							<a href="#panel-551687" data-toggle="tab">Reply</a>
						</li>
					</ul>
					<div class="tab-content">
						<div class="tab-pane active" id="panel-404795">
							<!-- タイムライン一覧 -->
								<div id="htimeline">取得中～ヾ（＊´∀｀＊）ﾉ</div>
						</div>
						<div class="tab-pane" id="panel-551687">
							<!-- リプライ一覧 -->
								<div id="mtimeline">取得中～ヾ（＊´∀｀＊）ﾉ</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row clearfix">
			<div class="col-md-12 column">
				<nav class="navbar navbar-default navbar-fixed-bottom navbar-inverse" role="navigation">
					<div class="navbar-header">
						 <a class="navbar-brand">Appllication Developer :
						 	<a href="https://twitter.com/mli_space" class="navbar-brand">@mli_space</a>
						 </a>
					</div>
				</nav>
			</div>
		</div>
<%
	}else{
%>
	<a href="./index.jsp">サインインする</a>
<%
	}
%>
	</div>
</body>
</html>