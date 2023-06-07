<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="weekplanner.Controller"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="robots" content="noindex">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="style.css">
<title>Week Planner</title>

<script src="script.js" defer></script>
</head>
<body>


	<header>
		<h3>Week Planner</h3>
		<button id="save-button">Save</button>
	</header>


	<main>




			<%
			// Loop through all the lists
			for(int listNumber=0; listNumber<Controller.taskLists.size(); listNumber++)
			{
				// Write the name of the list
				String listName = Controller.taskLists.get(listNumber).name;
				out.println("<h3>" + listName + "</h3>");
		
 				// Loop through all tasks for the day
 				for(int taskNumber=0; taskNumber < Controller.taskLists.get(listNumber).tasks.size(); taskNumber++)
 				{	
 					String taskDescription = Controller.taskLists.get(listNumber).tasks.get(taskNumber);
					String id = "task_" + taskNumber + "_list_" + listNumber; 
 					
 					// Make input box for each individual task
 					out.println("<input type='text' id='"+id+"' value='" + taskDescription + "'>");
 				}
			}
			%>


	</main>

	<footer>
		<nav>
			<ul>
				<li><a href="#">+</a></li>
				<li><a href="#">✘</a></li>
				<li><a href="#">↑</a></li>
				<li><a href="#">↓</a></li>
				<li><a href="#">←</a></li>
				<li><a href="#">✔</a></li>
			</ul>
		</nav>
	</footer>

</body>
</html>