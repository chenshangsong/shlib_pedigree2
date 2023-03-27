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
	<c:import url="/WEB-INF/inc/header3.jsp">
		<c:param name="menu" value="systemSetting" />
		<%-- 		<c:param name="menu" value="userInfo" /> --%>
	</c:import>
	<div class="container">
		<h2 id="top">用户信息
		 <a href="${ctx}/userInfo/userList"> <span
				class="btn btn-default btn-sm pull-right" tag="back"><i
					class="glyphicon glyphicon-chevron-left"></i>&nbsp;用户管理</span></a>
		</h2>
		
		<hr>
		<div class="desc">
			<p>
				登录名：${userInfo.userId}<input class="formSave" type='hidden'
					name='id' value="${userInfo.id}" />
			</p>
			<p>用户姓名：${userInfo.userName}</p>
			<%-- <p>身份证号：${userInfo.shLibIdentityNo}</p> --%>
			<p>借阅号：${userInfo.shLibBorrower}</p>
			<p>邮箱：${userInfo.mail}</p>
			<p>
				角色：<select class="formSave" name="roleId"><option value='1'
						<c:if test="${userInfo.roleId=='1'}">selected</c:if>>
						普通用户</option>
					<option <c:if test="${userInfo.roleId=='2'}">selected</c:if>
						value='2'>专家用户</option>
					<option <c:if test="${userInfo.roleId=='3'}">selected</c:if>
						value='3'>系统管理员</option></select>

			</p>
			<button id="btn_updateUser" class="btn btn-primary">
				<i class="icon-refresh icon-white"></i>同步用户信息
			</button>
			<button id="btn_updateRole" class="btn btn-primary">
				<i class="icon-pencil icon-white"></i>更新角色信息
			</button>
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
					showMsg(String.format(IMSG0004, "更新"));
					
				} else {
					alert('更新失败');
				}
			}
			function abc(){
				window.open('${ctx}/userInfo/userList', '_self');
			}
		});
	</script>
</body>
</html>