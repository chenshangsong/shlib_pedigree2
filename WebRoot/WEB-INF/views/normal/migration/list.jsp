<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
    <%--<script src="http://cdnjs.cloudflare.com/ajax/libs/d3/3.5.2/d3.js"></script>--%>
    <script src="/res/plugin/timeline/2.3.0/timeline-api.js?bundle=true" type="text/javascript"></script>
    <%--<link type="text/css" href="http://api.simile-widgets.org/ajax/2.2.1/styles/graphics.css" rel="stylesheet">--%>
    <link type="text/css" href="/res/plugin/timeline/2.3.0/timeline-bundle.css" rel="stylesheet">
    <c:import url="/WEB-INF/inc/link.jsp" />
    <style>
        .controls {
            margin: 1em 0;
            padding: 0.5em;
        }

        .timeline-default {
            border: 1px solid #aaa;
            font-family: Trebuchet MS,Helvetica,Arial,sans serif;
            font-size: 8pt;
        }

        table, tr, td {
            font-size: inherit;
        }

        tr, td {
            vertical-align: top;
        }

        a:link {
            color: #222;
        }

        a:visited {
            color: #666;
        }

        a:hover {
            color: #000;
        }

        a:active {
        }

        a:focus {
        }

        img, a img {
            border: none;
        }

        /* ----------------------------- Path ---------------------------- */

        #path {
            color: #333;
            background-color: #f8f8f8;
            border-bottom: 1px solid #ccc;
            padding: 3px 8px;
            margin: 0px;
        }

        #path li {
            display: inline;
            padding-left: 13px;
            padding-right: 3px;
            background-image: url(/res/images/arrow.gif);
            background-repeat: no-repeat;
            background-position: 1px 5px;
        }

        #path span {
            font-weight: bold;
        }

        /* ----------------------------- Header ---------------------------- */

        #header {
            margin: 24px 48px;
        }

        #header h1 {
            font-size: 250%;
            color: #222;
            margin: 0;
            margin-bottom: 6px;
        }

        #header h2 {
            font-size: 120%;
            color: #aaa;
            margin: 0;
        }

        /* ----------------------------- Content ---------------------------- */

        #content {
            margin: 24px 48px;
        }

        /* ----------------------------- Footer ---------------------------- */

        #footer {
            margin-top: 48px;
            border-top: 1px solid #ccc;
            padding: 6px;
            text-align: center;
            color: #888;
            font-size: 80%;
        }

        #footer a {
            color: #888;
        }

        /* ----------------------------- Path ---------------------------- */

        table.spaced-table {
            border-collapse: collapse;
        }

        table.spaced-table td {
            padding: 0;
            padding-left: 1em;
        }

        table.spaced-table td:first-child {
            padding-left: 0;
        }

        table.spaced-table tr td {
            padding-top: 1em;
        }

        table.spaced-table tr:first-child td {
            padding-top: 0;
        }
    </style>
</head>
<body onload="onLoad();" onresize="onResize();">
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
                <a class="label label-default" id="map_query">区域查询</a>
                <a class="label label-default" id="adv_query">高级查询</a>
                <a class="label label-default" id="mig_query">迁徙图</a>
                <span class="label label-primary" id="tree_query">世系表</span>
            </div>
        </h3>
        </div>
        <%--<form method="get" id="treeForm" action="${ctx}/service/migration/list" class="form-inline" style="padding-left: 10px;padding-top: 20px;">--%>
            <%--<div>--%>
                <%--<div class="form-group">--%>
                    <%--<label>家族名人：</label>--%>
                    <%--&lt;%&ndash;<input type="text-large" class="form-control" name="title" id="title" placeholder="在此输入待查家族名人">&ndash;%&gt;--%>
                    <%--<input id="name" type="text" style="max-width:500px" class="form-control" placeholder="在此输入名人或资源">--%>
                <%--</div>--%>
                <%--<a class="btn btn-primary" id="btn_query">查 询</a>--%>
            <%--</div>--%>
            <%--<hr>--%>
        <%--</form>--%>
        <br>
        <div id="tl" class="timeline-default" style="height: 450px;"></div>

        <div style="width: 100%">
            <table style="text-align: center; width: 100%">
                <tr>
                    <td><a href="javascript:centerTimeline(1);">1 AD</a></td>
                    <td><a href="javascript:centerTimeline(250);">250 AD</a></td>
                    <td><a href="javascript:centerTimeline(500);">500 AD</a></td>
                    <td><a href="javascript:centerTimeline(750);">750 AD</a></td>
                    <td><a href="javascript:centerTimeline(1000);">1000 AD</a></td>
                    <td><a href="javascript:centerTimeline(1250);">1250 AD</a></td>
                    <td><a href="javascript:centerTimeline(1500);">1500 AD</a></td>
                    <td><a href="javascript:centerTimeline(1750);">1750 AD</a></td>
                    <td><a href="javascript:centerTimeline(2000);">2000 AD</a></td>
                </tr>
            </table>
        </div>
        <div class="controls" id="controls"></div>
    </div>
</div>

<c:import url="/WEB-INF/inc/footer.jsp" />
<script src="${ctx}/res/js/normal/migration/list.js"></script>
<%--<script src='${ctx}/res/js/normal/migration/data.js'></script>--%>
</body>
</html>
