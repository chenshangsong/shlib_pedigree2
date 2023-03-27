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
    <h2 id="top">上海图书馆家谱谱籍分布图/地名表</h2><br>
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active"><a href="#tab_0" role="tab" data-toggle="tab">谱籍分布图</a></li>
        <li role="presentation"><a href="#tab_1" role="tab" data-toggle="tab">谱籍地名表</a></li>
    </ul>
    <div class="tab-content" id="main">
        <div role="tabpanel" class="tab-pane fade in active" id="tab_0">
            <div id="placeDiv"></div>
            <div tag="view" style="display:none;"></div>
        </div>
        <div role="tabpanel" class="tab-pane fade" id="tab_1">
            <div id="originDiv"></div>
            <div tag="view" style="display:none;"></div>
        </div>
    </div>
</div>
<div class="modal fade" id="placeModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="myModalLabel"></h4>
            </div>
            <div class="modal-body"></div>
        </div>
    </div>
</div>
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
<c:import url="/WEB-INF/inc/footer.jsp" />
<script src="${ctx}/res/plugin/openlayers/3.1.1/ol.js" type="text/javascript"></script>
<script src="${ctx}/res/js/sysmanager/placemanager/Conflist.js"></script>
</body>
</html>
