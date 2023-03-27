<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
    <c:import url="/WEB-INF/inc/link.jsp" />
    <link rel="stylesheet" href="${ctx}/res/plugin/openlayers/3.1.1/ol.css" type="text/css">
    <style>
        #map{width:100%; height: 100%;}
        .ol-mouse-position{margin-right:50px;font-weight: bold;color:blue;}
        .btn-group, .btn-group-vertical {
            float: left;
            position: relative;
            vertical-align: middle;
        }
        .input-group .form-control {
            float: left;
            margin-bottom: 0;
            position: relative;
            width: 40%;
            z-index: 2;
        }
    </style>
</head>
<body>
<c:import url="/WEB-INF/inc/header3.jsp">
    <c:param name="menu" value="index"/>
</c:import>
<div class="container">
    <h2 id="top"><span>家谱文献时间轴-地图浏览</span></h2>
    <hr>
    <form style="padding-left: 10px;">
        <div class="btn-group" data-toggle="buttons">
            <label class="btn btn-default" id="btn_polygon">
                <i class="glyphicon glyphicon-screenshot"></i>&nbsp;
                <input type="checkbox" autocomplete="off" id="polygon">多边形选择区域
            </label>
        </div>

        <div class="input-group" style="padding-left: 20px;">
            <span class="input-group-addon">
                <input type="radio" name="fn-radio" value="all" checked><span> 所有 </span>
                <input type="radio" name="fn-radio" value="陈"><span> 陈 </span>
                <input type="radio" name="fn-radio" value="王"><span> 王 </span>
                <input type="radio" name="fn-radio" value="李"><span> 李 </span>
                <input type="radio" name="fn-radio" value="周"><span> 周 </span>
                <input type="radio" name="fn-radio" value="丁"><span> 丁 </span>
                <input type="radio" name="fn-radio" value="other"><span> 其他 </span>
            </span>
            <input type="text" id="fn-input" style="width: 150px;" class="form-control" placeholder="输入姓氏">
            <a href="${ctx}/service" class="btn btn-default" id="timeline_query" style="float: right">时间轴浏览</a>
        </div>
    </form>
    <hr>
    <div class="panel panel-info">
        <div style="height:600px;width:100%;">
            <hr style="padding:0;margin:0;">
            <div id="map"><div id="popup"></div></div>
        </div>
    </div>
    </div>
</div>
<div class="modal fade" id="workModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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
<script src="${ctx}/res/js/normal/place/map.js"></script>
</body>
</html>
