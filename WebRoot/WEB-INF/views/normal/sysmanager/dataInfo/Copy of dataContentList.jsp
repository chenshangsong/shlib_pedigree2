<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>

<link href="${ctx}/res/sysmanager/bootstrap/css/bootstrap.min.css"
	rel="stylesheet" media="screen">
<link
	href="${ctx}/res/sysmanager/bootstrap/css/bootstrap-responsive.min.css"
	rel="stylesheet" media="screen">
<link href="${ctx}/res/sysmanager/assets/DT_bootstrap.css"
	rel="stylesheet" media="screen">
<c:import url="/WEB-INF/inc/sysmanagerlink.jsp" />
</head>
<body>
	<c:import url="/WEB-INF/inc/sysHeader.jsp">
		<c:param name="menu" value="todolist" />
	</c:import>
	<!-- table  Begin -->
	<div class="container">
		<div class="panel panel-default">
			<h2 id="top">数据维护</h2>
			<hr>
			<div class="row-fluid">
				<!-- block -->
				<div class="block">
					<div class="block-content collapse in">
						<div class="span12">
							<table id='tableData' cellpadding="0" cellspacing="0" border="0"
								class="table table-striped " id="example">
								<thead>
									<tr>
										<th width='5%'>序号</th>
										<th width='10%'>英文名称</th>
										<th width='10%'>中文名称</th>
										<th width='10%'>旧值</th>
										<th width='30%'>新值</th>
									</tr>
								</thead>
								<tbody id='tbodyData'>
									<c:forEach var="x" varStatus="status" items="${data1}">
										<tr class="odd gradeA">
											<td width='5%'>${status.index + 1}</td>
											<td width='10%'>${x.oldEnName}</td>
											<td width='10%'>${x.oldCnName}</td>
											<td width='10%'><a href="${x.oldValue}">${x.oldValue}</a>${x.oldValueCn}</td>
											<td width='30%'><input type="text"
												class="input form-control textNewValue formSave"
												name="newValue" value="" style='height: 40px; width: 60%'
												placeholder="在此输入新值"> <span class="spanNewValue"></span>
												<input type='hidden' id='hidCheckFlg' class='checkFlg'
												value='0' /> <input type='hidden' class="formSave"
												name="oldEnName" value="${x.oldEnName}" /> <input
												type='hidden' class="formSave" name="oldCnName"
												value="${x.oldCnName}" /> <input type='hidden'
												class="formSave" name="oldCnNameUri"
												value="${x.oldCnNameUri}" /> <input type='hidden'
												class="formSave" name="oldValue" value="${x.oldValue}" /> <input
												type='hidden' class="formSave" name="oldValueCn"
												value="${x.oldValueCn}" /> <a href="javascript:void(0)"
												id="btn_Search" class="btnSearch"> <i class="icon-list"></i>
											</a> <a href="javascript:void(0)" id="btn_Save"><i
													class="icon-remove"></i></a></td>


										</tr>
									</c:forEach>
								</tbody>
							</table>
							<div style="float:right;padding:5px 5px 5px 5px;">
							<button id="btn_save" class="btn btnSave btn-primary">
								<i class="icon-pencil icon-white"></i>保存
							</button>
						</div>
						</div>
						
					</div>
				</div>
				<!-- /block -->
			</div>
		</div>
	</div>
	<c:import url="/WEB-INF/inc/footer.jsp" />
	<script
		src="${ctx}/res/sysmanager/vendors/datatables/js/jquery.dataTables.min.js"></script>
	<script src="${ctx}/res/sysmanager/assets/scripts.js"></script>
	<script src="${ctx}/res/sysmanager/assets/DT_bootstrap.js"></script>
	<script src="../res/commJS/layer/layer.js"></script>
	<script>
		$(function() {
			//检索
			$(".btnSearch").click(function() {
				//取消所有选中行
				$(".checkFlg").val('0');
				//选中该行
				$(this).parent().find(".checkFlg").val('1');
				//弹出姓氏搜索框
				layer.open({
					type : 2,
					title : '家谱姓氏列表',
					shadeClose : true,
					shade : 0.8,
					area : [ '90%', '90%' ],
					content : '${ctx}/dataManager/familyName' //iframe的url
				});
				//获取本体唯一属性:去除空格
				var id = $.trim($(this).parents('tr').find('td:eq(2)').text());
				//window.location.href = '${ctx}/dataManager/familyName';
				/* 	 $.get("${ctx}/dataManager/familyName",null, function(data){
						  $("#personModal .modal-body").html(data);
				            hideLoading();
				            $("#personModal").modal("show");
				        }); */
			});

			//保存按钮
			$(".btnSave").click(function() {
				var param = [];
				var p = $(this).parents('tr').find('td:eq(1)').text();
				param.push({
					"oldEnName" : p
				});
				var parms = $(this).parents('tr').find(".formSave").toJson();
				//alert(JSON.stringify(parms));
				requestJson("${ctx}/dataManager/doSave", parms, successFun);
			});
			function successFun(data) {
				if (data.result == true) {
					alert("保存成功！");
				} else {
					alert("登录失败！");
				}
			}

		});
	</script>
</body>
</html>