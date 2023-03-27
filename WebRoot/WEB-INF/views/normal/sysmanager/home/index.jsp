<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
<c:import url="/WEB-INF/inc/editHomeLink.jsp" />
	<script src="${ctx}/res/js/sysmanager/home/home.js"></script>
	<script src="${ctx}/res/commJS/common.js"></script>
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
									<li class="active"><a href="${ctx}/home/index">首页</a></li>
									<li><a href="${ctx}/map/place/list">时空</a></li>
									<c:if test="${ctxRoleId!=null}">
										<li><a href="${ctx}/userInfo/personInfo">${ctxRoleId.getUserName()}</a></li>
										<li><a href="javascript:;" onclick="tologin()">退出</a></li>
									</c:if>
									<c:if test="${ctxRoleId==null}">
										<li><a href="javascript:;" onclick="login()">登录</a></li>
										<li><a target="_blank" href="http://beta.library.sh.cn/SHLibrary/register.html">注册</a></li>
									</c:if>
								</ul>
							</div>
						</div>
					</div>
				</nav>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span1">
				<h3></h3>
				<ul class="initials">
		    	</ul>   
			</div>
			<div class="span5">
				<h3></h3>
				<input type="hidden" name="firstChar" id="firstChar" />
				<div id="surname">
				<c:set var="initial" value="" />
				<c:set var="lineBreak" value="0" />
				<c:forEach var="x" varStatus="status" items="${data.surname}">
					<c:if test="${(lineBreak != 0 && lineBreak % 4 == 0) ||
					(x.initial != initial && initial != '')}" >
						</div><br/>
					</c:if>
					<c:if test="${x.initial != initial}">
						<c:set var="initial" value="${x.initial}" />
						<c:set var="lineBreak" value="0" />
						<p id="${x.initial}">${x.initial}</p>
						<div class="both"></div>
					</c:if>
					<c:if test="${lineBreak % 4 == 0}">
						<div class="row-fluid">
					</c:if>
					<div class="span3">
						<table cellpadding="1" cellspacing="1" style="color:${x.randColor};">
							<tr>
								<td colspan="2" align="center" valign="top" height="55">
									<div class="rotate" style="width:30px; font-size: 24px;line-height: 24px;">${x.originally}</div>
								</td>
								<td rowspan="3" align="center" valign="top" class="leftLine">
									<div style="display: block; width: 14px; float: left;">
										<c:if test="${(fn:indexOf(x.pinyin, '，')) > 0}">
											<div class="rotate">${fn:substring(x.pinyin, 0, fn:indexOf(x.pinyin, '，'))}</div>
										</c:if>
										<c:if test="${(fn:indexOf(x.pinyin, '，')) < 0}">
											<div class="rotate">${x.pinyin}</div>
										</c:if>	
									</div>
								</td>
								<td rowspan="2" align="center" valign="top" class="familyName"
									style="font-size: x-large; width: 14px">${x.familyNameS}</td>
							</tr>
							<tr style="font-size:small;line-height:14px;">
								<td valign="middle" width="14">家谱文献</td>
								<td valign="middle" width="14">先祖名人</td>
							</tr>
							<tr style="font-size:small">
								<td>
									<div style="display: block; width: 14px; float: left;">
										<div class="rotate">${x.genealogyCnt}</div>
									</div>
								</td>
								<td>
									<div style="display: block; width: 14px; float: left;">
										<div class="rotate">${x.celebrityCnt}</div>
									</div>
								</td>
							</tr>
						</table>
					</div>
					<c:set var="lineBreak" value="${lineBreak + 1}"/>
					</c:forEach>
				 	</div>
				 </div>
			</div>
			<div class="span6">
				<h3></h3>
				<div id="myCarousel" class="carousel slide">
					<ol class="carousel-indicators">
				      <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
				      <li data-target="#myCarousel" data-slide-to="1"></li>
				      <li data-target="#myCarousel" data-slide-to="2"></li>
				   </ol>
				   <div class="carousel-inner">
				      <div class="item active">
		      		  	<div class="span12">
							<div class="row-fluid">
								<div class="span3">
									<div class="row-fluid">
										<div class="span12" style="width: 20px;">先祖名人</div>
									</div>
									<div class="row-fluid">
										<div class="span12 celebrities">
											<ul id="persons">
												<c:forEach var="x" varStatus="status" items="${data.person}">
												<li><a href="javaScript:;" relatedWork="${x.relatedWork}" personUrl="${x.uri}">${x.name}</a></li>
												</c:forEach>
											</ul>
										</div>
									</div>
									<div class="row-fluid">
										<div class="span12" style="height: 80px;"></div>
									</div>
									<div class="row-fluid">
										<div class="span12" style="width: 20px;">家谱</div>
									</div>
									<div class="row-fluid">
										<div class="span12">
											<ul id="works">
											<c:forEach var="x" varStatus="status" items="${data.work}">
											<li><a href="javaScript:;" uri='${x.uri}' puri='${x.puri}'>${x.dtitle}</a></li>		
											</c:forEach>
											</ul>
											<input type="hidden" id="hidWorks" value="${data.work}" />
										</div>
									</div>
								</div>
								<div class="span9">
									<div class="row-fluid">
										<div class="span3" style="text-align:center">
											<p>
												<strong><font style="font-size: xx-large; line-height: 30px;">
													<a target="_blank" id="familyNameA" href="${ctx}/service/search/adv?familyName=${data.currentSurname.familyNameS}">${data.currentSurname.familyNameS}</a>				
												</font>
												</strong>
											</p>
											<p>
												<strong><font id="pinyin">${data.currentSurname.pinyin}</font></strong>
											</p>
										</div>
										<div class="span1"></div>
										<div class="span8">
											<p id="description" style="height: 180px; font-size: small;line-height: 18px;">${data.currentSurname.description}</p>
										</div>
									</div>
									<div class="row-fluid leftLine introduced" style="padding: 5px;">
										<div class="span12" style="font-size: 20px;">
											<p>
											<span id="personName">${data.currentPerson.name}</span><img src="${ctx}/res/sysmanager/images/1.png" />
											<span id="roles">
											<c:forEach var="x" items="${data.currentWork.familyRelations}">
												<c:if test="${x.uri == data.currentPerson.uri}">
													${x.roles}			
												</c:if>
											</c:forEach>
											</span>
											</p>
										</div>
										<div class="span12" style="font-size: 18px;">
											<img src="${ctx}/res/sysmanager/images/3.png" />
											<a id="title" href="${ctx}/service/work/list#uri=${data.currentWork.uri}" target="_blank">${data.currentWork.title}</a>
											<c:if test="${data.currentWork.places!=null}">
											<img src="${ctx}/res/sysmanager/images/2.png" /><a id="label" href="${ctx}/service/place/list#place=${data.currentWork.places[0].place}" target="_blank">${data.currentWork.places[0].label}</a>
											</c:if>
											<c:if test="${data.currentWork.places==null}">
											<img src="${ctx}/res/sysmanager/images/2.png" /><a id="label" href="" target="_blank"></a>
											</c:if>
										</div>
										<div class="span12" style="font-size: small">
											<ul>
												<c:if test="${data.currentWork.creator!=''}"><li id='licreator'>责任者：<span id="creator">${data.currentWork.creator}</span></li></c:if>
												<li>摘要：<span id="note">${data.currentWork.note}</span></li>
												<c:if test="${data.currentWork.tangh!=''}"><li id='litangh'>堂号：<span id="tangh">${data.currentWork.tangh}</span></li></c:if>
												<c:if test="${data.currentWork.instances!=null}">
													<li>撰修时间：<span id="temporal">${data.currentWork.instances[0].temporal}</span></li>
													<li>版本：<span id="edition">${data.currentWork.instances[0].edition}</span></li>
													<li>数量：<span id="extent">${data.currentWork.instances[0].extent}</span></li>
												</c:if>
												<c:if test="${data.currentWork.instances==null}">
													<li>撰修时间：<span id="temporal"></span></li>
													<li>版本：<span id="edition"></span></li>
													<li>数量：<span id="extent"></span></li>
												</c:if>
												<c:if test="${data.currentWork.uri==null}">
													<li><a id="heldBy" href="javaScript:;" target="_blank">馆藏信息</a></li>
												</c:if>
												<c:if test="${data.currentWork.uri!=null}">
													<li><a id="heldBy" href="${ctx}/service/work/list#uri=${data.currentWork.uri}" target="_blank">馆藏信息</a></li>
												</c:if>
											</ul>
										</div>
									</div>
								</div>
							</div>
						</div>
				      </div>
				      <div class="item">
				      	<div class="row-fluid">
				      		<div class="span12">
					      		<p style="text-align:center;">
					      		<img src="${ctx}/res/sysmanager/images/jiapu.png" class="img-responsive" />
					      		</p>
							</div>
				      	</div>
				      	<div class="row-fluid">
				      		<div class="span12">
		                        <p style="text-align:center;margin-top:50px;">
		                        <input id="textKeyWord" style="width:250px; height:30px;" class="input-medium search-query" type="text" placeholder="标题/姓氏/责任者/谱籍/堂号"/>
		                        <a href="javaScript:;" id="btnQuickSearch"><img src="${ctx}/res/sysmanager/images/search.png" /></a>  
		                         <a target="_blank" href="${ctx}/service" >高级检索</a>                      
		                        </p>
	                    	</div>
				      	</div>
				      	<div class="row-fluid">
				      		<div class="span12">
		                    	<div style="margin: 0 auto;width: 310px;text-align: center;color:#7B7B7B;">
			                         <dl>
			                         	<dd>
			                         		<h4 style="color:#999"><strong>踏上寻根问祖的文化之旅</strong></h4>
			                         	</dd>
							            <dd>
								            <strong>家谱，又称谱牒、族谱、宗谱、家乘、世谱等，是同宗共祖的血亲团体记载本族世系和相关事迹、反映本家族繁衍发展过程的历史图籍。它与正史、方志、构成了中华民族历史大厦的三大支柱，在中国乃至世界文明的文明发展历史上，堪称弥足珍贵的文化遗产。
								            </strong>
							            </dd>
						            </dl>
			                    </div>
		                    </div>
				      	</div>
				      </div>
				      <div class="item">
						<div class="row-fluid">
							<div class="span12">
								<p><h3 style="background-color: #7B7B7B; margin-top: 130px;">&nbsp;> 高级查询</h3></p>
							</div>
				      	</div>
				      	<div class="row-fluid">
				      		<table class="seniorSearch">
				                <tr>				                    
				                    <td>
				                    <input type="text" value="" class="form-control" name="title" id="title" placeholder="在此输入标题">
				                    <input type="text" value="" class="form-control" name="familyName" id="familyName" placeholder="在此输入姓氏"></td>
				                    <td>
				                </tr>
				                <tr>
				                	<td>
				                	<input type="text" value="" class="form-control" name="creator" id="creator" placeholder="在此输入家谱责任者">
				                	<input type="text" value="" class="form-control" name="place" id="place" placeholder="在此输入谱籍地名"></td>
				                    <td>
				                </tr>
				               <tr>
				                	<td>
				                	<input type="text" value="" class="form-control" name="tang" id="tang" placeholder="在此输入堂号">
				                    <input type="text" value="" class="form-control" name="person" id="person" placeholder="在此输入先祖/名人">
				                    </td>
				                </tr>
				                <tr>
				                    <td>
				                    <input type="text" value="" class="form-control" name="note" id="note" placeholder="在此输入摘要">
				                    <input type="text" value="" class="form-control" name="organization" id="organization" placeholder="在此输入馆藏地">
				                    </td>
				                </tr>
				            </table>
				      	</div>
				      	<div class="row-fluid">
				      		<div class="span12">
								<a href="javaScript:;" id="btn-adv"><img src="${ctx}/res/sysmanager/images/search.png" /></a>
							</div>
				      	</div>
				      </div>
				   </div> 
				</div>
			</div>
		</div>
	</div>
	<script>
	var getScroll = true;
	(function($){
		function parseUrl(){
	        var url=location.href;
	        var i=url.indexOf('?');
	        if(i==-1)return;
	        var querystr=url.substr(i+1);
	        var arr1=querystr.split('&');
	        var arr2=new Object();
	        for  (i in arr1){
	            var ta=arr1[i].split('=');
	            arr2[ta[0]]=ta[1];
	        }
	        return arr2;
		}
		var v = parseUrl();//解析所有参数
		var code;
		function successCallback(data){
			requestJson("${ctx}/userlogin/dologin", null, success);
		}
		if (v != undefined && v['code'] != undefined){
			requestJson("${ctx}/userlogin/loginer?code="+v['code'], null, success);
		}
		function success(data){
			var durl = data.result;
			window.location.href = durl;
		}
		
		$(window).load(function(){
			$("#surname").mCustomScrollbar({
				theme:"rounded",
				scrollButtons:{
					enable:true
				},
				callbacks:{
					whileScrolling:function(){
						if (getScroll && this.mcs.topPct > 85) {
							getScroll = false;
							onScroll();
						}
					}
				}
			});
			$("#myCarousel").carousel('cycle');
			var des = $("#description").html();
			var des2 = $("#description").html();
			if (des.length > 170) {
				des = des.substr(0, 170) + "...";
			}
			$("#description").html(des);
			$('.layui-layer-tips').css('display', 'none');
			$("#description").mouseover(function() {
				$('.layui-layer-tips').css('display', 'none');
				if (des2.length > 170) {
					layer.tips('...' + des2.substr(168), this, {
						tips : [ 2, '#777' ],
						time : 10000
					});
				}
			});
			$("#description").mouseout(function() {
				$('.layui-layer-tips').css('display', 'none');
			});
		});
	})(jQuery);
	
	//登录
	function login(){
		var durl=document.location.href;
		$.post('${ctx}'+"/userlogin/localpage",{durl: durl},function(data){
			data = jQuery.parseJSON(data);
		
			if(data.result=='1'){
				window.location.href = data.loginurl;
			}
		});
	}
	//退出
	function tologin(){
		$.get('${ctx}'+"/userlogin/tologin",function(data){
			if(data==true){
				window.location.href = "${ctx}/home/index";
			}
		});
	}
	</script>
	
</body>
</html>