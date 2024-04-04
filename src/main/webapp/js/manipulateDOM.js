function deleteTask() {

	// Get array allTasks and find index i of selectedTask in this array
	const allTasks = Array.from(mainElement.getElementsByClassName("task"));
	const i = allTasks.indexOf(selectedTask);

	// Try to find nextTask, preferentially task below, otherwise task above
	let nextTask = allTasks[i + 1];
	if (nextTask === undefined) {
		nextTask = allTasks[i - 1];
	}

	// If nextTask is undefined, it means there is only one task left,
	// and that task should not be deleted.
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
	const firstH1Element = mainElement.querySelector("h1");

	if (siblingElement !== firstH1Element) {
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

	// If selectedTask is undefined, which is only when site is just loaded (or reloaded)
	if (selectedTask === undefined) {

		// Make task the selectedTask:
		// - Put black border around back div
		selectedTask = task;
		selectedTask.querySelector("div").style.borderColor = myBlack;

		return;
	}

	// If task is not already selectedTask
	if (task !== selectedTask) {

		// Unselect current selectedTask:
		// - Put transparent border around back div
		// - Make front div reappear
		selectedTask.querySelector("div").style.borderColor = myTransparent;
		selectedTask.querySelector("div").nextElementSibling.style.display = "block";

		// Make task the selectedTask:
		// - Put black border around back div
		selectedTask = task;
		selectedTask.querySelector("div").style.borderColor = myBlack;

		return;

	}

	// If task is already selectedTask
	if (task === selectedTask) {

		const inputElement = selectedTask.querySelector("input");

		// If inputElement does not already have focus
		// (Somehow clicking a button does not make inputElement loose focus)
		if (inputElement !== document.activeElement) {

			// Make front div temporarily disappear
			selectedTask.querySelector("div").nextElementSibling.style.display = "none";

			// Sets cursor in inputElement at the end of the text
			const length = inputElement.value.length;
			inputElement.setSelectionRange(length, length);

			// Give inputElement focus
			inputElement.focus();

		}

		return;

	}

}



// Create the special task div
function createTask(description, done) {

	// Create the task which is actually a div element, with two overlapping childs divs inside
	const task = document.createElement("div");
	task.className = "task";
	task.onclick = function fun() { selectTask(this) };


	// Create the two child divs on top of each other
	task.innerHTML = "<div><input type='text' value='" + description + "'></div><div></div>";

	// Set background color of back div depending on if task is done or not
	if (done === true) {
		task.setAttribute("data-done-status", "true");
		task.querySelector("div").style.backgroundColor = myGreen;
	}
	else {
		task.setAttribute("data-done-status", "false");
		task.querySelector("div").style.backgroundColor = myYellow;
	}


	// NB: oninput event fires everytime user types a character in inputElement
	// (perhaps a bit wasteful)
	task.querySelector("input").oninput = function fun() {
		setMessage("Unsaved changes", false);
	};


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


// Go trough data object and create DOM
function buildViewFromData(data) {

	// Set the global variable mainElement to be the <main></main> of viewBodyHtml
	mainElement = document.querySelector("main");

	// Loop through lists in data
	for (const list of data.lists) {

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

// Go trough DOM and create data object
function buildDataFromView() {

	const data = {
		lists: []
	};
	let list;
	let task;

	// Loop through all h1 and div elements (not nested div elements)
	for (let element = mainElement.querySelector("h1"); element !== null; element = element.nextElementSibling) {

		// If element is a h1 element, then create new list and push it to data.lists array
		if (element.tagName.toLowerCase() === "h1") {

			list = {
				name: element.innerText,
				tasks: []
			};

			data.lists.push(list);
		}

		// If element is a div element, then create new task and push it to list.tasks array 
		if (element.tagName.toLowerCase() === "div") {

			task = {
				description: element.querySelector("input").value,
				done: stringToBoolean(element.getAttribute("data-done-status"))
			};

			list.tasks.push(task);

		}

	}

	return data;

}

// Helper function to convert string to boolean
function stringToBoolean(string) {

	const lowerCaseString = string.trim().toLowerCase();

	if (lowerCaseString === "true") {
		return true;
	}
	else if (lowerCaseString === "false") {
		return false;
	}

}


// This function writes to div with id "message"
// Both viewBodyHtml and loginBodyHtml have such a div
function setMessage(text, error) {

	const message = document.getElementById("message");

	if (error === true) {
		message.style.color = "red";
	} else {
		message.style.color = "black";
	}

	message.innerText = text;

}