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
<div class="container">
    <h2 id="top">上海图书馆家谱姓氏表
        <span class="btn btn-default" data="chs">简体</span>
        <span class="btn btn-default" data="cht">繁体</span>
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
    	//alert($(a).attr("auri"));
    	//alert($(a).attr("aname"));
    	
    	//获取父窗口tbale
    	 var parentTable = parent.$("#tableData>tbody");
    	//获取table中，选择的那一行
    	var ifcheck = parentTable.find("tr").find(".checkFlg");
    	ifcheck.each(function(i){
    		if($(this).val()=='1'){
    			/* if($(this).parent().find(".textNewValue").val()!=null && $(this).parent().find(".textNewValue").val()!=''){
	    			//URI赋值
	    			$(this).parent().find(".textNewValue").val($(this).parent().find(".textNewValue").val()+","+$(a).attr("auri"));
	    			//lable赋值
	    			$(this).parent().find(".spanNewValue").text($(this).parent().find(".spanNewValue").text()+","+$(a).attr("aname"));
    			}
    			else{
    				//URI赋值
	    			$(this).parent().find(".textNewValue").val($(a).attr("auri"));
	    			//lable赋值
	    			$(this).parent().find(".spanNewValue").text($(a).attr("aname"));
    			
    			} */
    			//URI赋值
    			$(this).parents("tr").find(".textNewValue").val($(a).attr("auri"));
    			//lable赋值
    			$(this).parents("tr").find(".spanNewValue").text($(a).attr("aname"));
    			var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
    			parent.layer.close(index);
    		//	parent.layer.msg('已选择 '+'"'+$(a).attr("aname")+'"'+"，您还可以继续选择。", {shade: 0.2})
    			return false;
    		}
    	});
     }
</script>
<c:import url="/WEB-INF/inc/footer.jsp" />
<script src="${ctx}/res/js/sysmanager/datamanager/familyName.js"></script>
</body>
</html>
