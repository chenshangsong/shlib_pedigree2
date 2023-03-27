<%@ page contentType="text/html;charset=UTF-8" language="java" %>



<%@include file="/WEB-INF/inc/tag.jsp"%>



<!DOCTYPE html>



<html lang="en">



<head>



    <title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>



    <c:import url="/WEB-INF/inc/link.jsp" />


</head>



<body>



<c:import url="/WEB-INF/inc/header.jsp" >



    <c:param name="menu" value="about"/>



</c:import>



<div class="container">



    <div class="row">



        <div class="col col-lg-8 col-md-8 col-sm-8" style="padding-top:10px;">



            <smal><h3 style="padding-bottom: 12px;">



                上海图书馆家谱本体V1.1版更新日志：</h3></smal>



            <div class="panel panel-default">



                <div class="panel-body desc" style="min-height: 550px;">



                    <p></p>
<p>更新日期：2014年12月2日。</p>
<p>更新内容如下：</p>

<p>1. bf:Creator的rdfs:domain 从shlgen:Person修改为bf:Authority，保持与BIBFRAME词表一致。</p>
 
<p>2. foaf:familyName rdfs:domain从shlgen:Person改为foaf:Person,rdfs:range 从shlgen:FamilyName修改为rdfs:Literal，保持与FOAF词表一致。仍在家谱本体中保留此属性有利于与外部系统的互操作。</p>

<p>3. 扩展自有属性 shlgen:familyName 其rdfs:domain为 shlgen:Person和shlgen:Family，其rdfs:range为 shlgen:FamilyName。</p>

<p>4. 为shlgen:Person增加定义shlgen:Person subClassOf foaf:Person，以便于继承性地复用foaf:Person的属性foaf:familyName。</p>

<p>5. 扩展shlgen:Place以处理地理信息，使其继承bf:Place和geonames（http://www.geonames.org）的gn:Feature，复用gn:Feature的属性gn:parentCountry,gn:parentADM1,gn:parentADM2,gn:parentADM3等。</p>

<p>6. 复用W3C的Time Ontology （http://www.w3.org/TR/owl-time/）的 ProperInterval 类，复用其属性 intervalStarts， intervalDuring， intervalFinishes， intervalAfter， intervalBefore， intervalMetBy等，用于描述朝代、年代等时间信息。</p>


                    
                </div>



            </div>



        </div>



        <div class="col col-lg-4 col-md-4 col-sm-4">



            <div class="panel panel-info" style="margin-top:20px;">



                <div class="panel-heading">



                    <div class="panel-title">



                        <span class="glyphicon glyphicon-link"></span>&nbsp;



                        <strong>Model & Vocabulary(模型与词表)</strong>



                    </div>



                </div>



                <div class="list-group">



                    <a href="${ctx}/view/model" class="list-group-item" style="font-size: 16px;"><i class="pull-right glyphicon glyphicon-chevron-right"></i>&nbsp;&nbsp;模型视图（Model View）</a>



                    <a href="${ctx}/view/class" class="list-group-item" style="font-size: 16px;"><i class="pull-right glyphicon glyphicon-chevron-right"></i>&nbsp;&nbsp;类视图（Class View）</a>



                    <a href="${ctx}/view/list" class="list-group-item" style="font-size: 16px;"><i class="pull-right glyphicon glyphicon-chevron-right"></i>&nbsp;&nbsp;列表视图（List View）</a>



                </div>



            </div>



            <div class="panel panel-warning">



                <div class="panel-heading">



                    <div class="panel-title">



                        <span class="glyphicon glyphicon-bookmark"></span>&nbsp;



                        <strong>Publications & Presentations(资料)</strong>



                    </div>



                </div>



                <ul class="list-group2">



                   <li><a href="${ctx}/doc/BIBFRAMEbyLiu.pdf" target="_blank">刘炜，夏翠娟：《书目数据新格式BIBFRAME及其应用》大学图书馆学报，2014,1</a></li>

                   <li><a href="${ctx}/doc/GenOntBasedONBIBFRAME.pdf" target="_blank">夏翠娟：以书目框架建模的上图家谱知识库系统</a></li>

                   <li><a href="${ctx}/doc/Paper_GenOntBasedONBIBFRAME.pdf" target="_blank">夏翠娟，刘炜，张磊，朱雯晶：《基于书目框架（BIBFRAME）的家谱本体设计》图书馆论坛，2014，11</a></li>





                </ul>



            </div>
            <div class="panel panel-warning">



                <div class="panel-heading">



                    <div class="panel-title">



                        <span class="glyphicon glyphicon-bookmark"></span>&nbsp;



                        <strong>Feedbacks(业界反馈)</strong>



                    </div>



                </div>



                <ul class="list-group2">



                   <li><a href="http://catwizard.net/posts/20141122223122.html" target="_blank">编目精灵：基于BIBFRAME的上海图书馆家谱本体发布</a></li>

                  




                </ul>



            </div>



        </div>



    </div>



</div>



<c:import url="/WEB-INF/inc/footer.jsp" />
</body>



</html>