<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">--%>
<%--<div class="container theme-showcase" role="main">--%>
    <%--<div class="navbar-header">--%>
        <%--<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"--%>
                <%--aria-expanded="false" aria-controls="navbar">--%>
            <%--<span class="sr-only">Toggle navigation</span>--%>
            <%--<span class="icon-bar"></span>--%>
            <%--<span class="icon-bar"></span>--%>
            <%--<span class="icon-bar"></span>--%>
        <%--</button>--%>
        <%--<a class="navbar-brand" href="#">PrintTable Library</a>--%>
    <%--</div>--%>
    <%--<div id="navbar" class="collapse navbar-collapse">--%>
        <%--<ul class="nav navbar-nav dropdown">--%>
            <%--<li class="${param.menu eq 'home' ? 'active' : ''}"><a href="${ctx}/view">Home</a></li>--%>
            <%--<li class="${param.menu eq 'model' ? 'active' : ''}"><a href="${ctx}/view/model">Model View</a></li>--%>
            <%--<li class="${param.menu eq 'class' ? 'active' : ''}"><a href="${ctx}/view/class">Class View</a></li>--%>
            <%--<li class="${param.menu eq 'list' ? 'active' : ''}"><a href="${ctx}/view/list">List View</a></li>--%>
        <%--</ul>--%>
    <%--</div>--%>
<%--</div>--%>
<%--</nav>--%>
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
				
				
					<a style="color: #c0bbb5;" href="${ctx}/userlogin/tologin">登录</a>
					<font color="#c0bbb5">|</font>
					<a style="color: #c0bbb5;" target="_blank"
						href="http://beta.library.sh.cn/SHLibrary/register.html">注册</a>
				
			</div>
		</div>
		<!-- 用户名称信息block -->
	</div>

</div>
<nav id="menu" class="navbar navbar-inverse navbar-static-top" role="navigation">
    <div class="container-fluid container" style="font-weight: bold;">
    <!--     <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>

            <a class="navbar-brand hidden-sm" href="http://www.library.sh.cn" target="_blank">上海图书馆</a>

        </div> -->
        <div class="collapse navbar-collapse" role="navigation">
            <ul class="nav navbar-nav">
                <c:forEach var="x" varStatus="status" items="${ctxRoleId.getMenuList()}">
					<li class="${param.menu eq x.menuEnglishName ? 'active' : ''}"><a
					href="${ctx}/${x.menuFunction}">${x.menuName}</a></li>
				</c:forEach>
            </ul>
           <%--  <div class="nav navbar-form navbar-right">
                <a href="${ctx}/view/shlgen.rdf" class="btn btn-default btn-sm" target="_blank">
                <i class="glyphicon glyphicon-share-alt"></i>&nbsp;RDF</a>
            </div> --%>

            <ul class="nav navbar-nav navbar-right">
                <li class="${param.menu eq 'about'?'active':''}"><a href="${ctx}/page/about" >About（关于本站）</a></li>
                <li class="${param.menu eq 'contact'?'active':''}"><a href="${ctx}/page/contact">Contact（联系我们）</a></li>
            </ul>

        </div>
    </div>
</nav>


