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
					<strong><a href='${ctx}/dataManager/datalist'>数据维护</a>><a
						id='aPUri'><span id="spanDataType"></span></a></strong>
				</h3>
			</div>
			<input type='hidden' id="hidId" class="formSaveMaster" name="id"
				value="${data.id}" /> <input type='hidden' id="hidGraph"
				class="formSaveMaster" name="graphUri" value="${data.graphUri}" />
			<input type='hidden' id="hidPageUrl" class="formSaveMaster"
				name="pageUrl" value="${data.pageUrl}" /> <input type='hidden'
				id="hidDataType" class="formSaveMaster" name="dataType"
				value="${data.dataType}" />
			<table id="tableData" class="table">
				<thead>
					<tr>
						<!-- <th width='10%'>序号</th> -->
						<th width='10%'>名称</th>
						<th width='40%'>新值</th>
						<th width='5%'></th>
					</tr>
				</thead>
				<tbody id='tbodyData'>
					<c:forEach var="x" varStatus="status" items="${data.editList}">
						<!-- 如果不是关闭状态的字段，则显示 -->
						<c:if test="${x.openFlg!='1' }">
							<tr class="text-left">
								<td width='10%'><span class="spanComment"
									comment="${x.oldComment}">${x.oldCnName}</span></td>
								<td width='40%' style="padding: 3px;"> 
										<c:forEach var="thisO" items="${fn:split(x.oldValue,',')}">
											<c:set var="str" value="${thisO}" />
											<c:set var="strLang" value="strnone" />
											<c:choose>
												<c:when test="${fn:contains(thisO, '@chs')}">
													<c:set var="strLang" value="strchs" />
													<span style="float: left;">简体：</span>
													<c:set var="str" value="${fn:substringBefore(thisO,'@')}" />
												</c:when>
												<c:when test="${fn:contains(thisO, '@cht')}">
													<c:set var="strLang" value="strcht" />
													<span style="float: left;">繁体：</span>
													<c:set var="str" value="${fn:substringBefore(thisO,'@')}" />
												</c:when>
												<c:when test="${fn:contains(thisO, '@en')}">
													<c:set var="strLang" value="stren" />
													<span style="float: left;">英文：</span>
													<c:set var="str" value="${fn:substringBefore(thisO,'@')}" />
												</c:when>
												<c:otherwise>
													<span style="float: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
												</c:otherwise>
											</c:choose>
											<textarea rows='1' type="text"
												<c:if test="${x.isUri=='1' }"> readonly="true" </c:if>
												class="input form-control textNewValue ${strLang}"
												style=" width:40%;"
												placeholder="在此输入新值" /><c:if test="${x.isUri=='1' }">${x.newValueCn} </c:if>${str}</textarea>
											<!-- <br> -->
										</c:forEach>
									
									<input type='hidden' id='hidCheckFlg' class='checkFlg'
									value='0' /> <input type='hidden' class="formSave oldComment"
									name="oldComment" value="${x.oldComment}" /> <input
									type='hidden' class="formSave oldEnName" name="oldEnName"
									value="${x.oldEnName}" /> <input type='hidden'
									class="formSave" name="oldCnName" value="${x.oldCnName}" /> <input
									type='hidden' class="formSave" name="oldCnNameUri"
									value="${x.oldCnNameUri}" /> <input type='hidden'
									class="formSave" name="oldValue" value="${x.oldValue}" /> <input
									type='hidden' class="formSave" name="oldValueCn"
									value="${x.oldValueCn}" /> <input
									type='hidden' class="formSave hidNewValueCn" name="newValueCn"
									value="${x.newValueCn}" /> <input type='hidden'
									class="formSave isUri" name="isUri" value="${x.isUri}" /> <input
									type='hidden' id="hidNewValue" class="formSave hidNewValue"
									name="newValue" value="${x.newValue}" /> <input type='hidden'
									class="formSave isMore" name="isMore" value="${x.isMore}" />
									</td>
								<td width='5%'><c:if test="${x.isUri!='1' }">
										<a class="btn_remove" style="float: right; padding-left: 5px;"
											href="javascript:void(0)" id="btn_Remove"><i
											class="icon-refresh"></i></a>
										
									</c:if> </td>
							</tr>
						</c:if>
					</c:forEach>
				</tbody>
			</table>
		</div>

	<!-- 	<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">
					<strong>备注</strong>
				</h3>
				<br />
				<p>
					<textarea rows='4' type="text"
						class="input form-control textNewValue formSaveMaster"
						style="width: 100%;" name="remarks" placeholder="在此输入备注" /></textarea>
				</p>
			</div>
		</div> -->

		<!-- /block -->
		<div class="btn_saveDiv" style="float: right; margin-right: 5px;">
			<button id="btn_save" class="btn btnSave btn-primary">
				<i class="icon-ok icon-white"></i>保存并发布
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
			//页面加载排他check
			initCheck();
			function initCheck(){
				var checkUrl = '${ctx}/dataHistory/isEditByOthers?dataUri='
					+ $("#hidId").val() + '&dataType='
					+ $("#hidDataType").val();
				requestJson(checkUrl, null,
						checkFun);
				function checkFun(data){
					if (data.result != '0') {
						parent.layer.msg('该记录已被编辑且尚未发布,暂不允许保存并发布', {shade: 0.3}, function(){
							$("#btn_save").attr('disabled',"true");
						})
					}
				}
			}
			//规范数据类型
			$("#spanDataType").text(
					$.dataTypeGroup.getName($("#hidDataType").val()));
			//根据规范数据类型得到Uri
			$("#aPUri")
					.attr(
							'href',
							'${ctx}/'
									+ $.dataUriGroup.getName($("#hidDataType")
											.val()));
			//显示tips注释
			$(".spanComment").each(function() {
				$(this).mouseover(function() {
					layer.tips($(this).attr("comment"), this, {
						tips : [ 2, 'RGB(250,165,35)' ],
						time : 3000
					});
				});
			});
			//清空
			$(".btn_remove").click(function() {
				//选中该行
				$(this).parents("tr").find(".textNewValue").val('');
			});
			//保存并发布按钮
			$(".btnSave").click(
					function() {
						
						//消息提示
					    showConfirmMsg(String.format(IMSG0003,"保存并发布"),saveFunc);
						
					  	//保存并发布函数
					   function saveFunc(){
						   var master = $(".formSaveMaster").toJson();
							var detailP = [];
							//得到List列表
							$.each($("#tableData>tbody tr"), function(i) {
								//多值字段(多)
								var newTextList = $(this).find(".textNewValue");
								//uri标识
								var isUri = $(this).find(".isUri").val();
								//临时值
								var tempValue = "";
								$.each(newTextList, function(j) {
									var strClass = $(this).attr("class");
									//拼音
									if (strClass.indexOf("stren") >= 0) {
										tempValue += $(this).val() + "@en,";
									}
									//繁体
									else if (strClass.indexOf("strcht") >= 0) {
										tempValue += $(this).val() + "@cht,";
									}
									//简体
									else if (strClass.indexOf("strchs") >= 0) {
										tempValue += $(this).val() + "@chs,";
									} else if (strClass.indexOf("strnone") >= 0) {
										//如果不是Uri格式，则进行赋值
									
										tempValue = $(this).val();
										
									}
								});
								//截掉最后一个分隔符。
								if (tempValue.lastIndexOf(',') > 0) {
									tempValue = tempValue.substring(0,
											tempValue.length - 1);
								}
								//将拼接后的数据赋予隐藏字段，传入后台
									if(isUri!='1'){
								          $(this).find(".hidNewValue").val(tempValue);
									}
								detailP.push($(this).find(".formSave").toJson());
							});
							//	alert(JSON.stringify(detailP));
							//拼接json列表
							var param = $.extend(master, {
								editList : detailP
							});
							//保存并发布事件
							requestJson("${ctx}/categoryManager/doSaveAdd", param,
									successFun);
					   }
					});

			function successFun(data) {
				if (data.result == true) {
					showMsg(String.format(IMSG0004,"保存并发布"));
					$("#btn_save").attr('disabled',"true");
					//id值
					$("#hidId").val(data.id);
					//得到data值
					var dataType = $("#hidDataType").val();
					//根据pageType得到pageURL
					var pageUrl = $.pageUrlGroup.getName(dataType);
					/* window.open('${ctx}/' + pageUrl + 'dataContentlist?id='
							+ $("#hidId").val(), '_self'); */
				} else {
					showErrorMsg(String.format(EMSG9000,"保存并发布"));
				}
			}
		});
	</script>
</body>
</html>