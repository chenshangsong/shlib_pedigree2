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
                list = map.get(item.fn_chs);
                if(!list){
                    list = [];
                    map.put(item.fn_chs, list);
                }
                list.push(item);
            })
            var provs = map.arr;
            var html = "";
            $.each(provs, function(i){
                var prov = provs[i].key;
                html += "<h3>" + prov + "</h3><ul class='left_ul'>";
                $.each(provs[i].value, function(j){
                    html += "<li><a onClick='aClick(this)' auri='" + provs[i].value[j]['uri'] + "' aname='" +  provs[i].value[j]['chs']  + "' href='javacript:void(0)'>" + provs[i].value[j]['chs'] + "</a></li>";
                });
                html += "</ul>";
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
    initQuery();
})