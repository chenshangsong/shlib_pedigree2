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
    <c:param name="menu" value="place"/>
</c:import>
<div class="container">
    <h2 id="top">谱籍地列表</h2><br>
     <a id='hSearch'
				href="${ctx}/service/place/map"> <span
				class="btn btn-default btn-sm pull-right"><i
					class="glyphicon glyphicon-map-marker"></i>&nbsp;地图浏览</span></a>
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active"><a href="#tab_0" role="tab" data-toggle="tab">国内</a></li>
        <li role="presentation"><a href="#tab_1" role="tab" data-toggle="tab">海外</a></li>
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
<script type="text/html" id="place_tpl">
    <h3><span tag="title"></span>&nbsp;
        <!--<span class="btn btn-primary btn-sm" tag="rdf">R</span>

        <a title="RDF" tag="rdf"><img src="${ctx}/res/images/rdf.gif" style="width:24px; height:24px;"></a>-->
        {@each rdfs as rdf}
        {@if rdf['p'].split("#")[1] == 'seeAlso'}
        <a href="@{rdf['o']}" target="_blank" title="GeoNames"><img src="${ctx}/res/images/globe.gif" style="width:24px; height:24px;"></a>
        {@/if}
        {@/each}
        <span class="btn btn-default btn-sm pull-right" tag="back"><i class="glyphicon glyphicon-chevron-left"></i>&nbsp;返回</span>
    </h3>
    {@if standPlace}
    <div style="padding-bottom: 10px;">
        {@if standPlace.county!=''}@{standPlace.county} - {@/if}
        {@if (standPlace.city != '')&&(standPlace.city != standPlace.prov)}@{standPlace.city} - {@/if}
        {@if standPlace.prov != ''}@{standPlace.prov}{@/if}
        {@if standPlace.country != ''}@{standPlace.country}{@/if}
 {@if standPlace.country != ''}@{standPlace.country}{@/if}
<input type='hidden' id='hidPlaceLabel' value="@{standPlace.label}"/>
    </div>
    {@/if}
    <div class="panel panel-info">
        <div class="panel-heading">
            <h3 class="panel-title">
                <span class="glyphicon glyphicon-th-list"></span>&nbsp;
                <strong>谱籍为此地的家谱文献</strong>
            </h3>
        </div>
        <div class="panel-body text-muted">
            <div class="row">
                <div class="col col-xs-9">
                    <div style="position:relative;width:100%;height:600px;" tag="workMap"><div id="popup"></div></div>
                </div>
                <div class="col col-xs-3">
                    <ul>
                    {@each works as item}
                        <li><a href="${ctx}/service/work/list#uri=@{item.work}" target="_blank" points="@{item.points}" >@{item.title}</a></li>
                    {@/each}
                    {@if works.length >= 20}
                        <li><a href="javascript:void(0);" target="_blank" tag="more"><i><b>查看更多...</b></i></a></li>
                    {@/if}
                    </ul>
                </div>
            </div>
        </div>
    </div>
</script>
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
<script src="${ctx}/res/js/normal/place/list.js"></script>
</body>
</html>
