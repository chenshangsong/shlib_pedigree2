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
		<c:param name="menu" value="systemSetting" />
	</c:import>

	<%--  <a href="${ctx}/systemSetting/main"> <span class="btn btn-default btn-sm pull-right" tag="back"><i class="glyphicon glyphicon-chevron-left"></i>&nbsp;返回</span></a>
    --%>
	<div class="container">
		<div  class="panel panel-default">
			<div  class="panel-body desc">
			
				 <button id="btn_importF" class="btn btn-primary">
					<i class="icon-edit icon-white"></i>XX姓氏导入
				</button>
				<button id="btn_importP" class="btn btn-primary">
					<i class="icon-edit icon-white"></i>XX先祖导入
				</button>
				<button id="btn_importMR" class="btn btn-primary">
					<i class="icon-edit icon-white"></i>XX名人导入
				</button>
				<button id="btn_importZXZ" class="btn btn-primary">
					<i class="icon-edit icon-white"></i>XX纂修者导入
				</button>
				<button id="btn_importTanghao" class="btn btn-primary">
					<i class="icon-edit icon-white"></i>XX堂号导入
				</button>
				<button id="btn_importPuming" class="btn btn-primary">
					<i class="icon-edit icon-white"></i>XX谱名导入
				</button>
				<button id="btn_importPlace" class="btn btn-primary">
					<i class="icon-edit icon-white"></i>地名导入
				</button>
				<button id="btn_importJigou" class="btn btn-primary">
					<i class="icon-edit icon-white"></i>机构导入
				</button>
				<button id="btn_importCategory" class="btn btn-primary">
					<i class="icon-edit icon-white"></i>取值词表导入
				</button>
				<button id="btn_importChaodai" class="btn btn-primary">
					<i class="icon-edit icon-white"></i>朝代/年号纪年导入
				</button> 
				<button id="btn_importShumu" class="btn btn-primary">
					<i class="icon-edit icon-white"></i>书目导入
				</button>
				<button id="btn_api" class="btn btn-primary">
					<i class="icon-edit icon-white"></i>WebApi
				</button>
				<button id="btn_Map" class="btn btn-primary">
					<i class="icon-edit icon-white"></i>地图测试
				</button>
				<button id="btn_tongji" class="btn btn-primary">
					<i class="icon-edit icon-white"></i>数据统计
				</button>
				<button id="btn_hs" class="btn btn-primary">
					<i class="icon-edit icon-white"></i>胡适普系图
				</button>
				<button id="btn_hspeiou" class="btn btn-primary">
					<i class="icon-edit icon-white"></i>胡适配偶
				</button>
			</div>
			书目导入步骤
			<button id="step1"  class="btn step btn-primary">
				<i class="icon-edit icon-white"></i>第1
			</button>
			<button id="step2"  class="btn step btn-primary">
				<i class="icon-edit icon-white"></i>第2
			</button>
			<button id="step3"  class="btn step btn-primary">
				<i class="icon-edit icon-white"></i>第3
			</button>
			<button id="step4"  class="btn step btn-primary">
				<i class="icon-edit icon-white"></i>第4
			</button>
			<button id="step5"  class="btn step btn-primary">
				<i class="icon-edit icon-white"></i>第5
			</button>
			<button id="step6"  class="btn step btn-primary">
				<i class="icon-edit icon-white"></i>第6
			</button>
			<button id="step7"  class="btn step btn-primary">
				<i class="icon-edit icon-white"></i>第7
			</button>
			<button id="step8"  class="btn step btn-primary">
				<i class="icon-edit icon-white"></i>第8
			</button>
			<button id="step9"  class="btn step btn-primary">
				<i class="icon-edit icon-white"></i>第9
			</button>
			<button id="step10"  class="btn step btn-primary">
				<i class="icon-edit icon-white"></i>10附注导入
			</button>
			<button id="step11"  class="btn step btn-primary">
				<i class="icon-edit icon-white"></i>第11
			</button>
			<button id="step12"  class="btn step btn-primary">
				<i class="icon-edit icon-white"></i>第12Instance
			</button>
			<button id="step13"  class="btn step btn-primary">
				<i class="icon-edit icon-white"></i>第13Item
			</button>
			<button id="step14"  class="btn step btn-primary">
				<i class="icon-edit icon-white"></i>第14
			</button>
			<button id="step15"  class="btn step btn-primary">
				<i class="icon-edit icon-white"></i>第15
			</button>
			<button id="step16"  class="btn step btn-primary">
				<i class="icon-edit icon-white"></i>第16
			</button>
			<button id="step17"  class="btn step btn-primary">
				<i class="icon-edit icon-white"></i>第17
			</button>
			
			<!-- 	<button id="step18"  class="btn step btn-primary">
				<i class="icon-edit icon-white"></i>更新居地
			</button> -->
			<button id="step19"  class="btn step btn-primary">
				<i class="icon-edit icon-white"></i>DOI测试
			</button>
			<button id="step20"  class="btn step btn-primary">
				<i class="icon-edit icon-white"></i>香港中大更新
			</button>
			<button id="step21"  class="btn step btn-primary">
				<i class="icon-edit icon-white"></i>书目X姓氏导入
			</button>
			<button id="step22"  class="btn step btn-primary">
				<i class="icon-edit icon-white"></i>盛档人员导入
			</button>
		</div>
	</div>
	<!-- 盛档 -->
	<div class="panel-body desc">
		<button id="btn_SDPerson" class="btn btn-primary">
			<i class="icon-edit icon-white"></i>盛档Person
		</button>
		<button id="btn_SDOrganization" class="btn btn-primary">
			<i class="icon-edit icon-white"></i>盛档Organization
		</button>
		<button id="btn_SDPlace" class="btn btn-primary">
			<i class="icon-edit icon-white"></i>盛档Place
		</button>
		<button id="btn_updateSureFamilyName" class="btn btn-primary">
			<i class="icon-edit icon-white"></i>首页姓氏更新
		</button>
	</div>
	<!-- 错误place调整-->
	<hr>
	
	<div class="panel-body desc">
		<button id="btn_ErrorPlace" class="btn btn-primary">
			<i class="icon-edit icon-white"></i>Place-1更新经纬度
		</button>
		<button id="btn_ErrorPlace2" class="btn btn-primary">
			<i class="icon-edit icon-white"></i>Place2更新VT
		</button>
		<button id="btn_ErrorPlace3" class="btn btn-primary">
			<i class="icon-edit icon-white"></i>Place3更新书目错误Place
		</button>
	</div>
	<hr>
	DOI测试
	<div class="panel-body desc">
	DOI:<input id='txtDOI' class='formSave' name='doi' type='text' ></input>
		<button id="btn_Open" class="btn btn-primary">
			<i class="icon-edit icon-white"></i>开放
		</button>
		<button id="btn_Close" class="btn btn-primary">
			<i class="icon-edit icon-white"></i>关闭
		</button>
	</div>
	<hr>
	-------------------新家谱数据导入-------------------
	<div class="panel-body desc">
		<button id="btn_jp_timing" class="btn btn-primary">
			<i class="icon-edit icon-white"></i>1,正题名导入
		</button>
		<button id="btn_jp_work" class="btn btn-primary">
			<i class="icon-edit icon-white"></i>2,Work数据导入
		</button>
		<button id="btn_jp_person" class="btn btn-primary">
			<i class="icon-edit icon-white"></i>3,Person数据导入
		</button>
		<button id="btn_jp_doi" class="btn btn-primary">
			<i class="icon-edit icon-white"></i>4,DOI开关导入
		</button>
	</div>
	<c:import url="/WEB-INF/inc/footer.jsp" />
	<script>
		$(function() {
			//新家谱数据导入Begin
			$("#btn_jp_timing").click(
				function() {
					requestJson("${ctx}/jpWorkImportManager/importZTM",
							null, successFun);
			});
			$("#btn_jp_work").click(
				function() {
					requestJson("${ctx}/jpWorkImportManager/importWork",
							null, successFun);
			});
			$("#btn_jp_person").click(
					function() {
						requestJson("${ctx}/jpWorkImportManager/importPerson",
								null, successFun);
				});
			$("#btn_jp_doi").click(
					function() {
						requestJson("${ctx}/jpWorkImportManager/updateDoi",
								null, successFun);
				});
			//end
			//ErrorPlace-1
			$("#btn_ErrorPlace").click(
				function() {
					requestJson("${ctx}/dataErrorPlaceImportManager/updateErrorPlace",
							null, successFun);
			});
			$("#btn_Open").click(
					function() {
						requestJson("${ctx}/dataShumuImportManager/updateDoi?doi="+$("#txtDOI").val()+"&flg=1",
								null, successFun);
				});
			$("#btn_Close").click(
					function() {
						requestJson("${ctx}/dataShumuImportManager/updateDoi?doi="+$("#txtDOI").val()+"&flg=0",
								null, successFun);
				});
			//ErrorPlace-2
			$("#btn_ErrorPlace2").click(
				function() {
					requestJson("${ctx}/dataErrorPlaceImportManager/updateVTPlace",
							null, successFun);
			});
			//ErrorPlace-3
			$("#btn_ErrorPlace3").click(
				function() {
					requestJson("${ctx}/dataErrorPlaceImportManager/chageWorkPlace",
							null, successFun);
			});
			//盛档SDPerson
			$("#btn_hs").click(
				function() {
					requestJson("${ctx}/dataImportManager/importHSFamilyName",
							null, successFun);
			});
			//盛档SDPerson
			$("#btn_hspeiou").click(
				function() {
					requestJson("${ctx}/dataImportManager/importHSPeiou",
							null, successFun);
			});
			//盛档SDOrganization
			$("#btn_SDOrganization").click(
				function() {
					requestJson("${ctx}/dataImportManager/importSDOrganization",
							null, successFun);
			});
			//盛档SDPlace
			$("#btn_SDPlace").click(
				function() {
					requestJson("${ctx}/dataImportManager/importSDPlace",
							null, successFun);
			});
			//首页姓氏更新
			$("#btn_updateSureFamilyName").click(
				function() {
					requestJson("${ctx}/dataImportManager/updateSureFamilyName",
							null, successFun);
			});
			//先祖导入
			$("#btn_importP").click(
					function() {
						requestJson("${ctx}/dataImportManager/importPerson1",
								null, successFun);
					});
			//名人导入
			$("#btn_importMR").click(
					function() {
						requestJson("${ctx}/dataImportManager/importPersonMR",
								null, successFun);
					});
			//纂修者导入
			$("#btn_importZXZ").click(
					function() {
						requestJson("${ctx}/dataImportManager/importPersonZXZ",
								null, successFun);
					});
			//姓氏导入
			$("#btn_importF").click(
					function() {
						requestJson(
								"${ctx}/dataImportManager/importFamilyName",
								null, successFun);
					});
			//姓氏导入
			$("#btn_importTanghao").click(
					function() {
						requestJson("${ctx}/dataImportManager/importTanghao",
								null, successFun);
					});
			//谱名导入
			$("#btn_importPuming").click(
					function() {
						requestJson("${ctx}/dataImportManager/importPuming",
								null, successFun);
					});
			//地名导入
			$("#btn_importPlace").click(
					function() {
						requestJson("${ctx}/dataImportManager/importPlace",
								null, successFun);
					});
			//机构导入
			$("#btn_importJigou").click(
					function() {
						requestJson("${ctx}/dataImportManager/importJigou",
								null, successFun);
					});

			//取词词表导入
			$("#btn_importCategory").click(
					function() {
						requestJson("${ctx}/dataImportManager/importCategory",
								null, successFun);
					});
			//书目导入
			$("#btn_importShumu")
					.click(
							function() {
								//消息提示
								showConfirmMsg(String.format(IMSG0003, "保存"),
										saveFunc);
								//保存函数
								function saveFunc() {
									requestJson(
											"${ctx}/dataShumuImportManager/importShumu",
											null, successFun);
								}
							});
			//朝代/年号纪年导入
			$("#btn_importChaodai")
					.click(
							function() {
								//消息提示
								showConfirmMsg(String.format(IMSG0003, "保存"),
										saveFunc);
								//保存函数
								function saveFunc() {
									requestJson(
											"${ctx}/dataImportManager/importChaodai",
											null, successFun);
								}
							});
			//朝代/年号纪年导入
			$("#btn_api").click(function() {
				$.ajax({
					url : "http://localhost:8080/webapi/temporal",
					type : "POST",
					success : function(data) {
						alert(json.stringify(data));
					}
				});
			});
			//朝代/年号纪年导入
			$("#btn_Map").click(function() {
				window.location.href = "${ctx}/map/place/list";
			});
			//数据统计
			$("#btn_tongji").click(
					function() {
						//消息提示
						showConfirmMsg(String.format(IMSG0003, "执行统计"),
								saveFunc);
						//保存函数
						function saveFunc() {
							requestJson("${ctx}/dataImportManager/tongji",
									null, successFun);
						}
					});
			//书目导入
			$(".step")
					.click(
							function() {
								var step = $(this).attr("id").substring(4);
								alert(step);
								//消息提示
								showConfirmMsg(String.format(IMSG0003, "保存"),
										saveFunc);
								//保存函数
								function saveFunc() {
									requestJson(
											"${ctx}/dataShumuImportManager/importShumu?step="+step,
											null, successFun);
									$("#step"+step).attr("disabled","true");
								}
							});
			//成功回调函数
			function successFun(data) {
				if (data.result == '0') {
					showMsg(String.format(IMSG0004, "导入"));
				} else {
					//错误弹框
					showErrorMsg(String.format(EMSG9000, "导入"));
				}
				if (data.data != null) {
					$("#dataDiv").html(JSON.stringify(data.data));
				}
			}
		});
	</script>
</body>


</html>