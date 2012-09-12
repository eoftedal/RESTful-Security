$(function() {
    $(".css-steps").each(function(i, elm) {
        var steps = parseInt($(elm).attr("data-steps"), 10);
        var step = 0;
        $(elm).addClass("step-0").click(function(evt) {
            evt.preventDefault();
            evt.stopPropagation();
            step = (step + 1) % steps;
            if (step === 0) {
                for (var i = 0; i < steps; i++) {
                    $(elm).removeClass("step-" + i);
                }
            }
            $(elm).addClass("step-" + step);
            
        });
    });
});