<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
    <c:import url="/WEB-INF/inc/link.jsp" />
    <link href="${ctx}/res/css/tree.css" rel="stylesheet"/>
</head>
<body>
<c:import url="/WEB-INF/inc/header.jsp">
    <c:param name="menu" value="class"/>
</c:import>
<%--<c:import url="breadcrumb.jsp">--%>
   <%--<c:param name="title" value="Class View" />--%>
<%--</c:import>--%>
<div class="container">
    <h1 id="top">Class View</h1>
    <div class="row">
        <div class="col-md-4 col-lg-4 col-sm-4">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <div class="panel-title">
                        <span class="glyphicon glyphicon-th-list"></span>&nbsp;
                        <strong>Classes</strong>
                    </div>
                </div>
                <div class="panel-body tree" style="min-height: 550px;">
                </div>
            </div>
        </div>
        <div class="col-md-8 col-lg-8 col-sm-8" style="padding-left:0px;" id="div_properties">
            <div class="panel panel-info">
                <div class="panel-heading">
                    <h3 class="panel-title">
                        <span class="glyphicon glyphicon-th-list"></span>&nbsp;
                        <strong>Properties</strong>
                    </h3>
                </div>
                <div class="panel-body text-muted"></div>
            </div>
        </div>
    </div>
</div>
<c:import url="/WEB-INF/inc/footer.jsp" />
<script src="${ctx}/res/bootstrap/js/bootstrap-tree.min.js"></script>
<script src="${ctx}/res/jquery/jquery.tablesorter.min.js"></script>
<script src="${ctx}/res/js/normal/view/class.js"></script>
</body>
</html>
