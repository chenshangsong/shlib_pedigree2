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
<c:import url="/WEB-INF/inc/sysmanagerlink.jsp" />
</head>
<body>
	<c:import url="/WEB-INF/inc/sysHeader.jsp">
		<c:param name="menu" value="todolist" />
	</c:import>
	<!-- table  Begin -->
	<div class="container">
		<div class="panel panel-default">
			<h2 id="top">数据维护</h2>
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
										<th width='15%'>RDF(元)</th>
										<th width='15%'>名称</th>
										<th width='15%'>属性</th>
										<th width='25%'>值</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="x" varStatus="status" items="${data}">
										<tr class="odd gradeA">
											<td width='5%'>${status.index + 1}</td>
											<td width='15%'>${x}</td>
											<td width='15%'><c:forEach varStatus="status2" var="y"
													items="${x}">
													<c:if test="${y.key=='label' }">${y.value}</c:if>
												</c:forEach></td>
											<td width='15%'><c:forEach varStatus="status2" var="y"
													items="${x}">
													<c:if test="${y.key=='p' }">${y.value}</c:if>
												</c:forEach></td>

											<td width='25%'><c:forEach varStatus="status2" var="y"
													items="${x}">
													<c:if test="${y.key=='p' }">
														原值： <a href="javaScript:void('0')">${y.value}</a>
													</c:if>
													<c:if test="${y.key=='range' }">
														<c:if test="${y.value=='rdfs:Literal' }">

															<input type="text" class="input form-control"
																style='height: 40px; width: 70%' placeholder="在此输入新值">
															<button class="btn btnAdd btn-primary btn-mini">
																<i class="icon-plus"></i>
															</button>
															<button class="btn btnDell btn-primary btn-mini">
																<i class="icon-remove"></i>
															</button>

															<br>
														</c:if>
													</c:if>

												</c:forEach></td>
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
	</div>
	<c:import url="/WEB-INF/inc/footer.jsp" />
	<script
		src="${ctx}/res/sysmanager/vendors/datatables/js/jquery.dataTables.min.js"></script>
	<script src="${ctx}/res/sysmanager/assets/scripts.js"></script>
	<script src="${ctx}/res/sysmanager/assets/DT_bootstrap.js"></script>
	<script>
		$(function() {

			$(".btnAdd").click(function() {
				var Obj = $(this).parents('tr').find('td:eq(2)');

			});
		});
	</script>
</body>


</html>