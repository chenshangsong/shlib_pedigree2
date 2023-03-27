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
	</c:import>
	<div class="container">
		<div class="panel panel-default">

			<div class="panel-body desc">
				<p style="font-size: 18px;">
					<a href="javascript:void(0)" target="_blank">登录用户信息</a>
				</p>
				<p>用户名：${userInfo.userId}<input class="formSave" type='hidden' name='id'
					value="${userInfo.id}" /></p>
				<p>用户姓名：${userInfo.userName}</p>
				<p>身份证号：${userInfo.shLibIdentityNo}</p>
				<p>邮箱：${userInfo.mail}</p>
				<p>
					角色：
					<c:choose>
						<c:when test="${userInfo.roleId== '1'}">  
                                                                  普通用户
						</c:when>
						<c:when test="${userInfo.roleId== '2'}">  
                                                                专家用户
						</c:when>
						<c:otherwise> 
                                                                         系统管理员
						</c:otherwise>
					</c:choose>
				</p>
				<button id="btn_updateUser" class="btn btn-primary">
					<i class="icon-refresh icon-white"></i>同步用户信息
				</button>
			</div>

		</div>
	</div>
	<c:import url="/WEB-INF/inc/footer.jsp" />
	<script>
		$(function() {
			//更新用户信息
			$("#btn_updateUser").click(function() {
				var parms = $('.formSave').toJson();
				requestJson("${ctx}/userInfo/doUpdateUserById", parms, successFun);
			});
			//成功回调函数
			function successFun(data) {
				if (data.result == '0') {
					alert('同步成功，请重新登录');
					window.open('${ctx}/userlogin/tologin', '_self');
				} else {
					alert('同步失败');
				}
			}
		});
	</script>
</body>


</html>