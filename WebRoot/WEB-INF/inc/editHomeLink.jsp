<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href="${ctx}/res/sysmanager/bootstrap/css/bootstrap-combined.min.css"
	rel="stylesheet" media="screen">
<link href="${ctx}/res/css/home/home.css" rel="stylesheet" media="screen">	
<link href="${ctx}/res/css/home/jquery.mCustomScrollbar.css" rel="stylesheet" media="screen">	
	
<script src="${ctx}/res/sysmanager/vendors/jquery-1.12.4.js"></script>
<script src="${ctx}/res/sysmanager/bootstrap/js/bootstrap.min.js"></script>
<script src="${ctx}/res/js/sysmanager/home/jquery.mCustomScrollbar.concat.min.js"></script>
<script src="${ctx}/res/commJS/layer/layer.js"></script>
<script>
//$('title').html('华人家谱总目——上海图书馆家谱知识服务平台Beta版');
	var ctx = "${ctx}";
	var ctxRoleId = "${ctxRoleId.getRoleId()}";
	var _hmt = _hmt || [];
	(function() {
	  var hm = document.createElement("script");
	  hm.src = "https://hm.baidu.com/hm.js?90d2152996e4266fafdcb7ed1301a851";
	  var s = document.getElementsByTagName("script")[0]; 
	  s.parentNode.insertBefore(hm, s);
	})();
</script>
