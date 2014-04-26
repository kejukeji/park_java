/*get the dormitory*/
function getDormitory() {
	var area_id = $("#area_id").val();
	$.ajax({
		type : "get",
		url : "/checkout/getdormitory",
		data : {
			area_id : area_id
		},
		dataType : "html"
	}).done(
			function(dormitory_res) {
				var dormitory_tag = $("#dormitory_id")[0];
				dormitory_tag.options.length = 0;
				var dormitory_obj = eval("(" + dormitory_res + ")");
				if(dormitory_obj.length==1){
					dormitory_tag.options[0] = new Option(
							dormitory_obj[0].dormitoryName,
							dormitory_obj[0].dormitoryId);
							$("#display-none").addClass("display-none");
							$("#extend-width").addClass("extend-width");
				}else{
    				for ( var k = 0; k < dormitory_obj.length; k++) {
    					dormitory_tag.options[k] = new Option(
    							dormitory_obj[k].dormitoryName,
    							dormitory_obj[k].dormitoryId);
    				}
					$("#display-none").removeClass("display-none");
					$("#extend-width").removeClass("extend-width");
				}
			});
}

/* product like js */

function tagLike(a) {
	var pid = $(a).attr("data");
	$.ajax({
		type : "get",
		url : "/customer/like",
		data : {id : pid}
	}).done(function(flag) {
		var aa=$(a);
		var d=aa.find('div');
		if (flag=="true") {
			aa.addClass('active');
		    d.html(" &nbsp;+1");
		    d.attr("style", "display:inline;color:red");
		}else{
			aa.removeClass('active');
		    d.html(" &nbsp;-1");
		    d.attr("style", "display:inline;color:lime");
		}
		d.animate({
			top:"-40px",
			opacity: 0,
			display: "none"
		}, 1000 );
	});
	return false;
}

function switchmodTag(modtag,modcontent,modk) {
	var i=0;
	for(i=1; i< 4; i++) {
	  if (i==modk) {
		document.getElementById(modtag+i).className="menuOn";
		document.getElementById(modcontent+i).className="grid-100";}
	  else {
		document.getElementById(modtag+i).className="menuNo";
		document.getElementById(modcontent+i).className="slidingList_none";}
	}
}
function christmasRecharge(){
	var card_id = $(".cart_id").text();
	var card_password = $(".password").text();
	var addString = '<div class="santa_claus sign_notice"><span class="left_lead"></span><span class="notice_word">';
	$.ajax({
		type : "get",
		url : "/app/account/rechargeSubmit",
		data : {
			re_number: card_id,
			re_pwd:card_password
		},
		dataType : "html"
	}).done(function(data) {
			data = eval('('+data+')');
			if(data.errorMessage){
				addString = addString +data.errorMessage;
			}else if(data.sucessMessage){
				addString = addString +"充值成功了~开心ing";
			}else{
				addString = addString+'出问题了？不开心。';
				alert("sdfa");
			}
			addString = addString+'</span><span class="right_leader"></span></div>';
		$('.activity_rules').html(addString);
		});
}
