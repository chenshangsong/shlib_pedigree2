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
		<c:param name="menu" value="todolist" />
	</c:import>
	<!-- table  Begin -->
	<div class="container">
		<h2 id="top">取值词表类别列表</h2>
		<hr>
		<button
			onclick="javascript:window.location.href='${ctx}/categoryManager/categoryTypeEdit'"
			class="btn btn-primary">
			<i class="icon-plus icon-white"></i>添加类别
		</button>
		<a href="${ctx}/categoryManager/list"> <span
			class="btn btn-default btn-sm pull-right" tag="back"><i
				class="glyphicon glyphicon-chevron-left"></i>&nbsp;取值词表维护</span></a> <a
			href="${ctx}/dataManager/datalist"> <span
			class="btn btn-default btn-sm pull-right" tag="back"><i
				class="glyphicon glyphicon-chevron-left"></i>&nbsp;数据维护</span></a>

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
									<th width='15%'>类别中文名</th>
									<th width='15%'>类别英文名</th>
									<th width='15%'>用于数据集</th>
									<th width='15%'>维护</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="x" varStatus="status" items="${data}">
									<tr class="odd gradeA">
										<input type='hidden' class="deleteId" value="${x.id}" />
										<td width='5%'>${status.index + 1}</td>
										<td width='10%'>${x.categoryNameCn}</td>
										<td width='10%'>${x.categoryNameEn}</td>
										<td width='10%'>${x.categoryUriIn}</td>
										<td width='10%' class="center">
											<button
												onclick="javascript:window.location.href='${ctx}/categoryManager/categoryTypeEdit?id=${x.id}'"
												id="btn_updateRole" class="btn btn-primary">
												<i class="icon-pencil icon-white"></i>维护
											</button>
											<button class="btn btn-primary btn_deleteRole">
												<i class="icon-pencil icon-white"></i>删除
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

			//删除
			$(".btn_deleteRole")
					.click(
							function() {
								//消息提示
								var $deleteId = $(this).parents("tr").find(
										".deleteId");
								showConfirmMsg(String.format(IMSG0003, "删除"),
										deleteFunc);
								//保存函数
								function deleteFunc() {
									requestJson(
											"${ctx}/categoryManager/categoryTypeDelete?id="
													+ $deleteId.val(), null,
											successFun);
								}
							});

			//删除回调函数
			function successFun(data) {
				if (data.result) {
					showMsg(String.format(IMSG0004, "删除"));
					window.open('${ctx}/categoryManager/categoryTypelist',
							'_self');
				} else {
					showErrorMsg(String.format(EMSG9000, "删除"));
				}
			}

		});
	</script>
</body>


</html>