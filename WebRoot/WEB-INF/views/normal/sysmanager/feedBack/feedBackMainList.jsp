<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/inc/tag.jsp"%>
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
		<c:param name="menu" value="personInfo" />
	</c:import>
	<!-- table  Begin -->
	<div class="container">
		<h2 id="top">反馈信息
			<a href="${ctx}/userInfo/personInfo"> <span
				class="btn btn-default btn-sm pull-right" tag="back"><i
					class="glyphicon glyphicon-chevron-left"></i>&nbsp;个人中心</span></a>
		</h2>
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
									<th width='5%'>文献题名</th>
									<th width='15%'>反馈标题</th>
									<th width='15%'>用户姓名</th>
									<th width='15%'>时间</th>
									<th width='15%'>状态</th>
									<th width='15%'>操作</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="x" varStatus="status" items="${data}">
									<tr class="odd gradeA">
										<td width='5%'>${status.index + 1}</td>
											<td width='12%'><a target="_blank" href="${ctx}/service/work/list#uri=${x.genealogyUri}">${x.genealogyTitle}</a></td>
										<td width='12%'>${x.title}</td>
										<td width='8%'>${x.name }</td>
										<td width='10%'>${x.createdDate}</td>
										<td width='8%'><c:if test="${x.status=='未回复'}">
												<font color='red'>${x.status}</font>
											</c:if> <c:if test="${x.status=='已回复'}">
												<font>${x.status}</font>
											</c:if></td>
										<td width='8%' class="center">
											<button
												onclick="javascript:window.location.href='${ctx}/feedBack/toEditFeedBack?id=${x.id}'"
												id="btn_updateRole" class="btn btn-primary">
												<i class="icon-list icon-white"></i>详细
											</button>
										</td>
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
	<script>
		$(function() {

		});
	</script>
</body>


</html>