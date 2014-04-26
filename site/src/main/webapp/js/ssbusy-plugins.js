/*! http://responsiveslides.com v1.54 by @viljamis */
(function(c,I,B){c.fn.responsiveSlides=function(l){var a=c.extend({auto:!0,speed:500,timeout:4E3,pager:!1,nav:!1,random:!1,pause:!1,pauseControls:!0,prevText:"Previous",nextText:"Next",maxwidth:"",navContainer:"",manualControls:"",namespace:"rslides",before:c.noop,after:c.noop},l);return this.each(function(){B++;var f=c(this),s,r,t,m,p,q,n=0,e=f.children(),C=e.size(),h=parseFloat(a.speed),D=parseFloat(a.timeout),u=parseFloat(a.maxwidth),g=a.namespace,d=g+B,E=g+"_nav "+d+"_nav",v=g+"_here",j=d+"_on",w=d+"_s",k=c("<ul class='"+g+"_tabs "+d+"_tabs' />"),x={"float":"left",position:"relative",opacity:1,zIndex:2},y={"float":"none",position:"absolute",opacity:0,zIndex:1},F=function(){var b=(document.body||document.documentElement).style,a="transition";if("string"===typeof b[a])return!0;s=["Moz","Webkit","Khtml","O","ms"];var a=a.charAt(0).toUpperCase()+a.substr(1),c;for(c=0;c<s.length;c++)if("string"===typeof b[s[c]+a])return!0;return!1}(),z=function(b){a.before(b);F?(e.removeClass(j).css(y).eq(b).addClass(j).css(x),n=b,setTimeout(function(){a.after(b)},h)):e.stop().fadeOut(h,function(){c(this).removeClass(j).css(y).css("opacity",1)}).eq(b).fadeIn(h,function(){c(this).addClass(j).css(x);a.after(b);n=b})};a.random&&(e.sort(function(){return Math.round(Math.random())-0.5}),f.empty().append(e));e.each(function(a){this.id=w+a});f.addClass(g+" "+d);l&&l.maxwidth&&f.css("max-width",u);e.hide().css(y).eq(0).addClass(j).css(x).show();F&&e.show().css({"-webkit-transition":"opacity "+h+"ms ease-in-out","-moz-transition":"opacity "+h+"ms ease-in-out","-o-transition":"opacity "+h+"ms ease-in-out",transition:"opacity "+h+"ms ease-in-out"});if(1<e.size()){if(D<h+100)return;if(a.pager&&!a.manualControls){var A=[];e.each(function(a){a+=1;A+="<li><a href='#' class='"+w+a+"'>"+a+"</a></li>"});k.append(A);l.navContainer?c(a.navContainer).append(k):f.after(k)}a.manualControls&&(k=c(a.manualControls),k.addClass(g+"_tabs "+d+"_tabs"));(a.pager||a.manualControls)&&k.find("li").each(function(a){c(this).addClass(w+(a+1))});if(a.pager||a.manualControls)q=k.find("a"),r=function(a){q.closest("li").removeClass(v).eq(a).addClass(v)};a.auto&&(t=function(){p=setInterval(function(){e.stop(!0,!0);var b=n+1<C?n+1:0;(a.pager||a.manualControls)&&r(b);z(b)},D)},t());m=function(){a.auto&&(clearInterval(p),t())};a.pause&&f.hover(function(){clearInterval(p)},function(){m()});if(a.pager||a.manualControls)q.bind("click",function(b){b.preventDefault();a.pauseControls||m();b=q.index(this);n===b||c("."+j).queue("fx").length||(r(b),z(b))}).eq(0).closest("li").addClass(v),a.pauseControls&&q.hover(function(){clearInterval(p)},function(){m()});if(a.nav){g="<a href='#' class='"+E+" prev'>"+a.prevText+"</a><a href='#' class='"+E+" next'>"+a.nextText+"</a>";l.navContainer?c(a.navContainer).append(g):f.after(g);var d=c("."+d+"_nav"),G=d.filter(".prev");d.bind("click",function(b){b.preventDefault();b=c("."+j);if(!b.queue("fx").length){var d=e.index(b);b=d-1;d=d+1<C?n+1:0;z(c(this)[0]===G[0]?b:d);if(a.pager||a.manualControls)r(c(this)[0]===G[0]?b:d);a.pauseControls||m()}});a.pauseControls&&d.hover(function(){clearInterval(p)},function(){m()})}}if("undefined"===typeof document.body.style.maxWidth&&l.maxwidth){var H=function(){f.css("width","100%");f.width()>u&&f.css("width",u)};H();c(I).bind("resize",function(){H()})}})}})(jQuery,this,0);

(function ($) {
	// Monkey patch jQuery 1.3.1+ css() method to support CSS 'transform'
	// property uniformly across Safari/Chrome/Webkit, Firefox 3.5+, IE 9+, and Opera 11+.
	// 2009-2011 Zachary Johnson www.zachstronaut.com
	// Updated 2011.05.04 (May the fourth be with you!)
	function getTransformProperty(element)
	{
		// Try transform first for forward compatibility
		// In some versions of IE9, it is critical for msTransform to be in
		// this list before MozTranform.
		var properties = ['transform', 'WebkitTransform', 'msTransform', 'MozTransform', 'OTransform'];
		var p;
		while (p = properties.shift())
		{
			if (typeof element.style[p] != 'undefined')
			{
				return p;
			}
		}
		
		// Default to transform also
		return 'transform';
	}
	
	var _propsObj = null;
	
	var proxied = $.fn.css;
	$.fn.css = function (arg, val)
	{
		// Temporary solution for current 1.6.x incompatibility, while
		// preserving 1.3.x compatibility, until I can rewrite using CSS Hooks
		if (_propsObj === null)
		{
			if (typeof $.cssProps != 'undefined')
			{
				_propsObj = $.cssProps;
			}
			else if (typeof $.props != 'undefined')
			{
				_propsObj = $.props;
			}
			else
			{
				_propsObj = {}
			}
		}
		
		// Find the correct browser specific property and setup the mapping using
		// $.props which is used internally by jQuery.attr() when setting CSS
		// properties via either the css(name, value) or css(properties) method.
		// The problem with doing this once outside of css() method is that you
		// need a DOM node to find the right CSS property, and there is some risk
		// that somebody would call the css() method before body has loaded or any
		// DOM-is-ready events have fired.
		if
		(
			typeof _propsObj['transform'] == 'undefined'
			&&
			(
				arg == 'transform'
				||
				(
					typeof arg == 'object'
					&& typeof arg['transform'] != 'undefined'
				)
			)
		)
		{
			_propsObj['transform'] = getTransformProperty(this.get(0));
		}
		
		// We force the property mapping here because jQuery.attr() does
		// property mapping with jQuery.props when setting a CSS property,
		// but curCSS() does *not* do property mapping when *getting* a
		// CSS property.  (It probably should since it manually does it
		// for 'float' now anyway... but that'd require more testing.)
		//
		// But, only do the forced mapping if the correct CSS property
		// is not 'transform' and is something else.
		if (_propsObj['transform'] != 'transform')
		{
			// Call in form of css('transform' ...)
			if (arg == 'transform')
			{
				arg = _propsObj['transform'];
				
				// User wants to GET the transform CSS, and in jQuery 1.4.3
				// calls to css() for transforms return a matrix rather than
				// the actual string specified by the user... avoid that
				// behavior and return the string by calling jQuery.style()
				// directly
				if (typeof val == 'undefined' && jQuery.style)
				{
					return jQuery.style(this.get(0), arg);
				}
			}

			// Call in form of css({'transform': ...})
			else if
			(
				typeof arg == 'object'
				&& typeof arg['transform'] != 'undefined'
			)
			{
				arg[_propsObj['transform']] = arg['transform'];
				delete arg['transform'];
			}
		}
		
		return proxied.apply(this, arguments);
	};
})(jQuery);

/**
 * Monkey patch jQuery 1.3.1+ to add support for setting or animating CSS
 * scale and rotation independently.
 * https://github.com/zachstronaut/jquery-animate-css-rotate-scale
 * Released under dual MIT/GPL license just like jQuery.
 * 2009-2012 Zachary Johnson www.zachstronaut.com
 */
(function ($) {
	// Updated 2010.11.06
	// Updated 2012.10.13 - Firefox 16 transform style returns a matrix rather than a string of transform functions.  This broke the features of this jQuery patch in Firefox 16.  It should be possible to parse the matrix for both scale and rotate (especially when scale is the same for both the X and Y axis), however the matrix does have disadvantages such as using its own units and also 45deg being indistinguishable from 45+360deg.  To get around these issues, this patch tracks internally the scale, rotation, and rotation units for any elements that are .scale()'ed, .rotate()'ed, or animated.  The major consequences of this are that 1. the scaled/rotated element will blow away any other transform rules applied to the same element (such as skew or translate), and 2. the scaled/rotated element is unaware of any preset scale or rotation initally set by page CSS rules.  You will have to explicitly set the starting scale/rotation value.
	
	function initData($el) {
		var _ARS_data = $el.data('_ARS_data');
		if (!_ARS_data) {
			_ARS_data = {
				rotateUnits: 'deg',
				scale: 1,
				rotate: 0
			};
			
			$el.data('_ARS_data', _ARS_data);
		}
		
		return _ARS_data;
	}
	
	function setTransform($el, data) {
		$el.css('transform', 'rotate(' + data.rotate + data.rotateUnits + ') scale(' + data.scale + ',' + data.scale + ')');
	}
	
	$.fn.rotate = function (val) {
		var $self = $(this), m, data = initData($self);
						
		if (typeof val == 'undefined') {
			return data.rotate + data.rotateUnits;
		}
		
		m = val.toString().match(/^(-?\d+(\.\d*e?-?\d+)?)(.+)?$/);
		if (m) {
			if (m[3]) {
				data.rotateUnits = m[3];
			}
			
			data.rotate = m[1];
			
			setTransform($self, data);
		}
		
		return this;
	};
	
	// Note that scale is unitless.
	$.fn.scale = function (val) {
		var $self = $(this), data = initData($self);
		
		if (typeof val == 'undefined') {
			return data.scale;
		}
		
		data.scale = val;
		
		setTransform($self, data);
		
		return this;
	};

	// fx.cur() must be monkey patched because otherwise it would always
	// return 0 for current rotate and scale values
	var curProxied = $.fx.prototype.cur;
	$.fx.prototype.cur = function () {
		if (this.prop == 'rotate') {
			return parseFloat($(this.elem).rotate());
			
		} else if (this.prop == 'scale') {
			return parseFloat($(this.elem).scale());
		}
		
		return curProxied.apply(this, arguments);
	};
	
	$.fx.step.rotate = function (fx) {
		var data = initData($(fx.elem));
		$(fx.elem).rotate(fx.now + data.rotateUnits);
	};
	
	$.fx.step.scale = function (fx) {
		$(fx.elem).scale(fx.now);
	};
	
	var animateProxied = $.fn.animate;
	$.fn.animate = function (prop) {
		if (typeof prop['rotate'] != 'undefined') {
			var $self, data, m = prop['rotate'].toString().match(/^(([+-]=)?(-?\d+(\.\d+)?))(.+)?$/);
			if (m && m[5]) {
				$self = $(this);
				data = initData($self);
				data.rotateUnits = m[5];
			}
			
			prop['rotate'] = m[1];
		}
		
		return animateProxied.apply(this, arguments);
	};
})(jQuery);