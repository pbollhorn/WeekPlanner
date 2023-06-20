const mainElement = document.querySelector("main");
const myGreen = "rgb(144, 238, 144)";  // Not a web safe color
const myYellow = "rgb(255,255,153)";
const myGray = "rgb(200,200,200)";
const myBlack = "rgb(0,0,0)";
const myTransparent = "rgb(0,0,0,0)";

let task_id_selected = null;

let task_id = 0;



function deleteTask() {

	if (task_id_selected !== null) {

		const taskElement = document.getElementById(task_id_selected);
		const allTaskElements = document.querySelectorAll(".task");

		// Loop through allTaskElements to find the next one after current taksElement
		for (let i = 0; i < allTaskElements.length; i++) {

			if (taskElement === allTaskElements[i]) {

				selectTask(allTaskElements[i + 1]);
			}


		}

		taskElement.remove();

	}

}


function markTaskDone() {

	if (task_id_selected !== null) {

		divElement = document.getElementById(task_id_selected);

		const doneStatus = divElement.getAttribute("data-done-status");

		if (doneStatus === "false") {
			divElement.setAttribute("data-done-status", "true");
			divElement.querySelector("div").style.backgroundColor = myGreen;
		}
		else {
			divElement.setAttribute("data-done-status", "false");
			divElement.querySelector("div").style.backgroundColor = myYellow;
		}

	}

}


function moveTaskUp() {

	taskElement = document.getElementById(task_id_selected);
	const otherTaskElement = taskElement.previousElementSibling;

	if (otherTaskElement.innerText !== "Monday") {

		mainElement.insertBefore(taskElement, otherTaskElement);

	}

}


function moveTaskDown() {

	taskElement = document.getElementById(task_id_selected);
	const otherTaskElement = taskElement.nextElementSibling;
	mainElement.insertBefore(otherTaskElement, taskElement);

}


// Add task
function addTask() {

	taskElement = document.getElementById(task_id_selected);
	let newTask = createTask("", false);
	taskElement.insertAdjacentElement("afterend", newTask);
	selectTask(newTask);

}




function selectTask(taskElement) {

	if (taskElement.id === task_id_selected) {

		const inputElement = taskElement.querySelector("input");

		if (inputElement !== document.activeElement) {


			const transparentDiv = taskElement.querySelector("div").nextElementSibling;
			transparentDiv.style.display = "none";

			const length = inputElement.value.length;
			inputElement.setSelectionRange(length, length);
			inputElement.focus();
		}

	} else {



		if (task_id_selected !== null) {

			// Unselect previous task element
			const prevElement = document.getElementById(task_id_selected);
			prevElement.querySelector("div").style.borderColor = myTransparent;
			const transparentDiv = prevElement.querySelector("div").nextElementSibling;
			transparentDiv.style.display = "block";

		}

		taskElement.querySelector("div").style.borderColor = myBlack;

		task_id_selected = taskElement.id;

	}


}



// Create the special task div
function createTask(description, done) {

	// Create the task which is actually a div element, with two overlapping childs divs inside
	const taskElement = document.createElement("div");
	taskElement.className = "task";
	taskElement.id = "task_" + task_id;
	task_id++;
	taskElement.onclick = function fun() { selectTask(this) };


	// Create the two child divs on top of each other
	taskElement.innerHTML = "<div><input type='text' value='" + description + "'></div><div></div>";

	// Set background color of most backward div depending on if the task is done or not
	if (done === true) {
		taskElement.setAttribute("data-done-status", "true");
		taskElement.querySelector("div").style.backgroundColor = myGreen;
	}
	else {
		taskElement.setAttribute("data-done-status", "false");
		taskElement.querySelector("div").style.backgroundColor = myYellow;
	}

	// Give the special task div a data-select-status of 0
	taskElement.setAttribute("data-select-status", "0");


	// IMPORTANT:
	// onkeydown event: If user presses Enter, then input element should be blurred
	taskElement.querySelector("input").onkeydown = function fun2(event) {
		if (event.key === "Enter") {
			this.blur();
		}
	};

	// IMPORTANT:
	// onfocus event: If input elements get focus when its not suppose to,
	// its because the user pressing Enter has forced it,
	// and therefore we blur it
	taskElement.querySelector("input").onfocus = function fun3() {
		if (this.parentNode.parentNode.id !== task_id_selected) { this.blur(); }
	};

	// Return the taskElement
	return taskElement;

}



function buildView() {

	// Loop through lists in plan
	for (const list of plan.lists) {

		// Write name of list in h1 element
		const h1Element = document.createElement("h1");
		h1Element.innerText = list.name;
		mainElement.appendChild(h1Element);

		// Loop through tasks in list
		for (const task of list.tasks) {
			const taskElement = createTask(task.description, task.done);
			mainElement.appendChild(taskElement);
		}

	}

}


buildView();