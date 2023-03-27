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
		<c:param name="menu" value="todolist" />
	</c:import>
	<div class="container">
			<h2 id="top">取值词表类别维护	<a href="${ctx}/categoryManager/categoryTypelist"> <span
			class="btn btn-default btn-sm pull-right" tag="back"><i
				class="glyphicon glyphicon-chevron-left"></i>&nbsp;取值词表类别列表</span></a></h2>
			<hr>

			<div class="row-fluid">
				<div class="span12">
					<form id="form_sample_1" class="form-horizontal">
						<fieldset>
							<div class="control-group">
								<label class="control-label">类别中文名：<span
									class="required">*</span></label>
								<div class="controls">
									<input placeholder="请输入类别中文名" value="${data.categoryNameCn}" class="formSave" type="text" name="categoryNameCn" data-required="1" class="span6 m-wrap"/>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">类别英文名：<span
									class="required">*</span></label>
								<div class="controls">
									<input placeholder="请输入类别英文名" value="${data.categoryNameEn}" class="formSave" type="text" name="categoryNameEn" data-required="1" class="span6 m-wrap"/>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">用于数据集：</label>
								<div class="controls">
									<input placeholder="请输入用于数据集" value="${data.categoryUriIn}" class="formSave" type="text" name="categoryUriIn" data-required="1" class="span6 m-wrap"/>
								</div>
							</div>
							<div class="form-actions">
								<button id="btn_submit"
									class="btn btn-primary"><i class="icon-ok icon-white"></i>保 存</button>
									
							</div>
							<input value="${data.id}" name="id" class="formSave" type="hidden" />
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

			//提交
			$("#btn_submit").click(
					function() {
						 //消息提示
					     showConfirmMsg(String.format(IMSG0003,"提交"),saveFunc);
						 //保存函数
						   function saveFunc(){
							   var parms = $('.formSave').toJson();
								requestJson("categoryTypeSubmit", parms, successFun);
						   }
					});
			
			//成功回调函数
			function successFun(data) {
				if (data.result == '0') {
					showMsg(String.format(IMSG0004,"提交"));
					window.open('${ctx}/categoryManager/categoryTypelist', '_self');
				} else {
					showErrorMsg(String.format(EMSG9000,"提交"));
				}
			}
		});
	</script>
</body>
</html>