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
		<c:param name="menu" value="systemSetting" />
	</c:import>
	<!-- table  Begin -->
	<div class="container">
		<h2 id="top">用户管理 <a href="${ctx}/systemSetting/main"> <span
				class="btn btn-default btn-sm pull-right" tag="back"><i
					class="glyphicon glyphicon-chevron-left"></i>&nbsp;系统管理</span></a></h2>
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
									<th width='15%'>登录号</th>
									<th width='15%'>用户姓名</th>
									<!-- <th width='15%'>身份证号</th> -->
									<th width='15%'>借阅号</th>
									<th width='15%'>邮箱</th>
									<th width='10%'>角色</th>
									<th width='15%'>操作</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="x" varStatus="status" items="${data.root}">
									<tr class="odd gradeA">
										<td width='5%'>${status.index + 1}</td>
										<td width='10%'>${x.userId }</td>
										<td width='10%'>${x.userName}</td>
									<%-- 	<td width='10%' class="center">${x.shLibIdentityNo}</td> --%>
										<td width='10%' class="center">${x.shLibBorrower}</td>
										<td width='10%' class="center">${x.mail}</td>
										<td width='10%' class="center"><c:choose>
												<c:when test="${x.roleId== '1'}">  
                                                                  普通用户
						</c:when>
												<c:when test="${x.roleId== '2'}">  
                                                                专家用户
						</c:when>
												<c:otherwise> 
                                                                         系统管理员
						</c:otherwise>
											</c:choose></td>
										<td width='10%' class="center">
											<button
												onclick="javascript:window.location.href='${ctx}/userInfo/toEditUser?id=${x.id}'"
												id="btn_updateRole" class="btn btn-primary">
												<i class="icon-pencil icon-white"></i>用户管理
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

			$("#btn_save").click(function() {
				requestJson("doInsertUser", null, successFun);
			});
			/* $("#btn_toSave").click(function() {
				window.open('${ctx}/userlogin/toEditUser','_self'); 
			}); */
			function successFun(data) {
				if (data.result == '0') {
					alert("保存成功！");
				} else {
					alert("保存失败！");
				}
			}
		});
	</script>
</body>


</html>