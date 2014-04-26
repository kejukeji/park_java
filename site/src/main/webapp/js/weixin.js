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
				//updateHeaderCartItemsCount(extraData.cartItemCount);
				if ($form.children('input.updateQuantity').val() == 0) {
					//showAddToCartButton(extraData.productId, 'cart');
				}
			}
        }
        $('#cart-info').html(data);
		$('#all_numb').text(extraData.cartItemCount);
        }
    );
    return false;
});
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

