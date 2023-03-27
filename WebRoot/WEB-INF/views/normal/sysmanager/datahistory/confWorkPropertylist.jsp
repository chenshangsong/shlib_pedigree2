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
		<div class="panel panel-default">
			<table id="tableData" class="table">
				<thead>
					<tr>
						<!-- <th width='10%'>序号</th> -->
						<th width="10%">名称</th>
						<th width="10%">前一版本值</th>
						<th width="30%">当前版本值</th>
					</tr>
				</thead>
				<tbody id="tbodyData">
					<c:forEach var="x" varStatus="status" items="${workData}">
						<tr class="text-left">
							<td width='10%'><span class="spanComment"
								comment="${x.oldComment}">${x.oldCnName}</span></td>
							<td width="10%" style="color: red;"><c:if
									test="${x.oldValue!=''}">
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
								</c:if> <c:if test="${x.isUri=='1'}">
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
												<c:if test="${x.oldEnName!='title'}">
													<a target="_blank" href="${str}">${oldValueCnList[j.count-1]}<i
														class="icon-hand-right"></i></a>
												</c:if>
												<br>
											</c:forEach>
										</c:if></td>
							<td width="30%"><c:if test="${x.newValue!=''}">
									<c:if test="${x.isUri!='1'}">
										<c:forEach var="thisO" items="${fn:split(x.newValue,',')}">
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
								</c:if> <c:if test="${x.isUri=='1'}">
									<a href="${x.newValue}">${x.newValueCn}<i
										class="icon-hand-right"></i></a>
								</c:if></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="panel-heading">
				<h3 class="panel-title">
					<strong>版本信息</strong>
				</h3>
			</div>
			<table id="tableData" class="table">
				<thead>
					<tr>
						<!-- <th width='10%'>序号</th> -->
						<th width="10%">名称</th>
						<th width="10%">前一版本值</th>
						<th width="30%">当前版本值</th>
					</tr>
				</thead>
				<tbody id="tbodyData">
					<c:forEach var="x" varStatus="status" items="${instanceData}">
						<tr class="text-left">
							<td width='10%'><span class="spanComment"
								comment="${x.oldComment}">${x.oldCnName}</span></td>
							<td width="10%" style="color: red;"><c:if
									test="${x.oldValue!=''}">
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
								</c:if> <c:if test="${x.isUri=='1'}">
									<a href="${x.oldValue}">${x.oldValueCn}<i
										class="icon-hand-right"></i></a>
								</c:if></td>
							<td width="30%"><c:if test="${x.newValue!=''}">
									<c:if test="${x.isUri!='1'}">
										<c:forEach var="thisO" items="${fn:split(x.newValue,',')}">
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
								</c:if> <c:if test="${x.isUri=='1'}">
									<a href="${x.newValue}">${x.newValueCn}<i
										class="icon-hand-right"></i></a>
								</c:if></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="panel-heading">
				<h3 class="panel-title">
					<strong>馆藏信息</strong>
				</h3>
			</div>
			<table id="itemTableData" class="itemTableData table">
				<thead>
					<tr>
					<th width='10%'>名称</th>
						<th width='10%'>前一版本值</th>
						<th width='30%'>当前版本本值</th>
					</tr>
				</thead>
				<tbody id='tbodyData'>
					<!--map取法： ${x['org']} -->
					<c:forEach var="x" varStatus="j" items="${itemData}">
					 <c:if test="${x.oldValueCn!=x.newValueCn}">
						<tr>
							<td >馆藏地</td>
							<td style="color: red;">${x.oldValueCn}</td>
							<td>${x.newValueCn}</td>
					    </tr>
					</c:if>
					 <c:if test="${x.oldValueCnDoi!=x.newValueCnDoi}">
						<tr>
							<td >DOI</td>
							<td style="color: red;">${x.oldValueCnDoi}</td>
							<td>${x.newValueCnDoi}</td>
						</tr>
					</c:if>
					 <c:if test="${x.oldValueCnBook!=x.newValueCnBook}">
						<tr>
							<td >索取号</td>
							<td style="color: red;">${x.oldValueCnBook}</td>
							<td>${x.newValueCnBook}</td>
						</tr>
				    </c:if>
				    <c:if test="${x.oldValueAccessLevel!=x.newValueAccessLevel}">
						<tr>
							<td >开关状态</td>
							<td style="color: red;">${x.oldValueAccessLevel=="0"?"开":"关"}</td>
							<td>${x.newValueAccessLevel=="0"?"开":"关"}</td>
						</tr>
				    </c:if>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
<script src="../res/commJS/layer/layer.js"></script>
<script>
	$(function() {
		//显示tips注释
		$(".spanComment").each(function() {
			$(this).mouseover(function() {
				layer.tips($(this).attr("comment"), this, {
					tips : [ 2, 'RGB(250,165,35)' ],
					time : 3000
				});
			});
		});
	});
</script>
</html>
