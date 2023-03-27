<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script>
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "https://hm.baidu.com/hm.js?1cf0b5d48607ee67feb689c8bd13976e";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();
</script>
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
		<!-- 用户名称信息block -->
		<div style='float: right; margin-right: -30px;'>
			<div style="float: right; margin-right: 0px;">
				<c:if test="${ctxRoleId!=null}">
					<a style="color: #c0bbb5;"
						href="${ctx}/userInfo/personInfo">
						${ctxRoleId.getUserName()} </a>
					<c:if test="${ctxRoleId.getRoleId()!='1' }">
						<font color="#c0bbb5">（${ctxRoleId.getRoleName()}）</font>
					</c:if>
					<font color="#c0bbb5">|</font>
					<a style="color: #c0bbb5;" href="javascript:;" onclick="tologin()">退出</a>
				</c:if>
				<c:if test="${ctxRoleId==null}">
					<a style="color: #c0bbb5;" onclick="login()" >登录</a>
					<font color="#c0bbb5">|</font>
					<a style="color: #c0bbb5;" target="_blank"
						href="http://beta.library.sh.cn/SHLibrary/register.html">注册</a>
				</c:if>
			</div>
		</div>
		<!-- 用户名称信息block -->
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
					class="navMoreColor" enName="index" href="${ctx}/home/index"><i
						class="glyphicon glyphicon-home"></i>&nbsp;首页</a></li>
				<li class="${param.menu eq 'work'?'active':''}"><a
					class="navMoreColor" enName="work" href="${ctx}/service/search"><i
						class="glyphicon glyphicon-book"></i>&nbsp;文献</a></li>
				<li class="${param.menu eq 'familyName'?'active':''}"><a
					class="navMoreColor" enName="familyName"
					href="${ctx}/service/person/familyName"><i
						class="glyphicon glyphicon-tasks"></i>&nbsp;姓氏</a></li>
				<li class="${param.menu eq 'person'?'active':''}"><a
					class="navMoreColor" enName="person"
					href="${ctx}/service/person/list"><i
						class="glyphicon glyphicon-user"></i>&nbsp;人物</a></li>
				<li class="${param.menu eq 'place'?'active':''}"><a
					class="navMoreColor" enName="place"
					href="${ctx}/service/place/list"><i
						class="glyphicon glyphicon-map-marker"></i>&nbsp;地点</a></li>
				<c:if test="${ctxRoleId!=null}">
					<li class="${param.menu eq 'personInfo'?'active':''}">
						<a class="navMoreColor" enName="personInfo" href="${ctx}/userInfo/personInfo">
							<i class="glyphicon glyphicon-heart"></i>&nbsp;个人中心
						</a>
					</li>
				</c:if>
				<c:forEach var="x" varStatus="status" items="${ctxRoleId.getMenuList()}">
					<li class="${param.menu eq x.menuEnglishName ? 'active' : ''}">
						<a class="navMoreColor" enName="${x.menuEnglishName}" href="${ctx}/${x.menuFunction}">
							<i class="${x.icon}"></i>&nbsp;${x.menuName}
						</a>
					</li>
				</c:forEach>
			</ul>
			<ul class="nav navbar-nav" style="float:right;">
				<li ><a id="btnNavSearch" style="color: #fff" href="javaScript:void(0)"><i
						class="glyphicon glyphicon-search"></i>&nbsp;搜索</a></li>
			</ul>
		</div>
	</div>
</nav>
	<div id="search_bar"
		style="width: 100%;z-index:1000;position:absolute;background-color: #555; display:none; margin: -20px auto 0; padding: 0px 0 0; ">
		<table style="width: 100%;">
			<tr>
				<td align="center">
					<form id="_searchForm" class="navbar-form navbar-left"
						role="search" action="${ctx}/service/search" style="width: 100%">
						<div class="input-group" style="width: 90%;">
							<input type="text" name="keyword" style="" class="form-control"
								placeholder="在此输入 标题/姓氏/责任者/谱籍/堂号"> <span
								class="input-group-btn" style="width: 40px;">
								<button class="btn btn-default" type="submit">
									<i class="glyphicon glyphicon-search"></i>
								</button>
							</span>
						</div>
						&nbsp;&nbsp;<a style="font-size: 14px; color: #fff"
							href="${ctx}/service" style="font-size: 15px;">高级查询</a>
							&nbsp;&nbsp;<a id="btnNavC" style="font-size: 14px; color: #fff"
							href="javaScript:void(0)" style="font-size: 15px;"><i class="glyphicon glyphicon-remove"></i></a>
					</form>
				</td>
			</tr>
		</table>
	</div>
<script>
//百度统计
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "//hm.baidu.com/hm.js?15d5d47e8ab2614ab39d595e11c2f897";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();

	$(function() {
		//导航颜色
		$(".navMoreColor").each(function(i) {
			var enName = $(this).attr("enName");
			var color = $.colorGroup.getColor(enName);
			$(this).css("color", color);
		});
		//列表头部颜色
		var titleEnName = '${param.menu}';
		var titleColor = $.colorGroup.getColor(titleEnName);
		if (titleColor != "white") {
			$("#top").css("color", titleColor);
		}
		//显示高级收索
		$("#btnNavSearch").click(function(){
			$("#search_bar").fadeIn(300);
		});
		//隐藏高级搜索
		$("#btnNavC").click(function(){
			$("#search_bar").fadeOut(300);
		});
		
	})
	
	//登录
	function login(){
		var durl=document.location.href;
		$.post('${ctx}'+"/userlogin/localpage", {durl: durl}, function(data){
			data = jQuery.parseJSON(data);
			if(data.result == '1'){
				window.location.href = data.loginurl;
			} 
		});
	}
	
	//退出
	function tologin(){
		$.get('${ctx}'+"/userlogin/tologin", function(data){
			if(data == true){
				window.location.href = "${ctx}/home/index";
			} 
		});
	}
</script>
