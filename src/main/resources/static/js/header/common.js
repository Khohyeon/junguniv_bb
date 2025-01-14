$(window).scroll(function() {
	var scroll = $(window).scrollTop();

	if (scroll >= 5) {
		$("body").addClass("scroll");
	} else {
		$("body").removeClass("scroll");
	}
});

// gnb accordion
$(function() {
	$("nav ul.depth1 > li.open").children(".depth2").show();
	//$("nav ul.depth1 > li:first-child").addClass("open");

	var Accordion = function(el, multiple) {
		this.el = el || {};
		this.multiple = multiple || false;

		// Variables privadas
		var links = this.el.find('.link');
		// Evento
		links.on('click', {el: this.el, multiple: this.multiple}, this.dropdown)
	}

	Accordion.prototype.dropdown = function(e) {
		var $el = e.data.el;
			$this = $(this),
			$next = $this.next();

		$next.slideToggle();
		$this.parent().toggleClass('open');

		if (!e.data.multiple) {
			$el.find('.depth2').not($next).slideUp().parent().removeClass('open');
		};
	}	

	var accordion = new Accordion($('nav .depth1'), false);
});

$(document).ready(function(){
	// menu btn
	$("div.main-top div.left a.menu-btn").click(function(){
		$(this).toggleClass("close");
		$("div.main-wrap").toggleClass("close");
	});

	// 검색버튼
	$("div.main-top div.right div.btn-group > a.search").click(function(){
		$(this).toggleClass("open");
		$("div.search-form").toggleClass("open");
	});

	// tab 2023-06-09 이채림 추가
	$("div.jv-tab-cont-wrap div.jv-tab-cont").hide();
	$("div.jv-tab-cont-wrap div.jv-tab-cont:nth-child(1)").show();
	$("ul.jv-tab-list li").click(function() {
		$("ul.jv-tab-list li").removeClass('active');
		$(this).addClass('active');

		var thisTab = $(this).attr('rel');
		$("div.jv-tab-cont-wrap div.jv-tab-cont").hide();
		$("div.jv-tab-cont-wrap #" + thisTab).fadeIn(400);
	});

});