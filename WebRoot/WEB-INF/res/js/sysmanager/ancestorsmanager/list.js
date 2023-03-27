$(function(){
	$("#wikiword .btn:first").addClass("btn-success");
    var pager = new Pager($("#listForm"));
    var dataTypeId = $("#hidDataTypeId").val();
    var loadPersonList = function(){
        showLoading();
        var firstChar = $.trim($("#wikiword .btn-success").text());
        if(firstChar=='全'){firstChar=''};
        $("#firstChar").val(firstChar);
        $.post(ctx + "/personManager/getPersonList", $("#listForm").serialize(), function(result){
            $("#pager").html(pager.paint(result.pager));
            $(".data_list").hide().html(juicer(getTemplate("sysmanagerPersons"), result)).fadeIn("slow");
            hideLoading();
        });
    };
    $("#name").change(function(){
        $(":hidden[name=uri]").val("");
    });
    $("#listForm").submit(function(){
        loadPersonList();
        return false;
    });
    $("#btn_query,#inference+ins:first").click(function(){
        loadPersonList();
    });
    $("#wikiword .btn").click(function(){
        var flag = $(this).hasClass("btn-success");
        if(flag){
            $(this).removeClass("btn-success");
            $("#wikiword .btn:first").addClass("btn-success");
        }
        if( !flag ){
        	$("#wikiword .btn").removeClass("btn-success");
            $(this).addClass("btn-success");
        }
        loadPersonList();
    });
    $(document.body).on("click", "a[tag=place]", function(){
        location.href = ctx + "/service/place/list#place=" + $(this).attr("href");
        return false;
    });
    $(".data_list").on("click", "a[view]", function(){
        showLoading();
        var uri = $(this).attr("href");
        var view = $(this).attr("view");
        $.get(ctx + "/service/person/get", {uri : uri, inference : $("#inference").is(":checked"), view : view}, function(data){
            $("#rdf_panel table:last").remove();
            var html = "";
            if(view == 'R'){
                html = (juicer(getTemplate("rdfs"), data));
            } else {
                html = juicer(getTemplate("person"), data) + juicer(getTemplate("works"), data);
            }
            $("#personModal .modal-body").html(html);
            initDefaultPhoto($("#personModal .modal-body"));
            $("#personModal .modal-header .modal-title").text(view == 'R' ? 'RDF' : 'View');
            hideLoading();
            $("#personModal").modal("show");
        });
        return false;
    });
    var initQuery = function(){
        PARAM_MGR.loadParams();
        var hashQuery = false;
        var params = ["char","familyName","time","place", "name", "uri"];
        $.each(params, function(i){
            var param = params[i];
            var value = PARAM_MGR.getParam(param);
            if(value){
                hashQuery = true;
                if(param == "char"){
                    $("#wikiword span").removeClass("btn-success").each(function(){
                        if($(this).text() == value){$(this).addClass("btn-success");return false;}
                    });
                } else {
                    $("input[name=" + param + "]").val(value);
                }
            }
        });
      //  hashQuery ? loadPersonList() : $("#wikiword .btn:first").click();
    if(hashQuery ) {$("#wikiword .btn:first").click();}
    };
    initQuery();
});
$(":checkbox,:radio").iCheck({
    checkboxClass: 'icheckbox_square-blue',
    radioClass: 'iradio_square-blue',
    increaseArea: '20%'
});