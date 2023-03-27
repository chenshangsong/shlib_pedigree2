<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
<c:import url="/WEB-INF/inc/link.jsp" />
<script>
	$(function() {
		$("#btn_login").click(function() {
			var uid = $("#uid").val();
			var pwd = $("#pwd").val()
			if ($.trim(uid) == "") {
				layer.tips("请输入用户名", $("#uid"), {
					tips : [ 1, 'RGB(250,165,35)' ],
					time : 3000
				});
				return false;
			}
			if ($.trim(pwd) == "") {
				layer.tips("请输入密码", $("#pwd"), {
					tips : [ 1, 'RGB(250,165,35)' ],
					time : 3000
				});
				return false;
			}
			var param = $("#form").toJson();
			requestJson("dologin", param, successFun);
		});
	});
	function successFun(data) {
		if (data.result.resultCode == '0') {
			if (data.user.roleId == '1') {
				//window.location.href = '${ctx}/service/work/list';
				window.location.href = '${ctx}/userInfo/personInfo';
			} else {
				window.location.href = '${ctx}/userInfo/personInfo';
				//window.location.href = '${ctx}/dataManager/datalist';
				//window.location.href = '${ctx}/toDoList/list';
			}
			//window.open('${ctx}/userlogin/home','_self'); 
		} else {
			alert("登录失败！");
		}
	}
</script>
</head>
<body>
	<c:import url="/WEB-INF/inc/header3.jsp">
	</c:import>
	<form:form commandName="UserLoginModel" id="form" method="post">
		<div class="container">
			<h2 id="top">用户登录</h2>
			<br>
			<div class="panel panel-default">

				<div class="panel-body desc">
					<p>
						用户名：<input type="text" id='uid' name="uid"
							class="input form-control" placeholder="在此输入用户名/卡号" required />
					</p>
					<p>
						密码：<input type="password" id='pwd' name="pwd"
							class="input form-control" placeholder="在此输入密码" required />
					</p>
					<a class="btn btn-primary" action='dologin'
						href="javascript:void('0')" id="btn_login"><i
						class="icon-ok icon-white"></i>登 录</a> <input type='hidden'
						id='hidUserType' name="userType" value='0' /> <a
						class="btn btn-primary" target="_blank"
						href="http://beta.library.sh.cn/SHLibrary/register.html"> <i
						class="glyphicon glyphicon-user"></i>注册
					</a>
					<div style="padding-top: 10px; color: #777;">
						提示：上海图书馆持证读者用“用户名或卡号+密码”登录，若无有效读者证，可至上海市各区县分馆或街镇服务点<a
							class=" btn-primary" target="_blank"
							href="http://www.library.sh.cn/fwzn/bzxz/">办理读者证</a>，或先<a
							class="btn-primary" target="_blank"
							href="http://beta.library.sh.cn/SHLibrary/register.html">网上注册</a>后再登录。
					</div>
				</div>

			</div>
		</div>
		<c:import url="/WEB-INF/inc/footer.jsp" />
	</form:form>

</body>
</html>