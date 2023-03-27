$(function(){
    var finished = 0;
    var loadClassList = function(){
        $.ajax({
            url : ctx + "/view/listClasses",
            type : "post",
            success : function(data){
                $("#class_list").hide().html($("#vocab_list",$(data)).html()).fadeIn("slow");
                $("#class_desc").hide().html($("#vocab_desc",$(data)).html()).fadeIn("slow");
                finished ++;
                callback();
            }
        });
    };
    var loadPropList = function(){
        $.ajax({
            url : ctx + "/view/listProperties",
            type : "post",
            success : function(data){
                $("#prop_list").hide().html($("#vocab_list",$(data)).html()).fadeIn("slow");
                $("#prop_desc").hide().html($("#vocab_desc",$(data)).html()).fadeIn("slow");
                finished ++;
                callback();
            }
        });
    };
    var callback = function(){(finished >= 2 && location.hash != "") ? location.href = location.hash:[];}

    // init
    loadClassList();
    loadPropList();
})