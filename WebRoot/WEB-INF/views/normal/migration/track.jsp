<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
    <c:import url="/WEB-INF/inc/link.jsp" />
    <script src="${ctx}/res/plugin/echarts/2.2.0/echarts.js"></script>
</head>

<body>
<c:import url="/WEB-INF/inc/header3.jsp">
    <c:param name="menu" value="index"/>
</c:import>
<div class="container">
    <div class="panel panel-info">
        <div class="panel-heading">
            <h3 class="panel-title" id="link_Classes">
                <strong>家谱文献查询</strong>
                <div id="switch">
                    <a class="label label-default" id="timeline_query">时空查询</a>
                    <a class="label label-default" id="map_query">谱籍查询</a>
                    <a class="label label-default" id="adv_query">高级查询</a>
                    <span class="label label-primary" id="mig_query">迁徙图</span>
                    <a class="label label-default" id="tree_query">世系表</a>
                </div>
            </h3>
        </div>
        <div id="mainMap" style="height:500px;border:1px solid #ccc;padding:10px;"></div>
    </div><!--/row-->
</div><!--/.fluid-container-->
<c:import url="/WEB-INF/inc/footer.jsp" />
<script src="${ctx}/res/js/normal/migration/track.js"></script>
</body>
</html>