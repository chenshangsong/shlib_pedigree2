$(function(){
   
	var pager = new Pager($("#todoListForm"));
	var todoListTpl = $("#todoListTpl").html();
	var loadDataList = function(){
		$("#todoListForm").submit()
	};
	$("#todoListForm").submit(function(){
        showLoading();
        $.post($(this).attr("action"), $(this).serialize(), function(result){
            $("#pager").html(pager.paint(result.pager));
            $("#dataDiv").html(juicer(todoListTpl, result)).animateCss("fadeIn");
            $(".btnDo").click(function(){
            	 var strFullPath=window.document.location.href;
                 var strPath=window.document.location.pathname;
                 var pos=strFullPath.indexOf(strPath);
                 var prePath=strFullPath.substring(0,pos);
                 var postPath=strPath.substring(0,strPath.substr(1).indexOf('/')+1);
    			var dataType = $(this).parents('tr').find('.dataType').val();
    			var dataUri = $(this).parents('tr').find('.dataUri').val();
    			var pageUrl =postPath+'/'+$.pageUrlGroup.getName(dataType);
    			window.open(pageUrl+'dataContentlist?id='
    					+ dataUri+'&dataTypeId='+dataType, '_self');
    		});
            hideLoading();
        });
        return false;
    });
	
	$("#btn_query").click(function(){
		$("#todoListForm [name=pageth]").val(1);
		loadDataList();
	});
	
	
	var initQuery = function(){
        PARAM_MGR.loadParams();
        var hashQuery = false;
        var params = ["title","place", "familyName"];
        var uri = PARAM_MGR.getParam("uri");
        if(uri){
            $("#top span").hide();
            $("#todoListForm").hide();
            showWork(uri)
        } else {
            $.each(params, function (i) {
                var param = params[i];
                var value = PARAM_MGR.getParam(param);
                if (value) {
                    hashQuery = true;
                    $("#todoListForm input[name=" + param + "]").val(value);
                }
            });
			loadDataList();
        }
    };
	
	initQuery();
})