<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="weekplanner.Debugger"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="robots" content="noindex">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="css/style.css">
<title>Week Planner</title>
</head>
<body>

	<h1>Week Planner</h1>
	<h2>Login</h2>

	<%
	// Get username from request, or set it to "" if it does not exists
	String username = request.getParameter("username");
	if (username == null)
		username = "";

	// Get password from request, or set it to "" if it does not exists
	String password = request.getParameter("password");
	if (password == null)
		password = "";
	%>


	<form action="controller" method="post">

		<input type="hidden" name="action" value="login"> <label> Username: <input type="text" name="username" value=<%=username%>>
		</label> <br> <br> <label> Password: <input type="password" name="password" value=<%=password%>>
		</label>

		<%
		String errorMessage = (String) request.getAttribute("errorMessage");
		if (errorMessage != null)
			out.println("<div style='color: red;'>" + errorMessage + "</div>");
		else
			out.println("<div style='color: white;'>-</div>");
		%>

		<br> <input type="submit" value="Login">

	</form>


</body>
</html>