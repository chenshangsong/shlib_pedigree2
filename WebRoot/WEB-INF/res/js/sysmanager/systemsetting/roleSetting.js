$(function(){
    var loadDataList = function(){$("#workForm").submit()};
    $("#workForm").submit(function(){
        showLoading();
        $.post($(this).attr("action"), $(this).serialize(), function(result){
        	var html = "";
            $.each(result.works, function(i){
                var item = result.works[i];
                html += "<tr class='odd gradeA'>";
                if (item.selected == true){
                	html += "<td width='5%'>";
                	html += "<input class='formSave'value=" + item.menuId + " type='checkbox' checked='checked' />";
                	html += "</td>";
                } else{
                	html += "<td width='5%'>";
                	html += "<input class='formSave'value=" + item.menuId + " type='checkbox' />";
                	html += "</td>";
                }
                
            	html +=	"<td width='10%'>" + item.menuName + "</td>";
            	html += "</tr>";
            })

            $("#originDiv").html(html);
            hideLoading();
        });
        return false;
    });
    
    $('#ctRole').change(function(){ 
    	loadDataList();
    });
    
    $("#btn_save").click(function(){
    	//消息提示
		showConfirmMsg(String.format(IMSG0003,"保存"),saveFunc);
		//保存函数
		function saveFunc(){
			var menuid = '';
			//得到List列表
			$.each($("#tableData input.formSave[type='checkbox']"), function(i) {
				if ( $(this).is(':checked') ) {
					menuid += $(this).val() + ",";
				}
			});
			//截掉最后一个分隔符。
			if (menuid.lastIndexOf(',') > 0) {
				menuid = menuid.substring(0, menuid.length - 1);
			}
			
			$("#hidMenuId").val(menuid);
			var param = $(".formSaveMaster").toJson();
			requestJson(ctx+'/systemSetting/saveRoleOfMenu', param, successFun);
		}
    });
    
    function successFun(data) {
		if (data.result == '0') {
			showMsg(String.format(IMSG0004,"保存"));
		} else {
			showErrorMsg(String.format(EMSG9000,"保存"));
		}
		window.location.reload();
	}
    
    loadDataList();
})
