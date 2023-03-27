$(function(){
    var init = function(){
        var languages = ['en','chs','cht'];
        $.each(familyNames, function(i){
            var familyName = familyNames[i];
            $.each(languages, function(j){
            	//var familyNameChar = familyName.char.toLocaleLowerCase();
            	var familyNameChar = familyName.char.toUpperCase();
            	var uri =ctx+'/familyNameManager/dataContentlist?id='+familyName.uri;
            	var strA = "<a href='"+uri+"'  uri='" + familyName.uri + "'>" + familyName[languages[j]] + "</a>";
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
})