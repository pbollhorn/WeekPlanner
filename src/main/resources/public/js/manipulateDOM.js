"use strict";

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

// This function is used by other functions,
// and it fires onclick of front div:
// First click:
// - Put black border around task
// Second click:
// - Make front div temporarily disappear, so it is not clickable
// - Give input element focus, with cursor positioned at the end
function selectTask(task) {

    // If task is not already selectedTask
    if (task !== selectedTask) {

        // Unselect current selectedTask:
        // (Unless selectedTask is undefined, which it is when site is just loaded or reloaded)
        // - Put transparent border around back div
        // - Make front div reappear (PERHAPS REDUNDANT BECAUSE OF ONBLUR EVENT)
        if (selectedTask !== undefined) {
            selectedTask.querySelector("div").style.borderColor = myTransparent;
            selectedTask.querySelector("div").nextElementSibling.style.display = "block";
        }

        // Make task the selectedTask:
        // - Put black border around back div
        selectedTask = task;
        selectedTask.querySelector("div").style.borderColor = myBlack;

        return;

    }

    // If task is already selectedTask
    if (task === selectedTask) {

        // Make front div temporarily disappear
        selectedTask.querySelector("div").nextElementSibling.style.display = "none";

        // Sets cursor in inputElement at the end of the text,
        // because the end of the text is hard to reach on mobile devices
        const inputElement = selectedTask.querySelector("input");
        const length = inputElement.value.length;
        inputElement.setSelectionRange(length, length);

        // Give inputElement focus
        inputElement.focus();

        return;

    }

}


// Create the special task div, with two overlapping childs divs inside (back div and front div)
function createTask(description, doneStatus) {

    // Create the task which is actually a div element
    const task = document.createElement("div");
    task.className = "task";

    // The back div
    const backDiv = document.createElement("div");
    const inputElement = document.createElement("input");
    inputElement.type = "text";
    inputElement.value = description;
    backDiv.appendChild(inputElement);
    task.appendChild(backDiv);

    // The front div
    // onclick event is used to select task
    const frontDiv = document.createElement("div");
    frontDiv.onclick = function() { console.log("onclick"); selectTask(this.parentElement) };
    task.appendChild(frontDiv);

    // Set custom "data-done-status" attribute for task
    // and background color for back div depending on doneStatus
    if (doneStatus === true) {
        task.setAttribute("data-done-status", "true");
        backDiv.style.backgroundColor = myGreen;
    }
    else {
        task.setAttribute("data-done-status", "false");
        backDiv.style.backgroundColor = myYellow;
    }


    // oninput event for inputElement:
    // Fires everytime user types character.
    // This is necessary to set unsaved changes to true.
    inputElement.oninput = function() { console.log("oninput"); setUnsavedChangesToTrue() };

    // onkeydown event for inputElement:
    // To detect if user presses Enter, and then blur inputElement.
    // This is in order for desktop devices to have
    // same behavior as mobile devices when Enter is pressed.
    inputElement.onkeydown = function(event) {
        if (event.key === "Enter") { console.log("onkeydown"); this.blur(); }
    };

    // onfocus event for inputElement:
    // This is important for mobile devices.
    // Because pressing Enter om mobile devices will give the input element below focus.
    // This blurs inputElement if it got focus that way.
    inputElement.onfocus = function() {
        console.log("onfocus");
        if (this !== selectedTask.querySelector("input")) { this.blur(); }
    };

    // onblur event for inputElement:
    // Makes front div reappear.
    // This ensures that the cursor in inputElement is set at the end of the text,
    // also after user presses e.g. "Save" button and then clicks on this task again.
    inputElement.onblur = function() { console.log("onblur"); this.parentElement.nextElementSibling.style.display = "block"; };


    return task;

}


// Go trough data object and build DOM
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

// Go trough DOM and build data object
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


// Set unsavedChanges to true and set message,
// but only if unsavedChanges is not already true
function setUnsavedChangesToTrue() {

    if (unsavedChanges === false) {
        unsavedChanges = true;
        setMessage("Unsaved changes", false);
    }
}