var rest = {
    request: function(evt) {
        var accept, contentType;
        var form = $(evt.target);
        evt.preventDefault();
        evt.stopPropagation();
        form.find(".output").css({"height" : "auto"}).text("").removeClass("filled");
        var requestConfig = {
            type: form.attr("method"),
            url: "http://localhost:9998" + form.attr("action")
        };
        if (form.attr("data-auth") !== "false") {
            requestConfig.headers = {"Authorization" : "nosecurity erlend" };
        }
        if (contentType = form.attr("enctype")) {
            requestConfig.contentType = contentType;
        }
        if (accept = form.attr("data-accept")) {
            requestConfig.dataType = accept;
        }

        if (requestConfig.type == "POST") {
            if (form.find(".input").text()) {
                requestConfig.data = form.find(".input").text();
            }
        }

        $.ajax(requestConfig).success(function(data) {
            if (requestConfig.dataType == "json") {
                data = JSON.stringify(data, null, 2);
            }
            var pp = $(".prettyprint").removeClass("prettyprint");
            form.find(".output").addClass("prettyprint").text(data);
            prettyPrint();
            pp.addClass("prettyprint");
            if (form.attr("data-copy")) {
                $(form.attr("data-copy")).text(data);
            }
            var height = form.find(".output").outerHeight();
            form.find(".output").css({"height": "0"}).addClass("filled");
            form.find(".output").css({"height": height});
        }).error(function(jqXHR, textStatus) {
            form.find(".output").text(jqXHR.status + " " +  jqXHR.statusText);
        });

    },
    load: function(url, callback) {
        $.ajax({
            url      : "http://localhost:9998" + url,
            headers  :  {"Authorization" : "nosecurity erlend" },
            dataType : "json"
        }).done(callback);
    }
};

$(function() {
    setTimeout(function() {
        $("form.rest").on('submit', rest.request);
        $("form.rest").each(function(i, elm) {
            var form = $(elm);
            $("<div>").addClass("bar")
                .append($("<span>").text(form.attr("method").toUpperCase()).addClass("method"))
                .append($("<span>").text(form.attr("action")).addClass("uri"))
                .append($("<button>").text("Send"))
                .prependTo(form);
        });
        $.ajax({
                url      : "http://localhost:9998/rest/comments/",
                headers  :  {"Authorization" : "nosecurity erlend" }
            }).error(function() {
                $("<div>").html("Test system not running").addClass("errorMessage").appendTo(document.body);
        });
    });
});