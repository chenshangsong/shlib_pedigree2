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
    <h2 id="top">先祖列表
     <a href="${ctx}/dataManager/datalist"> <span class="btn btn-default btn-sm pull-right" tag="back"><i class="glyphicon glyphicon-chevron-left"></i>&nbsp;数据维护</span></a>
   
    </h2>
   
    <hr>
      <a
				href="${ctx}/personManager/dataContentAdd?type=4"> <span
				class="btn btn-default btn-sm pull-right"><i
					class="icon-plus"></i>&nbsp;新增 </a>
    <form role="form" id="listForm" class="form-inline">
        <input type="hidden" name="firstChar" id="firstChar"/>
        <input type="hidden" name="dataTypeId" value="${dataTypeId}" id="hidDataTypeId"/>
        <input type="hidden" name="pageSize" value="90" />
           <input type="hidden" name="type" value="" />
              <input type="hidden" name="tag" value=0 />
        <div id="wikiword" style="padding-bottom:15px;">
            <c:forTokens items="全,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z" delims="," var="item">
                <span class="btn btn-default">${item}</span>
            </c:forTokens>
             <div class="form-group">
                  <!-- <label><input type="checkbox" name="inference" id="inference"> 合并</label> -->
            </div> 
        </div>
        <div class="form-group">
            <label>姓氏：</label>
            <input type="text" class="form-control" name="familyName" id="familyName" placeholder="在此输入姓氏">
        </div>
        <div class="form-group">
            <label>姓名：</label>
            <input type="hidden" name="uri" />
            <input type="text" class="form-control" name="name" id="name" placeholder="在此输入姓名">
        </div>
        <a class="btn btn-primary" id="btn_query"><i class="glyphicon glyphicon-search"></i>&nbsp;查 询</a>
        
        <hr>
        <div class="data_list">请输入条件进行查询...</div>
        <hr>
        <div id="pager"></div>
    </form>
</div>

<div class="modal fade" id="personModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="myModalLabel"></h4>
            </div>
            ${result.personsasdf}
            <div class="modal-body"></div>
        </div>
    </div>
</div>
<c:import url="/WEB-INF/inc/footer.jsp" />
<script src="${ctx}/res/plugin/page.js"></script>
<script src="${ctx}/res/js/sysmanager/ancestorsmanager/list.js"></script>
</body>
</html>
