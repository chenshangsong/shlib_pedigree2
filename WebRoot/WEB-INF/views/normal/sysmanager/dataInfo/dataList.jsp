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
		<c:param name="menu" value="todolist" />
	</c:import>
	<!-- table  Begin -->
	<div class="container">
		<h2 id="top">数据维护<a href="${ctx}/toDoList/list"> <span
				class="btn btn-default btn-sm pull-right" tag="back"><i
					class="glyphicon glyphicon-bell"></i>&nbsp;待办事项</span></a></h2>
		<hr>
		<div class="row">
			<div class="col col-lg-4 col-md-4 col-sm-4"
				style="height: 40%; cursor: pointer;"
				onclick="location.href='../titleManager/list'">

				<div class="well" style="background-color: #d0e9c6 !important;">

					<h2>谱名</h2>

					<br> <br>
					<button id="btn_save" class="btn btnSave btn-primary">
						<i class="icon-edit icon-white"></i>维护
					</button>
				</div>


			</div>


			<div class="col col-lg-4 col-md-4 col-sm-4" style="cursor: pointer;"
				onclick="location.href='../placeManager/list'">


				<div class="well" style="background-color: #f5f8f9 !important;">


					<h2>谱籍</h2>

					<br> <br>
					<button id="btn_save" class="btn btnSave btn-primary">
						<i class="icon-edit icon-white"></i>维护
					</button>
				</div>


			</div>


			<div class="col col-lg-4 col-md-4 col-sm-4" style="cursor: pointer;"
				onclick="location.href='../ancestraltempleManager/list'">

				<div class="well" style="background-color: #d9edf7 !important;">

					<h2>堂号</h2>

					<br> <br>
					<button id="btn_save" class="btn btnSave btn-primary">
						<i class="icon-edit icon-white"></i>维护
					</button>
				</div>


			</div>
			<div class="col col-lg-4 col-md-4 col-sm-4">
				<div class="well" style="background-color: #d0e9c6 !important;">
					<h2>先祖名人</h2>
					<br> <br>
					<button onclick="location.href='../personManager/ancestorsList'"
						class="btn  btn-primary">
						<i class="icon-edit icon-white"></i>先祖维护
					</button>
					<button onclick="location.href='../personManager/famousList'"
						class="btn  btn-primary">
						<i class="icon-edit icon-white"></i>名人维护
					</button>
				</div>

			</div>

			<div class="col col-lg-4 col-md-4 col-sm-4" style="cursor: pointer;"
				onclick="location.href='../personManager/writerList'">

				<div class="well" style="background-color: #f5f8f9 !important;">

					<h2>纂修者</h2>

					<br> <br>
					<button id="btn_save" class="btn btnSave btn-primary">
						<i class="icon-edit icon-white"></i>维护
					</button>

				</div>


			</div>
			<div class="col col-lg-4 col-md-4 col-sm-4">

				<div class="well" style="background-color: #d9edf7 !important;">

					<h2>姓氏</h2>

					<br> <br>
					<button id="btn_save"
						onclick="location.href='../familyNameManager/familyName'"
						class="btn btnSave btn-primary">
						<i class="icon-edit icon-white"></i>维护
					</button>

					<!-- <button 
							id="btn_add" 
							onclick="location.href='../familyNameManager/dataContentAdd'"
							class="btn btn_add btn-primary">
							<i class="icon-edit icon-white"></i>添加
						</button> -->
				</div>


			</div>


			<div class="col col-lg-4 col-md-4 col-sm-4" style="cursor: pointer;"
				onclick="location.href='../organizationManager/list'">
				<div class="well" style="background-color: #d0e9c6 !important;">
					<h2>机构</h2>
					<br> <br>
					<button id="btn_save" class="btn btnSave btn-primary">
						<i class="icon-edit icon-white"></i>维护
					</button>

				</div>


			</div>


			<div class="col col-lg-4 col-md-4 col-sm-4" style="cursor: pointer;">
				<div class="well" style="background-color: #f5f8f9 !important;">
					<h2>书目</h2>
					<br> <br>
					<button id="btn_save" onclick="location.href='../workManager/list'" class="btn btnSave btn-primary">
						<i class="icon-edit icon-white"></i>维护
					</button>
                    <button id="btn_delete" onclick="location.href='../workManager/workDelList'" class="btn btn_delete btn-primary">
						<i class="icon-list icon-white"></i>删除记录
					</button>
				</div>


			</div>
			<div class="col col-lg-4 col-md-4 col-sm-4">

				<div class="well" style="background-color: #d9edf7 !important;">


					<h2>取值词表</h2>
					<br> <br>
					<button id="btn_save" class="btn btnSave btn-primary"
						onclick="location.href='../categoryManager/list'">
						<i class="icon-edit icon-white"></i>维护
					</button>
					<button id="btn_categoryType" class="btn btnSave btn-primary"
						onclick="location.href='../categoryManager/categoryTypelist'">
						<i class="icon-edit icon-white"></i>类别维护
					</button>
				</div>
			</div>
		</div>
	</div>
	<c:import url="/WEB-INF/inc/footer.jsp" />
	<script>
		$(function() {
			/* $(".btn-primary").click(function() {
				//获取本体唯一属性:去除空格
				var id = $.trim($(this).parents('tr').find('td:eq(2)').text());
				//	window.location.href='${ctx}/dataManager/dataContentlist?id='+id;
				window.location.href = '${ctx}/dataManager/personList';
			}); */
		});
	</script>
</body>
</html>