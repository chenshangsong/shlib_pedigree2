$(function() {

	var isPause = false;
	// 默认轮播间隔10秒
	$('#myCarousel').carousel({
		interval : 10000
	})
	$('.span6').hover(function() {
		$("#myCarousel").carousel('pause');
	}, function() {
		isPause = false;
		$("#myCarousel").carousel('cycle');
	});

	$(".carousel-indicators li").click(function() {
		isPause = true;
	});

	$('#myCarousel').on('slid.bs.carousel', function(event) {
		if (isPause) {
			$("#myCarousel").carousel('pause');
		}
	});

	$('#textKeyWord').keydown(function(event) {
		if (event.keyCode == 13) {
			$("#btnQuickSearch").click();
		}
	});

	$("#btn-adv").click(function() {
		var tempForm = document.createElement("form");
		tempForm.id = "tempForm";
		tempForm.method = "post";
		tempForm.action = ctx + "/service/search/homeAdv";
		tempForm.target = "_blank";
		tempForm.style.display = "none";

		$.each($('.seniorSearch .form-control'), function(i, n) {
			var hideInput = document.createElement("input");
			hideInput.type = "hidden";
			hideInput.name = n.name;
			hideInput.value = n.value;
			tempForm.appendChild(hideInput);
		});

		$(tempForm).submit(function() {
			window.open("about:blank");
		});

		$(".seniorSearch").append(tempForm);
		tempForm.submit();
		$(tempForm).remove();
	});

	$("#btnQuickSearch").click(function() {
		var keyWork = $("#textKeyWord").val();
		window.open(ctx + "/service/search?keyword=" + keyWork);
	});

	$.each([ "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N",
			"O", "P", "Q", "R", "S", "T", "W", "X", "Y", "Z" ], function(i, n) {
		$(".initials").append("<li class='badge'>" + n + "</li>");
	});

	$(".initials")
			.delegate(
					"li",
					"click",
					function() {
						var firstChar = $(this).html();
						$
								.post(
										ctx + "/home/doInitial",
										{
											firstChar : firstChar
										},
										function(result) {
											var initial = "", lineBreak = 0, html = "";
											$
													.each(
															result.surname,
															function(i) {
																var surname = result.surname[i];
																if ((lineBreak != 0 && lineBreak % 4 == 0)
																		|| (surname.initial != initial && initial != '')) {
																	html += "</div><br/>";
																}
																if (surname.initial != initial) {
																	initial = surname.initial;
																	lineBreak = 0;
																	html += "<p id='"
																			+ initial
																			+ "'>"
																			+ initial
																			+ "</p>";
																	html += "<div class='both'></div>";
																}
																if (lineBreak % 4 == 0) {
																	html += "<div class='row-fluid'>";
																}
																html += bindSurname(surname);
																lineBreak = lineBreak + 1;
															});
											$("#surname .mCSB_container").html(
													html);
											$("#surname").mCustomScrollbar(
													'update');
											$("#surname").mCustomScrollbar(
													'scrollTo', '0');
											// $('#surname').mCustomScrollbar('scrollTo',
											// $('#surname').find('.mCSB_container').find(firstChar));
											// $("#surname").mCustomScrollbar("update");
										});
					});

	$("#persons").delegate("a", "click", function() {
		if($('#persons>li>a').length>1){
			$('#persons>li>a').removeAttr("style");
			$(this).css('color','#7B7B7B');
		}
		var relatedWork = $(this).attr("relatedWork");
		var personUrl = $(this).attr("personUrl");
		var personName = $(this).html();
		$("#firstChar").val(relatedWork);
		$.post(ctx + "/home/doFullName", {
			firstChar : $("#firstChar").val(),
			puri : personUrl
		}, function(result) {
			$("#personName").html(personName);
			setWork(result.work)
			setWorkDetail(result.currentWork);
			
		});
	});

	// 左侧家谱点击事件
	$("#works").delegate("a", "click", function() {
		if($('#works>li>a').length>1){
			$('#works>li>a').removeAttr("style");
			$(this).css('color','#7B7B7B');
		}
		var relatedWork = $(this).attr("uri");
		var personUrl = $(this).attr("puri");
		$.post(ctx + "/home/getWorkByUri", {
			uri : relatedWork,
			puri : personUrl
		}, function(result) {
			setWorkDetail(result.work);
		});
	});
	// 点击姓氏，刷新右侧
	$("#surname").delegate(
			"table",
			"click",
			function() {
				// 点击左边姓氏，导航默认0
				$("#myCarousel").carousel(0);
				var firstChar = $(this).find('.familyName').html();
				$("#firstChar").val(firstChar);
				$.post(ctx + "/home/doSurname", {
					firstChar : $("#firstChar").val()
				}, function(result) {
					var persons = "";
					$.each(result.person, function(i) {
						persons += "<li><a  href='javaScript:;' "
								+ " relatedWork="
								+ result.person[i].relatedWork + " personUrl="
								+ result.person[i].uri + ">"
								+ result.person[i].name + "</a></li>"
					});
					// layer.closeTips();
					$("#persons").html(persons);
					if($("#persons>li").length<=1){
						$("#persons>li>a").css('text-decoration','none');
						$("#persons>li>a").css('cursor','default');
						
					}
					$("#familyNameA").html(result.currentSurname.familyNameS);
					$("#familyNameA").attr(
							"href",
							ctx + "/service/search/adv?familyName="
									+ result.currentSurname.familyNameS);
					$("#pinyin").html(result.currentSurname.pinyin);
					// 截取显示
					var des = result.currentSurname.description;
					var des2 = result.currentSurname.description;
					if (des.length > 170) {
						des = des.substr(0, 170) + "...";
					}
					$("#description").html(des);
					$("#personName").html(result.currentPerson.name);
					setWork(result.work);
					setWorkDetail(result.currentWork);
					// 显示tips注释
					$('.layui-layer-tips').css('display', 'none');
					$("#description").mouseover(function() {
						$('.layui-layer-tips').css('display', 'none');
						if (des2.length > 170) {
							layer.tips('...' + des2.substr(168), this, {
								tips : [ 2, '#777' ],
								time : 10000
							});
						}
					});
					$("#description").mouseout(function() {
						$('.layui-layer-tips').css('display', 'none');
					});

				});
			});

	function setWork(work) {
		var works = "";
		$.each(work, function(i) {
			var dtitle = work[i].dtitle;
			/*
			 * var sortTitle =""; if(dtitle.length>6){ sortTitle =
			 * dtitle.substr(0,12)+"..."; }
			 */
			works += "<li ><a  class='workTitleR' href='javaScript:;' uri='"
					+ work[i].uri + "' fullTitle='" + dtitle + "' puri='"
					+ work[i].puri + "'>" + dtitle + "</a></li>";

		});
		$("#works").html(works);
		if($("#works>li").length<=1){
			$("#works>li>a").css('text-decoration','none');
			$("#works>li>a").css('cursor','default');
		}
		// 添加悬浮框
		/*
		 * $(".workTitleR").each(function(i){ var fulltitle =
		 * $(this).attr("fullTitle"); //显示tips注释
		 * $('.layui-layer-tips').css('display','none');
		 * $(this).mouseover(function() {
		 * $('.layui-layer-tips').css('display','none'); if(fulltitle.length>6){
		 * layer.tips('...'+fulltitle.substr(12), this, { tips : [ 2, '#777' ],
		 * time : 10000 }); } }); $(this).mouseout(function() {
		 * $('.layui-layer-tips').css('display','none'); });
		 * 
		 * });
		 */

	}

	function setWorkDetail(detail) {
		if (detail.creator != '') {
			$("#licreator").show();
			$("#creator").html(detail.creator);
		} else {
			$("#licreator").hide();
		}
		if (detail.tangh != '') {
			$("#litangh").show();
			$("#tangh").html(detail.tangh);
		} else {
			$("#litangh").hide();
		}
		$("#note").html(detail.note);
		// $("#title").html(detail.title);
		$("#title").html(detail.title);
		$("#title").attr("href", ctx + "/service/work/list#uri=" + detail.uri);
		if (detail.familyRelations != null && detail.familyRelations.length > 0) {
			$.each(detail.familyRelations, function(i) {
				if (detail.puri == detail.familyRelations[i]["uri"]) {
					$("#roles").html(detail.familyRelations[i]["roles"]);
					return false;
				}
			});
		}
		if (detail.places != null && detail.places.length > 0) {
			$("#label").html(detail.places[0]['label']);
			$("#label").attr(
					"href",
					ctx + "/service/place/list#place="
							+ detail.places[0]['place']);
		} else {
			$("#label").html("");
		}
		if (detail.instances != null && detail.instances.length > 0) {
			$("#temporal").html(detail.instances[0]["temporal"]);
			$("#edition").html(detail.instances[0]["edition"]);
			$("#extent").html(detail.instances[0]["extent"]);
		} else {
			$("#temporal").html("");
			$("#edition").html("");
			$("#extent").html("");
		}
		if (detail.uri != null && detail.uri != "") {
			$("#heldBy").attr("href",
					ctx + "/service/work/list#uri=" + detail.uri);
		} else {
			$("#heldBy").attr("href", "javaScript:;");
		}
	}
	//移除链接
	if($("#persons>li").length<=1){
		$("#persons>li>a").css('text-decoration','none');
		$("#persons>li>a").css('cursor','default');
		
	}
	if($("#works>li").length<=1){
		$("#works>li>a").css('text-decoration','none');
		$("#works>li>a").css('cursor','default');
		
	}
});

function onScroll() {

	var index = $('.initials li').text().indexOf($('#surname p:last').html());
	if (index == 22) {
		getScroll = true;
		return;
	}
	var firstChar = $('.initials li').get(index + 1).innerHTML;
	$.post(ctx + "/home/doInitial", {
		firstChar : firstChar,
		type : "next"
	}, function(result) {
		var html = "<br/><p id='" + firstChar + "'>" + firstChar + "</p>";
		html += "<div class='both'></div>";
		$.each(result.surname, function(i) {
			var surname = result.surname[i];
			if (i % 4 == 0) {
				html += "<div class='row-fluid'>";
			}
			html += bindSurname(surname);
			if (i != 0 && (i + 1) % 4 == 0 && i != result.surname.length - 1) {
				html += "</div><br/>";
			}
		});
		$("#surname .mCSB_container").append(html);
		// $("#surname").mCustomScrollbar("update");
		getScroll = true;
	});
}

function bindSurname(surname) {
    //如果是0，则不显示。
    if(surname.originally=='0'){
    	surname.originally='';
    }
    if(surname.genealogyCnt=='0'){
    	surname.genealogyCnt='';
    }
    if(surname.celebrityCnt=='0'){
    	surname.celebrityCnt='';
    }
	var html = "<div class='span3'>";
	html += "<table cellpadding='1' cellspacing='1' style='color:"
			+ surname.randColor + ";'>";
	html += "<tr>";
	html += "<td colspan='2' align='center' valign='top' height='55'>";
	html += "<div class='rotate' style='width:30px; font-size: 24px;line-height: 24px;'>"
			+ surname.originally + "</div>";
	html += "</td>";
	html += "<td rowspan='3' align='center' valign='top' class='leftLine'>";
	html += "<div style='display: block; width: 14px; float: left;'>";
	if (surname.pinyin.indexOf('，') < 0) {
		html += "<div class='rotate'>" + surname.pinyin + "</div>";
	} else {
		html += "<div class='rotate'>"
				+ surname.pinyin.substring(0, surname.pinyin.indexOf('，'))
				+ "</div>";
	}
	html += "</div>";
	html += "</td>";
	html += "<td rowspan='2' align='center' valign='top' class='familyName'";
	html += "style='font-size: x-large; width: 14px'>" + surname.familyNameS
			+ "</td>";
	html += "</tr>";
	html += "<tr style='font-size:small;line-height:14px;'>";
	html += "<td valign='middle' width='14'>家谱文献</td>";
	html += "<td valign='middle' width='14'>先祖名人</td>";
	html += "</tr>";
	html += "<tr style='font-size:small'>";
	html += "<td>";
	html += "<div style='display: block; width: 14px; float: left;'>";
	html += "<div class='rotate'>" + surname.genealogyCnt + "</div>";
	html += "</div>";
	html += "</td>";
	html += "<td>";
	html += "<div style='display: block; width: 14px; float: left;'>";
	html += "<div class='rotate'>" + surname.celebrityCnt + "</div>";
	html += "</div>";
	html += "</td>";
	html += "</tr>";
	html += "</table>";
	html += "</div>";
	return html;
}
