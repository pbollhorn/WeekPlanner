<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="weekplanner.Controller"%>
<!DOCTYPE html>
<html lang="en">
<head>

<meta charset="UTF-8">
<meta name="robots" content="noindex">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="css/style.css">
<title>Week Planner</title>

<script src="js/manipulateDOM.js" defer></script>
<script src="js/createTrialData.js" defer></script>
<script src="js/script.js" defer></script>
</head>

<body>


	<header>
		<h3>Week Planner</h3>
		<button id="save-button">Save</button>
	</header>


	<main>
		
		<div class="task">
			<div class ="back_element"><input type="text"></div>
			<div class ="front_element">Element 1</div>
		</div>


	</main>

	<footer>
		<nav>
			<ul>
				<li><a href="#">+</a></li>
				<li><p onclick="deleteTask()">✘</p></li>
				<li><a href="#">↑</a></li>
				<li><a href="#">↓</a></li>
				<li><a href="#">←</a></li>
				<li><p onclick="markTaskDone()">✔</p></li>
			</ul>
		</nav>
	</footer>

</body>
</html>