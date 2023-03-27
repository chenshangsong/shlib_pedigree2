$(function(){
    var places = null,provs={},placeData = null;
    var hashQuery = function(container){
        PARAM_MGR.loadParams();
        var uri = PARAM_MGR.getParam("place");
        if(uri) {
            var target = $("a[href='" + uri + "']", container);
            if (target.length == 0) {
                Dialog.error("未匹配到地域信息");
            } else {
                target[0].click();
            }
            location.hash = "#";
            PARAM_MGR.setParam("place", null);
        }
    }
	//朝代/年号纪年导入
	$("#btn_Map").click(function() {
		var strYear = $("#txtYear").val();
		showView($("#placeDiv"),strYear);
	});
    var showView = function(obj,strYear){
        showLoading();
        $.get(ctx + "/map/place/view", {year:strYear}, function(data){
            placeData = data;
            var cdInfo =data.cdInfo;
            var html = juicer($("#place_tpl").html(), placeData);
            obj.next().html(html).show().animateCss("fadeIn").find("[tag=title]").text(cdInfo.data);
            var points = [];
            $.each(data.works, function(i){
                var work = data.works[i];
                if(work.long!='' && work.lat!=''){
                	//经度
                    var long = work.long;
                    //维度
                    var lat = work.lat;
                    var pointList = "("+long+","+lat+");";
                    var workPoints =pointList.split(");");
                    for(var w=0;w<workPoints.length;w++) {
                        var workPoint = workPoints[w];
                        if(!workPoint) continue;
                        var tmps = workPoint.replace('(', '').replace(')', '').replace(';', '').split(",");
                        var x = parseFloat(tmps[0]), y = parseFloat(tmps[1]);
                        var exists = false;
                        $.each(points, function (j) {
                            var point = points[j];
                            if (point.x == x && point.y == y) {
                            	//var titles = work.title+"【"+work.time +"】";
                            	var titles = work.title;
                                point.content += "<br><a href='" + work.work + "' tag='work' class='text-nowrap'>" +titles + "</a>";
                                exists = true;
                                return false;
                            }
                        })
                        if (!exists&&x&&y) {
                            $.ajax({
                                url: ctx + "/map/place/name",
                                async: false,
                                data: {longx: x, lat: y},
                                success: function (label) {
                                	//var titles = work.title+"【"+work.time +"】";
                                	var titles = work.title;
                                    points.push({x: x, y: y, name: label, content:"</b><a href='" + work.work + "' tag='work' class='text-nowrap'>" + titles + "</a>"});
                                }
                            });
                        }
                    }
                }
            })
            drawWorkMap($("[tag=workMap]",obj.next())[0], points);
            hideLoading();
        });
    }
    $("[tag=view]").on("click", "a[tag=work]", function(){
        var uri = $(this).attr("href");
        showLoading();
        $.get(ctx + "/service/work/get", {uri : uri}, function(data){
            hideLoading();
            if(!data){ Dialog.error("未查询到文献信息");return;}
            var html = juicer(getTemplate("work"), data);
            $("#placeModal .modal-title").text("View");
            $("#placeModal").modal("show").find(".modal-body").html(html);
        });
        return false;
    }).on("click", "a[tag=more]", function(){
        location.href=ctx + "/service/work/list?place=" + $.trim($("[tag=title]").text());
    });
    $(document.body).on("click", "a[tag=place]", function(){
        location.href = ctx + "/map/place/list?place=" + $(this).attr("href");
        $("#placeModal").modal("hide");
        hashQuery($("#main .tab-pane.active div:first"));
        return false;
    });
    $("[tag=view]").on("click", "[tag=back]", function(){
        $(this).parent().parent().animateCss("fadeOut", function(){$(this).hide()})
            .prev().show().animateCss("fadeIn");;
    });
});