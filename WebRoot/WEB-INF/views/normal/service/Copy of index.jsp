<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
   
    <%--<link rel="stylesheet" href="${ctx}/res/plugin/openlayers/3.1.1/ol.css" type="text/css">--%>
    <script src="${ctx}/res/plugin/ol2/OpenLayers.js"></script>
    <%--<script src="http://openlayers.org/api/OpenLayers.js"></script>--%>
    <script type="text/javascript" src="${ctx}/res/plugin/timemap/lib/jquery-1.6.2.min.js"></script>
    <script type="text/javascript" src="${ctx}/res/plugin/timemap/lib/mxn/mxn.js?(openlayers)"></script>
    <script type="text/javascript" src="${ctx}/res/plugin/timemap/lib/timeline-1.2.js"></script>
    <script src="${ctx}/res/plugin/timemap/src/timemap.js" type="text/javascript"></script>
     <c:import url="/WEB-INF/inc/link.jsp" />
    <style>
        div#timelinecontainer{
             width: 100%;
             height: 200px;
         }

        .olControlLayerSwitcher .maximizeDiv, .olControlLayerSwitcher .minimizeDiv {
            cursor: pointer;
            height: 18px;
            right: 0;
            top: 5px;
            width: 18px;
        }

        div#timeline{
            width: 100%;
            height: 100%;
            font-size: 12px;
            background: #CCCCCC;
        }

        div#mapcontainer {
            width: 100%;
            height: 400px;
        }

        div#map {
            width: 100%;
            height: 100%;
            background: #EEEEEE;
        }

        div#timelinecontainer { height: 200px; }
        div#mapcontainer { height: 500px; }
        div.olFramedCloudPopupContent { width: 300px; }
    </style>
</head>

<body>
<c:import url="/WEB-INF/inc/header3.jsp">
    <c:param name="menu" value="index"/>
</c:import>
<div class="container">
    <h2 id="top"><span>家谱文献时间轴-地图浏览</span></h2>
    <hr>
    <div class="dropdown" style="display:inline;float:left;" id="time_nodes" >
        <button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown" aria-expanded="true">
            时间可视范围: <span id="time_label">5年</span>
            <span class="caret"></span>
        </button>
        <ul class="dropdown-menu" role="menu" aria-labelledby="time_nodes">
            <li role="presentation"><a role="menuitem" tabindex="-1" href="javascript:void(0)" data="230">5年</a></li>
            <li role="presentation"><a role="menuitem" tabindex="-1" href="javascript:void(0)" data="115">10年</a></li>
            <li role="presentation"><a role="menuitem" tabindex="-1" href="javascript:void(0)" data="57">20年</a></li>
        </ul>
    </div>
    <form style="display:none;" method="get" id="timeForm" action="${ctx}/service/work/getInGeo" class="form-inline" autocomplete="off">
        <div>
            <div class="form-group">
                <label>&nbsp;&nbsp;&nbsp;&nbsp;纂修朝代：</label>
                <select id="dynasty" class="form-control">
                    <option value="" selected>请选择</option>
                    <%--<option value="http://gen.library.sh.cn/Temporal/18">民国</option>--%>
                    <%--<option value="http://gen.library.sh.cn/Temporal/28">近代</option>--%>
                </select>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <label>文献数：</label>
                <select id="_unit" class="form-control">
                    <option value="" >请选择</option>
                    <option value="101" selected>100</option>
                    <option value="2011">200</option>
                    <option value="501">500</option>
                    <option value="1001">1000</option>
                    <option value="0">所有</option>
                </select>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <label>高亮姓氏：</label>
                <%--<input type="text-large" class="form-control" name="title" id="title" placeholder="在此输入待查家族名人">--%>
                <input id="name" type="text" style="max-width:100px" class="form-control" placeholder="输入姓氏">
            </div>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <a class="btn btn-primary" id="btn_query">确定</a>
            <a class="btn btn-default" id="map_query" style="float: right">地图浏览</a>
        </div>
    </form>
    <hr>
    <div class="panel panel-info">
        <%--<div class="panel-heading">--%>
            <%--<h3 class="panel-title" id="link_Classes">--%>
                <%--<strong>家谱文献查询</strong>--%>
                <%--<div id="switch">--%>
                    <%--<span class="label label-primary">时空查询</span>--%>
                    <%--<a class="label label-default" id="map_query">谱籍查询</a>--%>
                    <%--&lt;%&ndash;<a class="label label-default" id="adv_query">高级查询</a>&ndash;%&gt;--%>
                    <%--&lt;%&ndash;<a class="label label-default" id="mig_query">迁徙图</a>&ndash;%&gt;--%>
                    <%--&lt;%&ndash;<a class="label label-default" id="tree_query">世系表</a>&ndash;%&gt;--%>
                <%--</div>--%>
            <%--</h3>--%>
        <%--</div>--%>
        <div id="timemap">
            <div id="timelinecontainer">
                <div id="timeline"></div>
            </div>
            <div id="mapcontainer">
                <div id="map"></div>
            </div>
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
<%--<script src="${ctx}/res/plugin/openlayers/3.1.1/ol.js" type="text/javascript"></script>--%>
<script src="${ctx}/res/js/normal/service/index.js"></script>
</body>
</html>