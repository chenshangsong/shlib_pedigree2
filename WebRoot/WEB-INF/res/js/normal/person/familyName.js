$(function(){
    var init = function(){
        var languages = ['en','chs','cht'];
        $.each(familyNames, function(i){
            var familyName = familyNames[i];
            $.each(languages, function(j){
            	//var familyNameChar = familyName.char.toLocaleLowerCase();
            	var familyNameChar = familyName.char.toUpperCase();
            	var strA = "<a href='javascript:void(0);' uri='" + familyName.uri + "'>" + familyName[languages[j]] + "</a>";
            	$("div[char='" + familyNameChar + "'] div[tag='" + languages[j] + "']").append(strA);
            });
        });
        $("div[tag]").each(function(i){
            var text = $.trim($(this).text());
            if( text == ""){
                $(this).text("暂无");
            }
        });
    };
    init();
    $("h2 .btn").click(function(){
        $("h2 .btn").removeClass("btn-primary");
        $(this).addClass("btn-primary");
        $("div[tag]").hide();
        $("div[tag='" + $(this).attr('data') + "']").fadeIn("slow");
    }).first().click();
    $.get(ctx + "/service/person/personFamilyNames", function(data){
        $.each(data, function(i){
            var familyNames = $("a[uri='" + data[i].uri + "']").addClass("btn btn-primary btn-xs").css({"cursor":"pointer"});
            $.each(familyNames, function(j){
                var familyName = $(familyNames[j]);
                var char = familyName.parentsUntil("div.container", "div[char]").attr("char");
                var name = familyName.text();
                var uri = familyNames.attr("uri");
                familyName.popover({
                    placement: 'right',
                    html: true,
                    trigger:'focus',
                    content: '<a target=_blank href="' + ctx + '/service/person/list#char=' + char + '&familyName=' + name + '">先祖名人</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href="' + ctx + '/service/work/list#familyName=' + name + '" target=_blank>家谱文献</a></div>'
                });
            })
        });
    });
})