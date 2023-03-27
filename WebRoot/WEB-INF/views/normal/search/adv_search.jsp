<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
    <c:import url="/WEB-INF/inc/link.jsp" />
    <style>
        td.left10 {
            padding-left: 10px;
        }

        td.left30 {
            padding-left: 30px;
        }
    </style>
</head>
<body>
<c:import url="/WEB-INF/inc/header3.jsp">
   <c:param name="menu" value="work"/>
</c:import>
<div class="container">
    <h2 id="top">
			<span>上海图书馆家谱文献列表 (<span id="count">0</span>)</span>
			<a id='sSearch'
				href="${ctx}/service/search"> <span
				class="btn btn-default btn-sm pull-right" ><i class="glyphicon glyphicon-random"></i>&nbsp; 简单检索</span></a>
				<a id='hSearch'
				href="${ctx}/service"> <span
				class="btn btn-default btn-sm pull-right"><i
					class="glyphicon glyphicon-random"></i>&nbsp;高级检索</span></a>
		</h2>
    <form  method="post" id="searchForm" action="${ctx}/service/search/adv" class="form-inline">
        <input type="hidden" name="pageSize" value="10" />
        <div >
            <table>
                <tr>
                    <td><label>标题</label></td>
                    <td class="left10"><input type="text" value="${data.title}" class="form-control" name="title" id="title" placeholder="在此输入标题"></td>
                    <td class="left30"><label>姓氏</label></td>
                    <td class="left10"><input type="text" value="${data.familyName}" class="form-control" name="familyName" id="familyName" placeholder="在此输入家谱名人姓氏"></td>
                    <td class="left30"><label>责任者</label></td>
                    <td class="left10"><input type="text" value="${data.creator}" class="form-control" name="creator" id="creator" placeholder="在此输入家谱责任者"></td>
                    <td class="left30"><label>谱籍</label></td>
                    <td class="left10"><input type="text" value="${data.place}" class="form-control" name="place" id="place" placeholder="在此输入谱籍地名"></td>
                </tr>
                <tr>
                    <td><label>堂号</label></td>
                    <td class="left10"><input type="text" value="${data.tang}" class="form-control" name="tang" id="tang" placeholder="在此输入堂号"></td>
                    <td class="left30"><label>先祖/名人</label></td>
                    <td class="left10"><input type="text" value="${data.person}" class="form-control" name="person" id="person" placeholder="在此输入先祖/名人"></td>
                    <td class="left30"><label>摘要</label></td>
                    <td class="left10"><input type="text" value="${data.note}" class="form-control" name="note" id="note" placeholder="在此输入摘要"></td>
                     <td class="left30"><label>馆藏地</label></td>
                    <td class="left10"><input type="text" value="${data.organization}" class="form-control" name="organization" id="organization" placeholder="在此输入馆藏地"></td>
                  
                </tr>
                <tr>
                <td colspan='2'>
                
                <input type="checkbox" class="form-control" value="${data.accFlg}"  <c:if test="${not empty data.accFlg &&  data.accFlg =='true' }">checked="checked"</c:if> name="accFlg" id="accFlg" />
                	
                	上图全文外网访问</td>
                <td class="left30"><label>索书号</label></td>
                <td class="left10"><input type="text" value="${data.selfMark}" class="form-control" name="selfMark" id="selfMark" placeholder="在此输入索书号"></td>
              <td class="left30"><label>DOI</label></td>
              <td class="left10"><input type="text" value="${data.doi}" class="form-control" name="doi" id="doi" placeholder="在此输入DOI"></td>
               <td class="left30"><button type="submit" class="btn btn-primary" id="btn_query"><i class="glyphicon glyphicon-search"></i>&nbsp;查 询</button></td>
                </tr>
            </table>
        </div>
        <hr>
        <!-- <h2 id="top"><span>结果 (<span id="count"></span>)</span></h2>
        <hr> -->
        <div id="dataDiv" class="table-responsive data_list"></div>
        <hr>
        <div id="pager"></div>
    </form>
    <div style="display: none;" id="workView"></div>
</div>
<script type="text/html" id="searchTpl">
<table>
        {@each datas as work}
        <tr>
            <td>
                <a href="${ctx}/service/work/list#uri=@{work.work}" target="_blank"> 
{@if work.imageFrontPath != ''} 
<img src="@{work.imageFrontPath}" style="width: 100px;height:100px;">{@/if}   
 {@if work.imageFrontPath==''}
<img src="${ctx}/res/images/work.jpg" style="width: 100px;height:100px;">
{@/if}
</a>
            </td>
            <td style="padding-left: 20px;">
                <dt class="issue"><a href="${ctx}/service/work/list#uri=@{work.work}" target="_blank">@{work.dtitle}</a></dt>
 {@if work.title.length != 0}<dd><b>[正题名]  </b>&nbsp;&nbsp; @{work.title}</dd>{@/if}    
{@if work.subtitles.length != 0}<dd><b>[副题名]  </b>&nbsp;&nbsp; @{work.subtitles}</dd>{@/if}        
{@if work.label.length != 0}<dd><b>[堂号]  </b>&nbsp;&nbsp; @{work.label}</dd>{@/if}     
                {@if work.creators.length != 0}<dd><b>[责任者]</b> @{work.creators}</dd>{@/if}
{@if work.desc.length != 0}<dd><b>[摘要]  </b>&nbsp;&nbsp; @{work.desc}</dd>{@/if}   
<hr>
            </td>
        <tr>
        {@/each}
        {@if works.length == 0}
        <tr><td colspan="3">未查询到相关信息！</td></tr>
        {@/if}
    </table>
</script>
<c:import url="/WEB-INF/inc/footer.jsp" />
<script src="${ctx}/res/plugin/page.js"></script>
<script src="${ctx}/res/js/normal/search/adv.js"></script>
</body>
</html>
