$(function(){
	
	$("#btn_query").click(function(){
		var tempForm = document.createElement("form");
		tempForm.id="tempForm";
		tempForm.method="post";
		tempForm.action=ctx + "/service/search/serviceAdv";
		//tempForm.target="_blank";
		tempForm.style.display="none";
		
		$.each($('.seniorSearch .form-control'), function(i, n){
			var hideInput = document.createElement("input");
			hideInput.type = "hidden";
		    hideInput.name = n.name;
		    hideInput.value = n.value;
		    if(n.name=="accFlg" && !n.checked){
		    	hideInput.value = "";
		    }
		    tempForm.appendChild(hideInput); 
		}); 
        
		$(tempForm).submit( function () {
			window.open("about:blank"); 
		});

		$(".seniorSearch").append(tempForm);
	  	tempForm.submit();
		$(tempForm).remove();
	});
    $.get(ctx + "/service/work/getTimes", function(data){
        var obj =$('#dynasty');
       // obj.append($('<option value="">=请选择=</option>')); 
        for (var i = 0; i < data.length; i++) { 
        	obj.append($('<option beginy="' + data[i].begin + '" endy="' + data[i].end + '" value="' + data[i].uri + '">' + data[i].name + '</option>')); 
        } 
        $("#dynasty ").val('http://data.library.sh.cn/authority/temporal/7ase6ple2nud826q');
        /*for (var i=0;i<data.length;i++) {
            obj.options.add(new Option(data[i].name,data[i].uri));
        }*/
    });

    var tm;
    var beginY=1644;
    var endY=1911;
    var uri = "http://data.library.sh.cn/authority/temporal/7ase6ple2nud826q";
    var unit = 100;
	var globalData = null;

    getWorks(beginY,endY,uri, unit, "");
 
	// 绘制地图
	var drawMap = function(data, options) {
		
        $('#timeline').html('');
        $('#map').html('');
		tm = TimeMap.init({
			mapId: "map",               // Id of map div element (required)
			timelineId: "timeline",     // Id of timeline div element (required)
			options: {
				eventIconPath: ctx + "/res/plugin/timemap/images/"
			},
			datasets: [
				{
					id: "works",
					title: "Works",
					theme: "green",
					// note that the lines below are now the preferred syntax
					type: "basic",
					options: {
						items: data
					}
				}
			],
			bandIntervals: [
				Timeline.DateTime.YEAR,
				Timeline.DateTime.DECADE
			]
		}, options.size||230);
	}

    function getWorks(beginY,endY,uri, unit, name) {
    	   
        showLoading();
      
        $.get(ctx + "/service/work/getInGeo", {beginY:beginY,endY:endY,uri: uri,unit: unit, name: name}, function(result){
            var data = [];
            $.each(result, function(i){
                var item = {};
                item["end"] = item["start"] = result[i]["begin"];
                item["title"] = "<a href='" + ctx + "/service/work/list#uri=" + result[i]["work"] + "' target='_blank'>" + result[i]["title"] + "</a>";
                item["point"] = {lat: result[i]["lat"], lon: result[i]["long"]};
                var options = item["options"] = {};
                options["description"] = "[" + result[i]["label"] + "]  " + result[i]["note"];
                if(name){
                	var title = result[i]["title"];
                	var str = name + "氏";
                    if (title.indexOf(str)>0) {
                        options["theme"] = "orange";
                        options["eventTextColor"] = "orange";
                    }
                }
                data.push(item)
            })
			globalData = data;
			$("#time_nodes #time_label").text("5年");		
            drawMap(data, {});
         
            hideLoading();
        });
    }
	$("#time_nodes").on("click", ".dropdown-menu a[data]", function(){
		var size = $(this).attr("data");
		drawMap(globalData, {size : size});
		$("#time_nodes #time_label").text($(this).text());
//		return false;
	})
    $("#timeForm").submit(function(){$("#btn_query").click();return false;})
    $("#btn_query2").click(function(){
        $("div[id=timeline]").empty();
        $("div[id=map]").empty();

        var uri = $("#dynasty").val();
        var beginY = $("#dynasty").find("option:selected").attr('beginy');
        var endY =  $("#dynasty").find("option:selected").attr('endy');
        if (uri != '') {
            var unit = $("#_unit").val();
            var name = $("#name").val();
            getWorks(beginY,endY,uri, unit, name);
        } else {
        	beginY=1644;
        	endY=1911;
            uri = "http://data.library.sh.cn/authority/temporal/7ase6ple2nud826q";
            unit = 100;
            getWorks(beginY,endY,uri, unit, "");
        }

        return false;
    });

    $("#map_query").click(function(){
        self.location=ctx +'/service/map';
    });

    $("#mig_query").click(function(){
        self.location=ctx +'/service/migration/track';
    });

    $("#tree_query").click(function(){
        self.location=ctx +'/service/migration/list';
    });
});