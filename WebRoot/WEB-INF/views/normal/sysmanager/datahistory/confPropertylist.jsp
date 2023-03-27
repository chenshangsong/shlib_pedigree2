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
				<c:forEach var="x" varStatus="status" items="${data}">
					<tr class="text-left">
						<td width='10%'><span class="spanComment"
							comment="${x.oldComment}">${x.oldCnName}</span></td>
						<td width="10%" style="color: red;"><c:if test="${x.isUri!='1'}">
											<c:if test="${x.oldEnName!='address'}">
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
										</c:if>
										<c:if test="${x.oldEnName=='address'}">
											<c:set var="str" value="${x.oldValue}" />
											<c:if test="${fn:contains(str, '@chs')}">
													简体：<c:set var="str" value="${fn:substringBefore(str,'@')}" />
											</c:if>
											<c:if test="${fn:contains(str, '@cht')}">
													繁体：<c:set var="str" value="${fn:substringBefore(str,'@')}" />

											</c:if>
											<c:if test="${fn:contains(str, '@en')}">
													英文：<c:set var="str" value="${fn:substringBefore(str,'@')}" />
											</c:if>
											<c:out value="${str}" />
										</c:if><c:if test="${x.isUri=='1'}">
								<a href="${x.oldValue}">${x.oldValueCn}</a>
							</c:if></td>
						<td width="30%"><c:if test="${x.isUri!='1'}">
											<c:if test="${x.oldEnName!='address'}">
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
										</c:if>
										<c:if test="${x.oldEnName=='address'}">
											<c:set var="str" value="${x.newValue}" />
											<c:if test="${fn:contains(str, '@chs')}">
													简体：<c:set var="str" value="${fn:substringBefore(str,'@')}" />
											</c:if>
											<c:if test="${fn:contains(str, '@cht')}">
													繁体：<c:set var="str" value="${fn:substringBefore(str,'@')}" />

											</c:if>
											<c:if test="${fn:contains(str, '@en')}">
													英文：<c:set var="str" value="${fn:substringBefore(str,'@')}" />
											</c:if>
											<c:out value="${str}" />
										</c:if><c:if test="${x.isUri=='1'}">
								<a href="${x.newValue}">${x.newValueCn}<i
									class="icon-hand-right"></i></a>
							</c:if></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
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
