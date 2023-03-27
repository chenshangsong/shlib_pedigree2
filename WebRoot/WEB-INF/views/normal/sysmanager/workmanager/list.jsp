<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
<c:import url="/WEB-INF/inc/link.jsp" />
</head>
<body>
	<c:import url="/WEB-INF/inc/header3.jsp">
		<c:param name="menu" value="todolist" />
	</c:import>
	<div class="container">
		<h2 id="top">
			<span>上海图书馆家谱文献列表</span>
		</h2>
		<hr />
		<form method="post" id="workForm" action="${ctx}/service/work/list"
			class="form-inline">
			<input type="hidden" name="pageSize" value="10" />
			<div>
				<table>
					<tr>
						<td><label>标题</label></td>
						<td class="left10"><input type="text" value="${data.title}"
							class="form-control" name="title" id="title" placeholder="在此输入标题"></td>
						<td class="left30"><label>姓氏</label></td>
						<td class="left10"><input type="text"
							value="${data.familyName}" class="form-control" name="familyName"
							id="familyName" placeholder="在此输入家谱名人姓氏"></td>
						<td class="left30"><label>责任者</label></td>
						<td class="left10"><input type="text" value="${data.creator}"
							class="form-control" name="creator" id="creator"
							placeholder="在此输入家谱责任者"></td>
						<td class="left30"><label>谱籍</label></td>
						<td class="left10"><input type="text" value="${data.place}"
							class="form-control" name="place" id="place"
							placeholder="在此输入谱籍地名"></td>
					</tr>
					<tr>
						<td><label>堂号</label></td>
						<td class="left10"><input type="text" value="${data.tang}"
							class="form-control" name="tang" id="tang" placeholder="在此输入堂号"></td>
						<td class="left30"><label>先祖/名人</label></td>
						<td class="left10"><input type="text" value="${data.person}"
							class="form-control" name="person" id="person"
							placeholder="在此输入先祖/名人"></td>
						<td class="left30"><label>摘要</label></td>
						<td class="left10"><input type="text" value="${data.note}"
							class="form-control" name="note" id="note" placeholder="在此输入摘要"></td>
						<td class="left30"><label>馆藏地</label></td>
						<td class="left10"><input type="text"
							value="${data.organization}" class="form-control"
							name="organization" id="organization" placeholder="在此输入馆藏地"></td>
						
					</tr>
					<tr>
						<td><label>撰修时间</label></td>
						<td class="left10">
							<select class="form-control" style="width:100%;" name="temporalUri" id="temporalUri">
								<option value="">-请选择-</option>
								<option value="http://data.library.sh.cn/authority/temporal/pizh5ypd5d17u795">宋</option>
								<option value="http://data.library.sh.cn/authority/temporal/qafeg12mhoqfidor">元</option>
								<option value="http://data.library.sh.cn/authority/temporal/yex4deivsad41p9q">明</option>
								<option value="http://data.library.sh.cn/authority/temporal/7ase6ple2nud826q">清</option>
								<option value="http://data.library.sh.cn/authority/temporal/7sl8ulx4ouipdbg2">民国</option>
								<option value="http://data.library.sh.cn/authority/temporal/j6mijtwmcfzq3t8b">当代</option>
								<!-- <option value="其他">其他</option> -->
							</select>
						<!-- <input type="text" value=""
							class="form-control" name="temporalValue" id="tang" placeholder="在此输入堂号"> --></td>
						<td class="left30"></td>
						<td class="left10"></td>
						<td class="left30"></td>
						<td class="left10"></td>
						<td class="left10"></td>
						<td class="left30"></td>
						<td class="left30"><button type="submit"
								class="btn btn-primary" id="btn_query">
								<i class="glyphicon glyphicon-search"></i>&nbsp;查 询
							</button></td>
						
					</tr>
				</table>
			</div>
			<hr>
			 <h5 style="">本次检索出&nbsp;&nbsp;作品：（<span class="count" style="color: #a64343"></span>）；版本：（<span class="count" style="color: #a64343"></span>）；单件：（<span id="itemCount" style="color: #a64343">0</span>）</h5>
			<div id="dataDiv" class="table-responsive data_list"></div>
			<hr>
			<div id="pager"></div>
		</form>
		<div style="display: none;" id="workView"></div>
	</div>

	<div class="modal fade" id="migModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="myModalLabel"></h4>
				</div>
				<div class="modal-body"></div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="treeModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 1320px;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title"></h4>
				</div>
				<div class="modal-body" style="margin: 0px; padding: 0px;">
					<div style="width: 100%; height: 600px; overflow: auto;">
						<div id="tree_container"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/html" id="worksTpl">
<div class="row-fluid">
				<!-- block -->
				<div class="block">
					<div class="block-content collapse in">
						<div class="span12">
							<table cellpadding="0" cellspacing="0" border="0"
								class="table" id="example">
								<thead>
									<tr>
                                        <th width='15%'>题名</th>
										<th width='8%'>谱籍</th>
										<th width='8%'>堂号</th>
										<th width='8%'>责任者</th>
                                        <th width='20%'>摘要</th>
                                        <th width='10%'>操作 <button id="btn_delete" onclick="location.href='../workManager/workDelList'" class="btn btn_delete btn-primary">
						<i class="icon-list icon-white"></i>删除记录
					</button></th>
									</tr>
								</thead>
								<tbody>
								{@each works as x, i}
										<tr class="odd gradeA">
						<td width='10%'>     <a target="_blank" href="${ctx}/service/work/list#uri=@{x.work}">@{x.dtitle}</a>
        </td>
									
											<td width='15%'>@{x.location }</td>
											<td width='8%'>@{x.places}</td>
											<td width='8%'>@{x.creator}</td>
                                            <td width='20%'>@{x.desc}</td>
											<td width='10%'> 
<a class="btn btn-default btn-sm pull-right btnDelete" rel="@{x.work}" href="javascript:void(0)" ><i class="icon-remove"></i>删除</a>
  
     <a class="btn btn-default btn-sm pull-right" href="${ctx}/workManager/dataContentlist?id=@{x.work}&dataTypeId=10" ><i class="icon-edit"></i>编辑</a>
     
 </td>
										</tr>
									  {@/each}
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<!-- /block -->
			</div>
    {@if works.length == 0}<div class="row"><div class="col-xs-12">未查询到相关家谱信息！</div></div>{@/if}
</script>
	<c:import url="/WEB-INF/inc/footer.jsp" />
	<script src="${ctx}/res/plugin/page.js"></script>
	<script src="${ctx}/res/plugin/d3/d3.v3.min.js"></script>
	<script src="${ctx}/res/js/normal/work/list.js"></script>

</body>
</html>
