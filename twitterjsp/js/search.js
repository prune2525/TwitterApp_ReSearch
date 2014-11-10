function memo(keyword){
	console.log('../test?query=' + keyword);
	//これはAjaxのための呪文
	$.ajax({
		//通信方法
	    type: 'get',
	    //通信をするURL（相対でも絶対でもOK）
	    url: '../test?query=' + keyword,
	    success: function(data){
	    	//通信が成功した時の処理
	    	//alert("検索が成功しました");
	    },
		error: function(){
			//通信が失敗した時の処理
	    	alert("検索が失敗しました");
		}
	});
}