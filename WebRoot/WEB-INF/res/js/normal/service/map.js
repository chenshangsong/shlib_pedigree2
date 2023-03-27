$(function(){
    var temp=document.getElementsByName("fn-radio");

    
    var source = new ol.source.GeoJSON({
        projection: 'EPSG:3857',
        url:  ctx + '/res/plugin/openlayers/3.1.1/countries.geojson'
    });
    var vector = new ol.layer.Vector({
        source: source,
        style: new ol.style.Style({
            fill: new ol.style.Fill({
                color: 'rgba(255, 255, 255, 0.2)'
            }),
            stroke: new ol.style.Stroke({
                color: '#DAA520',
                width: 0.5
            }),
            image: new ol.style.Circle({
                radius: 7,
                fill: new ol.style.Fill({
                    color: '#ffcc33'
                })
            })
        })
    });
    var flagSource = new ol.source.Vector();
    var flagVector = new ol.layer.Vector({source: flagSource});
    var drawSource = new ol.source.Vector();
    var drawVector = new ol.layer.Vector({
        source: drawSource,
        style: new ol.style.Style({
            fill: new ol.style.Fill({
                color: 'rgba(255, 255, 255, 0.2)'
            }),
            stroke: new ol.style.Stroke({
                color: '#009ACD',
                width: 2
            }),
            image: new ol.style.Circle({
                radius: 7,
                fill: new ol.style.Fill({
                    color: '#ffcc33'
                })
            })
        })
    })
    //天地图
    var attribution = new ol.Attribution({
       // html: ' <a href="http://www.chinaonmap.com/map/index.html">gaode</a>'
        html: ' <a href="http://ditu.amap.com/">高德</a>'
        	
    });
    var map = new ol.Map({
    	 layers: [ new ol.layer.Tile({
             source: new ol.source.XYZ({
                 attributions: [attribution],
                 //高德
                 url:"http://webst0{1-4}.is.autonavi.com/appmaptile?style=7&x={x}&y={y}&z={z}"
                // 天地图url: "http://t2.tianditu.com/DataServer?T=vec_w&x={x}&y={y}&l={z}"
             })
         }),
             new ol.layer.Tile({
                 source: new ol.source.XYZ({
              	     //高德
                     url:"http://webst0{1-4}.is.autonavi.com/appmaptile?style=7&x={x}&y={y}&z={z}"
                    // 天地图url: "http://t2.tianditu.com/DataServer?T=vec_w&x={x}&y={y}&l={z}"
                })
             }), vector, drawVector, flagVector],
        controls: ol.control.defaults({
            attributionOptions: /** @type {olx.control.AttributionOptions} */ ({
                collapsible: false
            })
        }).extend([
            new ol.control.FullScreen(),
            new ol.control.ScaleLine({units: 'degrees'})
        ]),
        target: $('#map')[0],
        view: new ol.View({
            center: ol.proj.transform([103.23, 34.33], 'EPSG:4326', 'EPSG:3857'),
            zoom: 4
        }),
        controls: ol.control.defaults().extend([
                                                new ol.control.ScaleLine()
                                              ])
    });

    map.addControl(new ol.control.MousePosition({
        coordinateFormat: ol.coordinate.createStringXY(2),
        projection: 'EPSG:4326',
        //className: 'custom-mouse-position',
        //target: document.getElementById('mouse-position'),
        undefinedHTML: '&nbsp;'
    }));


    var draw;
    var addDraw = function() {
        var value = "Polygon"
        if (value !== 'None') {
            draw = new ol.interaction.Draw({
                source: drawSource,
                type: /** @type {ol.geom.GeometryType} */ (value)

            });
            map.addInteraction(draw);
            draw.on("drawstart", function(evt){
                drawSource.clear();
                flagSource.clear();
            })
            draw.on("drawend", function(evt){
                showLoading()
                var k = evt.feature.getGeometry().k;
                var points = [];
                for(var i = 0; i < k.length; i += 2){
                    points.push(ol.proj.transform([k[i], k[i+1]], 'EPSG:3857', 'EPSG:4326'));
                }
                loadPlacePoints(points);
                $("#btn_polygon").click();
            })
        }
    }

    var removeDraw = function(){
        map.removeInteraction(draw);
    }

    var loadPlacePoints = function(points){
        var pointStr = "";
        var familyName = "";

        $.each(points, function(i){
            pointStr += points[i][0] + " " + points[i][1] + ",";
        })
        pointStr = "POLYGON((" + (pointStr.substring(0, pointStr.length - 1)) + "))";

        for (i=0;i<temp.length;i++){
            //遍历Radio
            if(temp[i].checked)
            {
                familyName = temp[i].value;

                if (familyName == 'other') {
                    familyName = $('#fn-input').val();
                }
            }
        }

        $.get(ctx + "/service/place/listInArea", {points:pointStr, familyName:familyName}, function(data){drawPlacePoints(data)});
    }

    var drawPlacePoints = function(places){
        if(!places||places.length == 0){
            hideLoading()
            Dialog.warn("暂无数据");
            return;
        }
        var features = [];
        $.each(places, function(i){
            var place = places[i];
            if(!place.long || !place.lat){
                return;
            }
            var iconFeature = new ol.Feature({
                geometry: new ol.geom.Point(ol.proj.transform([parseFloat(place.long), parseFloat(place.lat)], 'EPSG:4326', 'EPSG:3857')),
                name: place.label,
                uri:place.place
                //population: 4000,
                //rainfall: 500
            })
            iconFeature.setStyle(new ol.style.Style({
                image: new ol.style.Icon(/** @type {olx.style.IconOptions} */ ({
                    anchor: [0.5, 46],
                    anchorXUnits: 'fraction',
                    anchorYUnits: 'pixels',
                    opacity: 0.75,
                    src: ctx + '/res/images/flag.png'
                }))
            }));
            features.push(iconFeature);
        });
        flagSource.addFeatures(features);
        hideLoading();
    }

    var element = document.getElementById('popup');
    var popup = new ol.Overlay({
        element: element,
        offset: [0, -30],
        positioning: "top-right",
        stopEvent: false,
        insertFirst: true
    });
    map.addOverlay(popup);

    map.on('click', function (evt) {
        $(element).popover('destroy');
        var familyName = "";

        feature = map.forEachFeatureAtPixel(evt.pixel, function (feature, layer) {
            if(layer == flagVector) return feature;
        });

        for (i=0;i<temp.length;i++){
            //遍历Radio
            if(temp[i].checked)
            {
                familyName = temp[i].value;

                if (familyName == 'other') {
                    familyName = $('#fn-input').val();
                }
                if (familyName == 'all') {
                    familyName = '';
                }
            }
        }

        if (feature) {
            var geometry = feature.getGeometry();
            var coord = geometry.getCoordinates();
            popup.setPosition(coord);
            var uri = ctx + "/service/work/list";
            $.post(uri, {place_uri: feature.get("uri"), familyName : familyName, pageSize: 10}, function(data){
                var works = data.works;
                var html = "";
                $.each(works, function(i){
                    html += '<a href="' + works[i].work + '" tag="work">' + works[i].title + "</a><br>";
                })
                if(html=="")html="暂无文献数据"
                if(data.pager.pageCount > 1){
                    html += '<a href="javascript:void(0);" place="' + feature.get("name") + '"title="' + familyName + '" tag="more"><i>更多...</i></a>';
                }
                $(element).popover({
                    placement: 'right',
                    html: true,
                    title: feature.get('name'),
                    content: html
                }).popover('show');
            });
        }
    });

    $(map.getViewport()).on('mousemove', function (e) {
        var pixel = map.getEventPixel(e.originalEvent);
        var hit = map.forEachFeatureAtPixel(pixel, function (feature, layer) {
            if(layer == flagVector) return true;
        });
        if (hit) {
            map.getTarget().style.cursor = 'pointer';
        } else {
            map.getTarget().style.cursor = '';
        }
    });

    $("#btn_polygon").click(function(){
        var self = $(this);
        self.hasClass("active") ? (function(){removeDraw();self.find("i").removeClass("glyphicon-check").addClass("glyphicon-screenshot")})()
            : (function(){addDraw();self.find("i").removeClass("glyphicon-screenshot").addClass("glyphicon-check")})();
    });
    $("#map").on("click", "a[tag=more]", function(){
        location.href=ctx + "/service/work/list#place=" + $(this).attr("place") + "&title=" + $(this).attr("title");
    })
    $(document.body).on("click", "a[tag=work]", function(){
    	
    	window.open(ctx + "/service/work/list#uri=" + $(this).attr("href"));
		return false;
       /* var uri = $(this).attr("href");
        showLoading();
        $.get(ctx + "/service/work/get", {uri : uri}, function(data){
            hideLoading();
            if(!data){ Dialog.error("未查询到文献信息");return;}
            var html = juicer(getTemplate("work"), data);
            $("#workModal .modal-title").text("家谱文献");
            $("#workModal").modal("show").find(".modal-body").html(html);
        });
        return false;*/
    }).on("click", "a[tag=place]", function(){
    	
       // location.href = ctx + "/service/place/list#place=" + $(this).attr("href");
        window.open(ctx + "/service/place/list#place=" + $(this).attr("href"));
        return false;
    });

    $("#timeline_query").click(function(){
        self.location=ctx +'/service';
    });

    $("#mig_query").click(function(){
        self.location=ctx +'/service/migration/track';
    });

    $("#tree_query").click(function(){
        self.location=ctx +'/service/migration/list';
    });
});