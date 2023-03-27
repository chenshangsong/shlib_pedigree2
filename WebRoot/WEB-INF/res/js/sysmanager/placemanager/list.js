$(function() {
	var places = null, provs = {}, placeData = null;
	var hashQuery = function(container) {
		PARAM_MGR.loadParams();
		var uri = PARAM_MGR.getParam("place");
		if (uri) {
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
	$.get(ctx + "/placeManager/listPlaces", function(data) {
		places = data;
		var append = function(place) {
			var o = provs[place.prov], t;
			if (!o) {
				o = provs[place.prov] = {
					prov : place.prov,
					uri : "",
					cities : {}
				};
			}
			if (place.city != "") {
				t = o["cities"][place.city];
				if (!t) {
					t = o["cities"][place.city] = {
						prov : place.prov,
						city : place.city,
						uri : "",
						counties : {}
					};
				}
				;
				o = t;
			}
			if (place.county != "") {
				t = o["counties"][place.county];
				if (!t) {
					t = o["counties"][place.county] = {
						prov : place.prov,
						city : place.city,
						county : place.county,
						uri : "",
						towns : {}
					};
				}
				o = t;
			}
			return o;
		};
		$.each(places, function(i) {
			var place = places[i];
			var o = append(place);
			o.uri = place.uri;
		});
		var container = $("#placeDiv");
		$.each(provs, function(i) {
			var prov = provs[i];
			var url = ctx + '/placeManager/dataContentlist?id=' + prov.uri;
			$("<h3>").html("<a href='" + url + "'>" + prov.prov + "</a>")
					.appendTo(container);
			var prov1 = $("<ul>").appendTo(container);
			var i1 = false;
			$.each(prov.cities, function(j) {
				var city = prov.cities[j];
				var city1 = null;
				if (city.city == prov.prov) {
					city1 = prov1;
				} else {
					var urlCity = ctx + '/placeManager/dataContentlist?id='
							+ city.uri;
					$("<li>").appendTo(prov1).html(
							"<a href='" + urlCity + "'>" + city.city + "</a>");
					city1 = $("<ul>").appendTo(prov1);
				}
				var j1 = false;
				$.each(city.counties, function(k) {
					i1 = true;
					var county = city.counties[k];
					var urlCounty = ctx + '/placeManager/dataContentlist?id='
							+ county.uri;
					$("<li>").appendTo(city1).html(
							"<a href='" + urlCounty + "'>" + county.county
									+ "</a>");
					var county1 = $("<ul>").appendTo(city1);
					$.each(county.towns, function(l) {
						j1 = true;
						var town = county.towns[l];
						$("<li>").appendTo(county1).html(
								"<a href='" + (town.uri || town.town) + "'>"
										+ town.town + "</a>");
					})
					if (!j1) {
						county1.remove()
					}
				})
				if (!j1) {
					city1.addClass("left_ul")
				}
				if (!i1) {
					city1.remove()
				}
				;
			});
			if (!i1) {
				prov1.addClass("left_ul")
			}
		})
		hashQuery(container);
	});
	$.get(ctx + "/placeManager/listOrigin", function(data) {
		var map = new Map();
		$.each(data, function(i) {
			var item = data[i];
			var list = null;
			item.prov = item.prov || "其他";
			list = map.get(item.prov);
			if (!list) {
				list = [];
				map.put(item.prov, list);
			}
			list.push(item);
		})
		var provs = map.arr;
		provs.sort(function(a, b) {
			if (a.key == '其他')
				return 1;
			if (b.key == '其他')
				return -1;
			return a.key > b.key ? 1 : -1
		});
		var html = "";
		$.each(provs, function(i) {
			var prov = provs[i].key;
			html += "<h3>" + prov + "</h3><ul class='left_ul'>";
			$.each(provs[i].value, function(j) {
				var url = ctx + '/placeManager/dataContentlist?id='
						+ provs[i].value[j]['uri'];
				html += "<li><a href='" + url + "'>"
						+ provs[i].value[j]['label'] + "</a></li>";
			});
			html += "</ul>";
		});
		$("#originDiv").html(html);
	});
	var showView = function(obj, self, uri) {
		showLoading();
		$
				.get(
						ctx + "/placeManager/view",
						{
							uri : uri
						},
						function(data) {
							placeData = data;
							var html = juicer($("#place_tpl").html(), placeData);
							obj.next().html(html).show().animateCss("fadeIn")
									.find("[tag=title]").text(self.text());
							var points = [];
							$
									.each(
											data.works,
											function(i) {
												var work = data.works[i];
												if (work.points != '') {
													var workPoints = work.points
															.split(");");
													for (var w = 0; w < workPoints.length; w++) {
														var workPoint = workPoints[w];
														if (!workPoint)
															continue;
														var tmps = workPoint
																.replace('(',
																		'')
																.replace(')',
																		'')
																.replace(';',
																		'')
																.split(",");
														var x = parseFloat(tmps[0]), y = parseFloat(tmps[1]);
														var exists = false;
														$
																.each(
																		points,
																		function(
																				j) {
																			var point = points[j];
																			if (point.x == x
																					&& point.y == y) {
																				point.content += "<br><a href='"
																						+ work.work
																						+ "' tag='work' class='text-nowrap'>"
																						+ work.title
																						+ "</a>";
																				exists = true;
																				return false;
																			}
																		})
														if (!exists && x && y) {
															$
																	.ajax({
																		url : ctx
																				+ "/placeManager/name",
																		async : false,
																		data : {
																			longx : x,
																			lat : y
																		},
																		success : function(
																				label) {
																			points
																					.push({
																						x : x,
																						y : y,
																						name : label,
																						content : "</b><a href='"
																								+ work.work
																								+ "' tag='work' class='text-nowrap'>"
																								+ work.title
																								+ "</a>"
																					});
																		}
																	});
														}
													}
												}
											})
							drawWorkMap($("[tag=workMap]", obj.next())[0],
									points);
							hideLoading();
						});
	}
	$("[tag=view]").on("click", "a[tag=work]", function() {
		var uri = $(this).attr("href");
		showLoading();
		$.get(ctx + "/service/work/get", {
			uri : uri
		}, function(data) {
			hideLoading();
			if (!data) {
				Dialog.error("未查询到文献信息");
				return;
			}
			var html = juicer(getTemplate("work"), data);
			$("#placeModal .modal-title").text("View");
			$("#placeModal").modal("show").find(".modal-body").html(html);
		});
		return false;
	}).on(
			"click",
			"a[tag=more]",
			function() {
				location.href = ctx + "/service/work/list#place="
						+ $.trim($("[tag=title]").text());
			});
	$(document.body).on(
			"click",
			"a[tag=place]",
			function() {
				location.href = ctx + "/placeManager/list#place="
						+ $(this).attr("href");
				$("#placeModal").modal("hide");
				hashQuery($("#main .tab-pane.active div:first"));
				return false;
			});
	$("[tag=view]").on("click", "[tag=back]", function() {
		$(this).parent().parent().animateCss("fadeOut", function() {
			$(this).hide()
		}).prev().show().animateCss("fadeIn");
		;
	});
});