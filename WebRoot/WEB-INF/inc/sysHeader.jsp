<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="page-header">
	<div class="container">
		<table class="header">
			<tr>

				<td><a href="http://www.library.sh.cn"><img class="logo"
						src="${ctx}/res/logo.png" /></a></td>

				<td style="padding: 0 30px 0;" valign="bottom">

					<h1 style="margin: 0px;">
						<small>上海图书馆&nbsp;&nbsp;&nbsp;&nbsp;Shanghai Library</small>
					</h1>

					<h2 style="margin: 0px;">
						<small>家谱知识库系统&nbsp;&nbsp;&nbsp;&nbsp; Genealogy Knowledge
							Database</small>
					</h2>

				</td>
			</tr>
		</table>
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
				href="http://www.library.sh.cn" target="_blank">上海图书馆</a>

		</div>
		<div class="collapse navbar-collapse" role="navigation">
			<ul class="nav navbar-nav">
				<li class="${param.menu eq 'index'?'active':''}"><a
					href="${ctx}/service">首页</a></li>
				<li class="${param.menu eq 'work'?'active':''}"><a
					href="${ctx}/service/work/list">家谱文献</a></li>
				<li class="${param.menu eq 'familyName'?'active':''}"><a
					href="${ctx}/service/person/familyName">家谱姓氏表</a></li>
				<li class="${param.menu eq 'person'?'active':''}"><a
					href="${ctx}/service/person/list">先祖名人录</a></li>
				<li class="${param.menu eq 'place'?'active':''}"><a
					href="${ctx}/service/place/list">谱籍分布图</a></li>
				<li class="${param.menu eq 'dynasty'?'active':''}"><a
					href="${ctx}/service/dynasty/list">朝代分布表</a></li>
				<c:if test="${ctxRoleId.getRoleId()=='2' || ctxRoleId.getRoleId()=='3'}">
					<li><a href="http://gen.library.sh.cn/view">本体</a></li>
						<li class="${param.menu eq 'dataInfo'?'active':''}"><a
						href="${ctx}/dataManager/datalist">数据维护</a></li>
					<li class="${param.menu eq 'userInfo'?'active':''}">
					<c:if test="${ctxRoleId.getRoleId()=='3'}">
					<a
						href="${ctx}/userInfo/userList">用户管理</a></li></c:if>
				</c:if>
				<li class="${param.menu eq 'feedBack'?'active':''}"><a href="${ctx}/feedBack/feedBackMainList">用户反馈</a></li>
               
				<li ><a 
					href="${ctx}/index.jsp">退出登录</a></li>
			</ul>
		</div>
	</div>
</nav>
<Script>
$(function(){
$("#linkLoginOut").click(function(){
	
	
});
});
</Script>
