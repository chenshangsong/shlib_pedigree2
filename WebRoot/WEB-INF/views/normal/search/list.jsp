<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
<c:import url="/WEB-INF/inc/link.jsp" />
</head>
<body>
	<c:import url="/WEB-INF/inc/header3.jsp">
		<c:param name="menu" value="work" />
	</c:import>

	<div class="container">
		<h2 id="top">
			<span>上海图书馆家谱文献列表</span> (<span id="count">0</span>)<a id='hSearch'
				href="${ctx}/service"> <span
				class="btn btn-default btn-sm pull-right"><i
					class="glyphicon glyphicon-random"></i>&nbsp;高级检索</span></a>
			<!-- <span>结果 (<span id="count"></span>)</span> -->
		</h2>
		<hr>
		<div style="text-align:center;">
		<form id="_searchForm" class="navbar-form navbar-left" role="search"
			action="${ctx}/service/search" style="width: 100%">
			<div class="input-group" style="width: 60%;">
				<input type="text" name="keyword" style="" class="form-control"
					placeholder="在此输入 标题/姓氏/责任者/谱籍/堂号 "> <span
					class="input-group-btn" style="width: 40px;">
					<button class="btn btn-default" type="submit">
						<i class="glyphicon glyphicon-search"></i>
					</button>
				</span>
			</div>
			
		</form>
		</div>
		<form method="post" id="searchForm" action="${ctx}/service/search"
			class="form-inline">
			<input type="hidden" name="pageSize" value="10" /> <input
				type="hidden" name="keyword" value="${param.keyword}" />
			<div id="dataDiv" class="table-responsive data_list"></div>
			<div id="pager"></div>
		</form>
		<div style="display: none;" id="workView"></div>
	</div>
	<script type="text/html" id="searchTpl">
<table width="100%">
        {@each datas as work}
        <tr>
            <td width="10%">
                <a href="${ctx}/service/work/list#uri=@{work.work}" target="_blank">
{@if work.imageFrontPath.length != 0}
 <img src="@{work.imageFrontPath}" style="width: 100px;">{@/if}   
 {@if work.imageFrontPath.length == 0}
<img src="${ctx}/res/images/work.jpg" style="width: 100px;">
{@/if}
</a>
            </td>
            <td tyle="padding-left: 20px;">
                <dt class="issue"><a href="${ctx}/service/work/list#uri=@{work.work}" target="_blank">@{work.dtitle} </a></dt>
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
	<c:import url="/WEB-INF/inc/footer.jsp" />
	<script src="${ctx}/res/plugin/page.js"></script>
	<script src="${ctx}/res/js/normal/search/list.js"></script>
</body>
</html>
