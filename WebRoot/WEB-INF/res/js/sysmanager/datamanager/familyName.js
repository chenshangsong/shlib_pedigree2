$(function(){
	
    var init = function(){
        var languages = ['en','chs','cht'];
        $.each(familyNames, function(i){
            var familyName = familyNames[i];
            $.each(languages, function(j){
                $("div[char='" + familyName.char + "'] div[tag='" + languages[j] + "']").append("<a onClick='aClick(this)' auri='" + familyName.uri + "' aname='" + familyName[languages[j]] + "'   href='javascript:void(0);' uri='" + familyName.uri + "'>" + familyName[languages[j]] + "</a>");
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
    $.get(ctx + "/dataManager/personFamilyNames", function(data){
        $.each(data, function(i){
            var familyNames = $("a[uri='" + data[i].uri + "']").addClass("btn btn-primary btn-xs").css({"cursor":"pointer"});
            $.each(familyNames, function(j){
                var familyName = $(familyNames[j]);
                var char = familyName.parentsUntil("div.container", "div[char]").attr("char");
                var name = familyName.text();
                var uri = familyNames.attr("uri");
              /*  var parms = name;
                familyName.popover({
                    placement: 'right',
                    html: true,
                    trigger:'focus',
                    content: '<a class="thisLink" auri="'+uri+'" aname="'+name+'"  target=_blank onClick="aClick(this)" href="javascript:void(0)">选择</a></div>'
                });*/
            })
        });
    });
  
   
})