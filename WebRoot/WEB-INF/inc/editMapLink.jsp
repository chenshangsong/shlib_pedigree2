<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<%-- css--%>
<link href="${ctx}/res/sysmanager/bootstrap/css/bootstrap-combined.min.css" rel="stylesheet" media="screen"/>
<link href="${ctx}/res/css/animate.css" rel="stylesheet"/>
<link href="${ctx}/res/css/style.css" rel="stylesheet"/>
<link href="${ctx}/res/plugin/openlayers/3.1.1/ol.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/res/css/map/map.css" rel="stylesheet" media="screen" />
<link href="${ctx}/res/css/map/knobKnob.css" rel="stylesheet" media="screen" />

<%-- js--%>
<script src="${ctx}/res/sysmanager/vendors/jquery.js"></script>
<script src="${ctx}/res/plugin/juicer-min.js"></script>
<script src="${ctx}/res/bootstrap/js/bootstrap.min.js"></script>
<script src="${ctx}/res/js/normal/common.js"></script>
<script src="${ctx}/res/plugin/openlayers/3.1.1/ol.js"></script>
<script src="${ctx}/res/js/sysmanager/map/list_map.js"></script>
<script src="${ctx}/res/js/sysmanager/map/transform.js"></script>
<script src="${ctx}/res/js/sysmanager/map/knobKnob.jquery.js"></script>
<script src="${ctx}/res/js/sysmanager/map/knob.js"></script>
<script>
//$('title').html('华人家谱总目——上海图书馆家谱知识服务平台Beta版');
	var ctx = "${ctx}";var ctxRoleId = "${ctxRoleId.getRoleId()}";
</script>
