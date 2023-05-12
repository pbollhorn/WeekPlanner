<%@page import="weekplanner.Controller"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset='UTF-8'>
<% String day = request.getParameter("day"); %>
<title><%=day%></title>
</head>
<body>
	<h1><%=day%></h1>
	
	<form method='post' action='Controller'>
	
		<input type='hidden' name='submitDay' value='<%=day%>'>
	
		<input type='submit' name='save' value='Save and go back to Front Page'><br><br>
	
		<%
			for(int task=0; task<Controller.weekPlan.get(Controller.dayStr2Int(day)).size(); task++)
			{
				out.println("<input type='text' name='task"+task+"' value='"+Controller.weekPlan.get(Controller.dayStr2Int(day)).get(task)+"'><br>");
			}
		%>
	
		<input type='submit' name='add' value='Save and Add Task'>
	
	</form>
</body>
</html>