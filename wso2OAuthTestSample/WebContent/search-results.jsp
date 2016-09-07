<%@page import="org.json.simple.parser.JSONParser"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<!DOCTYPE html>
<html><head>
<title>WSO2 OAuth2.0 Playground</title>
<meta charset="UTF-8">
<meta name="description" content="" />
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
<!--[if lt IE 9]><script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script><![endif]-->
<script type="text/javascript" src="js/prettify.js"></script>                                   <!-- PRETTIFY -->
<script type="text/javascript" src="js/kickstart.js"></script>                                  <!-- KICKSTART -->
<link rel="stylesheet" type="text/css" href="css/kickstart.css" media="all" />                  <!-- KICKSTART -->
<link rel="stylesheet" type="text/css" href="style.css" media="all" />                          <!-- CUSTOM STYLES -->

</head>

<body>

<a id="top-of-page"></a>

<div id="wrap" class="clearfix">
	<ul class="menu">
	<li class="current"><a href="index.jsp">Home</a></li>
	
	</ul>

<br/>
	<h3 align="center">WSO2 OAuth2 Playground - YouTube Search Results : WSO2</h3>

<%

String result = (String) session.getAttribute("result");

if(result != null){
        %>
	<p><pre><%=result%></pre></p>
		<%
} else {
	 out.print("No data received ");
}

%>

</div>

</body>
</html>
