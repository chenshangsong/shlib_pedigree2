<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
    <c:import url="/WEB-INF/inc/link.jsp" />
    <link rel="stylesheet" href="${ctx}/res/plugin/openlayers/3.1.1/ol.css" type="text/css">
    <style>
        .tab-pane ul li{min-height: 25px;font-size: 14px;}
    </style>
</head>
<body>
<c:import url="/WEB-INF/inc/header3.jsp">
    <c:param name="menu" value="todolist"/>
</c:import>
<div class="container">
    <h2 id="top">上海图书馆家谱谱籍分布图/地名表</h2><br>
    
   <a href="${ctx}/dataManager/datalist"> <span class="btn btn-default btn-sm pull-right" tag="back"><i class="glyphicon glyphicon-chevron-left"></i>&nbsp;数据维护</span></a>
   <a href="${ctx}/placeManager/dataContentAdd"> 
     	<span class="btn btn-default btn-sm pull-right"><i class="icon-plus"></i>&nbsp;新增
     </a>
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active"><a href="#tab_0" role="tab" data-toggle="tab">谱籍分布图</a></li>
        <li role="presentation"><a href="#tab_1" role="tab" data-toggle="tab">谱籍地名表</a></li>
    </ul>
    <div class="tab-content" id="main">
        <div role="tabpanel" class="tab-pane fade in active" id="tab_0">
            <div id="placeDiv"></div>
            <div tag="view" style="display:none;"></div>
        </div>
        <div role="tabpanel" class="tab-pane fade" id="tab_1">
            <div id="originDiv"></div>
            <div tag="view" style="display:none;"></div>
        </div>
    </div>
</div>
<div class="modal fade" id="placeModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="myModalLabel"></h4>
            </div>
            <div class="modal-body"></div>
        </div>
    </div>
</div>
<c:import url="/WEB-INF/inc/footer.jsp" />
<script src="${ctx}/res/plugin/openlayers/3.1.1/ol.js" type="text/javascript"></script>
<script src="${ctx}/res/js/normal/place/list_map.js"></script>
<script src="${ctx}/res/js/sysmanager/placemanager/list.js"></script>

</body>
</html>
