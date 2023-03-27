<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
    <script src="http://cdnjs.cloudflare.com/ajax/libs/d3/3.5.2/d3.js"></script>
    <c:import url="/WEB-INF/inc/link.jsp" />
    <style>
        #vid-container {
            width:100%;
            height:100%;
            width: 820px;
            height: 461px;
            float: none;
            clear: both;
            margin: 2px auto;
        }

        svg {
            border-radius:3px;
        }
        .node {
            cursor: pointer;
        }

        .node circle {
            fill: #fff;
            stroke-width: 2px;
        }

        .node text {
            font: 12px sans-serif;
        }

        .link {
            fill: none;
            stroke: #99ccff;
            stroke-width: 1px;
        }

        .hyper {
            color: red;
            text-decoration: underline;
        }

        .hyper:hover {
            color:yellow;
            text-decoration: none;
        }
        .selected {
            font-weight:bold;
        }
        .not-selected {
            font-weight:normal;
        }

        .form-inline .form-control {
            display: inline-block;
            vertical-align: middle;
            width: 400px;
        }
    </style>
</head>
<body>
<c:import url="/WEB-INF/inc/header3.jsp">
    <c:param name="menu" value="tree"/>
</c:import>
<div class="container">
    <h2 id="top"><span>上海图书馆家谱世系表查询</span>
        <a href="javascript:void(0);" id="btn_back" class="btn btn-default btn-sm pull-right" style="display:none;"><i class="glyphicon glyphicon-chevron-left"></i>&nbsp;返回文献列表</a>
    </h2>
    <hr/>
    <form method="get" id="treeForm" action="${ctx}/service/tree/list" class="form-inline">
        <div>
            <div class="form-group">
                <label>家族名人：</label>
                <%--<input type="text-large" class="form-control" name="title" id="title" placeholder="在此输入待查家族名人">--%>
                <input id="name" type="text" style="max-width:500px" class="form-control" placeholder="在此输入名人或资源">
            </div>
            <a class="btn btn-primary" id="btn_query">查 询</a>
        </div>
        <hr>
    </form>
    <div class="row">
        <div class="col-md-8"> <span id='tree'></span> </div>
    </div>
</div>

<c:import url="/WEB-INF/inc/footer.jsp" />
<script src="${ctx}/res/js/normal/migration/list.js"></script>
<%--<script src='${ctx}/res/js/normal/migration/data.js'></script>--%>
</body>
</html>
