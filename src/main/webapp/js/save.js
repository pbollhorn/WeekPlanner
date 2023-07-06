
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



// Go trough DOM and create newPlan object
function saveToBackend() {

	const newPlan = {
		username: "egon",
		lists: []
	};
	let list;
	let task;

	// Loop through all h1 and div elements (not nested div elements)
	for (let element = mainElement.querySelector("h1"); element !== null; element = element.nextElementSibling) {

		// If element is a h1 element, then create new list and push it to newPlan.lists array
		if (element.tagName.toLowerCase() === "h1") {

			list = {
				name: element.innerText,
				tasks: []
			};

			newPlan.lists.push(list);
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
	
	
	// newPlan object is build and now we are ready to send to backend
	const xhr = new XMLHttpRequest();
	xhr.open("POST", "controller?action=savedata");
	xhr.send(JSON.stringify(newPlan));
	
	// GET repsonse code from backend and let user know if save was succesfull or not

	
}