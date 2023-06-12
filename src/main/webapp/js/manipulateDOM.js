
var idLastClicked = -1;
var mainElement = document.querySelector("main");
var myGreen = "rgb(144, 238, 144)";  // Not a web safe color
var myYellow = "rgb(255,255,153)";   // I don't know if it is web safe

function manipDOM() {



	const inputElement = document.createElement("input");
	inputElement.type = "text";


	mainElement.appendChild(inputElement);



}



function deleteTask() {
	document.getElementById(idLastClicked).remove();
}


function markTaskDone() {
	document.getElementById(idLastClicked).style.backgroundColor = myGreen;
}


function noticeMe() {
	idLastClicked = this.id;
}





function buildView() {

	let idCounter = 0;

	const mainElement = document.querySelector("main");

	// Loop through lists in plan
	for (const list of plan.lists) {

		// Write name of list in h3 element
		const h3Element = document.createElement("h3");
		h3Element.innerText = list.name;
		mainElement.append(h3Element);

		// Loop through tasks in list
		for (const task of list.tasks) {

			// Write task description in input element
			const inputElement = document.createElement("input");
			inputElement.type = "text";
			inputElement.id = "input" + idCounter;
			idCounter++;
			inputElement.onclick = noticeMe;
			inputElement.value = task.description;

			// Set background color of input element depending on if the task is done or not
			if (task.done === true) {
				inputElement.style.backgroundColor = myGreen;
			}
			else {
				inputElement.style.backgroundColor = myYellow;
			}

			mainElement.append(inputElement);

		}

	}

}