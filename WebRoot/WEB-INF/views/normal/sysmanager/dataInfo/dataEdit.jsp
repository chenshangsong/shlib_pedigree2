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
<c:import url="/WEB-INF/inc/link.jsp" />
</head>
<body>
	<c:import url="/WEB-INF/inc/sysHeader.jsp">
		<c:param name="menu" value="userInfo" />
	</c:import>
	<div class="container">
		<div class="panel panel-default">
			<h2 id="top">
				规范数据信息
			</h2>
			<hr>
			<div class="desc">
				<p>登录名：${userInfo.userId}<input class="formSave" type='hidden' name='id'
					value="${userInfo.id}" /></p>
				<p>用户姓名：${userInfo.userName}</p>
				<%-- <p>身份证号：${userInfo.shLibIdentityNo}</p> --%>
				<p>借阅号：${userInfo.shLibBorrower}</p>
				<p>邮箱：${userInfo.mail}</p>
				<p>
					角色：<select class="formSave" name="roleId"><option
							value='1' <c:if test="${userInfo.roleId=='1'}">selected</c:if>>
							普通用户</option>
						<option <c:if test="${userInfo.roleId=='2'}">selected</c:if>
							value='2'>管理员</option></select>

				</p>
				<p>
				<button id="btn_updateUser" class="btn btn-primary">
					<i class="icon-refresh icon-white"></i>同步用户信息
				</button>
					<button id="btn_updateRole" class="btn btn-primary">
						<i class="icon-pencil icon-white"></i>保存角色信息
					</button>
			</div>

		</div>

	</div>
	<c:import url="/WEB-INF/inc/footer.jsp" />
	<script>
		$(function() {
			//更新角色信息
			$("#btn_updateRole").click(function() {
				var parms = $('.formSave').toJson();
				requestJson("doUpdateUserRoleById", parms, successFun);
			});
			//更新用户信息
			$("#btn_updateUser").click(function() {
				var parms = $('.formSave').toJson();
				requestJson("doUpdateUserById", parms, successFun);
			});
			//成功回调函数
			function successFun(data) {
				if (data.result == '0') {
					alert('更新成功');
					window.open('${ctx}/userInfo/userList', '_self');
				} else {
					alert('更新失败');
				}
			}
		});
	</script>
</body>
</html>