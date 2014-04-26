/* Events to trigger on every page load */
$('#customer_like_display').click(function(){
		alert("xx");
		var xmlhttp;
		if (window.XMLHttpRequest)
		  {// code for IE7+, Firefox, Chrome, Opera, Safari
		  xmlhttp=new XMLHttpRequest();
		  }
		else
		  {// code for IE6, IE5
		  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		  }
		
		xmlhttp.onreadystatechange=function()
		  {
			alert(xmlhttp.readyState);
		  if (xmlhttp.readyState==4 && xmlhttp.status==404)
		    {
			  alert(xmlhttp.readyState);
		    }
		  }
		
		xmlhttp.open("POST","http://localhost:8080/like?id=1",true);
		xmlhttp.send();
	})

$(function(){
    
    // options for mobile image swiping
    var $imgs = $('#product_thumbs');
    var IMG_WIDTH = 320;
    var currentImg = 0;
    var maxImages = $imgs.find('li').length;
    var speed = 250;
    var swipeOptions = {
            triggerOnTouchEnd   : false,    
            swipeStatus         : swipeStatus,
            allowPageScroll     : "vertical",
            threshold           : 40            
    }

    // Update the locale that has been selected
    HC.updateLocaleSelection();

    // Show the JavaScript version of product options if the user has JavaScript enabled
    $('.product-options').removeClass('hidden');
    $('.product-option-nonjs').remove();

    // Bind the JavaScript product option boxes to execute on click
    $('body').on('click', '.product-option-group li:not(.unavailable)', function() {
        HC.changeProductOption($(this));
        return false;
    });
    if (Modernizr.touch) {
        $imgs.swipe(swipeOptions);
    } else {
        $('.jqzoom').jqzoom({zoomType: 'innerzoom', title: false});
    }
    
    $('ul#products li .content').dotdotdot(); // trim product descriptions in the small layout
    $('ul#featured_products li .content').dotdotdot(); // trim product descriptions in the small layout

    /**
     * Catch each phase of the swipe.
     * move : we drag the div.
     * cancel : we animate back to where we were
     * end : we animate to the next image
     */         
    function swipeStatus(event, phase, direction, distance) {
        
        //If we are moving before swipe, and we are going Lor R in X mode, or U or D in Y mode then drag.
        if (phase == "move" && (direction == "left" || direction == "right")) {
            var duration = 0;
            if (direction == "left") {
                scrollImages((IMG_WIDTH * currentImg) + distance, duration);
            } else if (direction == "right") {
                scrollImages((IMG_WIDTH * currentImg) - distance, duration);
            }
        } else if ( phase == "cancel") {
            scrollImages(IMG_WIDTH * currentImg, speed);
        } else if ( phase =="end" ) {
            if (direction == "right") {
                previousImage();
            } else if (direction == "left") {           
                nextImage();
            }
        }
        
    }
    
    function previousImage() {
        currentImg = Math.max(currentImg-1, 0);
        scrollImages( IMG_WIDTH * currentImg, speed);
    }
    
    function nextImage() {
        currentImg = Math.min(currentImg+1, maxImages-1);
        scrollImages( IMG_WIDTH * currentImg, speed);
    }
    
    /**
     * Manually update the position of the images on drag
     */
    function scrollImages(distance, duration) {
        $imgs.css("-webkit-transition-duration", (duration/1000).toFixed(1) + "s");
        //inverse the number we set in the css
        var value = (distance<0 ? "" : "-") + Math.abs(distance).toString();
        $imgs.css("-webkit-transform", "translate3d("+value +"px,0px,0px)");
    }  
});


