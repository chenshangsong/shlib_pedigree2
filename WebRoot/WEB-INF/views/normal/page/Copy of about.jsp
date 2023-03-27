<%@ page contentType="text/html;charset=UTF-8" language="java"%>



<%@include file="/WEB-INF/inc/tag.jsp"%>



<!DOCTYPE html>



<html lang="en">



<head>



<title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>

<c:import url="/WEB-INF/inc/link.jsp" />
</head>



<body>



	<c:import url="/WEB-INF/inc/header.jsp">



		<c:param name="menu" value="about" />



	</c:import>



	<div class="container">



		<div class="row">



			<div class="col col-lg-8 col-md-8 col-sm-8"
				style="padding-top: 10px;">



				<smal>
				<h3 style="padding-bottom: 15px;">上海图书馆家谱本体发布说明：</h3>
				</smal>



				<div class="panel panel-default">



					<div class="panel-body desc" style="min-height: 550px;">



						<p></p>
						<p>
							<strong>当前最新版本为V1.1，点击<a href="${ctx}/page/updatenote">此处</a>查看更新日志。
							</strong>
						</p>
						<p>
						<h3 style="padding-bottom: 12px;">上海图书馆家谱本体第一版（V1.0）&nbsp;&nbsp;&nbsp;&nbsp;发布说明：</h3>
						</p>



						<p>
							上海图书馆家谱本体是华人家谱总目——上海图书馆家谱知识服务平台Beta版系统的前期成果，是为了更深入地对上海图书馆丰富的家谱馆藏资源进行基于内容的深度加工而设计的本体模型，包括一套术语词表。本体模型基于<a
								href="http://bibframe.org" target="_blank">书目框架（BIBFRAME）</a>而设计，词表也复用了BIBFRAME和FOAF的某些术语，同时根据家谱的特殊性自定义了少量术语。复用自BIBFRAME的术语前缀为bf，如bf:Work，自定义的术语前缀为shlgen，如shlgen:Person。
						</p>



						<p>上海图书馆家谱本体此次在本网站发布的是第一版（V1.0），随着家谱数据的处理和家谱知识库系统建设的进一步深化，将来会进一步扩展。</p>



						<p>
							本网站提供三种视图模式供用户浏览：<a href="${ctx}/view/model">
								&nbsp;&nbsp;模型视图（Model View）</a>、<a href="${ctx}/view/class">
								&nbsp;&nbsp;类视图（Class View）</a>和<a href="${ctx}/view/list">
								&nbsp;&nbsp;列表视图（List View）</a>。
						</p>



						<p>模型视图（Model View）：可视化地展示了家谱本体类和属性间的关系。</p>



						<p>类视图（Class View）：通过父类和子类的层级关系浏览类和属性。</p>



						<p>列表视图（List View）：按照类和属性名的首字母顺序排列展示类和属性。</p>

						<p>本网站对上海图书馆家谱本体的发布采用了关联数据技术，遵循关联数据的四原则。</p>
						<p>术语均赋予了URI，如http://gen.library.sh.cn/vocab/Person。</p>
						<p>
							实现内容协商，如用普通的浏览器访问http://gen.library.sh.cn/vocab/Person，系统将返回Html页面，当用语义浏览器或语义代理（程序）访问http://gen.library.sh.cn/vocab/Person，系统返回关于Person的RDF/XML数据。
							用W3C的<a href="http://www.w3.org/RDF/Validator/" target="_blank">RDF
								Validator</a>可以体验。
						</p>
						<p>
							<a href="http://gen.library.sh.cn/view/shlgen.rdf"
								target="_blank">这里</a>可以获取上海图书馆家谱本体的全部RDF数据。
						</p>

						<p>本网站采用RDF Store存储数据、Apache
							Jena作为语义Web开发框架、利用RDF可视化技术来实现关联数据技术架构。</p>

						<p>网站设计：上海图书馆，夏翠娟（cjxia@libnet.sh.cn）。</p>
						<p>技术支持：上海生命科学信息中心，陈涛（chentao01@sibs.ac.cn）。</p>

					</div>



				</div>



			</div>



			<div class="col col-lg-4 col-md-4 col-sm-4">



				<div class="panel panel-info" style="margin-top: 20px;">



					<div class="panel-heading">



						<div class="panel-title">



							<span class="glyphicon glyphicon-link"></span>&nbsp; <strong>Model
								& Vocabulary(模型与词表)</strong>



						</div>



					</div>



					<div class="list-group">



						<a href="${ctx}/view/model" class="list-group-item"
							style="font-size: 16px;"><i
							class="pull-right glyphicon glyphicon-chevron-right"></i>&nbsp;&nbsp;模型视图（Model
							View）</a> <a href="${ctx}/view/class" class="list-group-item"
							style="font-size: 16px;"><i
							class="pull-right glyphicon glyphicon-chevron-right"></i>&nbsp;&nbsp;类视图（Class
							View）</a> <a href="${ctx}/view/list" class="list-group-item"
							style="font-size: 16px;"><i
							class="pull-right glyphicon glyphicon-chevron-right"></i>&nbsp;&nbsp;列表视图（List
							View）</a>



					</div>



				</div>



				<div class="panel panel-warning">



					<div class="panel-heading">



						<div class="panel-title">



							<span class="glyphicon glyphicon-bookmark"></span>&nbsp; <strong>Publications
								& Presentations(资料)</strong>



						</div>



					</div>



					<ul class="list-group2">



						<li><a href="${ctx}/doc/BIBFRAMEbyLiu.pdf" target="_blank">刘炜，夏翠娟：《书目数据新格式BIBFRAME及其应用》大学图书馆学报，2014,1</a></li>

						<li><a href="${ctx}/doc/GenOntBasedONBIBFRAME.pdf"
							target="_blank">夏翠娟：以书目框架建模的上图家谱知识库系统</a></li>

						<li><a href="${ctx}/doc/Paper_GenOntBasedONBIBFRAME.pdf"
							target="_blank">夏翠娟，刘炜，张磊，朱雯晶：《基于书目框架（BIBFRAME）的家谱本体设计》图书馆论坛，2014，11</a></li>





					</ul>



				</div>
				<div class="panel panel-warning">



					<div class="panel-heading">



						<div class="panel-title">



							<span class="glyphicon glyphicon-bookmark"></span>&nbsp; <strong>Feedbacks(业界反馈)</strong>



						</div>



					</div>



					<ul class="list-group2">



						<li><a href="http://catwizard.net/posts/20141122223122.html"
							target="_blank">编目精灵：基于BIBFRAME的上海图书馆家谱本体发布</a></li>






					</ul>



				</div>



			</div>



		</div>



	</div>



	<c:import url="/WEB-INF/inc/footer.jsp" />
</body>



</html>