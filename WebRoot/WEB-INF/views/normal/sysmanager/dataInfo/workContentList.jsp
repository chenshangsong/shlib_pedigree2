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
				value="${work.id}" /> <input type='hidden' id="hidGraph"
				class="formSaveMaster" name="graphUri" value="${work.graphUri}" />
			<input type='hidden' id="hidPageUrl" class="formSaveMaster"
				name="pageUrl" value="${work.pageUrl}" /> <input type='hidden'
				id="hidDataType" class="formSaveMaster" name="dataType"
				value="${work.dataType}" />

			<table id="workTableData" class="table tableData">
				<thead>
					<tr>
						<th width='8%'>名称</th>
						<th width='20%'>旧值</th>
						<th width='40%'>新值</th>
						<th width='5%'></th>
					</tr>
				</thead>
				<tbody id='tbodyData'>
					<c:forEach var="x" varStatus="status" items="${work.editList}">
						<!-- 如果不是关闭状态的字段，则显示 -->
						<c:if test="${x.openFlg!='1' }">
							<tr class="text-left">
								<td width='10%'><span class="spanComment"
									comment="${x.oldComment}"> <c:if
											test="${x.oldCnName=='主题'}">
												姓氏</br>
												堂号
									</c:if> <c:if test="${x.oldCnName!='主题'}">
												${x.oldCnName}
									</c:if>
								</span></td>
								<td width='10%'><c:if test="${x.oldValue!=''}">
										<c:if test="${x.isUri!='1'}">
											<c:forEach var="thisO" items="${fn:split(x.oldValue,',')}">
												<c:set var="str" value="${thisO}" />
												<c:if test="${fn:contains(thisO, '@chs')}">
													简体：<c:set var="str"
														value="${fn:substringBefore(thisO,'@')}" />
												</c:if>
												<c:if test="${fn:contains(thisO, '@cht')}">
													繁体：<c:set var="str"
														value="${fn:substringBefore(thisO,'@')}" />

												</c:if>
												<c:out value="${str}" />
												<br>
											</c:forEach>
										</c:if>
										<c:if test="${x.isUri=='1'}">
											<c:set var="oldValueCnList"
												value="${fn:split(x.oldValueCn,',')}" />
											<c:forEach varStatus="j" var="thisO"
												items="${fn:split(x.oldValue,',')}">
												<c:set var="str" value="${thisO}" />
												<c:if test="${x.oldEnName=='title'}">
													<a target="_blank"
														href="${ctx}/titleManager/dataContentlist?id=${str}">${oldValueCnList[j.count-1]}<i
														class="icon-edit"></i></a>
												</c:if>
												<c:if test="${x.oldEnName!='title' }">
													<a target="_blank" href="${str}">${oldValueCnList[j.count-1]}<i
														class="icon-hand-right"></i></a>
												</c:if>
												<br>
											</c:forEach>
										</c:if>
									</c:if></td>
								<td width='40%' style="padding: 3px;"><c:if
										test="${x.oldEnName!='title' && x.oldEnName!='subject' && x.oldEnName!='titleFullName'}">
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
											<c:otherwise>
												<span style="float: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
											</c:otherwise>
										</c:choose>
										<textarea type="text"
											<c:if test="${x.oldEnName=='description'}"> rows='5'</c:if>
											class="input form-control textNewValue ${strLang}"
											style=" width:40%; <c:if test="${x.isUri=='1' }">display:none; </c:if>"
											placeholder="在此输入新值" />${x.oldValue}</textarea>
										<span class="spanNewValue" style="float: left;">${x.oldValueCn}</span>

									</c:if> <c:if
										test="${x.oldEnName=='title' || x.oldEnName=='subject' || x.oldEnName=='titleFullName' }">
										<c:set var="oldValueCnList"
											value="${fn:split(x.oldValueCn,',')}" />
										<c:forEach varStatus="j" var="thisO"
											items="${fn:split(x.oldValue,',')}">
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
											
											<c:otherwise>
												<span style="float: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
											</c:otherwise>
										</c:choose>
										
											<textarea type="text"
												<c:if test="${x.oldEnName=='description'}"> rows='5'</c:if>
												class="input form-control textNewValue ${strLang}"
												style=" width:40%; <c:if test="${x.isUri=='1' }">display:none; </c:if>"
												placeholder="在此输入新值" />${str}</textarea>
											<span class="spanNewValue" style="float: left;">${oldValueCnList[j.count-1]}</span>
											<c:if test="${j.count>0}">
												<br>
											</c:if>
										</c:forEach>
									</c:if> <input type='hidden' id='hidCheckFlg' class='checkFlg'
									value='0' /> <input type='hidden' class="formSave oldComment"
									name="oldComment" value="${x.oldComment}" /> <input
									type='hidden' class="formSave oldEnName" name="oldEnName"
									value="${x.oldEnName}" /> <input type='hidden'
									class="formSave" name="oldCnName" value="${x.oldCnName}" /> <input
									type='hidden' class="formSave" name="oldCnNameUri"
									value="${x.oldCnNameUri}" /> <input type='hidden'
									class="formSave" name="oldValue" value="${x.oldValue}" /> <input
									type='hidden' class="formSave" name="oldValueCn"
									value="${x.oldValueCn}" /> <input type='hidden'
									class="formSave hidNewValueCn" name="newValueCn" value="" /> <input
									type='hidden' class="formSave" name="isUri" value="${x.isUri}" />
									<input type='hidden' id="hidNewValue"
									class="formSave hidNewValue" name="newValue" value="" /> <input
									type='hidden' class="formSave isMore" name="isMore"
									value="${x.isMore}" /></td>
								<td width='5%'><c:if
										test="${x.oldEnName!='title' && x.oldEnName!='subject' }">
										<a class="btn_remove" style="float: right; padding-left: 5px;"
											href="javascript:void(0)" id="btn_Remove"><i
											class="icon-refresh"></i></a>
										<c:if test="${x.isUri=='1'}">
											<a href="javascript:void(0)" id="btn_Search"
												class="btnSearch" style="float: right;"> <i
												class="icon-list"></i>
											</a>
										</c:if>
									</c:if></td>
							</tr>
						</c:if>
					</c:forEach>
				</tbody>
			</table>
			<div class="panel-heading">
				<h3 class="panel-title">
					<strong>版本信息</strong>
				</h3>
			</div>
			<input type='hidden' id="hidInstanceId"
				class="formSaveMasterInstance" name="id" value="${instance.id}" />
			<input type='hidden' id="hidInstancepDataUri"
				class="formSaveMasterInstance" name="pdataUri" /> <input
				type='hidden' id="hidGraph" class="formSaveMasterInstance"
				name="graphUri" value="${instance.graphUri}" /> <input
				type='hidden' id="hidPageUrl" class="formSaveMasterInstance"
				name="pageUrl" value="${instance.pageUrl}" /> <input type='hidden'
				id="hidDataType" class="formSaveMasterInstance" name="dataType"
				value="${instance.dataType}" />
			<table id="instanceTableData" class="table tableNormalAndInstance">
				<thead>
					<tr>
						<!-- <th width='10%'>序号</th> -->
						<th width='10%'>名称</th>
						<th width='20%'>旧值</th>
						<th width='40%'>新值</th>
						<th width='5%'></th>
					</tr>
				</thead>
				<tbody id='tbodyData'>
					<c:forEach var="x" varStatus="status" items="${instance.editList}">
						<!-- 如果不是关闭状态的字段，则显示 -->
						<c:if test="${x.openFlg!='1' }">
							<tr class="text-left">
								<td width='10%'><span class="spanComment"
									comment="${x.oldComment}">${x.oldCnName}</span></td>
								<td width='10%'><c:if test="${x.oldValue!=''}">
										<c:if test="${x.isUri!='1'}">
											<c:forEach var="thisO" items="${fn:split(x.oldValue,',')}">
												<c:set var="str" value="${thisO}" />
												<c:if test="${fn:contains(thisO, '@chs')}">
													简体：<c:set var="str"
														value="${fn:substringBefore(thisO,'@')}" />
												</c:if>
												<c:if test="${fn:contains(thisO, '@cht')}">
													繁体：<c:set var="str"
														value="${fn:substringBefore(thisO,'@')}" />

												</c:if>
												<c:if test="${fn:contains(thisO, '@en')}">
													英文：<c:set var="str"
														value="${fn:substringBefore(thisO,'@')}" />
												</c:if>
												<c:out value="${str}" />
												<br>
											</c:forEach>
										</c:if>
										<c:if test="${x.isUri=='1'}">
											<a href="${x.oldValue}">${x.oldValueCn}<i
												class="icon-hand-right"></i></a>
										</c:if>
									</c:if></td>
								<td width='40%' style="padding: 3px;"><c:forEach
										var="thisO" items="${fn:split(x.oldValue,',')}">
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
											class="input form-control textNewValue ${strLang}"
											style=" width:40%; <c:if test="${x.isUri=='1' }">display:none; </c:if>"
											placeholder="在此输入新值" />${str}</textarea>
										
									</c:forEach> <span class="spanNewValue" style="float: left;">${x.oldValueCn}</span>
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
									value="${x.oldValueCn}" /> <input type='hidden'
									class="formSave hidNewValueCn" name="newValueCn" value="" /> <input
									type='hidden' class="formSave" name="isUri" value="${x.isUri}" />
									<input type='hidden' id="hidNewValue"
									class="formSave hidNewValue" name="newValue" value="" /> <input
									type='hidden' class="formSave isMore" name="isMore"
									value="${x.isMore}" /></td>
								<td width='5%'><c:if
										test="${x.oldEnName!='relatedWork' && x.oldEnName!='categoryType' }">
										<a class="btn_remove" style="float: right; padding-left: 5px;"
											href="javascript:void(0)" id="btn_Remove"><i
											class="icon-refresh"></i></a>
										<c:if test="${x.isUri=='1' }">
											<a href="javascript:void(0)" id="btn_Search"
												class="btnSearch" style="float: right;"> <i
												class="icon-list"></i>
											</a>
										</c:if>
									</c:if></td>
							</tr>
						</c:if>
					</c:forEach>
				</tbody>
			</table>
			<div class="panel-heading">
				<h3 class="panel-title">
					<strong>馆藏地</strong>
				</h3>
			</div>
			<input type='hidden' id="hidItemId" class="formSaveMasterItem"
				name="id" value="${item.id}" /> <input type='hidden' id="hidGraph"
				class="formSaveMasterItem" name="graphUri" value="${item.graphUri}" />
			<input type='hidden' id="hidItempDataUri" class="formSaveMasterItem"
				name="pdataUri" /> <input type='hidden' id="hidPageUrl"
				class="formSaveMasterItem" name="pageUrl" value="${item.pageUrl}" />
			<input type='hidden' id="hidDataType" class="formSaveMasterItem"
				name="dataType" value="${item.dataType}" />
			<table id="itemTableData" class="itemTableData table">
				<thead>
					<tr>
						<!-- <th width='10%'>序号</th> -->
						<th width='25%'>馆藏地</th>
						<th width='25%'>DOI</th>
						<th width='25%'>索书号</th>
						<th width='10%'>是否开放</th>
						<th width='10%'>操作（<a id='aAdd' style="color: #fff"
							href="javascript:void(0)">
								<!-- <i
								class="icon-plus"></i> -->+
						</a>）
						</th>
					</tr>
				</thead>
				<tbody id='tbodyData'>
					<!--map取法： ${x['org']} -->
					<c:forEach var="x" varStatus="j" items="${item.itemList}">
						<tr id="option${j.count-1}" class="text-left">
							<td><span class="spanNewValue">${x.oldValueCn}</span> <a
								class="btnitemOrg" onclick="openPlaceConf(this)"
								href="javascript:void(0)"><i class="icon-edit"></i></a></td>
							<td><input type='text' class="formSave" name="newValueCnDoi"
								value="${x.oldValueCnDoi}" /></td>
							<td><input type='text' class="formSave"
								name="newValueCnBook" value="${x.oldValueCnBook}" /></td>
							<td><input type='checkbox' class="formSave" 
								name="newValueAccessLevel" value="${x.oldValueAccessLevel}" /></td>
							<td><a href="javascript:void(0)"
								onclick='delRow(${j.count-1})'><i class="icon-remove"></i> </a>
								<input type='hidden' id='hidCheckFlg' class='checkFlg' value='0' />
								<input class="formSave uri" name="itemUri" type="hidden"
								value="${x.itemUri }" /> <input class="formSave helBy"
								name="oldValueUri" type="hidden" value="${x.oldValueUri }" /> <input
								class="formSave helByOrg" name="oldValueCn" type="hidden"
								value="${x.oldValueCn }" /> <input
								class="formSave helByNew textNewValue" name="newValueUri"
								type="hidden" value="${x.oldValueUri }" /> <input
								class="formSave helByOrgNew textNewCnValue" name="newValueCn"
								type="hidden" value="${x.oldValueCn }" /> <input
								class="formSave engName" name="engName" type="hidden"
								value="${x.engName }" /> <input class="formSave engNameBook"
								name="engNameBook" type="hidden" value="${x.engNameBook }" />
								<input type='hidden' class="formSave" name="oldValueCnBook"
								value="${x.oldValueCnBook}" />
								 <input class="formSave engNameDoi" name="engNameDoi" type="hidden"
								value="${x.engNameDoi }" /> <input type='hidden'
								class="formSave" name="oldValueCnDoi" value="${x.oldValueCnDoi}" />
								<input class="formSave engNameAccessLevel" name="engNameAccessLevel" type="hidden"
								value="${x.engNameAccessLevel }" /> <input type='hidden'
								class="formSave" name="oldValueAccessLevel" value="${x.oldValueAccessLevel}" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>

		<div class="panel panel-default">
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
		</div>

		<!-- /block -->
		<div class="adminDiv" style="float: right;">
			<button id="btn_saveAndRel" class="btn btn_saveAndRel btn-primary">
				<i class="icon-arrow-right icon-white"></i>保存并发布
			</button>
		</div>
		<div class="btn_saveDiv" style="float: right; margin-right: 5px;">
			<button id="btn_save" class="btn btnSave btn-primary">
				<i class="icon-ok icon-white"></i>保存
			</button>
		</div>
		<div style="float: right; margin-right: 5px;">
			<button id="btn_history" class="btn btn-primary">
				<i class="icon-list icon-white"></i>历史
			</button>
		</div>

	</div>

	<c:import url="/WEB-INF/inc/footer.jsp" />
	<script
		src="${ctx}/res/sysmanager/vendors/datatables/js/jquery.dataTables.min.js"></script>
	<script src="${ctx}/res/sysmanager/assets/scripts.js"></script>
	<script src="${ctx}/res/sysmanager/assets/DT_bootstrap.js"></script>
	<script src="${ctx}/res/commJS/roleFunction.js"></script>
	<script>
	var rowCount=0;
	//保存并发布区分
	var isRel='0';
	 //删除行  
	function delRow(rowIndex){  
	     $("#option"+rowIndex).remove();  
	     rowCount--;  
	 }
	 //添加行  
	function addRow(){  
	     rowCount++;  
	     var newRow='<tr id="option'+rowCount+'" class="text-left"><td ><span class="spanNewValue" ><font color="gray">请选择馆藏地...</font></span>'+
	     '<a class="btnitemOrg" onclick=openPlaceConf(this) href="javascript:void(0)"><i class="icon-edit"></i></a></td>'+
	     '<td><input type="text" class="formSave" name="newValueCnDoi" ></td>'+
	     '<td><input type="text"  class="formSave" name="newValueCnBook"></td>'+
	     '<td><input type="checkbox"  class="formSave"  name="newValueAccessLevel"></td>'+
	     '<td><a href="#" onclick=delRow('+rowCount+')><i class="icon-remove"></i></a>'+
	     '<input type="hidden" id="hidCheckFlg" class="checkFlg" value="0" /> '+	
	     '<input class="formSave uri" name="itemUri" type="hidden" value=""/>'+
				'<input class="formSave helBy" name="oldValueUri" type="hidden" value=""/>'+
				'<input class="formSave helByOrg" name="oldValueCn" type="hidden" value=""/>'+
				'<input class="formSave helByNew textNewValue" name="newValueUri" type="hidden" value=""/>'+
				'<input class="formSave helByOrgNew textNewCnValue" name="newValueCn" type="hidden" value=""/>'+
				'<input class="formSave engName" name="engName" type="hidden" value="bf:heldBy"/>'+
				'<input class="formSave engNameBook" name="engNameBook" type="hidden" value="bf:shelfMark"/>'+
				'<input class="formSave engNameDoi" name="engNameDoi" type="hidden" value="shl:DOI"/>'+
				'<input class="formSave engNameAccessLevel" name="engNameAccessLevel" type="hidden" value="shl:accessLevel"/>'+
				'</td></tr>';  
	     $("#itemTableData").append(newRow);  
	     InitcheckBoxClick();
	 }  
//机构弹框
 function openPlaceConf(thisa){
		//取消所有选中行
		$("#itemTableData").find(".checkFlg").val('0');
		//选中该行
		$(thisa).parents("tr").find(".checkFlg")
				.val('1');
		var strContent = null;
		var strTitle = null;
			strTitle = '机构列表';
			strContent = '${ctx}/organizationManager/confList';
			//弹出姓氏搜索框
			layer.open({
				type : 2,
				title : strTitle,
				shadeClose : true,
				shade : 0.8,
				area : [ '90%', '90%' ],
				content : strContent
			//iframe的url
			});
	}
	function InitcheckBoxClick(){
		//Item列表中，状态选中
		$("input[name='newValueAccessLevel']").click(function() {
			if($(this).is(':checked')){
				$(this).val('0')
			}
			else{
				$(this).val('1')
			}
			
		});
		
	}
		$(function() {
			InitcheckBoxClick();
			//item列表编号
			rowCount= $("#itemTableData>tbody tr").length-1; //行数默认4行  
			//item新增按钮
			$("#aAdd").click(function(){
				addRow();
			});
			
			//页面加载排他check
			initCheck();
			function initCheck() {
				var checkUrl = '${ctx}/dataHistory/isEditByOthers?dataUri='
						+ $("#hidId").val() + '&dataType='
						+ $("#hidDataType").val();
				requestJson(checkUrl, null, checkFun);
				function checkFun(data) {
					if (data.result != '0') {
						parent.layer.msg('该记录已被编辑且尚未发布,暂不允许保存', {
							shade : 0.3
						}, function() {
							$("#btn_save").attr('disabled', "true");
							$("#btn_saveAndRel").attr('disabled', "true");
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
			//Item列表中，状态选中
			$("input[name='newValueAccessLevel']").each(function() {
				if($(this).val()=='0'){
					$(this).attr('checked','checked');
				}
			});
		
			//历史按钮
			$("#btn_history").click(
					function() {
						var strContent = '${ctx}/dataWorkHistory/history?dataUri='
								+ $("#hidId").val() + '&dataType='
								+ $("#hidDataType").val();
						//弹出姓氏搜索框
						layer.open({
							type : 2,
							title : "历史记录",
							shadeClose : true,
							shade : 0.8,
							area : [ '90%', '90%' ],
							content : strContent
						//iframe的url
						});
					});
			//检索
			$(".btnSearch")
					.click(
							function() {
								var oldEnName = $(this).parents("tr").find(".oldEnName")
								.val();
								//取消所有选中行
								$(".checkFlg").val('0');
								//选中该行
								$(this).parents("tr").find(".checkFlg")
										.val('1');
								var strContent = null;
								var strTitle = null;
								//如果是其他责任者
								if (oldEnName =="contributor") {
									$(this).parents("tr").find(".isMore")
									.val('1');
									strTitle = '责任者列表';
									strContent = '${ctx}/personManager/confwriterList';
								}
								//如果是责任者
								else if (oldEnName == "creator") {
									strTitle = '责任者列表';
									strContent = '${ctx}/personManager/confwriterList';
								}//地点
								else if (oldEnName== "place") {
									strTitle = '谱籍列表';
									strContent = '${ctx}/placeManager/conflist';
								} 
								//地点
								else if (oldEnName == "subject") {
									strTitle = '堂号列表';
									strContent = '${ctx}/ancestraltempleManager/conflist';
								} 
								//分类
								else if (oldEnName == "category") {
									strTitle = '分类';
									strContent = '${ctx}/categoryManager/conflist?ctType=category';
								
								} 
								//版本类型
								else if (oldEnName== "edition") {
									strTitle = '版本类型';
									strContent = '${ctx}/categoryManager/conflist?ctType=edition';
								
								} 
								else {
									strTitle = '家谱姓氏列表';
									strContent = '${ctx}/dataManager/familyName';
								}
								//弹出姓氏搜索框
								layer.open({
									type : 2,
									title : strTitle,
									shadeClose : true,
									shade : 0.8,
									area : [ '90%', '90%' ],
									content : strContent
								//iframe的url
								});
							});
			//清空
			$(".btn_remove").click(function() {
				//选中该行
				$(this).parents("tr").find(".textNewValue").val('');
				//选中该行
				$(this).parents("tr").find(".spanNewValue").text('');
			});
			//保存并发布按钮
			$("#btn_saveAndRel").click(
					function() {
						//保存并发布
						isRel='1';
						//消息提示
					   showConfirmMsg(String.format(IMSG0003,"保存并发布"),saveFunc);
					});
			//保存按钮
			$(".btnSave").click(
					function() {
						isRel='0';
						//消息提示
					   showConfirmMsg(String.format(IMSG0003,"保存"),saveFunc);
					});
			   //保存函数
			   function saveFunc(){
				   var master = $(".formSaveMaster").toJson();
					var detailP = [];
					//得到List列表
					$.each($("#workTableData>tbody tr"), function(i) {
						//多值字段(多)
						var newTextList = $(this).find(".textNewValue");
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
							}
							else if (strClass.indexOf("strnone") >= 0) {
									tempValue += $(this).val() + ",";
								}
						});
						//截掉最后一个分隔符。
						if (tempValue.lastIndexOf(',') >= 0) {
							tempValue = tempValue.substring(0,
									tempValue.length - 1);
						}
						//将拼接后的数据赋予隐藏字段，传入后台
						$(this).find(".hidNewValue").val(tempValue);
						//uri时，存取中文值
						$(this).find(".hidNewValueCn").val($(this).find(".spanNewValue").text());
						detailP.push($(this).find(".formSave").toJson());
					});
					//拼接json列表
					var param = $.extend(master, {
						editList : detailP
					});
					//保存work
					requestJson("${ctx}/dataManager/doSave", param,
							successFun);
			   };
			  //保存instance
			function successFun(data) {
				if (data.result == true) {
					//父ID
					$("#hidInstancepDataUri").val(data.insertHistoryMainId);
					   var masterInstance = $(".formSaveMasterInstance").toJson();
						var detailPInstance = [];
					 //Instance得到List列表
						$.each($("#instanceTableData>tbody tr"), function(i) {
							//多值字段(多)
							var newTextList = $(this).find(".textNewValue");
							//临时值
							var tempValue = "";
							$.each(newTextList, function(j) {
								var strClass = $(this).attr("class");
								 if (strClass.indexOf("strnone") >= 0) {
										tempValue += $(this).val() + ",";
									}
							});
							//截掉最后一个分隔符。
							if (tempValue.lastIndexOf(',') >= 0) {
								tempValue = tempValue.substring(0,
										tempValue.length - 1);
							}
							//将拼接后的数据赋予隐藏字段，传入后台
							$(this).find(".hidNewValue").val(tempValue);
							//uri时，存取中文值
							$(this).find(".hidNewValueCn").val($(this).find(".spanNewValue").text());
							detailPInstance.push($(this).find(".formSave").toJson());
						});
						//拼接json列表
						var paramInstance = $.extend(masterInstance, {
							editList : detailPInstance
						});
					requestJson("${ctx}/dataManager/doSave", paramInstance,
							successFunItem);
				} else {
					showErrorMsg(String.format(EMSG9000, "保存"));
				}
			}
			//保存item
			function successFunItem(data) {
				if (data.result == true) {
					//父ID
					$("#hidItempDataUri").val(data.insertHistoryMainId);
					   var masterItem = $(".formSaveMasterItem").toJson();
						var detailPItem = [];
						//Item得到List列表
						$.each($("#itemTableData>tbody tr"), function(i) {
							//多值字段(多)
							var newTextList = $(this).find(".textNewValue");
							//临时值
							var tempValue = "";
							$.each(newTextList, function(j) {
								var strClass = $(this).attr("class");
								if (strClass.indexOf("strnone") >= 0) {
									tempValue += $(this).val() + ",";
								}
							});
							//截掉最后一个分隔符。
							if (tempValue.lastIndexOf(',') >= 0) {
								tempValue = tempValue.substring(0,
										tempValue.length - 1);
							}
							//将拼接后的数据赋予隐藏字段，传入后台
							$(this).find(".hidNewValue").val(tempValue);
							//uri时，存取中文值
							$(this).find(".hidNewValueCn").val($(this).find(".spanNewValue").text());
							detailPItem.push($(this).find(".formSave").toJson());
						});
						//拼接json列表
						var paramItem = $.extend(masterItem, {
							itemList : detailPItem
						});
					//Item保存方法，注意该方法为特殊处理方法
					requestJson("${ctx}/workManager/doSave", paramItem,
							successFunLast);
				} else {
					showErrorMsg(String.format(EMSG9000, "保存"));
				}
			}
			function successFunLast(data) {
				if (data.result == true) {
					//保存并发布
					if(isRel=='1'){
						//work在历史主表中的Id
						var id=$("#hidInstancepDataUri").val();
						requestJson("${ctx}/workManager/doRelease?id="+id, null, reload);
						showMsg(String.format(IMSG0004, "保存并发布"));
					}
					else{
						showMsg(String.format(IMSG0004, "保存"));
					}
					$("#btn_save").attr('disabled', "true");
					$("#btn_saveAndRel").attr('disabled', "true");
				} else {
					showErrorMsg(String.format(EMSG9000, "保存"));
				}
			}
		
		});
		//画面重载
		function reload(){
			//关闭后的操作
			window.location.href = '${ctx}/workManager/'
					+ 'dataContentlist?id=' + $("#hidId").val();
		}
		
	</script>
</body>
</html>