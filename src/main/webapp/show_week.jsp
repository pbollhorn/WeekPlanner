<%@page import="weekplanner.Controller"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Your week plan</title>
</head>
<body>
	<h1>Your week plan</h1>
	<a href="index.jsp">Back to front page</a>
		
	<%
	for(int day=0; day<7; day++)
	{
		out.println("<h3>"+Controller.dayInt2Str(day)+"</h3>");
		out.println("<ul>");			
		for(int task=0; task<Controller.weekPlan.get(day).size(); task++)
		{
			out.println("<li>" + Controller.weekPlan.get(day).get(task) + "</li>");
			
		}
		out.println("</ul>");
	}
	%>

</body>
</html>