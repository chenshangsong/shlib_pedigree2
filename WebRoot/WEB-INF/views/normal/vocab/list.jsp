<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
    <c:import url="/WEB-INF/inc/link.jsp" />
</head>
<body>
<c:import url="/WEB-INF/inc/header.jsp">
    <c:param name="menu" value="list"/>
</c:import>
<%--<c:import url="breadcrumb.jsp" >--%>
    <%--<c:param name="title" value="List View"/>--%>
<%--</c:import>--%>
<div class="container">
    <h1 id="top">List View</h1>
    <p class="well">本页面视图展示了一个有序的类列表和属性列表。浏览器的后退或前进操作将调整页面上的导航位置。</p>

    <div class="panel panel-primary">
        <!-- Default panel contents -->
        <div class="panel-heading">
            <h3 class="panel-title" id="link_Classes">
                <span class="glyphicon glyphicon-th-list"></span>&nbsp;
                <strong>Classes</strong>
            </h3>
        </div>
        <div class="panel-body text-muted">选择下面的一个类名并跳转到一个类面板和描述
            其用法如下:
        </div>
        <div id="class_list"></div>
    </div>

    <div class="panel panel-info">
        <!-- Default panel contents -->
        <div class="panel-heading">
            <h3 class="panel-title" id="link_Properties">
                <span class="glyphicon glyphicon-th-list"></span>&nbsp;
                <strong>Properties</strong>
            </h3>
        </div>
        <div class="panel-body text-muted">选择下面的一个属性名并跳转到一个属性面板和描述
            其用法如下:
        </div>
        <div id="prop_list"></div>
    </div>

    <h2>Classes</h2>
    <div id="class_desc"></div>

    <h2>Properties</h2>
    <div id="prop_desc"></div>
</div>
<c:import url="/WEB-INF/inc/footer.jsp" />
<script src="${ctx}/res/js/normal/view/list.js"></script>
</body>
</html>
