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
		setUnsavedChangesToTrue();
	}

}


function markTaskDone() {

	const doneStatus = stringToBoolean(selectedTask.getAttribute("data-done-status"));

	if (doneStatus === false) {
		selectedTask.setAttribute("data-done-status", "true");
		selectedTask.querySelector("div").style.backgroundColor = myGreen;
	}
	else {
		selectedTask.setAttribute("data-done-status", "false");
		selectedTask.querySelector("div").style.backgroundColor = myYellow;
	}

	setUnsavedChangesToTrue();

}


function moveTaskUp() {

	const siblingElement = selectedTask.previousElementSibling;
	const firstH1Element = mainElement.querySelector("h1");

	if (siblingElement !== firstH1Element) {
		mainElement.insertBefore(selectedTask, siblingElement);
		setUnsavedChangesToTrue();
	}

}


function moveTaskDown() {

	const siblingElement = selectedTask.nextElementSibling;

	if (siblingElement !== null) {
		mainElement.insertBefore(siblingElement, selectedTask);
		setUnsavedChangesToTrue();
	}

}


function addTask() {

	let newTask = createTask("", false);
	selectedTask.insertAdjacentElement("afterend", newTask);
	selectTask(newTask);
	setUnsavedChangesToTrue();

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
function createTask(description, doneStatus) {

	// Create the task which is actually a div element, with two overlapping childs divs inside
	const task = document.createElement("div");
	task.className = "task";
	task.onclick = function() { selectTask(this) };

	// Create the two child divs on top of each other
	task.innerHTML = "<div><input type='text' value='" + description + "'></div><div></div>";

	// Set background color of back div depending on doneStatus
	if (doneStatus === true) {
		task.setAttribute("data-done-status", "true");
		task.querySelector("div").style.backgroundColor = myGreen;
	}
	else {
		task.setAttribute("data-done-status", "false");
		task.querySelector("div").style.backgroundColor = myYellow;
	}

	// Get inputElement, so we can set events for it
	const inputElement = task.querySelector("input");

	// oninput event:
	// Fires everytime user types character.
	// This is necessary to set unsaved changes to true.
	inputElement.oninput = function() { setUnsavedChangesToTrue() };

	// onkeydown event:
	// To detect if user presses Enter, and then blur inputElement.
	// This is in order for desktop devices to have
	// same behavior as mobile devices when Enter is pressed. 
	inputElement.onkeydown = function(event) {
		if (event.key === "Enter") { this.blur(); }
	};

	// onfocus event:
	// This is important for mobile devices.
	// Because pressing Enter om mobile devices will give the input element below focus.
	// This blurs inputElement if it got focus that way.
	inputElement.onfocus = function() {
		if (this !== selectedTask.querySelector("input")) { this.blur(); }
	};


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
			const taskElement = createTask(task.description, task.doneStatus);
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

			const task = {
				description: element.querySelector("input").value,
				doneStatus: stringToBoolean(element.getAttribute("data-done-status"))
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

// Set unsavedChanges to true and set message,
// but only if unsavedChanges is not already true
function setUnsavedChangesToTrue() {

	if (unsavedChanges === false) {
		unsavedChanges = true;
		setMessage("Unsaved changes", false);
	}
}
