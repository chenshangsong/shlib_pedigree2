$(function() {
	var worksTpl = $("#worksTpl").html();
	$("#workForm").submit(function() {
		showLoading();
		$.post($(this).attr("action"), $(this).serialize(), function(result) {
			$("#dataDiv").html(juicer(worksTpl, result)).animateCss("fadeIn");
			hideLoading();
		});
		return false;
	});
	$("#btn_query").click(function() {
		loadDataList();
	});
	var loadDataList = function() {
		$("#workForm").submit()
	};
	loadDataList();
})