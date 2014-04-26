// login
$('.signon').click(function(){
	$.ajax({
		type : "get",
		url : "/login",
		dataType : "html"
	}).done(function(data) {
		   
			$('div.login_box').html(data);
			var windowwidth = $(window).width()+100;
			var widthbox = $(window).width()-149;
			var marginleft = (windowwidth - $('div.login_box').width()-100)/2;
			$('.v2').css({position:'absolute',overflow:'hidden',border:'100%',width:'1200px'});
			$('.v2').animate({left:-widthbox-15},{duration : 700,easing : 'easeOutBack'});
			$('.sha_pic').css({display:'block'});
			$('.register-container').css({'margin-top':($(window).height()-457)/2+10});
			$('div.login_box').css({position:'absolute',left:widthbox+150,top:'0px',width:widthbox+"px","margin-left":marginleft-150+'px'});
	});
	return false;
}); 
// register
$$.register=function(){
	$.ajax({
		type : "get",
		url : "/register",
		dataType : "html"
	}).done(function(data) {
			$('div.register_box').show().html(data);
			var windowwidth = $(window).width()+100;
			var widthbox = $(window).width()-149;
			var marginleft = (windowwidth - $('div.register_box').width()-100)/2;
			$('div.register_box').css({position:'absolute',left:"1200px",top:'0px',width:widthbox+"px","margin-left":marginleft+'px'});
			$('.v2').css({position:'absolute',overflow:'hidden',border:'100%',width:'1200px'});
			$('.register-container').css({'margin-top':($(window).height()-457)/2+10});
			$('.sha_pic').css({display:'block'});
			$('.v2').animate({left:-widthbox-15},{duration : 700,easing : 'easeOutBack'});
	});
	return false;
}; 
$$.showErrorMessage=function(code){
	var msg;
	if('password.invalid'==code){
		msg='密码不对哦';
	}else if('emailAddress.required'==code){
		msg='用户名(邮箱)不能为空哈';
	}else if('password.required'==code||'passwordConfirm.required'==code){
		msg='密码不能为空哈';
	}else if('login'==code){
		msg='用户名或密码错误，请重试';
	}else if('emailAddress.used'==code){
		msg='该用户已注册，请重试或直接登录';
	}else{
		msg='操作错误，请重试：'+code;
	}
	$('.left_pic').hide();
	$('.left_error_pic').show().text(msg);
};

$$.checkProtocol=function(o){
	if($(o).find('span.checkout-protocol').hasClass('checkedprotocol')){
		$(o).find('span.checkout-protocol').removeClass('checkedprotocol');
	}else{
		$(o).find('span.checkout-protocol').addClass('checkedprotocol');
	}
};
$$.tangregister=function(o){
	$('.left_error_pic').hide();
	var windowwidth = $(window).width()+100;
	var box=$('div.register_box');
	var marginleft = box.length>0?(windowwidth - box.width()-100)/2:0;
	var slipaway=null;
	var roles = $(o).attr("data");
	var slippicin = roles+'_pic';
	// 得到当前的角色
	if($('.register-container').hasClass('tang-div')){
			slipaway = "tang";
	}else if($('.register-container').hasClass('sun-div')){
		slipaway = "sun";
	}else if($('.register-container').hasClass('zhu-div')){
		slipaway = "zhu";
	}else if($('.register-container').hasClass('sha-div')){
		slipaway = "sha";
	}else if($('.register-container').hasClass('zixia-div')){
		slipaway = "zixia";
	}
	if(roles==slipaway){
		return false;
	}
	//  让线和右边的不动 
	$('.register-line').css({position:'absolute',left:$(window).width()/2-marginleft+"px"});
	$('.register-fix').css({position:'absolute',left:$(window).width()/2-marginleft+1+"px"});
	$('.'+slipaway+'_pic').css({display:'none'});
	// 左侧图片划入
	$('.'+slippicin).css({position:'absolute',left:'-1000px',display:'block'});
	$('.'+slippicin).animate({left:0},{duration : 1000,easing : 'easeOutBounce'});
	//  登录框 右边划入
	$('.register-fix').css({position:'absolute',left:$(window).width()+'px'});
	$('.register-container').addClass(roles+'-div');
	$('.register-container').removeClass(slipaway+'-div');
	$('.register-fix').animate({left:$(window).width()/2-marginleft+1+"px"},{duration : 1000,easing : 'easeOutBounce'});
	// 下面按钮
	$('.'+slipaway).removeClass('activity');
	$('.'+roles).addClass('activity');
};