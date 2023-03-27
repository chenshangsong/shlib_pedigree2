<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<div>
<div id="vocab_list" type="${type}">
<div class="table-responsive">
<table class="table" style="margin : 0;">
    <colgroup>
        <col class="col-xs-3"/>
        <col class="col-xs-3"/>
        <col class="col-xs-3"/>
        <col class="col-xs-3"/>
    </colgroup>
    <tbody>
    <tr>
        <td>
            <c:forEach items="${vocabList1}" var="item">
                <a href="#${item.label}">${item.label}</a><br/>
            </c:forEach>
        </td>
        <td>
            <c:forEach items="${vocabList2}" var="item">
                <a href="#${item.label}">${item.label}</a><br/>
            </c:forEach>
        </td>
        <td>
            <c:forEach items="${vocabList3}" var="item">
                <a href="#${item.label}">${item.label}</a><br/>
            </c:forEach>
        </td>
        <td>
            <c:forEach items="${vocabList4}" var="item">
                <a href="#${item.label}">${item.label}</a><br/>
            </c:forEach>
        </td>
    </tr>
    </tbody>
</table>
</div>
</div>

<div id="vocab_desc" type="${type}">
    <c:forEach items="${vocabList}" var="item">
        <h3 id="${item.label}">${fn:substringAfter(item.label, ':')}</h3>
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">
                    <strong>${item.label}</strong>
                </h3>
            </div>
            <table class="table">
                <colgroup>
                    <col class="col-xs-2">
                    <col class="col-xs-10">
                </colgroup>
            <c:forEach items="${item.attributes}" var="attr">
                <tr>
                    <td class="text-right"><a href="${rdfsLink}${attr.name}" target="_blank">${attr.name}</a></td>
                    <td class="text-info">
                        <c:choose>
                            <c:when test="${'rdfs:label' ne fn:toLowerCase(attr.name) and 'rdfs:comment' ne fn:toLowerCase(attr.name)}">
                                <a href="#${attr.value}">${attr.value}</a>&nbsp;
                                <c:if test="${not empty attr.link}">
                                <a href="${attr.link}" target="_blank">
                                    <i class="glyphicon glyphicon-share-alt"></i>
                                </a>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                ${attr.value}
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
            </table>
        </div>
        <p class="text-right" style="margin-right:20px;">
            <a href="#link_${type}" class="btn btn-default btn-xs">
                <c:if test="${fn:toLowerCase(type) eq 'classes'}">类列表</c:if>
                <c:if test="${fn:toLowerCase(type) eq 'properties'}">属性列表</c:if>
            </a>
            <a href="#top" class="btn btn-default btn-xs">回到顶部</a>
        </p>
    </c:forEach>
    </div>
</div>