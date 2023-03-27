$(function(){
    var personPager = new Pager($("#personForm")), workPager = new Pager($("#workForm"));
    var count = 0, dynasties = null, dynastyTpl = $("#dynastyTpl").html();
    var getDynasty = function(uri){
        var result = null;
        $.each(dynasties, function(i){
           if(dynasties[i].uri == uri){
               result = dynasties[i];
               return false;
           }
        });
        return result;
    }
    showLoading();
    $.get(ctx + "/service/dynasty/dynasties", function(datas){
        count += 2 * datas.length;
        dynasties = datas;
        $.each(datas, function(i){
            var dynasty = datas[i];
            var uri = dynasty.uri;
            $.ajax({
                url: ctx + "/service/dynasty/persons",
                data: {uri:uri,pageSize:5},
                async: false,
                success: function(result){
                    var persons = result.persons, container;
                    if(persons&&persons.length > 0){
                        container = $(juicer(dynastyTpl, {dynasty:dynasty})),container.appendTo($("#dynasty_person"));
                        container.find("[tag=dynasty]").attr("tag", "person_dynasty")
                        container = container.find(".panel-body");
                        $.each(persons, function(i){
                            container.append("<a href='" + persons[i]["person"] + "' tag='person'>" + persons[i]["name"] + "</a><br>");
                        });
                        if(persons.length >= 5 ) {
                            container.append("<a class='pull-right' href='javascript:void(0);' style='margin-top:-25px;' tag='person_more'><i>More...</i></a>");
                        }
                    }
                    if(--count == 0 ){hideLoading()}
                }
            });
            $.ajax({
                url: ctx + "/service/dynasty/works",
                data: {uri:uri,pageSize:5},
                async: false,
                success: function(result){
                    var works = result.works,container;
                    if(works&&works.length>0){
                        container = $(juicer(dynastyTpl, {dynasty:dynasty})),container.appendTo($("#dynasty_work"));
                        container.find("[tag=dynasty]").attr("tag", "work_dynasty")
                        container = container.find(".panel-body");
                        $.each(works, function(i){
                            container.append("<a href='" + works[i]["work"] + "' tag='work'>" + works[i]["title"] + "</a><br>");
                        });
                        if(works.length >= 5){
                            container.append("<a class='pull-right' style='margin-top:-25px;' href='javascript:void(0);' tag='work_more'><i>More...</i></a>");
                        }
                    }
                    if(--count == 0){hideLoading()}
                }
            })
        });
        
    });
    var personTpl = $("#personTpl").html(), workTpl = $("#workTpl").html();
    $("#personForm").submit(function(){
        showLoading();
        $.get(ctx + "/service/dynasty/persons", $("#personForm").serialize(), function(result){
            $("#personForm [tag=data]").html(juicer(personTpl, {persons: result.persons})).fadeIn("fast");
            $("#personDiv [tag=pager]").html(personPager.paint(result.pager));
            hideLoading();
        });
        return false;
    });
    $("#workForm").submit(function(){
        showLoading();
        $.get(ctx + "/service/dynasty/works", $("#workForm").serialize(), function(result){
            $("#workForm [tag=data]").html(juicer(workTpl, {works: result.works})).fadeIn("fast");
            $("#workDiv [tag=pager]").html(workPager.paint(result.pager));
            hideLoading();
        });
        return false;
    });
    $(document.body).on("click", "a[tag]", function(){
        var tag = $(this).attr("tag");
        if("person" == tag){
            location.href = ctx + "/service/person/list#uri=" + $(this).attr("href") + "&name=" + $(this).text();
        } else if("work" == tag){
            location.href = ctx + "/service/work/list#title=" + $(this).text();
        } else {
            var uri = $(this).parentsUntil("#listView", ".row").attr("uri");
            var dynasty = getDynasty(uri);
            if (!dynasty) {
                Dialog.error("朝代信息查询失败");
                return false;
            }
            ;
            $("#btn_back").show();
            var time = "公元" + dynasty.begin.replace("-","前") + "年 ~ 公元" + dynasty.end.replace("-","前") + "年";
            $("#top span").html(dynasty.name + "&nbsp;&nbsp;<small><i>(" + time + ")</i></small>");
            $("#listView").hide();
            $("#dynastyView").fadeIn("slow");
            $("#personForm :hidden[name=pageth]").val(1);
            $("#workForm :hidden[name=pageth]").val(1);
            $("#personForm :hidden[name=uri]").val(uri);
            $("#workForm :hidden[name=uri]").val(uri);
            $("#personForm").submit();
            $("#workForm").submit();
            var tag = $(this).attr("tag");
            if ($.startWith(tag, "work")) {
                $("a[href='#tab_1']").tab("show");
            } else {
                $("a[href='#tab_0']").tab("show");
            }
        }
        return false;
    })
    $("#btn_back").click(function(){
        $(this).hide();
        $("#top span").text("上海图书馆家谱朝代分布表");
        $("#dynastyView").hide();
        $("#listView").fadeIn("slow");
    });
});