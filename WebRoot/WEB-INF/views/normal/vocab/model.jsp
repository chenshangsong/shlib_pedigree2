<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
    <c:import url="/WEB-INF/inc/link.jsp" />
</head>
<body>
<c:import url="/WEB-INF/inc/header.jsp" >
    <c:param name="menu" value="model" />
</c:import>
<%--<c:import url="breadcrumb.jsp" >--%>
    <%--<c:param name="title" value="Model View"/>--%>
<%--</c:import>--%>
<div class="container" style="min-height: 500px;">
    <h1 id="top">Model View</h1>

    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title" id="link_Classes">
                <span class="glyphicon glyphicon-th-list"></span>&nbsp;
                <strong>Classes</strong>
            </h3>
        </div>
        <div class="panel-body text-muted">点击下面的类名查看具体类属性</div>
        <table class="table" id="table_class">
            <colgroup>
                <col class="col-xs-3"/>
                <col class="col-xs-3"/>
                <col class="col-xs-3"/>
                <col class="col-xs-3"/>
            </colgroup>
            <tbody>
            <c:forEach items="${classList}" var="item" varStatus="st">
                <c:if test="${st.index % 5 == 0}">
                    <tr>
                </c:if>
                <td style="${st.index > 4 ? "border-top:0px;padding-top:5px;" : ""}">
                    <h3><a href="javascript:void(0);" data="${item}">${item}</a><br/></h3>
                </td>
                <c:if test="${st.index % 5 == 4 }">
                    </tr>
                </c:if>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="panel panel-success" id="div_graph" style="display:none;">
        <div class="panel-heading">
            <h3 class="panel-title" id="link_Properties">
                <span class="glyphicon glyphicon-th-list"></span>&nbsp;
                <strong>Properties</strong>
            </h3>
        </div>
        <div class="row"></div>
    </div>

    <div class="panel panel-info" id="div_properties" style="display: none;">
        <div class="panel-heading">
            <h3 class="panel-title">
                <span class="glyphicon glyphicon-th-list"></span>&nbsp;
                <strong>Properties</strong>
            </h3>
        </div>
        <div class="panel-body text-muted"></div>
    </div>
</div>
<c:import url="/WEB-INF/inc/footer.jsp" />
<script src="${ctx}/res/jquery/jquery.tablesorter.min.js"></script>
<script src="${ctx}/res/js/normal/view/model.js"></script>
</body>
</html>
