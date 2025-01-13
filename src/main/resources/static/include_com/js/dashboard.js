$(document).ready(function(){
	// 상세검색
	$("div.column-tc-wrap.hidden").hide();
	$("div.search-more-btn").click(function(){
		$("div.column-tc-wrap.hidden").slideToggle();
		$("div.form-wrap.search-more").toggleClass("active");
	});

	// 과정안내 탭
	$(".jv-tab-cont").hide();
    $(".jv-tab-cont:first").show();

    $("ul.jv-tab-list li").click(function() {
        $(".jv-tab-cont").hide();
        var activeTab = $(this).attr("rel"); 
        $("#"+activeTab).fadeIn();		
		
        $("ul.jv-tab-list li").removeClass("active");
        $(this).addClass("active");
    });

	// 출제방식 설정
	$("div.form-wrap2.exam-setting ul.list01.second-list li div.list-con").slick({
       	infinite:false,
		slidesToShow:3,
  		slidesToScroll:1,
    });

});