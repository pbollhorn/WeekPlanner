var idLastClicked = -1;
var mainElement = document.querySelector("main");
var myGreen = "rgb(144, 238, 144)";  // Not a web safe color
var myYellow = "rgb(255,255,153)";   // I don't know if it is web safe



function deleteTask() {
	document.getElementById(idLastClicked).remove();
}


function markTaskDone() {
	document.getElementById(idLastClicked).style.backgroundColor = myGreen;
}


function clickTask(element) {

	// If task was clicked as the previous thing, then put focus on its inputElement
	if (element.id === idLastClicked) {
		const inputElement = element.querySelector("input");
		const length = inputElement.value.length;
		inputElement.setSelectionRange(length,length);
		inputElement.focus();
	}

	// Set this task to be the thing last clicked
	idLastClicked = element.id;

}







function buildView() {

	let task_id = 0;

	//const mainElement = document.querySelector("main");

	// Loop through lists in plan
	for (const list of plan.lists) {

		// Write name of list in h1 element
		const h1Element = document.createElement("h1");
		h1Element.innerText = list.name;
		mainElement.appendChild(h1Element);

		// Loop through tasks in list
		for (const task of list.tasks) {

			// Create the special task div
			const divElement = document.createElement("div");
			divElement.className = "task";
			divElement.id = "task_" + task_id;
			task_id++;
			divElement.onclick = function fun() { clickTask(this) };

			divElement.innerHTML = "<div class='background'><input type='text' value='" + task.description + "'></div><div class='transparent'></div>";

			mainElement.appendChild(divElement);
			
			

			// Set background color of input element depending on if the task is done or not
			if (task.done === true) {
				divElement.querySelector("div").style.backgroundColor = myGreen;
			}
			else {
				divElement.querySelector("div").style.backgroundColor = myYellow;
			}

			

		}

	}

}


buildView();