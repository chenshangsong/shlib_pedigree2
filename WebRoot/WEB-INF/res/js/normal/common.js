juicer.set({
    'tag::operationOpen': '{@',
    'tag::operationClose': '}',
    'tag::interpolateOpen': '@{',
    'tag::interpolateClose': '}',
    'tag::noneencodeOpen': '@@{',
    'tag::noneencodeClose': '}',
    'tag::commentOpen': '{#',
    'tag::commentClose': '}'
});

$.ajaxSetup({
    error : function(jqXHR, status, message){
        //Dialog.error("发生错误");
        hideLoading();
    }
})
var getTemplate=function(){
	var a={};
	return function(b){
		var c=a[b];
		return (c||$.ajax(ctx+"/res/tpl/"+b+".tpl?t=" + new Date().getTime(),{type:"get",async:!1,success:function(d){c=a[b]=d}}),c).replaceAll("\\$\\{ctx\\}", ctx)}}();


var showLoading = function(info){$("#modal-loading .modal-loading").center(true).find(".modal-info").text(info||"Loading...");$("#modal-loading").modal({show:true,backdrop:false});};
var hideLoading = function(){$("#modal-loading").modal("hide");};
var initDefaultPhoto = function(obj){(obj?$("img.photo",obj):$("img.photo")).error(function(){ this.src = ctx + "/res/anonymous.png";});};
initDefaultPhoto();
(function($){
    $.fn.center = function(disabledScroll){
        var top = ($(window).height() - this.height())/2;
        var left = ($(window).width() - this.width())/2;
        var scrollTop = $(document).scrollTop();
        var scrollLeft = $(document).scrollLeft();
        if(disabledScroll){scrollTop = scrollLeft = 0;}
        return this.css( { position : 'absolute', 'top' : top + scrollTop, left : left + scrollLeft } ).show();
    }
    $.fn.animateCss = function(c,callback){
        this.addClass(c + " animated").one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){
            $(this).removeClass(c + " animated");
            if(callback) callback.apply(this);
        });
        return this;
    }
})(jQuery)
/** Map定义 */
function Map(){var struct=function(key,value){this.key=key;this.value=value};var put=function(key,value){for(var i=0;i<this.arr.length;i++){if(this.arr[i].key===key){this.arr[i].value=value;return}}this.arr[this.arr.length]=new struct(key,value)};var get=function(key){for(var i=0;i<this.arr.length;i++){if(this.arr[i].key===key){return this.arr[i].value}}return null};var remove=function(key){var v;for(var i=0;i<this.arr.length;i++){v=this.arr.pop();if(v.key===key){continue}this.arr.unshift(v)}};var size=function(){return this.arr.length};var isEmpty=function(){return this.arr.length<=0};this.arr=new Array();this.get=get;this.put=put;this.remove=remove;this.size=size;this.isEmpty=isEmpty};
/** 参数管理 */
var PARAM_MGR={__PARAMS:new Map(),loadParams:function(){var data=new String(document.location.hash);data=unescape(data.replace("#",""));var datas=data.split("&");for(var i in datas){try{var temp=datas[i];var temps=temp.split("=");if(temps.length==2){this.__PARAMS.put($.trim(temps[0]),$.trim(temps[1]))}}catch(e){continue}}},toParams:function(){var temp="";for(var i in this.__PARAMS.arr){var obj=this.__PARAMS.arr[i];temp+="&"+obj.key+"="+obj.value}if(temp.length>0){temp=temp.slice(1)}return escape(temp)},setParam:function(key,value){var type=$.type(value);if(type=="object"){var temp="{";for(var i in value){temp+=i+":"+value[i]+","}if(temp.length>1){temp=temp.substring(0,temp.length-1)}temp+="}";this.__PARAMS.put(key,temp)}else{if(type=="array"){var temp="{";for(var i in value){if(value[i].name&&value[i].value!="undefine"){temp+=value[i].name+":'"+value[i].value+"',"}}if(temp.length>1){temp=temp.substring(0,temp.length-1)}temp+="}";this.__PARAMS.put(key,temp)}else{this.__PARAMS.put(key,value)}}},getParam:function(key){var value=this.__PARAMS.get(key);if(value=="undefine"||$.trim(value).length==0){return null}else{return $.trim(value)}}};
$.startWith = function(str1, str2){if(!(str2 && str1)){return false;} return new RegExp("^"+str2).test(str1);	}
$.endWith = function(str1, str2){if(!(str2 && str1)){return false;} return new RegExp(str2 + "$").test(str1);	}
String.prototype.replaceAll=function(reallyDo,replaceWith,ignoreCase){if(!RegExp.prototype.isPrototypeOf(reallyDo)){return this.replace(new RegExp(reallyDo,(ignoreCase?"gi":"g")),replaceWith)}else{return this.replace(reallyDo,replaceWith)}};
$.loader=(function(){var loadOne=function(url,type){var dtd=$.Deferred();var onload=function(){dtd.resolve()};if("script"==type){var jsNode=document.createElement("script");jsNode.type="text/javascript";$(jsNode).load(onload).bind("readystatechange",function(){if(jsNode.readyState=="loaded"){onload()}});document.getElementsByTagName("head")[0].appendChild(jsNode);jsNode.src=url}else{if("css"==type){var cssNode=document.createElement("link");cssNode.rel="stylesheet";cssNode.type="text/css";document.getElementsByTagName("head")[0].appendChild(cssNode);cssNode.href=url}}return dtd.promise()};var loadScript=function(urls){if(!$.isArray(urls)){return load([urls])}var ret=[];for(var i=0;i<urls.length;i++){ret[i]=loadOne(urls[i],"script")}return $.when.apply($,ret)};var loadCss=function(urls){if(!$.isArray(urls)){return load([urls])}var ret=[];for(var i=0;i<urls.length;i++){ret[i]=loadOne(urls[i],"css")}return $.when.apply($,ret)};return{loadScript:loadScript,loadCss:loadCss}})();