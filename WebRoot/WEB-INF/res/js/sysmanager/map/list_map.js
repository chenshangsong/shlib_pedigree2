var drawWorkMap = function(target, points){
    var iconFeatures = [];
    var center = [100, 30], zoom = 4;
    for(var i = 0; i < points.length; i ++){
        var iconFeature = new ol.Feature({
            geometry: new ol.geom.Point(ol.proj.transform([points[i].x, points[i].y], 'EPSG:4326', 'EPSG:3857')),
            name: points[i].name,
            content: points[i].content
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
        iconFeatures.push(iconFeature);
        if(i == 0){
            center = ol.proj.transform([points[i].x, points[i].y], 'EPSG:4326', 'EPSG:3857');
            zoom += 2;
        }
    }
    var vectorLayer = new ol.layer.Vector({
        source: new ol.source.Vector({features: iconFeatures})
    });
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
               }), vectorLayer],
        target: target,
        view: new ol.View({
            center: ol.proj.transform([103.23, 34.33], 'EPSG:4326', 'EPSG:3857'),
            zoom: 4
        }),
        controls: ol.control.defaults().extend([
                                                new ol.control.ScaleLine()
                                              ])
    });

    var element = document.getElementById('popup');
    var popup = new ol.Overlay({
        element: element,
        offset: [0, -30],
        positioning: "top-right",
        stopEvent: false,
        insertFirst: true
    });
    map.addOverlay(popup);

// display popup on click
    map.on('click', function (evt) {
        $(element).popover('destroy');
        feature = map.forEachFeatureAtPixel(evt.pixel, function (feature, layer) {
            return feature;
        });
        if (feature) {
            var geometry = feature.getGeometry();
            var coord = geometry.getCoordinates();
            popup.setPosition(coord);
            $(element).popover({
                placement: 'right',
                html: true,
                title:feature.get('name'),
                content: feature.get('content')
            });
            $(element).popover('show');
        }
    });

// change mouse cursor when over marker
    $(map.getViewport()).on('mousemove', function (e) {
        var pixel = map.getEventPixel(e.originalEvent);
        var hit = map.forEachFeatureAtPixel(pixel, function (feature, layer) {
            return true;
        });
        if (hit) {
            map.getTarget().style.cursor = 'pointer';
        } else {
            map.getTarget().style.cursor = '';
        }
    });
};