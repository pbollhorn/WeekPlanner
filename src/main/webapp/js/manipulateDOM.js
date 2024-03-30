function deleteTask() {

	// Get array allTasks and find index i of selectedTask in this array
	const allTasks = Array.from(mainElement.getElementsByClassName("task"));
	const i = allTasks.indexOf(selectedTask);

	// Try to find nextTask, either task below, otherwise task above
	let nextTask = allTasks[i + 1];
	if (nextTask === undefined) {
		nextTask = allTasks[i - 1];
	}

	// If we havent been able to find a nextTask,
	// it means there is only one task left,
	// and we should'nt delete that task
	if (nextTask !== undefined) {
		selectedTask.remove();
		selectTask(nextTask);
		setMessage("Unsaved changes", false);
	}

}


function markTaskDone() {

	const doneStatus = selectedTask.getAttribute("data-done-status");

	if (doneStatus === "false") {
		selectedTask.setAttribute("data-done-status", "true");
		selectedTask.querySelector("div").style.backgroundColor = myGreen;
	}
	else {
		selectedTask.setAttribute("data-done-status", "false");
		selectedTask.querySelector("div").style.backgroundColor = myYellow;
	}

	setMessage("Unsaved changes", false);

}


function moveTaskUp() {

	const siblingElement = selectedTask.previousElementSibling;

	if (siblingElement.innerText !== "Monday") {
		mainElement.insertBefore(selectedTask, siblingElement);
		setMessage("Unsaved changes", false);
	}

}


function moveTaskDown() {

	const siblingElement = selectedTask.nextElementSibling;

	if (siblingElement !== null) {
		mainElement.insertBefore(siblingElement, selectedTask);
		setMessage("Unsaved changes", false);
	}

}


function addTask() {

	let newTask = createTask("", false);
	selectedTask.insertAdjacentElement("afterend", newTask);
	selectTask(newTask);
	setMessage("Unsaved changes", false);

}




function selectTask(task) {

	if (task === selectedTask) {

		const inputElement = task.querySelector("input");

		if (inputElement !== document.activeElement) {

			// Make the transparent div temporarily disappear
			const transparentDiv = task.querySelector("div").nextElementSibling;
			transparentDiv.style.display = "none";

			const length = inputElement.value.length;
			inputElement.setSelectionRange(length, length);
			inputElement.focus();
			
			setMessage("Unsaved changes", false);
		}

	} else {


		// Put transparent border around previous task element, i.e. unselect it
		// and make the transparent div (re)appear
		// The if sentence is only necessary for when starting up the document,
		// because here selectedTask is undefined
		if (selectedTask !== undefined) {
			selectedTask.querySelector("div").style.borderColor = myTransparent;
			const transparentDiv = selectedTask.querySelector("div").nextElementSibling;
			transparentDiv.style.display = "block";
		}


		// Put black border around the task and make that the selectedTask
		task.querySelector("div").style.borderColor = myBlack;
		selectedTask = task;

	}


}



// Create the special task div
function createTask(description, done) {

	// Create the task which is actually a div element, with two overlapping childs divs inside
	const task = document.createElement("div");
	task.className = "task";
	task.id = "task_" + task_id;
	task_id++;
	task.onclick = function fun() { selectTask(this) };


	// Create the two child divs on top of each other
	task.innerHTML = "<div><input type='text' value='" + description + "'></div><div></div>";

	// Set background color of most backward div depending on if the task is done or not
	if (done === true) {
		task.setAttribute("data-done-status", "true");
		task.querySelector("div").style.backgroundColor = myGreen;
	}
	else {
		task.setAttribute("data-done-status", "false");
		task.querySelector("div").style.backgroundColor = myYellow;
	}


	// IMPORTANT:
	// onkeydown event: If user presses Enter, then input element should be blurred
	task.querySelector("input").onkeydown = function fun(event) {
		if (event.key === "Enter") { this.blur(); }
	};

	// IMPORTANT:
	// onfocus event: If input elements get focus when its not suppose to,
	// its because the user pressing Enter has forced it,
	// and therefore we blur it
	task.querySelector("input").onfocus = function fun() {
		if (this !== selectedTask.querySelector("input")) { this.blur(); }
	};


	// Return the task
	return task;

}



function buildView(plan) {

	// Set the global variable mainElement to be the <main></main> of viewBodyHtml
	mainElement = document.querySelector("main");

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
	
	// Select the first task
	selectTask(mainElement.querySelector(".task"));

}


