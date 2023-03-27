<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
<c:import url="/WEB-INF/inc/link.jsp" />
</head>
<body>
	<c:import url="/WEB-INF/inc/header3.jsp">
		<c:param name="menu" value="systemSetting" />
	</c:import>
	<!-- table  Begin -->
	<div class="container">
	<h2 id="top">系统管理</h2>
		<%-- <button
				onclick="javascript:window.location.href='${ctx}/feedBack/feedBackInsertPage'"
				 class="btn btn-primary">
				<i class="icon-pencil icon-white"></i>添加反馈
			</button> --%>

		<hr>
			<div class="row">
				<div class="col col-lg-4 col-md-4 col-sm-4" style="cursor: pointer;"
					onclick="location.href='../systemSetting/menuList'">


					<div class="well" style="background-color: #d0e9c6 !important;">


						<h2>菜单管理</h2>

						<br> <br>

					</div>

				</div>
				<div class="col col-lg-4 col-md-4 col-sm-4" style="cursor: pointer;"
					onclick="location.href='../systemSetting/roleSetting'">


					<div class="well" style="background-color: #f5f8f9 !important;">


						<h2>权限管理</h2>

						<br> <br>

					</div>

				</div>
				<div class="col col-lg-4 col-md-4 col-sm-4"
					style="height: 40%; cursor: pointer;"
					onclick="location.href='../userInfo/userList'">

					<div class="well" style="background-color: #d9edf7 !important;">
						<h2>用户管理</h2>

						<br> <br>
					</div>


				</div>
					<div class="col col-lg-4 col-md-4 col-sm-4"
					style="height: 40%; cursor: pointer;"
					onclick="location.href='../service/place/listfull'">

					<div class="well" style="background-color: #d9edf7 !important;">
						<h2>谱籍列表</h2>

						<br> <br>
					</div>


				</div>
				<c:if test="${ctxRoleId.getUserName()=='陈尚松'}">

					<div class="col col-lg-4 col-md-4 col-sm-4"
						style="cursor: pointer;"
						onclick="location.href='../dataImportManager/dataImportMain'">


						<div class="well" style="background-color: #d0e9c6 !important;">

							<h2>数据导入</h2>

							<br> <br>
						</div>

					</div>
				</c:if>
				<div class="col col-lg-4 col-md-4 col-sm-4"
					style="height: 40%; cursor: pointer;"
					onclick="location.href='../systemSetting/toDoi'">

					<div class="well" style="background-color: #f5f8f9 !important;">
						<h2>外网全文设置</h2>

						<br> <br>
					</div>


				</div>
			</div>
		</div>
	<c:import url="/WEB-INF/inc/footer.jsp" />
	<script>
		$(function() {
			/* $(".btn-primary").click(function() {
				//获取本体唯一属性:去除空格
				var id = $.trim($(this).parents('tr').find('td:eq(2)').text());
				//	window.location.href='${ctx}/dataManager/dataContentlist?id='+id;
				window.location.href = '${ctx}/dataManager/personList';
			}); */
		});
	</script>
</body>
</html>



