<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>

<link href="${ctx}/res/sysmanager/bootstrap/css/bootstrap.min.css"
	rel="stylesheet" media="screen">
<link
	href="${ctx}/res/sysmanager/bootstrap/css/bootstrap-responsive.min.css"
	rel="stylesheet" media="screen">
<link href="${ctx}/res/sysmanager/assets/DT_bootstrap.css"
	rel="stylesheet" media="screen">
<c:import url="/WEB-INF/inc/sysmanagerlink.jsp" />
</head>
<body>
	<c:import url="/WEB-INF/inc/sysHeader.jsp">
		<c:param name="menu" value="todolist" />
	</c:import>
	<!-- table  Begin -->
	<div class="container">
		<div class="panel panel-default">
			<h2 id="top">数据维护</h2>
			<hr>
			<div class="col-xs-4">
				<div class="panel panel-info">
					<div class="panel-body desc">
						<a href="http://gen.library.sh.cn/Work/14224" tag="work">吴兴钮氏西支家谱</a><br>
						<a href="http://gen.library.sh.cn/Work/14452" tag="work">资阳王氏九修支谱</a><br>
						<a href="http://gen.library.sh.cn/Work/14476" tag="work">资江枯杉黄氏七修族谱</a><br>
						<a href="http://gen.library.sh.cn/Work/14683" tag="work">盛氏七修族谱</a><br>
						<a href="http://gen.library.sh.cn/Work/14871" tag="work">王氏族谱</a><br>
						<a class="pull-right" style="margin-top: -25px;"
							href="javascript:void(0);" tag="work_more"><i>More...</i></a>
					</div>
				</div>
			</div>
				<div class="col-xs-4">
				<div class="panel panel-info">
					<div class="panel-body desc">
						<a href="http://gen.library.sh.cn/Work/14224" tag="work">吴兴钮氏西支家谱</a><br>
						<a href="http://gen.library.sh.cn/Work/14452" tag="work">资阳王氏九修支谱</a><br>
						<a href="http://gen.library.sh.cn/Work/14476" tag="work">资江枯杉黄氏七修族谱</a><br>
						<a href="http://gen.library.sh.cn/Work/14683" tag="work">盛氏七修族谱</a><br>
						<a href="http://gen.library.sh.cn/Work/14871" tag="work">王氏族谱</a><br>
						<a class="pull-right" style="margin-top: -25px;"
							href="javascript:void(0);" tag="work_more"><i>More...</i></a>
					</div>
				</div>
			</div>
			<!-- <div class="row-fluid">
				<!-- block -->
				<div class="block">
					<div class="block-content collapse in">
						<div class="span12">

							<table cellpadding="0" cellspacing="0" border="0"
								class="table table-striped " id="example">
								<thead>
									<tr>
										<th width='5%'>序号</th>
										<th width='25%'>RDF(元)</th>
										<th width='25%'>属性</th>
										<th width='10%'>操作</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="x" varStatus="status" items="${data}">
										<tr class="odd gradeA">
											<td width='5%'>${status.index + 1}</td>
											<td width='25%'>${x}</td>
											<td width='25%'><c:forEach var="y" items="${x}">
													<span>${y.value}</span>
												</c:forEach></td>
											<td width='10%' class="center">
												<button id="btn_update" class="btn btn-primary">
													<i class="icon-pencil icon-white"></i>数据管理
												</button>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				</div> -->
				<!-- /block -->
			</div>
		</div>
	</div>

	<c:import url="/WEB-INF/inc/footer.jsp" />
	<script>
		$(function() {
			$(".btn-primary").click(function() {
				//获取本体唯一属性:去除空格
				var id = $.trim($(this).parents('tr').find('td:eq(2)').text());
				//	window.location.href='${ctx}/dataManager/dataContentlist?id='+id;
				window.location.href = '${ctx}/dataManager/personList';
			});
		});
	</script>
	<script
		src="${ctx}/res/sysmanager/vendors/datatables/js/jquery.dataTables.min.js"></script>
	<script src="${ctx}/res/sysmanager/assets/scripts.js"></script>
	<script src="${ctx}/res/sysmanager/assets/DT_bootstrap.js"></script>
</body>
</html>