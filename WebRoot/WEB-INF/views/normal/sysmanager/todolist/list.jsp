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
		<c:param name="menu" value="personInfo" />
	</c:import>
	<div class="container">
		<h2 id="top">
			<span>待办事项</span> 
				<a href="${ctx}/userInfo/personInfo"> <span
				class="btn btn-default btn-sm pull-right" tag="back"><i
					class="glyphicon glyphicon-chevron-left"></i>&nbsp;个人中心</span></a>
			
		</h2>

		<hr />
		<form method="post" id="todoListForm" action="${ctx}/toDoList/list"
			class="form-inline">
			<input type="hidden" name="pageSize" value="10" />
			<div>
				<div class="form-group">
					<label>类别：</label> <select name="selDataType"
						class="input form-control">
						<option value=''>全部</option>
						<option value='1'>堂号</option>
						<option value='2'>姓氏</option>
						<option value='3'>机构</option>
						<option value='4'>先祖</option>
						<option value='5'>名人</option>
						<option value='6'>纂修者</option>
						<option value='7'>谱籍</option>
						<option value='8'>谱名</option>
						<option value='9'>取值词表</option>
						<option value='10'>书目</option>
					</select> <label>状态：</label> <select name="selCheckStatus"
						class="input form-control">
						<option value=''>全部</option>
						<option value='0'>待核准</option>
						<option value='1'>已核准</option>
					</select>
				</div>
				<a class="btn btn-primary" id="btn_query"><i class="glyphicon glyphicon-search"></i>&nbsp;查 询</a>
			</div>
			<hr>
			<div id="dataDiv" class="table-responsive data_list"></div>
			<!--  <hr> -->
			<div id="pager"></div>
		</form>
		<div style="display: none;" id="workView"></div>
	</div>
	<script type="text/html" id="todoListTpl">


							<table cellpadding="0" cellspacing="0" border="0"
								class="table table-striped " id="example">
								<thead>
									<tr>
										<th width='15%'>类型</th>
										<th width='15%'>用户姓名</th>
										<th width='15%'>时间</th>
										<th width='15%'>状态</th>
										<th width='15%'>操作</th>
									</tr>
								</thead>
								<tbody>
									 {@each works as x, i}
       	<tr class="odd gradeA">
											
											<td width='10%'>@{x.dataTypeName}</td>
											<td width='10%'>@{x.createUser }</td>
											<td width='10%'>@{x.createDate}</td>
											<td width='10%'>
											{@if x.checkStatus=='0'}
											<font color='red'>待核准</font>
											{@/if}
											{@if x.checkStatus=='1'}
											<font>已核准</font>
											{@/if}
											</td>
											<td width='10%' class="center">

												<input type='hidden' class="formSave dataType"
													name="dataType" value="@{x.dataType}" />
												<input type='hidden' class="formSave dataUri"
													name="dataUri" value="@{x.dataUri}" />
												<input type='hidden' class="formSave graphUri"
													name="graphUri" value="@{x.graphUri}" />

												<a id="btn_DealWith" href="javascript:void(0);" class="btn btn-primary btnDo">
													<i class="icon-edit icon-white"></i>处理
												</a>
											</td>
										</tr>
    {@/each}
								
								</tbody>
							</table>
</script>
	<c:import url="/WEB-INF/inc/footer.jsp" />
	<script src="${ctx}/res/plugin/page.js"></script>
	<script src="${ctx}/res/js/sysmanager/todolist/list.js"></script>
</body>
</html>
