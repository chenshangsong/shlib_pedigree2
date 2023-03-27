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
		<c:param name="menu" value="personInfo" />
	</c:import>
	<div class="container">
		<h2 id="top">
			反馈信息 <a href="${ctx}/feedBack/feedBackMainList"> <span
				class="btn btn-default btn-sm pull-right" tag="back"><i
					class="glyphicon glyphicon-chevron-left"></i>&nbsp;反馈列表</span></a>
		</h2>
		<hr><div class="row-fluid">
			<div class="span12">
				<form id="form_sample_1" class="form-horizontal">
					<fieldset>
					<div class="control-group">
							<label class="control-label">文献题名：</label>
							<div class="controls">
								<span class="help-block"><a target="_blank" href="${ctx}/service/work/list#uri=${detail.genealogyUri}">${detail.genealogyTitle}</a></span>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">标题：</label>
							<div class="controls">
								<span class="help-block">${detail.title}</span>
							</div>
							<input type="hidden" name="genealogyUri"
								value="${detail.genealogyUri}" />
						</div>
						<div class="control-group">
							<label class="control-label">内容：</label>
							<div class="controls">
								<textarea name="feedbackContent" readonly="true"
									class="input-xlarge textarea"
									style="width: 500px; height: 100px">${detail.feedbackContent}</textarea>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">反馈人：</label>
							<div class="controls">
								<span class="help-block">${detail.feedbackUser}</span>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">专家回复：<span class="required">*</span></label>
							<div class="controls">
								<textarea class="formSave required" placeholder="请输入回复内容" type="text"
									name="postilContent"
									<c:if test="${detail.roleId !='2' &&  detail.roleId !='3'}"> readonly="true" </c:if>
									class="input-xlarge textarea span6 m-wrap"
									style="width: 500px; height: 100px">${detail.postilContent}</textarea>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">回复人：</label>
							<div class="controls">
								<span class="help-block">${ctxRoleId.getUserName()}</span>
							</div>
						</div>
						<div class="form-actions">
							<button id="btn_submit"
								<c:if test="${detail.roleId !='2' &&  detail.roleId !='3' }"> disabled="disabled" </c:if>
								class="btn btn-primary">
								<i class="icon-ok icon-white"></i>提交
							</button>
						</div>
						<input class="formSave" type="hidden" name="markId"
							value="${detail.markId}" /> <input class="formSave"
							type="hidden" name="feedbackMainId"
							value="${detail.feedbackMainId}" />
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
			$("#btn_submit").click(function() {
				//消息提示
				showConfirmMsg(String.format(IMSG0003, "提交"), saveFunc);
				//保存函数
				function saveFunc() {
					var parms = $('.formSave').toJson();
					requestJson("doFeedBackUpdate", parms, successFun);
				}
			});

			//成功回调函数
			function successFun(data) {
				if (data.result == '0') {
					showMsg(String.format(IMSG0004, "提交"));
					window.open('${ctx}/feedBack/feedBackMainList', '_self');
				} else {
					showErrorMsg(String.format(EMSG9000, "提交"));
				}
			}
		});
	</script>
</body>
</html>