<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
<c:import url="/WEB-INF/inc/editMapLink.jsp" />
</head>
<body>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<nav class="navbar navbar-default navbar-fixed-top"
					role="navigation">
					<div class="navbar-innerBlack">
						<div class="container-fluid">
							<a class="btn btn-navbar" data-toggle="collapse"
								data-target=".navbar-responsive-collapse"><span
								class="icon-bar"></span><span class="icon-bar"></span><span
								class="icon-bar"></span></a>
							<div class="nav-collapse collapse navbar-responsive-collapse">
								<ul class="nav">
								</ul>
								<ul class="nav pull-right">
									<li><a href="${ctx}/home/index">首页</a></li>
									<li class="active"><a href="${ctx}/map/place/list">时空</a></li>
									<c:if test="${ctxRoleId!=null}">
										<li><a
											href="${ctx}/userInfo/personInfo">${ctxRoleId.getUserName()}</a></li>
										<li><a href="javascript:;" onclick="tologin()">退出</a></li>
									</c:if>
									<c:if test="${ctxRoleId==null}">
										<li><a href="javascript:;" onclick="login()">登录</a></li>
										<li><a target="_blank"
											href="http://beta.library.sh.cn/SHLibrary/register.html">注册</a></li>
									</c:if>
								</ul>
							</div>
						</div>
					</div>
				</nav>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span8">
				<h3></h3>
				<div class="tab-content" id="main">
					<div role="tabpanel" class="tab-pane fade in active" id="tab_0">
						<div id="placeDiv"></div>
						<div tag="view" style="display: none;"></div>
					</div>
				</div>
				<div>
					<input id="btn-All" style='display: none;' class="btn btn-default"
						type="button" value="查看更多">
				</div>
			</div>
			<script type="text/html" id="place_tpl">
    			<div class="panel panel-info">
            		<div class="row-fluid">
                		<div class="span12">
                    		<div style="position:relative;width:100%;height:480px;" tag="workMap"><div id="popup"></div></div>
                		</div>
            		</div>
    			</div>
			</script>

			<div class="span4">
				<h3></h3>
				<br /> <br /> <br />
				<div class="span12">
					<section id="main">
						<div align="center">
							<input id="degree" maxlength="5" value="0"></input> <input
								type="hidden" name="hidDeg" id="hidDeg" value="-1"> <input
								type="hidden" name="lastDeg" id="lastDeg" value="0">
							</td>
						</div>
						<div style="min-height: 100px !important">
							<h1 id="dynasty"></h1>
						</div>
						<div id="bars">
							<div id="control"></div>
						</div>
					</section>
				</div>
			</div>
		</div>
	</div>
	<!-- showloding -->
	<div class="modal fade" id="modal-loading">
		<div class="modal-dialog modal-loading">
			<div class="modal-content" style="margin-top: -50px;">
				<div class="modal-body text-center">
					<img src="${ctx}/res/images/loading.gif" /> &nbsp;&nbsp;<span
						class="modal-info">Loading...</span>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
<script>
function login(){
	var durl=document.location.href;
	 $.post('${ctx}'+"/userlogin/localpage",{durl: durl},function(data){
		data= jQuery.parseJSON(data);
	
		 if(data.result=='1'){
			 window.location.href=data.loginurl;
		 } 
		  });
	}
function tologin(){
	 $.get('${ctx}'+"/userlogin/tologin",function(data){
		 if(data==true){
			 window.location.href="${ctx}/home/index";
		 } 
		  });
	}

</script>