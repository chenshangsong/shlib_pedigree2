$(function(){
    var drawModelGraph = function(className, uri){
        $.ajax({
            url : ctx + "/view/modelGraph",
            type : "post",
            data : {className : className, uri : uri},
            success : function(html){
                $("#div_graph").show();
                var autoHeight = function(){
                    var maxTop = 0;
                    $("svg>g[transform]>g").each(function(){
                        maxTop = Math.max($(this).offset().top, maxTop);
                    });
                    var height = maxTop - $("#div_graph").offset().top;
                    $("#div_graph svg").attr("height",height + "px");
                };
                $("#div_graph>.row").hide().html(html).fadeIn("slow", function(){
                    autoHeight();
                });
                autoHeight();
            }

        });
    };
    $("#table_class").on("click", "td a[data]", function(){
        var data = $(this).attr("data");
        $("#div_properties .panel-title strong").text(data + " Properties");
        $("#div_graph .panel-title strong").html("<a href='javascript:void(0);' data='" + data + "'>" + data + "</a> Visualization");
        drawModelGraph(data);
        $.ajax({
            url : ctx + "/view/properties",
            type : "post",
            data : {className : data},
            success : function(html){
                $("#div_properties").show().find("table:last").remove();
                var obj = $(html).hide();
                $("#div_properties").append(obj).find("table:last").fadeIn("slow").tablesorter({sortList:[[0,0]]});
            }
        });
        $.get(ctx + "/view/comment?className=" + data, function(data){
            data = $.trim(data);
            if(data == ""){data = "No Comment"}
            $("#div_properties .text-muted").text(data);
        });
    });
    $("#div_graph").on("click", "strong a[data]", function(){
       drawModelGraph($(this).attr("data"));
    });
    $("#div_graph").on("click", "g a", function(){
        var link = $(this).attr("xlink:href");
        if(link){
            drawModelGraph("", link);
        }
        return false;
    })
    $("a[data='bf:Work']").click();
});