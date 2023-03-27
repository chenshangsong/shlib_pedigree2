<div class="panel-group" id="works_accordion" role="tablist" aria-multiselectable="true">
							<table cellpadding="0" cellspacing="0" border="0"
								class="table table-striped table-bordered" id="example">
								<thead>
									<tr>
										<th width='15%'>类型asdfasdf</th>
										<th width='15%'>用户姓名</th>
										<th width='15%'>时间</th>
										<th width='15%'>状态</th>
										<th width='15%'>操作</th>
									</tr>
								</thead>
								<tbody>
									 {@each works as x, i}
       	<tr class="odd gradeA">
											
											<td width='10%'>@{x.dataTypeName}</td>
											<td width='10%'>@{x.createUser }</td>
											<td width='10%'>@{x.createDate}</td>
											<td width='10%'>
											{@if x.checkStatus=='0'}
											<font color='red'>待核准</font>
											{@/if}
											{@if x.checkStatus=='1'}
											<font>已核准</font>
											{@/if}
											</td>
											<td width='10%' class="center">

												<input type='hidden' class="formSave dataType"
													name="dataType" value="@{x.dataType}" />
												<input type='hidden' class="formSave dataUri"
													name="dataUri" value="@{x.dataUri}" />
												<input type='hidden' class="formSave graphUri"
													name="graphUri" value="@{x.graphUri}" />

												<a id="btn_DealWith" href="javascript:void(0);" class="btn btn-primary btnDo">
													<i class="icon-edit icon-white"></i>处理
												</a>
											</td>
										</tr>
    {@/each}
								
								</tbody>
							</table>
							</div>