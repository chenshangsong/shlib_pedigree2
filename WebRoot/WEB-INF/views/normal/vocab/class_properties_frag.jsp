<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<%--<div class="table-responsive">--%>
<table class="table  table-striped table-hover tablesorter">
    <colgroup>
        <col class="col-xs-2">
        <col class="col-xs-3">
        <col class="col-xs-2">
        <col class="col-xs-2">
        <col class="col-xs-3">
    </colgroup>
<c:if test="${empty properties}">
    <tr>
        <td colspan="5">
            <c:if test="${not empty link}">
                <%--<i class="glyphicon glyphicon-chevron-right"></i>--%>
                <%--Click <a href="${link}" target="_blank">here</a> for more information from <a href="http://bibframe.org/" target="_blank">bibframe.org</a>.--%>
                Click <a href="${link}" target="_blank">here</a> for more information.
            </c:if>
            <c:if test="${empty link}">
                no properties!
            </c:if>
        </td>
    </tr>
</c:if>
<c:if test="${not empty properties}">
    <thead>
    <tr>
        <th class="header">Property</th>
        <th class="header">Subproperty of</th>
        <th class="header">Label</th>
        <th class="header">Used With</th>
        <th class="header">Expected Value</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${properties}" var="item">
        <c:set var="property" value="${item.value}"/>
        <tr>
            <td>
            <c:if test="${not empty property['name.link']}">
                <a href="${property['name.link']}" target="_blank"><c:out value="${item.key}"/></a>
            </c:if>
            <c:if test="${empty property['name.link']}">
                <c:out value="${item.key}"/>
            </c:if>
            </td>
            <td>
                <c:if test="${not empty property['subPropertyOf.link']}">
                    <a href="${property['subPropertyOf.link']}" target="_blank"><c:out value="${property.subPropertyOf}"/></a>
                </c:if>
                <c:if test="${empty property['subPropertyOf.link']}">
                    <c:out value="${property.subPropertyOf}"/>
                </c:if>
            </td>
            <td><c:out value="${property.label}"/></td>
            <td>
                <c:if test="${not empty property['domain.link']}">
                    <a href="${property['domain.link']}" target="_blank"><c:out value="${property.domain}"/></a>
                </c:if>
                <c:if test="${empty property['domain.link']}">
                    <c:out value="${property.domain}"/>
                </c:if>
            </td>
            <td>
                <c:if test="${not empty property['range.link']}">
                    <a href="${property['range.link']}" target="_blank"><c:out value="${property.range}"/></a>
                </c:if>
                <c:if test="${empty property['range.link']}">
                    <c:out value="${property.range}"/>
                </c:if>
            </td>
        </tr>
    </c:forEach>
</c:if>
</tbody>
</table>
<%--</div>--%>