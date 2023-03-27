<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
<c:import url="/WEB-INF/inc/editLink.jsp" />
</head>
<body>
	<c:import url="/WEB-INF/inc/header3.jsp">
		<c:param name="menu" value="systemSetting" />
	</c:import>
	<div class="container">                                      
		<h2 id="top">
			菜单修改<a href="${ctx}/systemSetting/menuList"> <span
				class="btn btn-default btn-sm pull-right" tag="back"><i
					class="glyphicon glyphicon-chevron-left"></i>&nbsp;菜单管理</span></a>
		</h2>
		<hr>
		<div class="row-fluid">
			<div class="span12">
				<form id="form_sample_1" class="form-horizontal">
					<fieldset>
						<div class="control-group">
							<label class="control-label">菜单名：<span class="required">*</span></label>
							<div class="controls">
								<input placeholder="请输入菜单名" value="${menu.menuName}"
									class="formSave" type="text" name="menuName" data-required="1"
									class="span6 m-wrap" />
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">菜单英文名：</label>
							<div class="controls">
								<span class="help-block">${menu.menuEnglishName}</span>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">菜单路径：</label>
							<div class="controls">
								<span class="help-block">${menu.menuFunction}</span>
							</div>
						</div>
						<div class="form-actions">
							<button id="btn_submit" class="btn btn-primary"><i class="icon-ok icon-white"></i>保 存</button>
						</div>
						<input value="${menu.id}" name="id" class="formSave" type="hidden" />
					</fieldset>
				</form>
			</div>
		</div>
	</div>

	<c:import url="/WEB-INF/inc/footer.jsp" />
	<script>
		jQuery(document).ready(function() {
			FormValidation.init();
		});
		$(function() {

			//保存
			$("#btn_submit").click(function() {
				//非法字符验证
				if(isUnlawfulStr(".formSave")){
				//消息提示
				showConfirmMsg(String.format(IMSG0003, "保存"), saveFunc);
				}
				//保存函数
				function saveFunc() {
				
						var parms = $('.formSave').toJson();
						requestJson("editMenu", parms, successFun);
					
				}
			});

			//成功回调函数
			function successFun(data) {
				if (data.result == '0') {
					showMsg(String.format(IMSG0004, "保存"));
				} else {
					showErrorMsg(String.format(EMSG9000, "保存"));
				}
			}
		});
	</script>
</body>
</html>