<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
<title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>

<link href="${ctx}/res/sysmanager/bootstrap/css/bootstrap.min.css"
	rel="stylesheet" media="screen">
<link
	href="${ctx}/res/sysmanager/bootstrap/css/bootstrap-responsive.min.css"
	rel="stylesheet" media="screen">
<link href="${ctx}/res/sysmanager/assets/DT_bootstrap.css"
	rel="stylesheet" media="screen">
<c:import url="/WEB-INF/inc/link.jsp" />
</head>
<body>
	<c:import url="/WEB-INF/inc/header3.jsp">
		<c:param name="menu" value="systemSetting" />
	</c:import>
	<!-- table  Begin -->
	<div class="container">
		<h2 id="top">书目删除记录 <a href="${ctx}/workManager/list"> <span
				class="btn btn-default btn-sm pull-right" tag="back"><i
					class="glyphicon glyphicon-chevron-left"></i>&nbsp;书目列表</span></a></h2>
		<hr>
		<div class="row-fluid">
			<!-- block -->
			<div class="block">
				<div class="block-content collapse in">
					<div class="span12">

						<table cellpadding="0" cellspacing="0" border="0"
							class="table table-striped " id="example">
							<thead>
								<tr>
									<th width='5%'>序号</th>
									<th width='15%'>题名</th>
									<th width='15%'>责任者</th>
									<th width='10%'>书目URI</th>
									<th width='10%'>删除时间</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="x" varStatus="status" items="${data}">
									<tr class="odd gradeA">
										<td width='5%'>${status.index + 1}</td>
										<td width='10%'>${x.workDtitle}</td>
										<td width='10%'>${x.creator}</td>
										<td width='10%'>${x.workUri }</td>
										<td width='10%'>${fn:substring(x.deleteDate ,0,19 )}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<!-- /block -->
		</div>
	</div>

	<c:import url="/WEB-INF/inc/footer.jsp" />
	<script
		src="${ctx}/res/sysmanager/vendors/datatables/js/jquery.dataTables.min.js"></script>
	<script src="${ctx}/res/sysmanager/assets/scripts.js"></script>
	<script src="${ctx}/res/sysmanager/assets/DT_bootstrap.js"></script>
</body>


</html>