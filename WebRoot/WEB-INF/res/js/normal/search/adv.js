$(function(){
	
    var pager = new Pager($("#searchForm"));
    var searchTpl = $("#searchTpl").html();
    $("#search_bar a").click(function(){return false;})
    $("#searchForm").submit(function(){
    	if($("#accFlg").is(':checked')){
    		$("#accFlg").val('true');
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
    $("#searchForm").submit();
    $("#dataDiv").on("click", "a[tag=work]", function(){
        location.href = ctx + "/service/work/list#uri=" + $(this).attr("href");
        return false;
    });
});