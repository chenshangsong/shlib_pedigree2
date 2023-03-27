<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
    <c:import url="/WEB-INF/inc/link.jsp" />
</head>
<body>
<div class="container">
    <h2 id="top"><span>上海图书馆家谱堂号列表</span> </h2>
    <hr/>
    <form method="post" id="workForm" action="${ctx}/ancestraltempleManager/list" class="form-inline">
        <input type="hidden" name="pageSize" value="60" />
        <div>
            <div class="form-group">
                <label>堂号：</label>
                <input type="text" class="form-control" name="label"  placeholder="在此输入堂号">
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
<script src="${ctx}/res/js/sysmanager/ancestraltemplemanager/Conflist.js"></script>
<script>
    function aClick(a){
    	//获取父窗口tbale
    	 var parentTable = parent.$(".tableData>tbody");
    	//获取table中，选择的那一行
    	var ifcheck = parentTable.find("tr").find("td:eq(2)").find(".checkFlg");
    	ifcheck.each(function(i){
    		if($(this).val()=='1'){
    			//URI赋值
    			$(this).parent().find(".textNewValue").val($(a).attr("auri"));
    			//lable赋值
    			$(this).parent().find(".spanNewValue").text($(a).attr("aname"));
    			var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
    			parent.layer.close(index);
    		//	parent.layer.msg('已选择 '+'"'+$(a).attr("aname")+'"'+"，您还可以继续选择。", {shade: 0.2})
    			return false;
    		}
    	});
     }
</script>
</body>
</html>
