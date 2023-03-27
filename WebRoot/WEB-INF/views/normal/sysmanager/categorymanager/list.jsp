<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
    <c:import url="/WEB-INF/inc/link.jsp" />
    <style>
        .tree * {margin: 0; padding: 0;}
        .tree {width:1300px;}
        .tree ul {
            padding-top: 20px; position: relative;

            transition: all 0.5s;
            -webkit-transition: all 0.5s;
            -moz-transition: all 0.5s;
        }

        .tree li {
            float: left; text-align: center;
            list-style-type: none;
            position: relative;
            padding: 20px 5px 0 5px;

            transition: all 0.5s;
            -webkit-transition: all 0.5s;
            -moz-transition: all 0.5s;
        }

        /*We will use ::before and ::after to draw the connectors*/

        .tree li::before, .tree li::after{
            content: '';
            position: absolute; top: 0; right: 50%;
            border-top: 1px solid #ccc;
            width:50%; height: 20px;
        }
        .tree li::after{
            right: auto; left: 50%;
            border-left: 1px solid #ccc;
        }

        /*We need to remove left-right connectors from elements without
        any siblings*/
        .tree li:only-child::after, .tree li:only-child::before {
            display: none;
        }

        /*Remove space from the top of single children*/
        .tree li:only-child{ padding-top: 0;}

        /*Remove left connector from first child and
        right connector from last child*/
        .tree li:first-child::before, .tree li:last-child::after{
            border: 0 none;
        }
        /*Adding back the vertical connector to the last nodes*/
        .tree li:last-child::before{
            border-right: 1px solid #ccc;
            border-radius: 0 5px 0 0;
            -webkit-border-radius: 0 5px 0 0;
            -moz-border-radius: 0 5px 0 0;
        }
        .tree li:first-child::after{
            border-radius: 5px 0 0 0;
            -webkit-border-radius: 5px 0 0 0;
            -moz-border-radius: 5px 0 0 0;
        }

        /*Time to add downward connectors from parents*/
        .tree ul ul::before{
            content: '';
            position: absolute; top: 0; left: 50%;
            border-left: 1px solid #ccc;
            width: 0; height: 20px;
        }

        .tree li a{
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
            background: #c8e4f8; color: #000; border: 1px solid #94a0b4;
        }
        /*Connector styles on hover*/
        .tree li a:hover+ul li::after,
        .tree li a:hover+ul li::before,
        .tree li a:hover+ul::before,
        .tree li a:hover+ul ul::before{
            border-color:  #94a0b4;
        }
        .popover-content{white-space: normal;}
    </style>
</head>
<body>
<c:import url="/WEB-INF/inc/header3.jsp">
    <c:param name="menu" value="todolist"/>
</c:import>
<div class="container">
    <h2 id="top"><span>上海图书馆家谱取值词表</span> </h2>
    <hr/>
     <a href="${ctx}/dataManager/datalist"> 
     	<span class="btn btn-default btn-sm pull-right" tag="back"><i class="glyphicon glyphicon-chevron-left"></i>&nbsp;数据维护</span>
     </a>
     <a id="add"> 
     	<span class="btn btn-default btn-sm pull-right"><i class="icon-plus"></i>&nbsp;添加</span>
     </a>
     <a href="${ctx}/categoryManager/categoryTypelist"> 
     	<span class="btn btn-default btn-sm pull-right"><i class="icon-wrench"></i>&nbsp;类别维护</span>
     </a>
   
    <form method="post" id="workForm" action="${ctx}/categoryManager/list" class="form-inline">
        <input type="hidden" name="pageSize" value="60" />
        <div>
            <div class="form-group">
                <label>类别名称：</label>
                <select id="ctType" name="tang" class="input form-control">
                <c:forEach var="x" items="${data}">
                	<option value='${x.categoryNameEn}'
                	<c:if test="${x.categoryNameEn=='edition'}">
						selected="selected"
					</c:if>
					>${x.categoryNameCn}</option>
                </c:forEach>
				</select>
            </div>
            <a class="btn btn-primary" id="btn_query"><i class="glyphicon glyphicon-search"></i>&nbsp;查 询</a>
        </div>
        <hr>
        <div id="dataDiv" class="table-responsive data_list">
        
        </div>	
       <!--  <hr> -->
        <div id="pager"></div>
    </form>
    <div style="display: none;" id="workView"></div>
</div>
<script type="text/html" id="worksTpl">
    {@each works as work, i}
       <div class="col-xs-2">
            <a href="${ctx}/categoryManager/dataContentlist?id=@{work.s}">@{work.o}</a>
        </div>
    {@/each}
    {@if works.length == 0}<div class="row"><div class="col-xs-12">未查询到相关信息！</div></div>{@/if}
</script>

<script>
	$(function(){
		$('#add').click(function(){
			var ctType = $('#ctType').val();
			var url = ctx+'/categoryManager/dataContentAdd?ctType='+ctType;
			window.location.href = url; 
		});
	})
</script>

<c:import url="/WEB-INF/inc/footer.jsp" />
<script src="${ctx}/res/plugin/page.js"></script>
<script src="${ctx}/res/js/sysmanager/categorymanager/list.js"></script>
</body>
</html>
