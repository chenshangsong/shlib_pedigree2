$(function(){
    var pager = new Pager($("#workForm"));
    var worksTpl = $("#worksTpl").html();
    var loadDataList = function(){$("#workForm").submit()};
    $("#workForm").submit(function(){
        showLoading();
        $.post($(this).attr("action"), $(this).serialize(), function(result){
            $("#pager").html(pager.paint(result.pager));
            var map = new Map();
            $.each(result.works, function(i){
                var item = result.works[i];
                var list = null;
                list = map.get(item.place);
                if(!list){
                    list = [];
                    map.put(item.place, list);
                }
                list.push(item);
            })
            var provs = map.arr;
            var html = "";
            $.each(provs, function(i){
                var prov = provs[i].key;
                html += "<h3>" + prov + "</h3><ul class='left_ul'>";
                $.each(provs[i].value, function(j){
                	var url = provs[i].value[j]['uri'];   
                    html += "<a onClick='aClick(this)' auri='"+url+"' aname='" + provs[i].value[j]['chs'] + "' href='javascript:void(0)' >" + provs[i].value[j]['chs'] +"<i class='icon-ok-sign'></i></a>&nbsp;&nbsp;"
                    		+"</li>";
                });
                html += "</ul><hr>";
            });
            $("#originDiv").html(html);
            
            hideLoading();
        });
        return false;
    });
    $("#btn_query").click(function(){$("#workForm [name=pageth]").val(1);loadDataList();});
    var initQuery = function(){
        PARAM_MGR.loadParams();
        var hashQuery = false;
        var params = ["title","place", "familyName"];
        var uri = PARAM_MGR.getParam("uri");
        if(uri){
            $("#top span").hide();
            $("#workForm").hide();
            showWork(uri)
        } else {
            $.each(params, function (i) {
                var param = params[i];
                var value = PARAM_MGR.getParam(param);
                if (value) {
                    hashQuery = true;
                    $("#workForm input[name=" + param + "]").val(value);
                }
            });
			loadDataList();
        }
    };
    $("#dataDiv").on("click", "a[tag=work]", function(){
        var uri = $(this).attr("href");
        showWork(uri)
        return false;
    })
    initQuery();
});