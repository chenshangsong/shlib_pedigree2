$(function(){
    $("#dynastyTable").on("click", "a[data]", function(){
        location.href = ctx + "/service/person/list#time=" + $(this).attr("data");
    })
});