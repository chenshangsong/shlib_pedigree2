<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
<c:import url="/WEB-INF/inc/link.jsp" />
</head>
<body>
	<div class="container">
		<h2 id="top">责任者列表</h2>
		<hr>
		<form role="form" id="listForm" class="form-inline">
			<input type="hidden" name="firstChar" id="firstChar" /> <input
				type="hidden" name="pageSize" value="90" /> <input type="hidden"
				name="dataTypeId" value="${dataTypeId}" id="hidDataTypeId" /> <input
				type="hidden" name="type" value="" /> <input type="hidden"
				name="tag" value=2 />
			<div id="wikiword" style="padding-bottom: 15px;">
				<c:forTokens
					items="全,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z"
					delims="," var="item">
					<span class="btn btn-default">${item}</span>
				</c:forTokens>
			<!-- 	<div class="form-group">
					<label><input type="checkbox" name="inference"
						id="inference"> 合并</label>
				</div> -->
			</div>
			<div class="form-group">
				<label>姓氏：</label> <input type="text" class="form-control"
					name="familyName" id="familyName" placeholder="姓氏">
			</div>
			<div class="form-group">
				<label>姓名：</label> <input type="hidden" name="uri" /> <input
					type="text" class="form-control" name="name" id="name"
					placeholder="在此输入姓名姓名">
			</div>
			<div class="form-group">
				<label>朝代：</label> <input type="text" class="form-control"
					name="time" id="time" placeholder="在此输入朝代">
			</div>
			<div class="form-group">
				<label>谱籍：</label> <input type="text" class="form-control"
					name="place" id="place" placeholder="在此输入谱籍地名">
			</div>
			<a class="btn btn-primary" id="btn_query"><i class="glyphicon glyphicon-search"></i>&nbsp;查 询</a>
			<!-- <a
				class="btn btn-primary" id="btn_Add">新增</a> -->
			<hr>
			<div class="data_list">请输入条件进行查询...</div>
			<hr>
			<div id="pager"></div>
		</form>
	</div>

	<div class="modal fade" id="personModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="myModalLabel"></h4>
				</div>
				${result.persons}
				<div class="modal-body"></div>
			</div>
		</div>
	</div>
	<script>
		$(function() {
			var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
			$('#btn_Add').on('click', function() {
				var url = '${ctx}/personManager/dataContentAdd';
				window.location.href = url;
				//parent.layer.close(index); //执行关闭
			});
		});
		function aClick(a) {
			//获取父窗口tbale
			var parentTable = parent.$(".table>tbody");
			//获取table中，选择的那一行
			var ifcheck = parentTable.find("tr").find("td:eq(2)").find(
					".checkFlg");
			ifcheck
					.each(function(i) {
						if ($(this).val() == '1') {
							//是否是多值
							var isMore = $(this).parent().find(".isMore").val();
							if (isMore == '1') {
								if ($(this).parent().find(".textNewValue")
										.val() != null
										&& $(this).parent().find(
												".textNewValue").val() != '') {
									//URI赋值
									$(this).parent().find(".textNewValue").val(
											$(this).parent().find(
													".textNewValue").val()
													+ "," + $(a).attr("auri"));
									//lable赋值
									$(this)
											.parent()
											.find(".spanNewValue")
											.text(
													$(this).parent().find(
															".spanNewValue")
															.text()
															+ ","
															+ $(a)
																	.attr(
																			"aname"));
								} else {
									//URI赋值
									$(this).parent().find(".textNewValue").val(
											$(a).attr("auri"));
									//lable赋值
									$(this).parent().find(".spanNewValue")
											.text($(a).attr("aname"));

								}
								parent.layer.msg('已选择 ' + '"'
										+ $(a).attr("aname") + '"'
										+ "，您还可以继续选择。", {
									shade : 0.2
								})
							} else {
								$(this).parent().find(".textNewValue").val(
										$(a).attr("auri"));
								//lable赋值
								$(this).parent().find(".spanNewValue").text(
										$(a).attr("aname"));
								var index = parent.layer
										.getFrameIndex(window.name); //获取窗口索引
								parent.layer.close(index);
								return false;
							}
						}
					});
		}
	</script>
	<c:import url="/WEB-INF/inc/footer.jsp" />
	<script src="${ctx}/res/plugin/page.js"></script>
<script src="${ctx}/res/js/sysmanager/ancestorsmanager/Conflist.js"></script>
</body>
</html>
