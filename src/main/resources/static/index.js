var host = "http://localhost:8081"

$(function(){
	// 获取视频列表
	function get_list(){
		$.ajax({
			type:'get',
			url:host+"/api/v1/video/page?size=30&page1",
			dataType:'json',
			success:function(res){
				var data = res.data;
				// console.info(data)
				
				for(var i = 0; i< data.length; i++){
					// console.log(data[i])
					var video = data[i];
					var price = video.price/100;
					var template = "<div class='col-sm-6 col-md-3'><div class='thumbnail'>"+
					"<img src='video_cover.png' alt='通用的占位符缩略图'>"+
					"<div class='caption'><h3>"+video.title+"</h3><p>价格:"+price+"元</p>"+
					"<p><a href='' onclick='save_order("+video.id+")' data-toggle='modal' data-target='#myModal' class='btn btn-primary' role='button'>立刻购买</a></p></div></div></div>";
					
					$(".row").append(template);
				}
			}
		})
	}

	// 获取微信扫码地址
	function get_wechat_login() {
		// 获取当前页面地址
		var current_page = window.location.href;

		$.ajax({
			type:'get',
			url:host+"/api/v1/wechat/login_url?access_page="+current_page,
			dataType:'json',
			success:function(res){
				console.info(res.data)
			}
		})
	}
	
	get_list();
	get_wechat_login();
})