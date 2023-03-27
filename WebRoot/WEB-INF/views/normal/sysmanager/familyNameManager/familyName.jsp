<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
    <c:import url="/WEB-INF/inc/link.jsp" />
    <style>.well a{padding:0 10px 0 10px; line-height: 25px;}</style>
</head>
<body>
<c:import url="/WEB-INF/inc/header3.jsp">
  <c:param name="menu" value="todolist" />
</c:import>
<div class="container">
    <h2 id="top">上海图书馆家谱姓氏表
        <span class="btn btn-default" data="chs">简体</span>
        <span class="btn btn-default" data="cht">繁体</span>
         <a href="${ctx}/dataManager/datalist"> <span class="btn btn-default btn-sm pull-right" tag="back"><i class="glyphicon glyphicon-chevron-left"></i>&nbsp;数据维护</span></a>
     <a href="${ctx}/familyNameManager/dataContentAdd"> 
     	<span class="btn btn-default btn-sm pull-right"><i class="icon-plus"></i>&nbsp;新增
     </a>
    </h2>
   
    <hr>
     
    <c:forTokens items="A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z" delims="," var="item">
        <div char="${item}">
            <h3>${item}</h3>
            <div class="well">
                <div tag="chs" style="display: none;"></div>
                <div tag="cht" style="display: none;"></div>
                <div tag="en" style="display: none;"></div>
            </div>
        </div>
    </c:forTokens>
</div>
<script>
    var familyNames = ${jsonData};
    function aClick(a){
    	var id = $(a).attr("auri");
    	window.location.href='${ctx}/familyNameManager/dataContentlist?id='+id;
     }
</script>
<c:import url="/WEB-INF/inc/footer.jsp" />
<script src="${ctx}/res/js/sysmanager/familyNameManager/familyName.js"></script>
</body>
</html>
