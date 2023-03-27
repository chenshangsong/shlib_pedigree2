<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
<c:import url="/WEB-INF/inc/editLink.jsp" />
</head>
<body>
<div class="container">
		<h2 id="top">撰写反馈</h2>
		<hr>
		<div class="row-fluid">
			<div class="span12">
				<form id="form_sample_1" class="form-horizontal">
					<fieldset>
						<input type="hidden" class="formSave" name="genealogyUri"
							value="${model.genealogyUri}" />
							<input type="hidden" class="formSave" name="genealogyTitle"
							value="${model.genealogyTitle}" />
							<div class="control-group">
							<label class="control-label">文献题名：</label>
							<div class="controls">
								<span class="help-block">${model.genealogyTitle}</span>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">标题：<span class="required">*</span></label>
							<div class="controls">
								<input class="formSave" type="text" name="title"
									data-required="1" class="span6 m-wrap" />
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">内容：</label>
							<div class="controls">
								<textarea class="formSave" name="feedbackContent"
									class="input-xlarge textarea" placeholder="请输入反馈内容"
									style="width: 500px; height: 100px"></textarea>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">反馈人：</label>
							<div class="controls">
								<span class="help-block">${ctxRoleId.getUserName()}</span>
							</div>
							
						</div>
						<div style="float:right;">
							<button id="btn_submit" class="btn btn-primary"><i class="icon-ok icon-white"></i>提交</button>
						</div>
					</fieldset>
				</form>
			</div>
		</div>
	</div>
	<!-- table  Begin -->
	<div class="container">
		<div class="panel panel-default">
			<div class="row-fluid">
				<!-- block -->
				<div class="block">
					<div class="block-content collapse in">
						<div class="span12">

							<table cellpadding="0" cellspacing="0" border="0"
								class="table" id="example">
								<thead>
									<tr>
									<th width='5%'>序号</th>
										<th width='15%'>标题</th>
										<th width='15%'>反馈内容</th>
										<th width='8%'>反馈人</th>
										<th width='8%'>反馈日期</th>
										<th width='15%'>回复内容</th>
										<th width='8%'>回复专家</th>
										<th width='8%'>回复日期</th>
										<th width='5%'>状态</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="x" varStatus="status" items="${data}">
										<tr class="odd gradeA">
										<td width='5%'>${status.index + 1}</td>
											<td width='10%'>${x.title}</td>
											<td width='10%'>${x.feedbackContent}</td>
											<td width='10%'>${x.createdname }</td>
											<td width='10%'>${fn:replace(x.createdDate, "00:00:00.0", "")}</td>
											<td width='10%'>${x.postilContent}</td>
											<td width='10%'>${x.postilname }</td>
											<td width='10%'>${fn:replace(x.postilDate, "00:00:00.0", "")}</td>
											<td width='10%'><c:if test="${x.status=='未回复'}">
													<font color='red'>${x.status}</font>
												</c:if> <c:if test="${x.status=='已回复'}">
													<font>${x.status}</font>
												</c:if></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<!-- /block -->
			</div>
		</div>
	</div>

</body>
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
					requestJson("doFeedBackInsert", parms, successFun);
				}
			});

			//成功回调函数
			function successFun(data) {
				if (data.result == '0') {
					showMsg(String.format(IMSG0004, "提交"));
					//alert('发布成功');
					var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
					parent.layer.msg('反馈成功 ', {
						//shade : 0.1
						msgTime : 1
					})
					parent.layer.close(index);

				} else {
					showErrorMsg(String.format(EMSG9000, "反馈"));
				}
			}
		});
	</script>

</html>