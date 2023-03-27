<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
<c:import url="/WEB-INF/inc/link.jsp" />
</head>
<body>
	<!-- table  Begin -->
	<div class="container">
		<table id="tableHistoryData" class="table">
			<thead>
				<tr>
					<!-- <th >序号</th> -->
					<th width="10%">修改时间</th>
					<th width="10%">修改人</th>
					<th width="10%">发布状态</th>
					<th width="10%">发布时间</th>
					<th width="10%">发布人</th>
					<th width="20%">备注</th>
					<th width="30%">操作</th>
				</tr>
			</thead>
			<tbody id='tbodyData'>
				<c:forEach var="x" varStatus="status" items="${data}">
					<tr class="text-left">
						<td width='5%'>${x.createDate}</td>
						<td width='5%'>${x.createUser}</td>
						<td width='5%'><c:if test="${x.checkStatus=='0' }">
								<font color='red'>未发布</font>
							</c:if> <c:if test="${x.checkStatus=='1' }">
						已发布
						</c:if></td>
						<td width='5%'>${x.releaseDate}</td>
						<td width='5%'>${x.releaseUser}</td>
						<td width='10%'>${x.remarks}<input type='hidden'
							class="formSave" name="graphUri" value="${x.graphUri}" /> <input
							type='hidden' class="formSave id" name="id" value="${x.id}" /> <input
							type='hidden' class="formSave dataUri" name="dataUri"
							value="${x.dataUri}" /> <input type='hidden'
							class="formSave datatype" name="dataType" value="${x.dataType}" />
						</td>
						<td width='10%' class="center">
							<div style="float: right;">
							
								<button id="btn_historyDetailed"
									class="btn btn-primary btn_historyDetailed">
									<i class="icon-list-alt icon-white"></i>详细
								</button>
							</div> <c:if test="${x.checkStatus=='0' }">
								<div style="float: right;">
									<button id="btn_historyRelease"
										class="btn btn-primary btn_historyRelease">
										<i class="icon-arrow-right icon-white"></i>核准
									</button>
									&nbsp;&nbsp;
								</div>
								<div style="float: right;">
									<button id="btn_historyDelete" class="btn_historyDelete btn btn-danger">
										<i class="icon-remove icon-white"></i>删除
									</button>
									&nbsp;&nbsp;
								</div>
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

	</div>
</body>
<script src="../res/commJS/layer/layer.js"></script>
<script>
	var dataId, pageUrl, rowIndex;
	//发布
	$(".btn_historyRelease").click(function() {
		var $this = $(this);
		//发布提示
	    showConfirmMsg(String.format(IMSG0003,"发布"),releaseFunc);
	   	function releaseFunc(){
	   		//var formSave = $this.parents("tr").find(".formSave");
			var dataType = $this.parents("tr").find(".datatype").val();
			dataId = $this.parents("tr").find(".dataUri").val();
			var id = $this.parents("tr").find(".id").val();
			pageUrl = $.pageUrlGroup.getName(dataType);
			//var param = formSave.toJson();
			//requestJson("${ctx}/workManager/doRelease", param, successReleaseFun);
			requestJson("${ctx}/workManager/doRelease?id="+id, null, successReleaseFun);
	   	}
	});

	//发布回调函数
	function successReleaseFun(data) {
		if (data.result == true) {
			//alert('发布成功');
			var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
			parent.layer.msg('发布成功 ', {
				shade : 0.1
			}, function() {
				//关闭后的操作
				parent.location.href = '${ctx}/' + pageUrl
						+ 'dataContentlist?id=' + dataId;
				parent.layer.close(index);
			})

		} else {
			showErrorMsg(String.format(EMSG9000,"发布"));
		}
	}
	//删除
	$(".btn_historyDelete").click(function() {
		var $this = $(this);
		//发布提示
	    showConfirmMsg(String.format(IMSG0003,"发布"),deleteFunc);
	   	function deleteFunc(){
	   		var formSave = $this.parents("tr").find(".formSave");
			var param = formSave.toJson();
			var dataType = $this.parents("tr").find(".datatype").val();
			dataId = $this.parents("tr").find(".dataUri").val();
			pageUrl = $.pageUrlGroup.getName(dataType);
			rowIndex = $(".text-left").index($this.parents("tr"));
			requestJson("${ctx}/workManager/doDelete", param, successDeleteFun);
	   	}
	});

	//删除回调函数
	function successDeleteFun(data) {
		if (data.result == true) {
			parent.layer.msg('删除成功 ', {
				shade : 0.1
			}, function() {
				//删除该行
				$('#tableHistoryData tr:eq(' + (rowIndex + 1) + ')').remove();
				//启用父页面保存按钮
				//$("#btn_save" , parent.document).removeAttr('disabled')
				//关闭后的操作
				parent.location.href = '${ctx}/' + pageUrl
						+ 'dataContentlist?id=' + dataId;
				parent.layer.close(index);
			})
		} else {
			showErrorMsg(String.format(EMSG9000,"删除"));
		}
	}

	//详细按钮
	$(".btn_historyDetailed").click(function() {
		var id = $(this).parents("tr").find(".id").val();
		
		var strContent = '${ctx}/dataWorkHistory/confPropertylist?id=' + id;
		//弹出姓氏搜索框
		layer.open({
			type : 2,
			title : "属性列表",
			shadeClose : true,
			shade : 0.8,
			area : [ '90%', '90%' ],
			content : strContent
		//iframe的url
		});
	});
</script>
</html>