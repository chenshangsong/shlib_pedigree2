<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
<c:import url="/WEB-INF/inc/link.jsp" />

</head>
<body>
	<c:import url="/WEB-INF/inc/header3.jsp">
		<c:param name="menu" value="todolist" />
	</c:import>
	<!-- table  Begin -->
	<div class="container">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">
					<strong>数据维护</strong>
				</h3>
			</div>
			<input type='hidden' id="hidId" class="formSaveMaster" name="id"
				value="${data1.id}" />
			<table id="tableData" class="table">
				<thead>
					<tr>
						<!-- <th width='10%'>序号</th> -->
						<th width='10%'>名称</th>
						<th width='10%'>旧值</th>
						<th width='30%'>新值</th>
					</tr>
				</thead>
				<tbody id='tbodyData'>
					<c:forEach var="x" varStatus="status" items="${data1.editList}">
						<!-- 如果不是关闭状态的字段，则显示 -->
						<c:if test="${x.openFlg!='1' }">
							<tr class="text-left">
								<%-- <td width='5%'>${status.index + 1}</td> --%>
								<td width='10%'><span class="spanComment" comment="${x.oldComment}">${x.oldCnName}</span></td>
								<td width='10%'><c:if test="${x.oldValue!=''}">
										<c:if test="${x.isUri!='1'}">${x.oldValue}</c:if>
										<c:if test="${x.isUri=='1'}">
											<a href="${x.oldValue}">${x.oldValueCn}<i
												class="icon-hand-right"></i></a>
										</c:if>
									</c:if></td>
								<td width='30%'><c:if
										test="${x.oldEnName=='roleOfFamily' }">
										<select class="input form-control textNewValue " style="width:20%;">
											<option value=''>-请选择-</option>
											<option value=''>先祖</option>
											<option value=''>名人</option>
										</select>
									</c:if> <c:if test="${x.oldEnName!='roleOfFamily' }">
										<textarea rows='1'  type="text"
											<c:if test="${x.oldEnName=='relatedWork' }"> readonly="true" </c:if>
											class="input form-control textNewValue formSave"
											style="float: left; width:50%;" name="newValue" placeholder="在此输入新值" />${x.oldValue}</textarea>
											<span class="spanNewValue" style="float: left;">${x.oldValueCn}</span>
									<input type='hidden' id='hidCheckFlg' class='checkFlg'
									value='0' /> <input type='hidden' class="formSave oldEnName"
									name="oldEnName" value="${x.oldEnName}" /> <input
									type='hidden' class="formSave" name="oldCnName"
									value="${x.oldCnName}" /> <input type='hidden'
									class="formSave" name="oldCnNameUri" value="${x.oldCnNameUri}" />
									<input type='hidden' class="formSave" name="oldValue"
									value="${x.oldValue}" /> <input type='hidden' class="formSave"
									name="oldValueCn" value="${x.oldValueCn}" /> <input
									type='hidden' class="formSave" name="isUri" value="${x.isUri}" />
									<c:if test="${x.oldEnName!='relatedWork' }">
										<a class="btn_remove" style="float: right; padding-left: 5px;"
											href="javascript:void(0)" id="btn_Remove"><i
											class="icon-refresh"></i></a>
										<c:if test="${x.isUri=='1'}">
											<a href="javascript:void(0)" id="btn_Search"
												class="btnSearch" style="float: right;"> <i
												class="icon-list"></i>
											</a>
										</c:if>
									</c:if>
									</c:if> </td>
							</tr>
						</c:if>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<!-- /block -->
		<div style="float: right;">
			<button id="btn_save" class="btn btnSave btn-primary">
				<i class="icon-ok icon-white"></i>保存
			</button>
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
			//显示tips注释
			$(".spanComment").each(function(){
				$(this).mouseover(function(){
					layer.tips($(this).attr("comment"), this, {
					    tips: [2, 'RGB(250,165,35)'],
					    time: 2000
					});
				});
			});
			//检索
			$(".btnSearch").click(function() {
				//取消所有选中行
				$(".checkFlg").val('0');
				//选中该行
				$(this).parent().find(".checkFlg").val('1');
				var strContent = null;
				var strTitle=null;
				//如果是父
				if($(this).parent().find(".oldEnName").val()=="childOf"){
					strTitle = '先祖名人列表';
					 strContent='${ctx}/ConfPerson/personList';
				}
				else{
					strTitle = '家谱姓氏列表';
					strContent='${ctx}/dataManager/familyName';
				}
				//弹出姓氏搜索框
				layer.open({
					type : 2,
					title : strTitle,
					shadeClose : true,
					shade : 0.8,
					area : [ '90%', '90%' ],
					content : strContent //iframe的url
				});
			});
			//清空
			$(".btn_remove").click(function() {
				//选中该行
				$(this).parent().find(".textNewValue").val('');
				//选中该行
				$(this).parent().find(".spanNewValue").text('');
			});
			//保存按钮
			$(".btnSave").click(function() {
				var master = $(".formSaveMaster").toJson();
				var detailP = [];
				//得到List列表
				$.each($("#tableData>tbody tr"), function(i) {
					detailP.push($(this).find(".formSave").toJson());
				});
				//	alert(JSON.stringify(detailP));
				//拼接json列表
				var param = $.extend(master, {
					editList : detailP
				});
				requestJson("${ctx}/dataManager/doSave", param, successFun);
			});
			function successFun(data) {
				if (data.result == true) {
					alert("保存成功！");
					window.location.href = '${ctx}/dataManager/dataContentlist?id='
							+ $("#hidId").val();
				} else {
					alert("保存失败！");
				}
			}
		});
	</script>
</body>
</html>