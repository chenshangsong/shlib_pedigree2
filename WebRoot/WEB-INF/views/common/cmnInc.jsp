<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<link type="text/css" rel="stylesheet" href="<c:url value='/css/style.css'/>">
<link type="text/css" rel="stylesheet" href="<c:url value='/jqueryui/css/custom/jquery-ui-1.9.2.custom.css'/>">
<link type="text/css" rel="stylesheet" href="<c:url value='/jqgrid/ui.multiselect.css'/>">
<link type="text/css" rel="stylesheet" href="<c:url value='/jqgrid/ui.jqgrid.css'/>">
<link type="text/css" rel="stylesheet" href="<c:url value='/jquery_easy_ui/themes/default/easyui.css'/>">
<link type="text/css" rel="stylesheet" href="<c:url value='/jquery_easy_ui/themes/icon.css'/>">
<link type="text/css" rel="stylesheet" href="<c:url value='/loadmask/jquery.loadmask.css'/>">
<link type="text/css" rel="stylesheet" href="<c:url value='/jqueryui/jquery.tooltip.css'/>">

<style>
html,body {
	margin: 0; /* Remove body margin/padding */
	padding: 0;
	overflow-x: hidden; 
	font-size: 75%;
}
</style>

<%-- <script type="text/javascript" src="<c:url value='/js/jquery-1.8.3.js'/>"></script> --%>
<script  type="text/javascript" src="${ctx}/res/sysmanager/vendors/jquery.js"></script>
<script type="text/javascript" src="<c:url value='/jquery_easy_ui/jquery.easyui.min.js'/>"></script>

<script type="text/javascript" src="<c:url value='/js/jquery.form.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jqgrid/js/i18n/grid.locale-cn.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jqgrid/js/grid.base.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jqgrid/js/grid.celledit.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jqgrid/js/grid.common.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jqgrid/js/grid.custom.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jqgrid/js/grid.filter.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jqgrid/js/grid.formedit.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jqgrid/js/grid.grouping.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jqgrid/js/grid.import.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jqgrid/js/grid.inlinedit.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jqgrid/js/grid.jqueryui.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jqgrid/js/grid.subgrid.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jqgrid/js/grid.tbltogrid.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jqgrid/js/grid.treegrid.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jqgrid/js/jqDnR.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jqgrid/js/jqModal.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jqgrid/js/jquery.fmatter.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jqgrid/js/JsonXml.js'/>"></script>

<script type="text/javascript" src="<c:url value='/jqgrid/plugins/grid.addons.js'/>"></script>

<script type="text/javascript" src="<c:url value='/jqgrid/plugins/grid.postext.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jqgrid/plugins/grid.setcolumns.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jqgrid/plugins/jquery.contextmenu.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jqgrid/plugins/jquery.searchFilter.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jqgrid/plugins/jquery.tablednd.js'/>"></script>

<!-- 
<script type="text/javascript" src="<c:url value='/jqgrid/plugins/ui.multiselect.js'/>"></script>
 -->
<link type="text/css" rel="stylesheet" href="<c:url value='/superfish/css/superfish.css'/>">
<script type="text/javascript" src="<c:url value='/superfish/js/hoverIntent.js'/>"></script>
<script type="text/javascript" src="<c:url value='/superfish/js/superfish.js'/>"></script>
<script type="text/javascript" src="<c:url value='/superfish/js/supersubs.js'/>"></script>

<script type="text/javascript" src="<c:url value='/ajaxfileupload/ajaxfileupload.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/common.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/message.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/check.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/userDialog.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/warehouseDialog.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/giftProvideRelatedListDialog.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/userApproverDialog.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/userApproverGroupDialog.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/customerDialog.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/commonUtil.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jqgridUtil.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/easyuiUtil.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/json2.js'/>"></script>
<script type="text/javascript" src="<c:url value='/loadmask/jquery.loadmask.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery.meio.mask.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jqueryui/jquery.tooltip.js'/>"></script>

		<div style="display:none;" id="dialog-confirm" title="Confirm">
			<p>
				<span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>
				<span id="confirm_msg">你确定要删除以下内容:</span>
			</p>
		</div>
		

		<iframe id="downloadFrame" style="display:none"></iframe>
<style>
textarea[disabled] {
	color: #000000;
}
</style>
<script type="text/javascript">
	webName = '${pageContext.request.contextPath}';
	webName = webName.substring(1);
</script>