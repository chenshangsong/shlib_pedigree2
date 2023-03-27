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
		<c:param name="menu" value="systemSetting" />
	</c:import>
	<div class="container">
		<h2 id="top">
			<span>外网全文设置</span> <a href="${ctx}/systemSetting/main"> <span
				class="btn btn-default btn-sm pull-right" tag="back"><i
					class="glyphicon glyphicon-chevron-left"></i>&nbsp;系统管理</span></a>
		</h2>
		<hr />
		<div class="form-group">
			<table>
				<tr>
					<td valign='top'>DOI号：</td>
					<td><textarea rows="5" cols="80" id='doi'></textarea></td>
					<td valign='top' style='color: red;font-size:14px;'>&nbsp;&nbsp;(如果输入多个Doi号,请用分号隔开,如:doi1;doi2)</td>
				</tr>
				<tr>
					<td colspan=2 align='right'>
						<a class="btn btn-primary" id="btn_open">开放</a>
						<a class="btn btn-primary" id="btn_close">关闭</a>
					</td>
				</tr>
			</table>
		</div>
		<br />
		<div class="tab-content" id="dataDiv">
			<div>
				<div>
					<table id="tableData" cellpadding="0" cellspacing="0" border="0"
						class="table table-striped " id="example">
						<thead>
							<tr>
								<th>Doi</th>
								<th>操作结果</th>
							</tr>
						</thead>
						<tbody id="originDiv">
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<c:import url="/WEB-INF/inc/footer.jsp" />
	<script src="${ctx}/res/plugin/echarts/2.2.0/echarts.js"></script>
	<script src="${ctx}/res/plugin/d3/d3.v3.min.js"></script>
	<script src="${ctx}/res/js/sysmanager/systemsetting/doi.js"></script>
</body>
</html>
