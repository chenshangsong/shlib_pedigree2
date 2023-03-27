<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
<c:import url="/WEB-INF/inc/link.jsp" />
<script src="${ctx}/res/js/sysmanager/zoomcharts/lib/zoomcharts.js"></script>
</head>
<body>
	<c:import url="/WEB-INF/inc/header3.jsp">
		<c:param name="menu" value="work" />
	</c:import>
	<input id="hidUri" type="hidden" value="${uri}" />
	<br />
	<div class="container">
		<div class="one"></div>
		<!-- <div class="two">
			<div class="panel-group" id="dendrogram_accordion" role="tablist"
				aria-multiselectable="true">
				<div class="panel panel-info">
					<div class="panel-heading" role="tab">
						<h4 class="panel-title">
							<a data-toggle="collapse"
								data-parent="#dendrogram_accordion" href="#collapse_dendrogram"
								aria-expanded="true" aria-controls="#collapse_dendrogram"> <i
								class="glyphicon glyphicon-resize-vertical">谱系图</i>
							</a>
							&nbsp;&nbsp;&nbsp;<i class="glyphicon glyphicon-eye-open"></i>
                     		<a id="isWife" href="javascript:;">不查看配偶</a>
                     		&nbsp;&nbsp;&nbsp;<i class="glyphicon glyphicon-refresh"></i>
                     		<a id="isTree" href="javascript:;">使用分散形态查看</a>
						</h4>
					</div>
					<div id="collapse_dendrogram" class="panel-collapse collapse in"
						role="tabpanel">
						<div class="panel-body">
							<div class="zoomChart" style="width:100%;"></div>
						</div>
					</div>
				</div>
			</div>
		</div> -->
	</div>
	<c:import url="/WEB-INF/inc/footer.jsp" />

</body>
<script>
	var chart = null, isShowWife = true, isTree = true;
	$(function() {
		var uri = $("#hidUri").val();
		var view = "V";
		$.get(ctx + "/service/person/get", {
			uri : uri,
			inference : true,
			view : view
		}, function(data) {
			var html = "";
			html = juicer(getTemplate("person"), data)
					+ juicer(getTemplate("works"), data);
			$(".one").html(html);
			initDefaultPhoto($(".one"));
			//getDendrogram();
			hideLoading();
		});
		
		$("#isWife").click(function(){
			if (chart == null){
				return;
			}
			chart.updateFilters();
			isShowWife = !isShowWife;
			if (isShowWife){
				$('#isWife').html('不查看配偶');
			} else {
				$('#isWife').html('查看配偶');
			}
		});
		
		$("#isTree").click(function(){
			if (chart == null){
				return;
			}
			updateSetting();
			isTree = !isTree;
			if (isTree){
				$('#isTree').html('使用分散形态查看');
			} else {
				$('#isTree').html('使用树状形态查看');
			}
		});
		
		$(".one").delegate(".dendrogram", "click", function(){ 
			
			var checkUri = $(this).attr('check');

			var check = false;
			if (checkUri == "http://data.library.sh.cn/jp/resource/work/jklhb5c3ga1rvxe3") {
				check = true;
			}
			
			if (checkUri == "http://data.library.sh.cn/jp/resource/work/zxww5ignvetnkd8d") {
				check = true;
			}
			
			if (checkUri == "http://data.library.sh.cn/jp/resource/work/kkgv5g289866rbm4") {
				check = true;
			}
			
			if (!check) {
				alert('谱系图制作中...');
			} else {
				var uri = $('#hidUri').val().split(';')[0];
				window.open("../work/dendrogram?uri=" + uri);
			}

			return false;
		}); 

		return false;
	});

	function getDendrogram() {

		var uri = $("#hidUri").val();
		$.get(ctx + "/service/person/dendrogram", {
			uri : uri,
			out : "",
			isMe : true
		}, function(result) {
			bindZoomChart(result);
		});
	}

	function bindZoomChart(data) {

		chart = new NetChart({
			container : $(".zoomChart")[0],
			area : {
				height : 500
			},
			data : {
				preloaded : {
					"nodes" : eval(data.nodes),
					"links" : eval(data.links)
				}
			},
			info : {
				enabled : true,
				nodeContentsFunction : function(itemData, item) {
					if (itemData.role == "2") {
						return;
					}
					
					var content = "<div style='margin:auto; width:200px; height:100%; padding': 10px;>";
					content += "<h3 style='font-weight: 300; font-size: 21px; color: #2f256e; padding-bottom: 3px; margin:0px'>" + itemData.title + "</h3>";
					content += "<br /><p style='padding-left:5px;'>字：" + itemData.person.courtesyName + "</p>"
					content += "<p style='padding-left:5px;'>行：" + itemData.person.orderOfSeniority + "</p>"
					content += "<p style='padding-left:5px;'>号：" + itemData.person.pseudonym + "</p>"
					content += "<p style='padding-left:5px;'>生：" + itemData.person.birthday + "</p>"
					content += "<p style='padding-left:5px;'>卒：" + itemData.person.deathday + "</p>"
					content += "</div>";
					
					return content;
				},
				linkContentsFunction : function(itemData, item) {
				}
			},
			events : {
				onDoubleClick : graphDoubleClick
			},
			style : {
				node : {
					display : "roundtext"
				},
				nodeLabel : {
					backgroundStyle : {
						fillColor : "#93B17F",
						//lineColor : "blue"
					}
				},
				linkLabel : {
					backgroundStyle : {
						fillColor : "#93B17F",
						lineColor : "blue"
					}
				},
				nodeStyleFunction : nodeStyle,
				linkStyleFunction : linkStyle,
				link : {
					fillColor : "#46D0F7"
				}
			},
			layout:{
				mode:"hierarchy",
	            nodeSpacing: 10,
				rowSpacing: 60
				//aspectRatio:true
	        },
	        filters: {
	            nodeFilter: nodeFilter
	        }
		});
	}
	
	function updateSetting() {
		
		if (isTree) {
			chart.updateSettings({
				layout: { mode: "dynamic"}, 
				style : {
					linkStyleFunction : linkStyle
					
				},
			});
		} else {
			chart.updateSettings({
				layout: { mode: "hierarchy"}, 
			});
		}
	}

	function nodeStyle(node) {

		node.label = node.data.title;
		node.imageCropping = "crop";
		if (node.data.role == "2") {
			node.radius = 30;
			node.fillColor = "#fbe5d6";
		} else {
			node.radius = 40;
		}
	}
	
	function nodeFilter(nodeData){
		if (isShowWife){
			return true;
		} else {
			return nodeData.role != "2";
		}
    }

	function linkStyle(link) {

		//link.toDecoration = "arrow";
		if (link.to.data.role == "2") {
			link.fillColor = "#fbe5d6";
			link.direction = "R";
		}  else if (link.to.data.role == "1" || link.to.data.role == "3") {
			link.direction = "D";
			
		}  else if (link.to.data.role == "3") {
			link.direction = "U";
		}
	}

	function graphDoubleClick(event) {

		if (event.clickNode && event.clickNode.data.click) {
			event.clickNode.data.click = false;
			var uri = event.clickNode.data.id;
			var out;
			if (event.clickNode.data.role == "3") {
				out = event.clickNode.dataLinks[0].to;
			} else {
				out = event.clickNode.dataLinks[0].from;
			}

			$.get(ctx + "/service/person/dendrogram", {
				uri : uri,
				out : out,
				isMe : false
			}, function(result) {
				var data = {
					nodes : eval(result.nodes),
					links : eval(result.links)
				};
				chart.addData(data);
			});
		}
	}
</script>
</html>
