<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
    <c:import url="/WEB-INF/inc/link.jsp" />
</head>
<body>
<c:import url="/WEB-INF/inc/header3.jsp"></c:import>
<div class="container">
    <h1 id="top">${url}</h1><br>
    <div class="table-responsive">
        <table class="table  table-hover">
            <colgroup>
                <col class="col-xs-4">
                <col class="col-xs-8">
            </colgroup>
            <thead>
                <th>Label</th>
                <th>Value</th>
            </thead>
            <c:forEach items="${stats}" var="stat">
                <tr>
                    <td>
                        <c:choose>
                            <c:when test="${fn:startsWith(stat['label'], 'http://')}">
                                <a href="${stat['label']}">${stat['label']}</a>
                            </c:when>
                            <c:otherwise>${stat['label']}</c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${fn:startsWith(stat['value'], 'http://')}">
                                <a href="${stat['value']}">${stat['value']}</a>
                            </c:when>
                            <c:otherwise>${stat['value']}</c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>
<c:import url="/WEB-INF/inc/footer.jsp" />
<script src="${ctx}/res/js/normal/rdf/view.js"></script>
</body>
</html>
