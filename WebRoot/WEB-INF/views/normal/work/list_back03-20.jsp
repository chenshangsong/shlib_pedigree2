<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
<c:import url="/WEB-INF/inc/link.jsp" />
<style>
.tree * {
	margin: 0;
	padding: 0;
}

.tree {
	width: 1300px;
}

.tree ul {
	padding-top: 20px;
	position: relative;
	transition: all 0.5s;
	-webkit-transition: all 0.5s;
	-moz-transition: all 0.5s;
}

.tree li {
	float: left;
	text-align: center;
	list-style-type: none;
	position: relative;
	padding: 20px 5px 0 5px;
	transition: all 0.5s;
	-webkit-transition: all 0.5s;
	-moz-transition: all 0.5s;
}

/*We will use ::before and ::after to draw the connectors*/
.tree li::before, .tree li::after {
	content: '';
	position: absolute;
	top: 0;
	right: 50%;
	border-top: 1px solid #ccc;
	width: 50%;
	height: 20px;
}

.tree li::after {
	right: auto;
	left: 50%;
	border-left: 1px solid #ccc;
}

/*We need to remove left-right connectors from elements without
        any siblings*/
.tree li:only-child::after, .tree li:only-child::before {
	display: none;
}

/*Remove space from the top of single children*/
.tree li:only-child {
	padding-top: 0;
}

/*Remove left connector from first child and
        right connector from last child*/
.tree li:first-child::before, .tree li:last-child::after {
	border: 0 none;
}
/*Adding back the vertical connector to the last nodes*/
.tree li:last-child::before {
	border-right: 1px solid #ccc;
	border-radius: 0 5px 0 0;
	-webkit-border-radius: 0 5px 0 0;
	-moz-border-radius: 0 5px 0 0;
}

.tree li:first-child::after {
	border-radius: 5px 0 0 0;
	-webkit-border-radius: 5px 0 0 0;
	-moz-border-radius: 5px 0 0 0;
}

/*Time to add downward connectors from parents*/
.tree ul ul::before {
	content: '';
	position: absolute;
	top: 0;
	left: 50%;
	border-left: 1px solid #ccc;
	width: 0;
	height: 20px;
}

.tree li a {
	border: 1px solid #ccc;
	padding: 5px 10px;
	text-decoration: none;
	color: #666;
	font-family: arial, verdana, tahoma;
	font-size: 11px;
	display: inline-block;
	border-radius: 5px;
	-webkit-border-radius: 5px;
	-moz-border-radius: 5px;
	transition: all 0.5s;
	-webkit-transition: all 0.5s;
	-moz-transition: all 0.5s;
}

/*Time for some hover effects*/
/*We will apply the hover effect the the lineage of the element also*/
.tree li a:hover, .tree li a:hover+ul li a {
	background: #c8e4f8;
	color: #000;
	border: 1px solid #94a0b4;
}
/*Connector styles on hover*/
.tree li a:hover+ul li::after, .tree li a:hover+ul li::before, .tree li a:hover+ul::before,
	.tree li a:hover+ul ul::before {
	border-color: #94a0b4;
}

.popover-content {
	white-space: normal;
}
</style>
</head>
<body>
	<c:import url="/WEB-INF/inc/header3.jsp">
		<c:param name="menu" value="work" />
	</c:import>
	<div class="container">
		<h2 id="top">
			<span>上海图书馆家谱文献列表(<span id="count">0</span>)</span>
			<a href="javascript:void(0);" target='_blank' id="btnPxList"
					class="btn btnFeedBack btn-default btn-sm pull-right"
					style="display: none;"><i class="icon-inbox"></i>&nbsp;查看谱系图</a>
			<c:if test="${ctxRoleId!=null}">
				<a href="javascript:void(0);" id="btnFeedBackList"
					class="btn btnFeedBack btn-default btn-sm pull-right"
					style="display: none;"><i class="icon-inbox"></i>&nbsp;反馈</a>
				<a href="javascript:void(0)" target="_blank" id="btnEdit"
					class="btn btnFeedBack btn-default btn-sm pull-right" style="display: none;"><i
					class="icon-edit"></i>&nbsp;编辑</a>
			</c:if>
			<input type='hidden' id='hidUri'></input> <a id='sSearch' class="Search"
				href="${ctx}/service/search"> <span
				class="Search btn btn-default btn-sm pull-right" ><i class="glyphicon glyphicon-random"></i>&nbsp; 简单检索</span></a>
				<a id='hSearch' class="Search"
				href="${ctx}/service"> <span
				class=" btn btn-default btn-sm pull-right"><i
					class="glyphicon glyphicon-random"></i>&nbsp;高级检索</span></a>

		</h2>

		<hr />

		<form method="post" id="workForm" action="${ctx}/service/work/list"
			class="form-inline">
			<input type="hidden" name="pageSize" value="60" />
			<div>
				<table>
					<tr>
						<td><label>标题</label></td>
						<td class="left10"><input type="text" value="${data.title}"
							class="form-control" name="title" id="title" placeholder="在此输入标题"></td>
						<td class="left30"><label>姓氏</label></td>
						<td class="left10"><input type="text"
							value="${data.familyName}" class="form-control" name="familyName"
							id="familyName" placeholder="在此输入家谱名人姓氏"></td>
						<td class="left30"><label>责任者</label></td>
						<td class="left10"><input type="text" value="${data.creator}"
							class="form-control" name="creator" id="creator"
							placeholder="在此输入家谱责任者"></td>
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
							name="organization" id="organization" placeholder="在此输入馆藏地"></td>
						<td class="left30"><button type="submit"
								class="btn btn-primary" id="btn_query">
								<i class="glyphicon glyphicon-search"></i>&nbsp;查 询
							</button></td>
					</tr>
				</table>
			</div>
			<hr>
			<div id="dataDiv" class="table-responsive data_list"></div>
			<hr>
			<div id="pager"></div>
		</form>
		<div style="display: none;" id="workView"></div>
	</div>

	<div class="modal fade" id="migModal" tabindex="-1" role="dialog"
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
	<div class="modal fade" id="treeModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 1320px;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title"></h4>
				</div>
				<div class="modal-body" style="margin: 0px; padding: 0px;">
					<div style="width: 100%; height: 600px; overflow: auto;">
						<div id="tree_container"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="personModal" tabindex="-1" role="dialog"
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

	<script type="text/html" id="worksTpl">
<table>
        {@each works as work}
        <tr>
            <td>
              <a href="${ctx}/service/work/list#uri=@{work.work}" target="_blank">  <img src="../../res/images/work.jpg" style="width: 100px;"></a>
            </td>
            <td style="padding-left: 20px;">
                <dt class="issue"><a href="${ctx}/service/work/list#uri=@{work.work}" target="_blank">@{work.dtitle}  </a></dt>
 {@if work.title.length != 0}<dd><b>[正题名]  </b>&nbsp;&nbsp; @{work.title}</dd>{@/if}    
 {@if work.subtitles.length != 0}<dd><b>[副题名]  </b>&nbsp;&nbsp; @{work.subtitles}</dd>{@/if}        
{@if work.label != 0}<dd><b>[堂号]  </b>&nbsp;&nbsp; @{work.label}</dd>{@/if}     
                {@if work.creators.length != 0}<dd><b>[责任者]</b> @{work.creators}</dd>{@/if}
{@if work.desc != 0}<dd><b>[摘要]  </b>&nbsp;&nbsp; @{work.desc}</dd>{@/if}   
<hr>
            </td>
        <tr>
        {@/each}
        {@if works.length == 0}
        <tr><td colspan="3">未查询到相关信息！</td></tr>
        {@/if}
    </table>
</script>
	<script type="text/html" id="workTpl">
    <div class="row">
        <div class="col-xs-8">
            <ul class="work">
                <li style="line-height: 30px;">
                   {@if title != ''}   <p>【正题名】 @{title}</p>{@/if}
                   {@if subTitle != ''}   <p>【副题名】 @{subTitle}</p>{@/if}
					{@if creator != ''}  <p>【责任者】 @{creator}</p>{@/if}
                    {@if tangh != ''}   <p>【堂号】 @{tangh}</p>{@/if}
                   {@if instances && instances.length > 0 && instances[0]['temporal'] != ''}  <p>【纂修时间】 &nbsp;&nbsp;&nbsp@{instances[0]['temporal']}</p>{@/if}
					{@if note != ''}    <p>【摘要】 &nbsp;&nbsp;&nbsp;@{note}</p>{@/if}              
 {@if instances && instances.length > 0}<p>【版本】 &nbsp;&nbsp;&nbsp@{instances[0]['edition']}</p> 
                                        {@if instances[0]['extent'] != ''}  <p>【数量】 &nbsp;&nbsp;&nbsp@{instances[0]['extent']}</p>{@/if}{@/if}
                    {@if instances && instances.length > 0}
             <ul class="nav nav-tabs" role="tablist">
                    {@each instances as instance, st}
                        <li role="presentation" class="@{st == 0 ? 'active':''}"><a href="#tab_@{st}" role="tab" data-toggle="tab"> @{instance['ab']?instance['ab']:'其他'}</a></li>
                    {@/each}
                    </ul>
                    <div class="tab-content">
                        {@each instances as instance, st}
                          <div role="tabpanel" class="tab-pane @{st == 0 ? ' fade in active':''}" id="tab_@{st}">
                             @{instance['ab']?'馆藏地：'+instance['org']:''}{@if instance['ab']}<br>{@/if}
                             @{instance['ab']?'地址：'+instance['addr']:''}{@if instance['ab']}<br>{@/if}
                             {@if instance['ab']} @@{instance['description']?instance['description']:''}{@/if}                            
                            {@if !instance['ab']} @{instance['description']?instance['description']:''}{@/if}                            
                          </div>
                        {@/each}
                    </div>
                    {@/if}
                </li>
            </ul>
        </div>
        <div class="col-xs-4">
            <div class="panel panel-info">
                <!-- Default panel contents -->
                <div class="panel-heading">
                    <h3 class="panel-title" id="link_Classes">
                        <strong>家族先祖、名人</strong>
                    </h3>
                </div>
                <div class="table-responsive">
                    <table id='personTable' class="table table-hover">
                        {@if !familyRelations || familyRelations.length == 0}
                        <tr>
                            <td colspan="3">暂无家族先祖、名人！</td>
                        </tr>
                        {@/if}
                        {@if familyRelations && familyRelations.length > 0}
                        <tr>
                            <th>姓名</th>
                            <th>类型</th>
                            <th>朝代</th>
                        </tr>
                        {@each familyRelations as relation}
                        <tr>
<!--<td><a href="${ctx}/service/person/list?uri=&name=@{relation.name}" target="_blank">@{relation.name}</a></td>-->
                          <td><a href="${ctx}/service/work/persons?uri=@{relation.uri}" target="_blank">@{relation.name}</a>   </td>
                            <td>@{relation.roles}</td>
                            <td>@{relation.time}</td>
                        </tr>
                        {@/each}
                        {@/if}
                    </table>
                </div>
            </div>
        </div>
    </div>
</script>
	<c:import url="/WEB-INF/inc/footer.jsp" />
	<script src="${ctx}/res/plugin/page.js"></script>
	<script src="${ctx}/res/plugin/echarts/2.2.0/echarts.js"></script>
	<script src="${ctx}/res/plugin/d3/d3.v3.min.js"></script>
	<script src="${ctx}/res/js/normal/work/list.js"></script>
	<script src="${ctx}/res/commJS/roleFunction.js"></script>
</body>
<script>
<!--修改 陈尚松 new add -->
	$(function() {

		
		var user = '${ctxRoleId.getRoleId()}';
		if(user!=$.roleGroup.normal){
			$("#btnEdit").show();
		}
		$("#btnEdit").click(
						function() {
							window.location.href = '${ctx}/workManager/dataContentlist?id='
									+ $("#hidUri").val() + '&dataTypeId=10';
						}
				);
		$("#btnFeedBackList").click(
				function() {
					var strContent = null;
					var strTitle = null;
					strTitle = '反馈信息';
					strContent = '${ctx}/feedBack/feedBackMainListConf?uri='
							+ $("#hidUri").val();
					// 弹出姓氏搜索框
					layer.open({
						type : 2,
						title : strTitle,
						shadeClose : true,
						shade : 0.8,
						area : [ '90%', '90%' ],
						content : strContent
					// iframe的url
					});
				});
	});
</script>
<!-- 分享按钮 BEGIN -->
<script type="text/javascript" >
var jiathis_config={
	summary:"家谱知识服务平台-家谱文献",
	pic:"http://jpv1.library.sh.cn/jp/res/logo.png",
	showClose:true,
	shortUrl:true,
	hiservicere:false
}
</script>
<script type="text/javascript" src="http://v3.jiathis.com/code/jiathis_r.js?btn=r2.gif&move=0" charset="utf-8"></script>
<!-- 分享按钮 END -->


</html>
