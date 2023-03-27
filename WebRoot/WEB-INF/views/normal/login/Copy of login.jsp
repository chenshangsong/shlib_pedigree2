<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
<c:import url="/WEB-INF/inc/link.jsp" />
<script>
	$(function() {
		$("input[type='checkbox']").click(function(){
		     $("input[type='checkbox']").attr('checked',false);
		     $(this).attr('checked',true);
		    
		});
		
		$("#btn_login").click(function() {
			var userType=$("input[type='checkbox']:checked").val();
			$("#hidUserType").val(userType);
			var param = $("#form").toJson();
			
			requestJson("dologin", param, successFun);
		});
	});
	function successFun(data) {
		if (data.result.resultCode == '0') {
			//window.location.href = '${ctx}/userlogin/home';
			window.open('${ctx}/userlogin/home','_self'); 
		} else {
			alert("登录失败！");
		}
	}
</script>
</head>
<body>
	<c:import url="/WEB-INF/inc/headerLogin.jsp">
	</c:import>
	<form:form commandName="UserLoginModel" id="form" method="post">
		<div class="container">
		<h2 id="top">用户登录</h2><br>
			<div class="panel panel-default">
				<p style="font-size: 18px;">
					<a href="javascript:void(0)" target="_blank">登录信息</a>
				</p>
				<div class="panel-body desc">

					<p>
						用户名：<input type="text" id='uid' name="uid"
							class="input form-control" placeholder="在此输入用户名" />
					</p>
					<p>
						密码：<input type="password" id='pwd' name="pwd"
							class="input form-control" placeholder="在此输入密码" />
					</p>
				<!-- 	用户类型：<input id='type1' type='checkBox' value='0' checked>外网用户&nbsp;<input id='type2' value='1' type='checkBox' >内网用户 
					 --><a class="btn btn-primary" action='dologin'
						href="javascript:void('0')" id="btn_login">登 录</a>
						<input type='hidden' id='hidUserType' name="userType" value='0'/>
				</div>

			</div>
		</div>
	</form:form>
	<c:import url="/WEB-INF/inc/footer.jsp" />
</body>


</html>