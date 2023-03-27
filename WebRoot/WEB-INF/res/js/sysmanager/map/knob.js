var sysYear = parseInt(new Date().getFullYear() + 1);
	var year = [
		-1989,-1159,-1123,-1046,-770,-476,-221,-206,8,220,281,317,420,589,618,907,960,1127,1279,1368,1644,1864,1912,1949
	];
	var dynasty = [
		"","夏","商","周","西周","东周、春秋","东周、战国","秦","西汉","东汉","三国","晋（西晋）","晋（东晋）","南北朝","隋","唐","五代","宋（北宋）","宋（南宋）","元","明","清、南明","清","民国"
	];	
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

    $("[tag=view]").on("click", "a[tag=work]", function(){
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
/////////////////////////////////////////////////////
	var colors = [ '616161', '616161', '616161', '616161', '616161', '616161',
		'616161', '616161', '616161', '616161', '616161', '616161',
		'616161', '616161', '616161', '616161', '616161', '616161',
		'616161', '616161', '616161', '616161', '616161', '616161',
		'616161', '616161', '616161', '616161', '616161', '616161' ];

  	var rad2deg = 180/Math.PI;
  	var deg = 0;
  	var bars = $('#bars');
  	
  	for(var i=0;i<colors.length;i++){
  		
  		deg = i*12;
  		
  		// Create the colorbars
  		
  		$('<div class="colorBar">').css({
  			backgroundColor: '#'+colors[i],
  			transform:'rotate('+deg+'deg)',
  			top: -Math.sin(deg/rad2deg)*80+100,
  			left: Math.cos((180 - deg)/rad2deg)*80+100,
  		}).appendTo(bars);
  	}
	      	
  	var colorBars = bars.find('.colorBar');
  	var numBars = 0, lastNum = -1;
  	
  	$('#control').knobKnob({
  		snap : 10,
  		value: 154,
  		turn : function(ratio){
  			numBars = Math.round(colorBars.length*ratio);
  			if(numBars == lastNum){
  				return false;
  			}
  			lastNum = numBars;
  			colorBars.removeClass('active').slice(0, numBars).addClass('active');
  		}
  	});

	
	$("#degree").keydown(function(event){
		var keyCode = event.which; 
		
  		if (keyCode == 189 || keyCode == 109 || keyCode == 46 || keyCode == 8 || keyCode == 37 || 
		keyCode == 39 || (keyCode >= 48 && keyCode <= 57) || (keyCode >= 96 && keyCode <= 105) ) 
     	{return true;} 
		else { return false; } 
	});
	$("#degree").keyup(function(event){
		loadxuanNData();
	});
	
	$("#degree").focusout(function() {
		var strYear = $("#degree").val();
		var lastDeg = $("#lastDeg").val();
		if (!isNaN(strYear)){  
			if(lastDeg!=strYear)
			{
			  getServerData();
			  $('#lastDeg').val(strYear);
			}
		}
	});
	
	//初始化数据
	$('#degree').val("1880");
	//旋钮重新加载
	loadxuanNData();
	getServerData();
	$("#btn-All").click(function(){
		var strYear = $("#degree").val();
		showView($("#placeDiv"),strYear,true);
	});
});

function showView(obj,strYear,isAll){
   // showLoading();
    $.get(ctx + "/map/place/view", {year:strYear,isAll:isAll}, function(data){
    	if (data.all){
    		$("#btn-All").show();
    	} else {
    		$("#btn-All").hide();
    	}
    	
        placeData = data;
        var cdInfo =data.cdInfo;
       // alert(cdInfo.data);
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
                        	var titles = work.title;
                        	point.content += "<br><a href='"+ctx+"/service/work/list#uri=" + work.work + "' target='_blank'  class='text-nowrap'>" + work.title + "</a>";
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
                                points.push({x: x, y: y, name: label, content:"</b><a href='"+ctx+"/service/work/list#uri=" + work.work + "' target='_blank' class='text-nowrap'>" + work.title + "</a>"});
                                  }
                        });
                    }
                }
            }
        })
        drawWorkMap($("[tag=workMap]",obj.next())[0], points);
      //  hideLoading();
    });
}
/**
 * 获取后台数据
 */
function getServerData(){
	var strYear = $("#degree").val();
	showView($("#placeDiv"),strYear,false);
}
/**
 * 刷新输入框下的朝代名称
 */
function getDynasty(){
	for(var i=0;i<year.length;i++){
		if(year[i]> parseInt($("#degree").val())){
				$("#dynasty").html(dynasty[i]);
				return;
			}
	}
	$("#dynasty").html("当代");
};

/**
 * 年代输入弹起事件
 */
function loadxuanNData(){
	var x=parseInt($('#degree').val());
	if(x<-1580)x=-1580;
	if(x>sysYear)x=sysYear
	currentDeg = parseInt((x+1580)/10);
	$('#hidDeg').val(currentDeg);
	$('.top').css('transform','rotate('+(currentDeg)+'deg)');
	var rad2deg = 180/Math.PI;
	var bars = $('#bars');
	var colorBars = bars.find('.colorBar');
	var numBars = Math.round(colorBars.length*currentDeg/359);
	lastNum = numBars;
	colorBars.removeClass('active').slice(0, numBars).addClass('active');
	getDynasty();
}