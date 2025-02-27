<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
<meta http-equiv="Content-Security-Policy" content="upgrade-insecure-requests">
<link rel="stylesheet" type="text/css" href="${ctx}/res/mirador/css/mirador-combined.css">
<script src="${ctx}/res/mirador/mirador.js"></script>
    <script type="text/javascript">
    var ctx = "${ctx}";
     //document.oncontextmenu=new Function("event.returnValue=false");  
     //document.onselectstart=new Function("event.returnValue=false");  
    </script>
</head>
<body>
	<input type="hidden" id="org" value="${org}"/>
	<%-- <input type="hidden" id="user" value="${userId}"/> --%>
	<input type="hidden" id="manifest" value="${manifest}"/>
	<input type="hidden" id="isInnerIP" value="${isInnerIP}"/>
    <div id="viewer"></div>
	<script src="${ctx}/res/js/fullimg/viewer.js"></script>
</body>
</html>
