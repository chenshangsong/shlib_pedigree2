<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
    <c:import url="/WEB-INF/inc/link.jsp" />
</head>
<body>
<c:import url="/WEB-INF/inc/header3.jsp">
    <c:param name="menu" value="todolist"/>
</c:import>
<div class="container">
    <h2 id="top"><span>上海图书馆家谱机构列表</span> </h2>
    <hr/>
     <a href="${ctx}/dataManager/datalist"> <span class="btn btn-default btn-sm pull-right" tag="back"><i class="glyphicon glyphicon-chevron-left"></i>&nbsp;数据维护</span></a>
     <a href="${ctx}/organizationManager/dataContentAdd"> 
     	<span class="btn btn-default btn-sm pull-right"><i class="icon-plus"></i>&nbsp;新增
     </a>
    <form method="post" id="workForm" action="${ctx}/organizationManager/list" class="form-inline">
        <input type="hidden" name="pageSize" value="60" />
        <div>
            <div class="form-group">
                <label>机构名称：</label>
                <input type="text" class="form-control" name="place" id="place" placeholder="在此机构名称">
            </div>
            <a class="btn btn-primary" id="btn_query">查 询</a>
        </div>
        <hr>
       	<div class="tab-content" id="dataDiv">
	        <div>
	            <div id="originDiv"></div>
	            <div tag="view" style="display:none;"></div>
	        </div>
    	</div>
        <hr>
        <div id="pager"></div>
    </form>
    <div style="display: none;" id="workView"></div>
</div>

<c:import url="/WEB-INF/inc/footer.jsp" />
<script src="${ctx}/res/plugin/page.js"></script>
<script src="${ctx}/res/plugin/echarts/2.2.0/echarts.js"></script>
<script src="${ctx}/res/plugin/d3/d3.v3.min.js"></script>
<script src="${ctx}/res/js/sysmanager/organizationmanager/list.js"></script>
</body>
</html>
