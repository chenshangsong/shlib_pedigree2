$(function () {

    $(".introduced").hide()

    $(".celebrities a").click(function () {
        $(".introduced").fadeIn("slow");
    });

    $(".surname").click(function () {
        window.location = "2.html";
    });
})