$(function(){
	//不是管理员，保存并发布按钮隐藏
	if(ctxRoleId!=$.roleGroup.admin){
		$(".adminDiv").css('display','none');
	}
});