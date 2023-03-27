var Pager = function(f, t){
    var form = f, template = t||'';
    form.on("click", "nav ul.pagination>li>a", function(){
        var page = $(this).attr("page"), pageth,
            currPageth = parseInt($("nav ul.pagination>li.active>a", form).attr("page")),
            pageCount = parseInt($("nav ul.pagination", form).attr("pagecount"));
        if($(this).parent().hasClass("disabled")) return;
        if("prev" == page){
            pageth = Math.max(currPageth - 1, 1);
        } else if("next" == page ){
            pageth = Math.min(currPageth + 1, pageCount);
        } else {
            pageth = Math.max(Math.min(parseInt(page), pageCount), 1);
        }
        var input = $(":hidden[name=pageth]", form);
        if( input.length == 0 ){
            input = $("<input type='hidden' name='pageth' />").appendTo(form);
        }
        input.val(pageth);
        if(pageth == currPageth){return;}
        form.submit();
    }).change(function(){$(this).find(":hidden[name=pageth]").val(1)});
    this.paint = function(pager){return juicer(getTemplate("page"), pager)}
    return this;
}