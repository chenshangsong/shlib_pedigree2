<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
<c:import url="/WEB-INF/inc/link.jsp" />
</head>
<body>
	<c:forEach var="x" items="${data}">
		<div class="col-xs-2">
			<a auri="${x.s}" aname="${x.o}" target=_blank
				onClick="aClick(this)" href="javascript:void(0)">${x.o}<i
				class="icon-ok-sign"></i></a>
		</div>
	</c:forEach>
</body>
<script>
	function aClick(a) {
		//获取父窗口tbale
		var parentTable = parent.$(".tableNormalAndInstance>tbody");
		//获取table中，选择的那一行
		var ifcheck = parentTable.find("tr").find("td:eq(2)").find(".checkFlg");
		ifcheck.each(function(i) {
					if ($(this).val() == '1') {
						//URI赋值
						$(this).parent().find(".textNewValue").val(
								$(a).attr("auri"));
						//lable赋值
						$(this).parent().find(".spanNewValue").text(
								$(a).attr("aname"));
						var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
						parent.layer.close(index);
						return false;
					}
				});
	}
</script>
</html>
