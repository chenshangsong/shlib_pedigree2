<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${ctx}/res/bootstrap/css/bootstrap.css" rel="stylesheet"
	media="all" />
<link href="${ctx}/res/css/animate.css" rel="stylesheet" />
<link href="${ctx}/res/css/style.css" rel="stylesheet" />
<link href="${ctx}/res/icheck/skins/all.css" rel="stylesheet" />
<%--<link href="${ctx}/res/icheck/square/blue.css" />--%>
<!--[if lt IE 9]>
<script src="${ctx}/res/html5/html5shiv.js"></script>
<script src="${ctx}/res/html5/respond.min.js"></script>
<![endif]-->
<script src="${ctx}/res/plugin/juicer-min.js"></script>

<%--chenss --%>
<script  type="text/javascript" src="${ctx}/res/sysmanager/vendors/jquery-1.12.4.js"></script>
<script src="${ctx}/res/commJS/layer/layer.js"></script>
<script src="${ctx}/res/commJS/message.js"></script>
<script src="${ctx}/res/commJS/json2.js"></script>
<script src="${ctx}/res/commJS/common.js"></script>
<script src="${ctx}/res/commJS/commonUtil.js"></script>
<script src="${ctx}/res/commJS/systemvariable.js"></script>
<%--chenss --%>
<script>
	//$('title').html('华人家谱总目——上海图书馆家谱知识服务平台Beta版');
	var ctx = "${ctx}";
	var ctxRoleId = "${ctxRoleId.getRoleId()}";
</script>
