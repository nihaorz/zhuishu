/**
 * Created by Nihaorz on 2018/2/9.
 */
document.querySelector("body").onkeydown = function (e) {
    var eCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
    var href = null;
    if (eCode == 37) {
        // left arrow
        href = $("#j_chapterPrev").attr("href");
    } else if (eCode == 39) {
        // right arrow
        href = $("#j_chapterNext").attr("href");
    }
    if(href != null){
        window.location.href = href;
    }
}