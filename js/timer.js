$(function() {
    function format(n) {
        return n < 10 ? "0" + n : n; 
    }

    setInterval(function() {
        var h = format(new Date().getHours());
        var m = format(new Date().getMinutes());
        var s = format(new Date().getSeconds());
        $("#timer").text(h + ":" + m + ":" + s);
    }, 1000);
});