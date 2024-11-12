<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
<script src="${ctx}/res/plugin/ol2/OpenLayers.js"></script>
<%-- <script type="text/javascript"
	src="${ctx}/res/plugin/timemap/lib/jquery-1.6.2.min.js"></script> --%>
	<script  type="text/javascript" src="${ctx}/res/sysmanager/vendors/jquery.js"></script>
<script type="text/javascript"
	src="${ctx}/res/plugin/timemap/lib/mxn/mxn.js?(openlayers)"></script>
<script type="text/javascript"
	src="${ctx}/res/plugin/timemap/lib/timeline-1.2.js"></script>
<script src="${ctx}/res/plugin/timemap/src/timemap.js"
	type="text/javascript"></script>
<c:import url="/WEB-INF/inc/link.jsp" />
<script>
	//高德地图加载
	OpenLayers.Layer.GaodeCache = OpenLayers
			.Class(
					OpenLayers.Layer.TMS,
					{

						tileOriginCorner : 'tl',

						type : 'png',

						myResolutions : [ 156543.0339, 78271.516953125,
								39135.7584765625, 19567.87923828125,
								9783.939619140625, 4891.9698095703125,
								2445.9849047851562, 1222.9924523925781,
								611.4962261962891, 305.74811309814453,
								152.87405654907226, 76.43702827453613,
								38.218514137268066, 19.109257068634033,
								9.554628534317016, 4.777314267158508,
								2.388657133579254, 1.194328566789627,
								0.5971642833948135, ],

						tileOrigin : new OpenLayers.LonLat(-20037508.3427892,
								20037508.3427892),

						initialize : function(name, url, options) {

							OpenLayers.Layer.TMS.prototype.initialize.apply(
									this, [ name, url, options ]);
						},

						getURL : function(bounds) {
							var res = this.map.getResolution();
							var x = parseInt((bounds.getCenterLonLat().lon - this.tileOrigin.lon)
									/ (256 * res));
							var y = parseInt((this.tileOrigin.lat - bounds
									.getCenterLonLat().lat)
									/ (256 * res));
							var z = this.map.getZoom();
							if (Math.abs(this.myResolutions[z] - res) > 0.0000000000000000001) {
								for (var i = 0; i < this.myResolutions.length; i++) {
									if (Math.abs(this.myResolutions[i] - res) <= 0.0000000000000000001) {
										z = i;
										break;
									}
								}
							}

							if (OpenLayers.Util.isArray(this.url)) {
								var serverNo = parseInt(Math.random(0,
										this.url.length));
								return this.url[serverNo] + "&z=" + z + '&y='
										+ y + '&x=' + x;
							} else {
								return this.url + "&z=" + z + '&y=' + y + '&x='
										+ x;
							}
						},

					});
	var gdlayer = new OpenLayers.Layer.GaodeCache(
			"Gaode",
			[
					"http://webrd01.is.autonavi.com/appmaptile?lang=zh_cn&size=1&scale=1&style=7",
					"http://webrd02.is.autonavi.com/appmaptile?lang=zh_cn&size=1&scale=1&style=7",
					"http://webrd03.is.autonavi.com/appmaptile?lang=zh_cn&size=1&scale=1&style=7",
					"http://webrd04.is.autonavi.com/appmaptile?lang=zh_cn&size=1&scale=1&style=7" ]);
</script>
<style>
div#timelinecontainer {
	width: 100%;
	height: 200px;
}

.olControlLayerSwitcher .maximizeDiv, .olControlLayerSwitcher .minimizeDiv
	{
	cursor: pointer;
	height: 18px;
	right: 0;
	top: 5px;
	width: 18px;
}

div#timeline {
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

div#timelinecontainer {
	height: 200px;
}

div#mapcontainer {
	height: 500px;
}

div.olFramedCloudPopupContent {
	width: 300px;
}
</style>
</head>

<body>
	<c:import url="/WEB-INF/inc/header3.jsp">
		<c:param name="menu" value="index" />
	</c:import>
	<div class="container">
		<h2 id="top">
			<span>时间轴/地图浏览</span> <a id='hSearch' href="${ctx}/service/search">
				<span class="btn btn-default btn-sm pull-right"><i
					class="glyphicon glyphicon-random"></i>&nbsp; 简单检索</span>
			</a>
		</h2>
		<hr>
		<div>
			<table class="seniorSearch">
				<tr>
					<td><label>标题</label></td>
					<td class="left10"><input type="text" value="${data.title}"
						class="form-control" name="title" id="title" placeholder="在此输入标题"></td>
					<td class="left30"><label>姓氏</label></td>
					<td class="left10"><input type="text"
						value="${data.familyName}" class="form-control" name="familyName"
						id="familyName" placeholder="在此输入姓氏"></td>
					<td class="left30"><label>责任者</label></td>
					<td class="left10"><input type="text" value="${data.creator}"
						class="form-control" name="creator" id="creator"
						placeholder="在此输入责任者"></td>
					<td class="left30"><label>谱籍</label></td>
					<td class="left10"><input type="text" value="${data.place}"
						class="form-control" name="place" id="place"
						placeholder="在此输入谱籍地名"></td>
				</tr>
				<tr>
					<td><label>堂号</label></td>
					<td class="left10"><input type="text" value="${data.tang}"
						class="form-control" name="tang" id="tang" placeholder="在此输入堂号"></td>
					<td class="left30"><label>先祖/名人</label></td>
					<td class="left10"><input type="text" value="${data.person}"
						class="form-control" name="person" id="person"
						placeholder="在此输入先祖/名人"></td>
					<td class="left30"><label>摘要</label></td>
					<td class="left10"><input type="text" value="${data.note}"
						class="form-control" name="note" id="note" placeholder="在此输入摘要"></td>
					<td class="left30"><label>馆藏地</label></td>
					<td class="left10"><input type="text"
						value="${data.organization}" class="form-control"
						name="organization" id="organization"
						placeholder="在此输入馆藏地"></td>
							
				
					
				</tr>
				 <tr>
                	<!-- <td colspan='2'><input type="checkbox" class="form-control" style='width:20px;height:20px;'  value="1" name="accFlg" id="accFlg" />上图全文外网访问</td> -->
                	<td align="right"><input type="checkbox" class="form-control" style='width:15px;height:15px;' value="true" name="accFlg" id="accFlg" />
                	</td> 
                	<td align="left">上图全文外网访问</td>
                <td class="left30"><label>索书号</label></td>
                <td class="left10"><input type="text" value="${data.selfMark}" class="form-control" name="selfMark" id="selfMark" placeholder="在此输入索书号"></td>
              <td class="left30"><label>DOI</label></td>
              <td class="left10"><input type="text" value="${data.doi}" class="form-control" name="doi" id="doi" placeholder="在此输入DOI"></td>
               <td class="left30"><button type="button"
							class="btn btn-primary" id="btn_query">
							<i class="glyphicon glyphicon-search"></i>&nbsp;查询
						</button></td>
                </tr>
			</table>
		</div>	
		<hr>
		<div class="dropdown" style="display: inline; float: left;"
			id="time_nodes">
			<button class="btn btn-default dropdown-toggle" type="button"
				data-toggle="dropdown" aria-expanded="true">
				时间可视范围: <span id="time_label">5年</span> <span class="caret"></span>
			</button>
			<ul class="dropdown-menu" role="menu" aria-labelledby="time_nodes">
				<li role="presentation"><a role="menuitem" tabindex="-1"
					href="javascript:void(0)" data="230">5年</a></li>
				<li role="presentation"><a role="menuitem" tabindex="-1"
					href="javascript:void(0)" data="115">10年</a></li>
				<li role="presentation"><a role="menuitem" tabindex="-1"
					href="javascript:void(0)" data="57">20年</a></li>
			</ul>
		</div>
		<form method="get" id="timeForm" action="${ctx}/service/work/getInGeo"
			class="form-inline" autocomplete="off">
			<div>
				<div class="form-group">
					<label>&nbsp;&nbsp;&nbsp;&nbsp;纂修朝代：</label> <select id="dynasty"
						class="form-control">
						<!--    <option value="" selected>请选择</option> -->
						<%--<option value="http://gen.library.sh.cn/Temporal/18">民国</option>--%>
						<%--<option value="http://gen.library.sh.cn/Temporal/28">近代</option>--%>
					</select> &nbsp;&nbsp;&nbsp;&nbsp; <label>文献数：</label> <select id="_unit"
						class="form-control">
						<option value="">请选择</option>
						<option value="101" selected>100</option>
						<option value="2011">200</option>
						<option value="501">500</option>
						<option value="1001">1000</option>
						<option value="0">所有</option>
					</select>
					<%--     &nbsp;&nbsp;&nbsp;&nbsp;
                <label>高亮姓氏：</label>
                <input type="text-large" class="form-control" name="title" id="title" placeholder="在此输入待查家族名人">
                <input id="name" type="text" style="max-width:100px" class="form-control" placeholder="输入姓氏">
            --%>
				</div>
				&nbsp;&nbsp;&nbsp;&nbsp; <a class="btn btn-primary" id="btn_query2"><i
					class="glyphicon glyphicon-refresh"></i>刷新</a> <a
					class="btn btn-default" id="map_query" style="float: right">地图浏览</a>
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
	<div class="modal fade" id="workModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="myModalLabel"></h4>
				</div>
				<div class="modal-body"></div>
			</div>
		</div>
	</div>
	<c:import url="/WEB-INF/inc/footer.jsp" />
	<script src="${ctx}/res/js/normal/service/index.js"></script>
</body>
</html>