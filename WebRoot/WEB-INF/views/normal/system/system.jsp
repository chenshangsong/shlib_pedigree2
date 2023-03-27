<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
    <c:import url="/WEB-INF/inc/link.jsp" />
    <style>
        .tab-pane{padding: 30px 0 0;}
    </style>
</head>
<body>
<c:import url="/WEB-INF/inc/header3.jsp">
    <c:param name="menu" value="system"/>
</c:import>
<div class="container">
    <h1 id="top">系统管理</h1><br>
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active"><a href="#tab_0" role="tab" data-toggle="tab">生成拼音</a></li>
        <li role="presentation"><a href="#tab_1" role="tab" data-toggle="tab">资源合并</a></li>
    </ul>
    <div class="tab-content">
        <div role="tabpanel" class="tab-pane fade in active" id="tab_0">
            <div class="row">
                <div class="col-xs-3">Person Graph</div>
                <div class="col-xs-7">（为家谱名人和责任者添加拼音）</div>
                <div class="col-xs-2 text-right"><a href="javascript:void(0);" class="btn btn-primary" command=""><i class="glyphicon glyphicon-ok"></i>&nbsp;执 行</a></div>
            </div>
            <hr>
            <div class="row">
                <div class="col-xs-3">FamilyName Graph</div>
                <div class="col-xs-7">（为姓氏表添加拼音）</div>
                <div class="col-xs-2 text-right"><a href="javascript:void(0);" class="btn btn-primary" command=""><i class="glyphicon glyphicon-ok"></i>&nbsp;执 行</a></div>
            </div>
            <hr>
        </div>
        <div role="tabpanel" class="tab-pane fade" id="tab_1">
            <form role="form" class="form-horizontal">
                <div class="form-group">
                    <div class="row">
                        <div class="col-xs-2 text-right"><label>Resource：</label></div>
                        <div class="col-xs-6">
                            <input type="text" class="form-control" name="resource" placeholder="Resource">
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="row">
                        <div class="col-xs-2 text-right"><label>From：</label></div>
                        <div class="col-xs-6">
                            <input type="text" class="form-control" name="from" placeholder="From">
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="row">
                        <div class="col-xs-2 text-right"><label>To：</label></div>
                        <div class="col-xs-6">
                            <input type="text" class="form-control" name="to" placeholder="To">
                        </div>
                        <div class="col-xs-4">
                            <button class="btn btn-primary"><i class="glyphicon glyphicon-ok"></i>&nbsp;执 行</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<c:import url="/WEB-INF/inc/footer.jsp" />
<script src="${ctx}/res/plugin/openlayers/3.1.1/ol.js" type="text/javascript"></script>
<script src="${ctx}/res/js/normal/place/list_map.js"></script>
<script src="${ctx}/res/js/normal/place/list.js"></script>
</body>
</html>
