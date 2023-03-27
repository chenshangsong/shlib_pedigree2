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
		<c:param name="menu" value="personInfo" />
	</c:import>
	<div class="container">
		<h2 id="top">
			个人中心 
			<c:if test="${ctxRoleId.roleId != '1'}">
				<a href="${ctx}/toDoList/list">
					<span class="btn btn-default btn-sm pull-right">
						<i class="glyphicon glyphicon-bell"></i>&nbsp;待办事项
					</span>
				</a>
				<a href="${ctx}/feedBack/feedBackMainList">
					<span class="btn btn-default btn-sm pull-right">
						<i class="glyphicon glyphicon-envelope"></i>&nbsp;反馈信息
					</span>
				</a>
			</c:if>
		</h2>
		<div class="modal-body">
			<div class="panel-group" id="works_accordion" role="tablist"
				aria-multiselectable="true">
				<div class="panel panel-info">
					<div id="collapse_0" class="panel-collapse collapse in"
						role="tabpanel" aria-labelledby="heading_0">
						<div class="panel-body">
							<p>
								【登录名】
								${ctxRoleId.getUserId()}<%-- 
								${userInfo.userId} --%><input class="formSave" type='hidden' name='id' value="${ctxRoleId.id}" />
							</p>
							<p>
								【姓&nbsp;&nbsp;&nbsp;&nbsp;名】
								${ctxRoleId.getUserName()}
								<%-- ${userInfo.userName} --%>
							</p>
							<p>
								【借阅号】
								${ctxRoleId.getShLibBorrower()}
								<%-- ${userInfo.shLibBorrower} --%>
							</p>
							<p>
								【邮&nbsp;&nbsp;&nbsp;&nbsp;箱】
								${ctxRoleId.getMail()}
								<%-- ${userInfo.mail} --%>
							</p>
							<p>
								【角&nbsp;&nbsp;&nbsp;&nbsp;色】
								${ctxRoleId.getRoleName()}
								<%-- <select <c:if test="${userInfo.roleId!='3'}">disabled="true"</c:if> class="formSave" name="roleId">
									<option <c:if test="${userInfo.roleId == '1'}">selected</c:if> value='1'>普通用户</option>
									<option <c:if test="${userInfo.roleId == '2'}">selected</c:if> value='2'>专家用户</option>
									<option <c:if test="${userInfo.roleId == '3'}">selected</c:if> value='3'>系统管理员</option>
								</select> --%>
							</p>
							
							<%-- <button id="btn_updateUser" class="btn btn-primary">
								<i class="icon-refresh icon-white"></i>同步用户信息
							</button>
							<c:if test="${userInfo.roleId=='3'}">
								<button id="btn_updateRole" class="btn btn-primary">
									<i class="icon-pencil icon-white"></i>更新角色信息
								</button>
							</c:if> --%>
						</div>
					</div>
				</div>
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
					showMsg(String.format(IMSG0004, "更新"));

				} else {
					alert('更新失败');
				}
			}
			function abc() {
				window.open('${ctx}/userInfo/userList', '_self');
			}
		});
	</script>
</body>
</html>