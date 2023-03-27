<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="page-header">
	<div class="container">
		<div style='float: left;'>
			<table class="header">
				<tr>
					<td><a href="${ctx}"><img class="logo"
							src="${ctx}/res/logo.png" /></a></td>

					<!-- 	<td style="padding: 0 30px 0;" valign="bottom">

						<h1 style="margin: 0px;">
							<small><font color="#c0bbb5">知识服务平台</font></small>
						</h1>
						<h2 style="margin: 0px;">
							<small><font color="#c0bbb5"> Genealogy Knowledge
									Service Platform</font></small>
						</h2>
					</td> -->
				</tr>
			</table>
		</div>
			<div style='float: right; margin-right: -30px;'>
			<div style="float: right; margin-right: 0px;">
			 <c:if test="${ctxRoleId!=null}">
			<font color="#c0bbb5">${ctxRoleId.getUserName()}
			 <c:if test="${ctxRoleId.getRoleId()!='1' }">
			（${ctxRoleId.getRoleName()}）</font>
				</c:if>
				<font color="#c0bbb5">|</font> <a style="color: #c0bbb5;"
					href="javascript:;" onclick="tologin()">退出</a>
					</c:if>
			</div>
		</div>
	</div>

</div>
<nav id="menu" class="navbar navbar-inverse navbar-static-top"
	role="navigation">
	<div class="container-fluid container" style="font-weight: bold;">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target=".navbar-collapse">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>

			<a class="navbar-brand hidden-sm hidden-md hidden-lg"
				href="${ctx}" target="_blank">上海图书馆</a>

		</div>
		<div class="collapse navbar-collapse" role="navigation">
			<ul class="nav navbar-nav">
				<c:forEach var="x" varStatus="status"
					items="${ctxRoleId.getMenuList()}">
					<li class="${param.menu eq x.menuEnglishName ? 'active' : ''}"><a class="navMoreColor" enName="${x.menuEnglishName}"
						href="${ctx}/${x.menuFunction}">${x.menuName}</a></li>
				</c:forEach>
			</ul>
		</div>
	</div>
</nav>
<div id="float_bar" style="width: 100%; text-align: center;">
	<div id="search_bar"
		style="background-color: #444; margin: -40px auto 0; padding: 10px 0 0; border-radius: 0 0 200px 200px; -webkit-border-radius: 0 0 200px 200px; box-shadow: 0px 2px 2px #DDD; width: 50%;">
		<table style="width: 100%;">
			<tr>
				<td align="center">
					<form id="_searchForm" class="navbar-form navbar-left"
						role="search" action="${ctx}/service/search" style="width: 100%">
						 <div class="input-group" style="width: 60%;">
							<input type="text" name="keyword" style="" class="form-control"
								placeholder="标题/姓氏/责任者/谱籍/堂号"> <span
								class="input-group-btn" style="width: 40px;">
								<button class="btn btn-default" type="submit">
									<i class="glyphicon glyphicon-search"></i>
								</button>
							</span>
						</div> 
						<a href="${ctx}/service/search/adv"
							style="font-size: 15px; color: white">高级查询</a>
					</form>
				</td>
			</tr>
		</table>
	</div>
</div>
<script>
$(function(){
	$(".navMoreColor").each(function(i){
		var enName = $(this).attr("enName");
		var color = $.colorGroup.getColor(enName);
		$(this).css("color",color);
	});
	//列表头部颜色
	var titleEnName = '${param.menu}';
	var titleColor = $.colorGroup.getColor(titleEnName);
	if(titleColor!="white"){
		$("#top").css("color",titleColor);
	}
})

//退出
function tologin(){
	$.get('${ctx}'+"/userlogin/tologin",function(data){
		if(data == true){
			window.location.href="${ctx}/home/index";
		}
	});
}
</script>