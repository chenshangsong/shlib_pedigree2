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
			<h2 id="top">反馈信息
				<a href="${ctx}/feedBack/feedBackMainList"> <span
				class="btn btn-default btn-sm pull-right" tag="back"><i
					class="glyphicon glyphicon-chevron-left"></i>&nbsp;反馈列表</span></a>
			</h2>
		
			<hr>

			<div class="row-fluid">
				<div class="span12">
					<form id="form_sample_1" class="form-horizontal">
						<fieldset>
						<input type="hidden" class="formSave"  name="genealogyUri" value="${detail.genealogyUri}"/> 
							<div class="control-group">
								<label class="control-label">标题：<span
									class="required">*</span></label>
								<div class="controls">
									<input class="formSave" type="text" name="title" data-required="1" class="span6 m-wrap"/>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">反馈内容：</label>
								<div class="controls">
									<textarea class="formSave" name="feedbackContent"
										class="input-xlarge textarea" placeholder="请输入反馈内容"
										style="width: 500px; height: 100px"></textarea>
								</div>
							</div>
							<div class="form-actions">
								<button id="btn_submit"
									class="btn btn-primary"><i class="icon-ok icon-white"></i>提交</button>
							</div>
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
								requestJson("doFeedBackInsert", parms, successFun);
						   }
					});

			//成功回调函数
			function successFun(data) {
				if (data.result == '0') {
					showMsg(String.format(IMSG0004,"提交"));
					window.open('${ctx}/feedBack/feedBackMainList', '_self');
				} else {
					showErrorMsg(String.format(EMSG9000,"提交"));
				}
			}
		});
	</script>
</body>
</html>