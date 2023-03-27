var webName = "gbcard";
var sub_Function;

Date.prototype.format = function(format) // author: meizz
{
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
	// millisecond
	};
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
	return format;
};
// 计算begin和end两个"yyyy-MM-dd"格式时间相隔多少天
function getDiffDays(begin, end) {
	var ab = begin.split("-");
	var ae = end.split("-");
	var db = new Date();
	db.setUTCFullYear(ab[0], ab[1] - 1, ab[2]);
	db.setHours(0, 0, 0, 0);
	var de = new Date();
	de.setUTCFullYear(ae[0], ae[1] - 1, ae[2]);
	de.setHours(0, 0, 0, 0);
	var unixDb = db.getTime();
	var unixDe = de.getTime();
	var i = 0;
	for (var k = unixDb + 24 * 60 * 60 * 1000; k < unixDe;) {
		// console.log((new Date(parseInt(k))).format());
		k = k + 24 * 60 * 60 * 1000;
		i = i + 1;
	}
	return i + 1;
}

function resetBtnByRole() {
	if (sub_Function) {
		if (sub_Function.length > 0) {
			$(".easyui-linkbutton")
					.each(
							function() {
								var btnId = $(this).attr("id");

								var isDisabled = true;
								for (node in sub_Function) {
									// console.log(sub_Function[node].function_id+"
									// "+btnId);
									if (sub_Function[node].function_id == btnId) {
										isDisabled = false;
										break;
									}
								}
								// 只对画面disabled设置为false的按钮并且权限为可用的
								if ($('#' + btnId).linkbutton('options').disabled == false
										&& isDisabled == true) {
									$('#' + btnId).linkbutton({
										disabled : isDisabled
									});
								}
							});
		} else {
			$(".easyui-linkbutton").each(function() {
				var btnId = $(this).attr("id");
				$('#' + btnId).linkbutton({
					disabled : true
				});
			})
		}
	}
}

function readonly(id, readonly) {

	if (readonly) {
		if ($(id).hasClass("easyui-numberspinner")) {
			$(id).addClass("inputReadonly");
			$(id).attr("readonly", "readonly");
			$(id).numberspinner('disable');
			$(id).attr("tabindex", "-1");
		} else if ($(id).hasClass("easyui-searchbox")) {
			$(id).searchbox('textbox').attr("readonly", "readonly");
			$(id).searchbox('textbox').addClass("inputReadonly");
			$(id).searchbox('textbox').attr("tabindex", "-1");
		} else if ($(id).hasClass("easyui-datebox")) {
			$(id).datebox('textbox').attr("readonly", "readonly");
			$(id).datebox('textbox').addClass("inputReadonly");
			$(id).datebox('textbox').attr("tabindex", "-1");
			$(id).datebox('disable');
		} else if ($(id).hasClass("easyui-combobox")) {
			$(id).combobox('textbox').attr("readonly", "readonly");
			$(id).combobox('textbox').addClass("inputReadonly");
			$(id).combobox('textbox').attr("tabindex", "-1");
			$(id).combobox('disable');
		} else {
			$(id).attr("readonly", "readonly");
			$(id).addClass("inputReadonly");
			$(id).attr("tabindex", "-1");
		}
	} else {
		if ($(id).hasClass("easyui-numberspinner")) {
			$(id).numberspinner('enable');
			$(id).removeAttr("readonly");
			$(id).removeAttr("tabindex");
			$(id).removeClass("inputReadonly");
		} else if ($(id).hasClass("easyui-searchbox")) {
			$(id).searchbox('textbox').removeAttr("readonly");
			$(id).searchbox('textbox').removeClass("inputReadonly");
			$(id).searchbox('textbox').removeAttr("tabindex");
		} else if ($(id).hasClass("easyui-datebox")) {
			$(id).datebox('textbox').removeAttr("readonly");
			$(id).datebox('textbox').removeClass("inputReadonly");
			$(id).datebox('enable');
			$(id).datebox('textbox').removeAttr("tabindex");
		} else if ($(id).hasClass("easyui-combobox")) {
			$(id).combobox('textbox').removeAttr("readonly");
			$(id).combobox('textbox').removeClass("inputReadonly");
			$(id).combobox('textbox').removeAttr("tabindex");
			$(id).combobox('enable');
		} else {
			$(id).removeAttr("readonly");
			$(id).removeClass("inputReadonly");
			$(id).removeAttr("tabindex");
		}
	}

}

function reInit() {
	parent.window.location = "/" + webName + "/index/home.do?forceLogin=1";
}

function reInit2() {
	parent.window.location = "/" + webName + "/index/reInit.do";
}

function openWin(url, target, newHeight, unloadFunc) {
	var width = 1280;
	var height;
	if (newHeight) {
		height = newHeight;
	} else {
		height = 540;
	}
	var left = Math.floor((screen.availWidth - width) / 2);
	var top = Math.floor((screen.availHeight - height - 60) / 2);
	var windowFeatures = "width=" + width + ",height=" + height
			+ ",menubar=no,toolbar=no,scrollbars=no,resizable=no," + "left="
			+ left + ",top=" + top + ",screenX=" + left + ",screenY=" + top;
	if (!target) {
		target = "_blank";
	}
	var win = window.open(url, target, windowFeatures);
	if (unloadFunc) {
		$(win).unload(unloadFunc);
	}

}

var _menuAction = "";

/**
 * 用于判断是否有请求正在处理，为了防止多重提交
 */
var _isRequesting = false;

/**
 * 下载文件
 * 
 * @param newWindow
 *            true:已弹出窗口方式下载，false：iframe方式
 */
function downloadFile(urlPath, newWindow, form) {
	if (newWindow) {
		// 弹出画面下载方式
		var param = "toolbar=yes,location=yes,menubar=yes, scrollbars=yes,resizable=yes,dependent=yes";
		if (form) {
			$(form).target = '_blank';
			$(form).attr('action', urlPath);
			$(form).submit();
		} else {
			window.open(urlPath, 'newwin', param);
		}
	} else {
		// 不弹出画面直接下载方式
		$("#downloadFrame").attr("src", urlPath);
	}

}

/**
 * 打开新画面
 * 
 * @param action
 *            新画面
 */
function returnToPage(action) {
	$("#mainframe").attr('src', action);
	_menuAction = action;
}

/**
 * 打开新画面
 * 
 * @param action
 *            新画面
 */
function openPage(action) {
	// if(_menuAction != action){
	$("#mainframe").attr('src', action);
	// }
	_menuAction = action;
	$("ul.sf-menu").hideSuperfishUl();
}

/**
 * 普通json方式，ajax请求
 */
function requestJson(postUri, param, successFun, errorFun, completeFun) {
	// 判断当前有没有请求 有则结束方法
	if (_isRequesting == true) {
		return;
	}
	// 设置请求状态
	_isRequesting = true;
	if (param) {
		for (node in param) {
			if (node.indexOf('__') == 0) {
				delete param[node];
			} else {
				// if (param.hasOwnProperty(node)) {
				// param[node]= $.trim(param[node]);
				// }
			}
		}
	}
	$
			.ajax({
				async : false,// ajax提交生成文件后返回文件名到前台，然后再请求iframe下载时
				// 会IE iframe download causing security warning，并且无法下载，加了此参数可以避免
				type : "POST",
				contentType : 'application/json; charset=UTF-8',
				url : postUri,
				data : JSON.stringify(param),
				dataType : "json",
				success : function(data, textStatus) {// 服务器返回数据，返回状态
					// console.log(data);
					// console.log(textStatus);
					// 设置请求结束
					_isRequesting = false;

					// 清空所有错误信息
					var errEle = $('body').find('.ui-state-error');
					errEle.each(function(i) {
						$(this).removeClass('ui-state-error');
						$(this).attr("title", "");
					});

					if (data) {
						// 如果返回信息中包含"UpdateExclusive"，
						// 表明是MybatisPlugin中抛出的ExclusiveException
						if (data.length == 1 && data[0].value) {
							if (data[0].value == "EMSG9002") {
								showConfirmDialog(EMSG9002, reInit2);
								return;
							} else if (data[0].value == "EMSG9004") {
								showConfirmDialog(EMSG9004, reInit);
								return;
							} else if (data[0].value == "EMSG9003") {
								showConfirmDialog(EMSG9003, reInit2);
								return;
							} else {
								if (data[0].value.startWith("EMSG")
										|| data[0].value.startWith("IMSG")) {
									$.messager.show({
										title : '提示信息',
										msg : data[0].label,
										showType : 'show'
									});
									// showInfoDialog(data[0].value + ":" +
									// data[0].label);
									return;
								}
							}
						}

						// 错误存在判断
						var hasError = false;
						var msgAll = "";
						// 设置错误信息
						$
								.each(
										data,
										function() {
											// 不是错误信息直接退出循环
											if (this.msgId == undefined) {
												return false;// break
											}

											control = null;
											if (this.msgId == '') {
												if (this.msgInfo) {
													msgAll = msgAll + "</br>"
															+ this.msgInfo;
													hasError = true;
													return false;// break
												}
											}
											if (this.msgId == 'list') {
												// 设置明细错误对象
												control = $('#'
														+ this.msgTargetIndex
														+ '_' + this.msgTarget);

											} else {
												// 设置主表错误信息
												control = $(':input[id='
														+ this.msgId + ']');// chen2013-name-id
												if (control.is(":hidden")) {
													var controlParent = control
															.parent();
													// 选择jqueryEasyui数字选择控件和下拉列表控件的输入框
													control = controlParent
															.find(".combo-text");
													if (control.length == 0) {
														// 选择jqueryEasyui查询控件的输入框
														control = controlParent
																.find(".searchbox-text");
													}

													if (control.length == 0) {
														// 选择jqueryEasyui查询控件的输入框
														control = controlParent
																.find(".validatebox-text");
													}
												}
											}
											if (control) {
												hasError = true;
												// 设置背景
												control
														.addClass('ui-state-error');
												// 设置错误信息
												control.attr("title",
														this.msgInfo);
												msgAll = msgAll + "</br>"
														+ this.msgInfo;
											}
										});

						if (hasError) {
							$.messager.show({
								title : '提示信息',
								msg : msgAll,
								showType : 'show'
							});
							return;
						}
					}

					// data could be xmlDoc, jsonObj, html, text, etc..
					if (successFun) {
						if ($.isFunction(successFun)) {
							successFun(data);
						}
					}
				},
				error : function(xmlHttpRequest, textStatus, errorThrown) {
					// console.log(xmlHttpRequest.status );
					// 设置请求结束
					_isRequesting = false;

					if (xmlHttpRequest.status == "403") {// session timeout
						parent.window.location = './../common/exception.do?code=EMSG9003';
					} else if (xmlHttpRequest.status == "408") {
						parent.window.location = './../common/exception.do?code=EMSG9003';
					} else {
						parent.window.location = './../common/exception.do?code=EMSG9000';
					}
				},
				complete : function(XMLHttpRequest, textStatus) {
					// 设置请求结束
					_isRequesting = false;
					if (completeFun) {
						completeFun(XMLHttpRequest, textStatus);
					}
				}
			});
}

// 鼠标右键屏蔽
$('body').bind("contextmenu", function() {
	self.event.returnValue = true;
	return true;
});

// 防止backspace
$(document).keydown(
		function(e) {
			var doPrevent;
			// for IE && Firefox
			var varkey = (e.keyCode) || (e.which) || (e.charCode);
			if (varkey == 8) {
				var d = e.srcElement || e.target;
				if (d.tagName.toUpperCase() == 'INPUT'
						|| d.tagName.toUpperCase() == 'TEXTAREA') {
					doPrevent = d.readOnly || d.disabled;
					// for button,radio and checkbox
					if (d.type.toUpperCase() == 'SUBMIT'
							|| d.type.toUpperCase() == 'RADIO'
							|| d.type.toUpperCase() == 'CHECKBOX'
							|| d.type.toUpperCase() == 'BUTTON') {
						doPrevent = true;
					}
				} else {
					doPrevent = true;
				}
			} else {
				doPrevent = false;
			}
			if (doPrevent)
				e.preventDefault();
		});

// 第一个控件获取焦点
$(
		'input[type=text]:not(.inputReadonly,.combo-text,.searchbox-text)\
					,textarea:not(.inputReadonly)\
					,input[type=password]:not(.inputReadonly)')
		.filter(function() {
			return $(this).css("display") != "none";
		}).first().focus();

function ajaxFileUpload(_url, _formName, successFunc, errorFunc) {
	if (_isRequesting) {
		showInfoDialog("处理中，请稍后...");
		return;
	}
	_isRequesting = true;
	var _formData = $(_formName).toJson();
	if (!_formData) {
		_formData = {};
	}

	$.ajaxFileUpload({
		url : _url,
		secureuri : false,
		fileElementId : 'uploadFile',
		dataType : 'json',
		formData : _formData,
		success : function(data, status) {
			if (successFunc) {
				successFunc(data, status);
			}
			_isRequesting = false;
		},
		error : function(data, status, e) {
			if (errorFunc) {
				errorFunc(data, status, e);
			}
			_isRequesting = false;
		}
	})
	return false;
}
/**
 * 验证是否为正数字
 * 
 * @param obj
 * @returns
 */
function isNaNChk(obj) {

	if (obj.length <= 0) {
		return false;
	}
	if (isNaN(obj)) {
		return false;
	}
	if (parseFloat(obj) <= 0) {
		return false;
	}

	return true;
}

/**
 * 类型转换
 * 
 * @param obj
 * @returns
 */
function parseToFloat(obj) {
	if (obj) {
		obj = (obj + '').replace(/,/g, '');
	}
	if (obj.length <= 0) {
		return 0;
	}
	if (isNaN(obj)) {
		return 0;
	}
	if (parseFloat(obj) < 0) {
		return 0;
	}
	return parseFloat(obj);
}
/**
 * 清空画面上的所有内容
 */
function clearAllValue(clearBoxId) {
	if (clearBoxId) {
		// 清空所有jqgrid
		$.each($(clearBoxId).find(".ui-jqgrid"), function() {
			var id = this.id.replace('gbox_', '');
			$('#' + id).jqGrid("clearGridData", true);
		});

		// 清空所有INPUT和TEXTAREA的值
		$(clearBoxId).find("input,textarea,select").val('');

		// 清空所有错误信息
		var errEle = $(clearBoxId).find('.ui-state-error');
		errEle.each(function(i) {
			$(this).removeClass('ui-state-error');
			$(this).attr("title", "");
		});
	} else {
		// 清空所有jqgrid
		$.each($(".ui-jqgrid"), function() {
			var id = this.id.replace('gbox_', '');
			$('#' + id).jqGrid("clearGridData", true);
		});

		// 清空所有INPUT和TEXTAREA的值
		$("input,textarea,select").val('');

		// 清空所有错误信息
		var errEle = $('body').find('.ui-state-error');
		errEle.each(function(i) {
			$(this).removeClass('ui-state-error');
			$(this).attr("title", "");
		});
	}
}

////////////////////////////////////消息提示//////////////////////////////////////
/**
 * 带确认框弹框
 * 
 * @param msg：messID
 * @param func：回调函数
 */
function showConfirmMsg(msg,func){
	layer.confirm(msg, {icon: 3}, function(index){
	    layer.close(index);
	    func();
	});
}
/**
 * 直接弹框：2秒自隐
 * 
 * @param msg
 * @param func
 */
function showMsg(msg){
	layer.msg(msg);
}
/**
 * 直接弹框：2秒后调用函数
 * 
 * @param msg
 * @param func
 */
function showMsgCallBack(msg,callbackFunc){
  layer.msg(msg, 1, 1);
  setTimeout(function () {
	  callbackFunc();
  }, 2000);
}
/**
 * 直接弹框：错误框：手动关闭
 * 
 * @param msg
 * @param func
 */
function showErrorMsg(msg){
  layer.alert(msg);
}

/**
 * 非法字符验证
 * @param contId
 * @returns {Boolean}
 */
function isUnlawfulStr(contId){
	var flg =true;
	//js验证 `~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？  
	  var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？  ]"); 
	// 清空所有jqgrid
		$.each($(contId), function(i) {
			 if($(this).val() != "" && $(this).val() != null){  
			      if(pattern.test($(this).val())){  
			    	  layer.alert('内容包含非法字符，请检查数据。');   
			    	//  $(this).val('');  
			    	  $(this).focus();  
			         flg=false;  
			         return flg;
			      }  
			  } 
		});
	  return flg;
	}

 
