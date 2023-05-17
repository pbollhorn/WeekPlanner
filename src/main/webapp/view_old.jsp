<%@page import="weekplanner.Controller"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Week Planner</title>
</head>
<body>

	<h1>Week Planner</h1>
		
	<%
	// Loop through the 7 days of the week
	for(int dayNumber=0; dayNumber<7; dayNumber++)
	{
		// Write the name of the day
		String dayName = Controller.dayInt2Str(dayNumber);
		out.println("<h3>" + dayName + "</h3>");
		
		// Loop through all tasks for the day
		for(int taskNumber=0; taskNumber < Controller.weekPlan.get(dayNumber).size(); taskNumber++)
		{	
			String taskDescription = Controller.weekPlan.get(dayNumber).get(taskNumber);
		
			// Make HTML form for each individual task with corresponding buttons
			out.println("<form action='Controller' method='post'>");
			out.println("<input type='hidden' name='dayName' value='" + dayName + "'>");
			out.println("<input type='hidden' name='taskNumber' value='" + taskNumber + "'>");
			out.println("<input type='text' name='taskDescription' value='" + taskDescription + "'>");
			out.println("<input type='submit' name='save' value='Save'>");
			out.println("<input type='submit' name='delete' value='Delete'>");
			out.println("<input type='submit' name='moveUp' value='Move Up'>");
			out.println("<input type='submit' name='moveDown' value='Move Down'><br>");
			out.println("</form>");
		}
		
		
		// Make HTML form with 'add task' button
		out.println("<form action='Controller' method='post'>");
		out.println("<input type='hidden' name='dayName' value='" + dayName + "'>");
		out.println("<input type='submit' name='add' value='Add'>");
		out.println("</form>");
	}
	%>

</body>
</html>