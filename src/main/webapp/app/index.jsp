<html>
<head>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<script>
$(function() {
	$.getJSON("/rest/comments/", function(data) {
		$.each(data, function(i, elm) {
			var div = $("<div>").addClass("comment").appendTo("body");
			$("<div>").addClass("date").text("Date: " + new Date(elm.date)).appendTo(div);
			$("<div>").addClass("username").text("Username: " + elm.username).appendTo(div);
			$("<div>").addClass("title").text("Title: " + elm.title).appendTo(div);
			$("<div>").addClass("body").text("Body: " + elm.body).appendTo(div);
		});	
	});
});

</script>
<style>
.comment {
	margin-bottom: 10px;
}

</style>
</head>
<body>
<h1>Comments:</h1>





</body>
</html>