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
    <c:param name="menu" value="systemSetting"/>
</c:import>
<div class="container">
    <h2 id="top"><span>权限设置</span> <a href="${ctx}/systemSetting/main"> <span
				class="btn btn-default btn-sm pull-right" tag="back"><i
					class="glyphicon glyphicon-chevron-left"></i>&nbsp;系统管理</span></a></h2>
<hr/>
     
    <form method="post" id="workForm" action="${ctx}/systemSetting/loadRoleOfMenu" class="form-inline">
        <div>
            <div class="form-group">
            <input type="hidden" id="hidMenuId" name='menuId' class="formSaveMaster" value='' /> 
                <label>权限类型：</label>
                <select id="ctRole" name="roleId" class="input form-control formSaveMaster">
                <c:forEach var="x" items="${data.roles}">
                	<option <c:if test="${x.key==data.roleId}"> selected="selected" </c:if> value='${x.key}' >
                	${x.value}
                	</option>
                </c:forEach>
				</select>
            </div>
            <a class="btn btn-primary" id="btn_save"><i class="icon-ok icon-white"></i>保存</a>
        </div>
        <hr>
       	<div class="tab-content" id="dataDiv">
	        <div>
	            <div>
	            	<table id="tableData" cellpadding="0" cellspacing="0" border="0"
						class="table table-striped " id="example">
						<thead>
							<tr>
								<th>选择</th>
								<th>菜单名</th>
							</tr>
						</thead>
						<tbody id="originDiv">
						</tbody>
					</table>
	            </div>
	            <div tag="view" style="display:none;"></div>
	        </div>
    	</div>
        <hr>
    </form>
    <div style="display: none;" id="workView"></div>
</div>

<c:import url="/WEB-INF/inc/footer.jsp" />
<script src="${ctx}/res/plugin/echarts/2.2.0/echarts.js"></script>
<script src="${ctx}/res/plugin/d3/d3.v3.min.js"></script>
<script src="${ctx}/res/js/sysmanager/systemsetting/roleSetting.js"></script>
</body>
</html>
