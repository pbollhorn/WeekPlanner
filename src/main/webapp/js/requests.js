function checkCredentials() {


	const xhr = new XMLHttpRequest();

	xhr.responseType = "text";

	xhr.open("POST", "controller/checkcredentials");

	xhr.onload = () => {

		if (xhr.status == 200) {
			// Change the body of the current HTML page to be view-body.html which is in the response
			// and run loadData()
			document.body.innerHTML = xhr.response;
			loadData();
		}
		else {
			// Change the HTML of the current document to be what is in the response, which is login.html
			document.body.innerHTML = xhr.response;

		}
	}

	xhr.send();

}




function login() {

	credentials = {
		username: document.getElementById("username").value,
		password: document.getElementById("password").value
	};

	const xhr = new XMLHttpRequest();



	xhr.responseType = "text";

	xhr.open("POST", "controller/login");
	xhr.setRequestHeader("Content-Type", "application/json");

	xhr.onload = () => {

		if (xhr.status == 200) {
			// Change the body of the current HTML page to be view-body.html which is in the response
			// and run loadData()
			document.body.innerHTML = xhr.response;
			loadData();
		}
		else {

			const message = document.getElementById("message");
			message.style.color = "red";
			message.innerText = "Wrong username or password";

		}
	}

	xhr.send(JSON.stringify(credentials));

}




function logout() {

	const xhr = new XMLHttpRequest();
	xhr.responseType = "text";

	xhr.open("POST", "controller/logout");


	xhr.onload = () => {

		if (xhr.status == 200) {
			document.body.innerHTML = xhr.response;
		}
	}

	xhr.send();

}



function loadData() {


	const xhr = new XMLHttpRequest();

	xhr.responseType = "json";

	xhr.open("GET", "controller/loaddata");

	xhr.onload = () => {

		plan = xhr.response;

		/////// MAIN FUNCTION ////////////////////////
		// Build the view and select the first task
		buildView();
		selectTask(mainElement.querySelector(".task"));
		//////////////////////////////////////////////
	}

	xhr.send();

}



// Go trough DOM and create newPlan object
function saveData() {

	const newPlan = {
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
	xhr.open("PUT", "controller/savedata");
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(newPlan));

	// GET repsonse code from backend and let user know if save was succesfull or not


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