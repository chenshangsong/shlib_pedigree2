$(function(){
    var loadClasses = function(){
        $.getJSON(ctx + "/view/classes", function(data){
            if(!data) return ;
            var html = '<ul>';
            for(var i = 0; i < data.length; i ++ ){ html += drawTreeNode(data[i]); }
            html += "</ul>";
            $("div.tree").hide().html(html).fadeIn("slow");
            $.renderTree();
        })
    };

    var loadProperties = function(){
        var className = $("li>span.active").attr("data");
        $("#div_properties .panel-title strong").text(className);
        $.ajax({
            url: ctx + "/view/properties",
            data: {className:className},
            type: "post",
            success : function(html){
                $("#div_properties .panel").find("table:last").remove();
                var obj = $(html).hide();
                $("#div_properties .panel").append(obj).find("table:last").fadeIn("slow").tablesorter({sortList:[[0,0]]});
            }
        });
        $.get(ctx + "/view/comment?className=" + className, function(data){
            data = $.trim(data);
            if(data == ""){data = "No Comment"}
            $("#div_properties .text-muted").text(data);
        });
    }

    var drawTreeNode = function(treeNode){
        var html = '<li class="parent_li" role="treeitem">';
        html += '<span data="' + treeNode.className + '">';
        if( treeNode.children && treeNode.children.length > 0 ){
            html += '<i class="glyphicon glyphicon-plus"></i>&nbsp;';
        } else {
            html += '<i class="glyphicon glyphicon-minus"></i>&nbsp;';
        }
        html += "<b style='font-weight: normal;'>" + treeNode.className + '</b></span>';
        if( treeNode.children && treeNode.children.length > 0 ){
            html += '<ul style="display:none;">';
            for( var i = 0; i < treeNode.children.length; i ++ ){
                html += drawTreeNode(treeNode.children[i]);
            }
            html += '</ul>';
        }
        html += "</li>";
        return html;
    }

    $("div.tree").on("click", "span", function(){

    });
    loadClasses();
    $("#div_properties").pin();

    $("div.tree").on("click","ul>li>span>b",function(e){
        e.stopPropagation();
        e.preventDefault();
        $("div.tree li>span").removeClass("active");
        $(this).parent().addClass("active");
        loadProperties();
    });
});