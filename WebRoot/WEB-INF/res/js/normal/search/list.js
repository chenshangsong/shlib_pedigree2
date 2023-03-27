$(function(){
    var pager = new Pager($("#searchForm"));
   
    var searchTpl = $("#searchTpl").html();
    $("#_searchForm input[name=keyword]").val($("#searchForm :hidden[name=keyword]").val());
    $("#_searchForm").submit(function(){
        $("#searchForm :hidden[name=keyword]").val($("#_searchForm :input[name=keyword]").val());
        $("#searchForm").find(":hidden[name=pageth]").val(1);
        $("#searchForm").submit();
        return false;
    })
    $("#searchForm").submit(function(){
    	 var keyword = $("#_searchForm input[name=keyword]").val()
        if($.trim(keyword) == "") {
            Dialog.error("请输入关键词");
            $("#_searchForm input[name=keyword]").focus();
        }
        showLoading();
        $.post($(this).attr("action"), $(this).serialize(), function(result){
            $("#pager").html(pager.paint(result.pager));
            $("#count").html(result.pager.rowCount);
            $("#dataDiv").html(juicer(searchTpl, result)).animateCss("fadeIn");
            hideLoading();
        });
        return false;
    });
    //主页跳入，不传参数，不提交
    var keyword = $("input[name=keyword]").val()
    if($.trim(keyword) != "") {
    //默认不提交
       $("#searchForm").submit();
    }
    $("#dataDiv").on("click", "a[tag=work]", function(){
        location.href = ctx + "/service/work/list#uri=" + $(this).attr("href");
        return false;
    });
});