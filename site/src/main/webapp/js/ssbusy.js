(function(){

////////// signon/signin dialog ///////////
var fancySigninOptions = {
    maxWidth    : 940,
    maxHeight   : 570,
    fitToView   : false,
    width       : '100%',
    height      : '100%',
    autoSize    : true,
    closeClick  : false,
    topRatio    : 0,
    openEffect  : 'none',
    closeEffect : 'none',
    type        : 'ajax',
    autoCenter  : true,
    padding     : 5
};

var cartFancySigninOptions = {
	    maxWidth    : 600,
	    maxHeight   : 570,
	    fitToView   : false,
	    width       : '100%',
	    height      : '100%',
	    autoSize    : true,
	    closeClick  : false,
	    topRatio    : 0,
	    openEffect  : 'none',
	    closeEffect : 'none',
	    type        : 'ajax',
	    autoCenter  : true,
	    padding     : 5
	};
$('.sel-region').click(function() {
    var extendedOptions = $.extend({ href : $(this).attr('href') }, fancySigninOptions);
    openFancyBox(extendedOptions);
    return false;
});
function openFancyBox(opts) {
	if(opts.href) {
		opts.href += (opts.href.indexOf('?')<0?'?':'&') + 'blcAjax=true';
	}
	$.fancybox.open(opts);
}
$('.account .signon,.account .signin').click(function() {
    var extendedOptions = $.extend({ href : $(this).attr('href') }, fancySigninOptions);
    openFancyBox(extendedOptions);
    return false;
});
$('.cart').click(function() {
    var extendedOptions = $.extend({ href : $(this).attr('href') }, cartFancySigninOptions);
    openFancyBox(extendedOptions);
    return false;
});

/**
 * 延迟hover效果
 */
(function (){
	var hoverShowSwitch = function (container, showed, hide, pre) {
		if(hide) { // 隐藏掉
			container.removeClass('active');
			showed.fadeOut(100,function(){});
			// container.trigger('mouseleave', true);
		} else {
			$('.hover-showed-abs,.hover-show1ed-abs').fadeOut(20);
			// container.trigger('mouseenter', true);
			container.siblings().removeClass('active');
			container.addClass('active');
			showed.fadeIn(80);
		}
		showed.removeData('hoverTimer'+pre);
	};
	$('.hover-showed').addClass('hover-showed-abs').hide().removeClass('hover-showed');
	$('.hover-show1ed').addClass('hover-show1ed-abs').hide().removeClass('hover-show1ed');
	var onMouseEnter = function(e, pre, move) {
		e.stopImmediatePropagation();
		var target = $(e.target);
		if(!target.hasClass(pre))
			target = target.parents('.'+pre);
		var showed = target.find('.'+pre+'ed-abs');

		if(move && showed.is(":visible"))
			return;

		var hoverTimer = showed.data('hoverTimer'+pre);
		if(hoverTimer) {
			clearTimeout(hoverTimer);
			showed.removeData('hoverTimer'+pre);
		}
		if(showed.is(":visible"))
			return;
		showed.data('hoverTimer'+pre, setTimeout(function(){hoverShowSwitch(target, showed,false,pre);},20));
	};
	var onMouseLeave = function(e, pre) {
		e.stopImmediatePropagation();
		var target = $(e.target);
		if(!target.hasClass(pre))
			target = target.parents('.'+pre);
		var showed = target.find('.'+pre+'ed-abs');
		var hoverTimer = showed.data('hoverTimer'+pre);
		if(hoverTimer) {
			clearTimeout(hoverTimer);
			showed.removeData('hoverTimer'+pre);
		}
		if(!showed.is(":visible"))
			return;
		showed.data('hoverTimer'+pre, setTimeout(function(){hoverShowSwitch(target, showed, true,pre);},800));
	};
	$('.hover-show')
		.mouseenter(function(e){onMouseEnter(e, 'hover-show');})
		.mousemove(function(e){onMouseEnter(e, 'hover-show', true);})
		.mouseleave(function(e){onMouseLeave(e, 'hover-show');});
	$('.hover-show1')
		.mouseenter(function(e){onMouseEnter(e, 'hover-show1');})
		.mousemove(function(e){onMouseEnter(e, 'hover-show1', true);})
		.mouseleave(function(e){onMouseLeave(e, 'hover-show1');});
})();

$('body').on('change', 'input.updateQuantity', function() {
    var $form = $(this).closest('form');
    BLC.ajax({url: $form.attr('action'),
            type: "get", 
            data: $form.serialize() 
        }, function(data, extraData) {
            if (extraData) {
				if(extraData.error=="InventoryUnavailable"){
						data = "InventoryUnavailable,please get more next time !";
				}else{
					updateHeaderCartItemsCount(extraData.cartItemCount);
					if ($form.children('input.updateQuantity').val() == 0) {
						showAddToCartButton(extraData.productId, 'cart');
					}
				}
            }
            $('.fancybox-inner').html(data);
        }
    );
    return false;
  });

$('body').on('click', 'a.remove_from_cart', function() {
    var link = this;
    BLC.ajax({url: $(link).attr('href'),
            type: "GET"
        }, function(data, extraData) {
            $('.fancybox-inner').html(data);
            updateHeaderCartItemsCount(extraData.cartItemCount);
        }
    );
    return false;
});

/*
var $customerRating = $('.customer-rating');
$('.customer-rating').each(function() {
    var customerRating = $(this).data('customer-rating') - 1;
    $(this).find('input.star').rating('select', customerRating).rating('disable');
});

$customerRating.each(function() {
    var customerRating = $(this).data('customer-rating') - 1;
    $(this).find('input.star').rating('select', customerRating).rating('disable');
});

var $communityRatingWidget = $communityRating.find('input.star');

*/

$('body').on('click', '.addToCart', function() {
    var $button = $(this),
    $form = $button.closest('form'),
    $errorSpan = $form.find('div.error');
    $successSpan = $button.children('div');
    
    BLC.ajax({url: $form.attr('action'), 
            type: "GET",
            dataType: "json",
            data: BLC.serializeObject($form)
        }, function(data, extraData) {
            if (data.error) {
            	var msg;
                if (data.error == 'allOptionsRequired') {
					
                	msg = data.errorMessage;;
                } else if (data.error == 'inventoryUnavailable') {
					
                	msg =data.errorMessage;
                } else if(data.errorMessage) {
                	msg = data.errorMessage;
                } else {
                	msg = data.errorMessage;;
                }
                $errorSpan.length > 0 ? $errorSpan.text(msg) : alert(msg);
            } else {
                $errorSpan.html('&nbsp;'); 
                $successSpan.attr("style", "display:inline;").animate({
            			top:"-40px",
            			right:0,
            			opacity: 0,
            			display: "none"
            		}, 1000 );
                updateHeaderCartItemsCount(data.cartItemCount);
            }
        }
    );
    return false;
});

})($);

function updateHeaderCartItemsCount(cartItemCount) {
	$('#cartItemCount').text(cartItemCount);
}


function minus(e) {
	var i = $(e).parent().children('input[type="number"]');
	var oq = i.val();
	var q = parseInt(i.val()) - 1;
	if(q < 1)q=1;
	if(q == 1){$(e).addClass('disabled');i.val(q);}
	else {$(e).removeClass('disabled');i.val(q);}
	if(oq!=q)i.trigger('change');
}
function plus(e) {
	var i = $(e).parent().children('input[type="number"]');
	var oq = i.val();
	var q = parseInt(i.val()) + 1;
	if(q < 1)q=1;
	if(q == 1){$(e).addClass('disabled');i.val(q);}
	else {$(e).parent().children().removeClass('disabled');i.val(q);}
	if(oq!=q)i.trigger('change');
}

$("#back-to-top").click(function(){
    $('body,html').animate({scrollTop:0},500);
  		return false;
 });
 
function skuSelect(e, sel) {
	e = $(e);
	$(sel).removeClass('active');
	var form = e.closest('form');
	form.find('#skuId').val(e.addClass('active').attr('sid'));
	form.find('#locationId').val(e.children('b').text());
	form.find('.price .has-sale').remove();
	form.find('.price .sale').text(e.children('ins').text());
	form.find('#inventoryCnt').text(e.children('s').text());
}

function regionSelect(e) {
	e = $(e);
    BLC.ajax({url: e.attr('href'),
        type: "get"
    }, function(data) {
    	window.location.reload();
    	return;
    	$('#curr-region').text(e.text());
    	var region_ajax = false;
    	$('.region_ajax').each(function(i, e){region_ajax = region_ajax || ('true' == e.value);});
		$.fancybox.close();
    });
    return false;
}
function reviewTheProduct(e){
	 BLC.ajax({url: $(e).attr('href'),
        type: "get"
        }, function(data){
		$(".review-box").html("");
        	$("#"+$(e).attr('data')).html(data);
			$("#"+$(e).attr('data')).addClass("success");
        });
	return false;
}
$('body').on('click','input.voteForMe',function(){
		var $form = $(this).closest("form");
        BLC.ajax({url: $form.attr('action'), 
                type: "get",
                data: $form.serialize()
            }, function(redata, extraData){
				if(redata){
					if(redata.info=="failed"){
						alert("请登录之后再投票。谢谢配合～～");
					}
					if(redata.info=="success"){
						$('.tabs .active.tab').trigger('mouseover');
						alert("投票成功了，谢谢合作～～");
					}
				}
            }
        );
	return false;
	})
 $('body').on('click','input.review_button', function() {
        var $form = $(this).closest("form");
        BLC.ajax({url: $form.attr('action'), 
                type: "POST",
                data: $form.serialize()
            }, function(redata, extraData){
           		$(".review-box").html("");
				if(redata){
    				if(redata.message == 'failed'){
    					$(".success").html("o(>﹏<)，哎呀，评论不能为空哟。").removeClass("success");
    				}else{
    					$(".success").html("( ^_^ )，恭喜你评论成功。").removeClass("success");
    				}
				}
            }
        );
        return false;
    });     

function addReview(e){
    var $button = $(e),
    $form = $button.closest('form');
    BLC.ajax({url: $form.attr('action'), 
            type: "POST",
            dataType: "json",
            data: BLC.serializeObject($form)
        }, function(data) {
         $('.fancybox-inner').html(data);
        }
    );
    return false;
}


$(document).ready(function(){
	if(document.location.href=="http://www.onxiao.com/"){
		$("#nav-follow").trigger("mouseover");
		setTimeout("$('#nav-follow').trigger('mouseout')",2000);
	}
});

$('div.weixin').mouseover(function(){
	$('div.qrcode_for_wexin').removeClass('qrcode_for_wexin').addClass('qrcode_for_wexin_block');
	
	
});
$('div.weixin').mouseleave(function(){
	$('div.qrcode_for_wexin_block').removeClass('qrcode_for_wexin_block').addClass('qrcode_for_wexin');
	
});

$('body').on('click', '.app_download', function() {
    var $button = $(this),
    $form = $button.parent('form');
    BLC.ajax({url: $form.attr('action'), 
            type: "POST",
			dataType:"json",
            data: BLC.serializeObject($form)
        }, function(data, extraData) {
            if (data.error) {
            	var msg;
                if (data.error == 'numberIsNull') {
                	msg = data.errorMessage;
                } else if (data.error == 'overMax') {
                	msg = data.errorMessage;
                } else if (data.error == 'numberUnmatch') {
                	msg = data.errorMessage;
                } else if(data.error == 'success') {
                	msg = data.errorMessage;
                }
                $(".response").html(msg);
            }
        }
    );
    return false;
});
/*!
 * jQuery Cookie Plugin v1.4.0
 * https://github.com/carhartl/jquery-cookie
 *
 * Copyright 2013 Klaus Hartl
 * Released under the MIT license
 */
(function(e){if(typeof define==="function"&&define.amd){define(["jquery"],e)}else{e(jQuery)}})(function(e){function n(e){return u.raw?e:encodeURIComponent(e)}function r(e){return u.raw?e:decodeURIComponent(e)}function i(e){return n(u.json?JSON.stringify(e):String(e))}function s(e){if(e.indexOf('"')===0){e=e.slice(1,-1).replace(/\\"/g,'"').replace(/\\\\/g,"\\")}try{e=decodeURIComponent(e.replace(t," "))}catch(n){return}try{return u.json?JSON.parse(e):e}catch(n){}}function o(t,n){var r=u.raw?t:s(t);return e.isFunction(n)?n(r):r}var t=/\+/g;var u=e.cookie=function(t,s,a){if(s!==undefined&&!e.isFunction(s)){a=e.extend({},u.defaults,a);if(typeof a.expires==="number"){var f=a.expires,l=a.expires=new Date;l.setDate(l.getDate()+f)}return document.cookie=[n(t),"=",i(s),a.expires?"; expires="+a.expires.toUTCString():"",a.path?"; path="+a.path:"",a.domain?"; domain="+a.domain:"",a.secure?"; secure":""].join("")}var c=t?undefined:{};var h=document.cookie?document.cookie.split("; "):[];for(var p=0,d=h.length;p<d;p++){var v=h[p].split("=");var m=r(v.shift());var g=v.join("=");if(t&&t===m){c=o(g,s);break}if(!t&&(g=o(g))!==undefined){c[m]=g}}return c};u.defaults={};e.removeCookie=function(t,n){if(e.cookie(t)!==undefined){e.cookie(t,"",e.extend({},n,{expires:-1}));return true}return false}});
function vswtch(newVer) {
	$.cookie('uiv2', newVer, {expires: 30, path: '/'});
	window.location.href='/';
}



$.fn.countdown=function(end_time){
	end_time = end_time.substring(0,19);
	var self = $(this);
	var reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/;
	var r = end_time.match(reg);
	if(r==null)return false;
	var d= new Date(r[1], r[3]-1,r[4],r[5],r[6],r[7]); 
	var sys_second = (d.getTime()-new Date().getTime())/1000;
	var day,hour,minute,second;
	var timer = setInterval(function(){
		sys_second = sys_second - 1;
		if (sys_second >= 0) {
			day = Math.floor((sys_second / 3600) / 24);
			hour = Math.floor((sys_second / 3600) % 24);
			minute = Math.floor((sys_second / 60) % 60);
			second = Math.floor(sys_second % 60);
			hour = (hour<10?"0"+hour:hour);
			minute = (minute<10?"0"+minute:minute);
			second = (second<10?"0"+second:second);
			self.text(day+":"+hour+":"+minute+":"+second);
		} else {
			clearInterval(timer);
		}
	}, 1000);
};
