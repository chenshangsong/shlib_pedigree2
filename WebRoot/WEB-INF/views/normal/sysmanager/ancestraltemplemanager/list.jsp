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
    <h2 id="top"><span>上海图书馆家谱堂号列表</span> </h2>
    <hr/>
     <a href="${ctx}/dataManager/datalist"> <span class="btn btn-default btn-sm pull-right" tag="back"><i class="glyphicon glyphicon-chevron-left"></i>&nbsp;数据维护</span></a>
   <a href="${ctx}/ancestraltempleManager/dataContentAdd"> 
     	<span class="btn btn-default btn-sm pull-right"><i class="icon-plus"></i>&nbsp;新增
     </a>
    <form method="post" id="workForm" action="${ctx}/ancestraltempleManager/list" class="form-inline">
        <input type="hidden" name="pageSize" value="60" />
        <div>
            <div class="form-group">
                <label>堂号：</label>
                <input type="text" class="form-control" name="label" id="" placeholder="在此输入堂号">
              <label>姓氏：</label>
                <input type="text" class="form-control" name="familyName" id="tang" placeholder="在此输入姓氏">
            </div>
            <a class="btn btn-primary" id="btn_query"><i class="glyphicon glyphicon-search"></i>&nbsp;查 询</a>
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
<script src="${ctx}/res/js/sysmanager/ancestraltemplemanager/list.js"></script>
</body>
</html>
