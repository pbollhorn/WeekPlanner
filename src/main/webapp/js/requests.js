// Wrapper function for XMLHttpRequest:
// if contentType === "application/json" then body is converted to json and sent.
// if contentType !== undefined then body is sent as is.
// if contentType === undefined then body is not sent.
// Return value: The XMLHttpRequest object is returned both onload and onerror.
// onload: server responded and status is whatever status code the server responded with
// onerror: something went wrong and server never responded, so status is 0
function sendHttpRequest(method, url, contentType, body) {

	const promise = new Promise((resolve) => {

		const xhr = new XMLHttpRequest();

		xhr.open(method, url);

		xhr.onload = () => { resolve(xhr); }

		xhr.onerror = () => { resolve(xhr); }

		if (contentType === "application/json") {
			xhr.setRequestHeader("Content-Type", "application/json");
			xhr.send(JSON.stringify(body));
		} else if (contentType !== undefined) {
			xhr.setRequestHeader("Content-Type", contentType);
			xhr.send(body);
		}
		else if (contentType === undefined) {
			xhr.send();
		}

	});
	return promise;
}



function checkCredentials() {

	sendHttpRequest("POST", "controller/checkcredentials").then(xhr => {

		if (xhr.status == 200) {
			// Change the body of the current HTML page to be viewBodyHtml and run loadData()
			document.body.innerHTML = viewBodyHtml;
			loadData();
		}
		else {
			// Change the HTML of the current document to be loginBodyHtml
			document.body.innerHTML = loginBodyHtml;
		}



	});

}




function login() {

	credentials = {
		username: document.getElementById("username").value,
		password: document.getElementById("password").value
	};

	sendHttpRequest("POST", "controller/login", "application/json", credentials).then(xhr => {

		if (xhr.status == 200) {
			// Change the body of the current HTML page to be view-body.html which is in the response
			// and run loadData()
			document.body.innerHTML = viewBodyHtml;
			loadData();
		}
		else if (xhr.status == 401) {
			const message = document.getElementById("message");
			message.style.color = "red";
			message.innerText = "Wrong username or password";
		}
		else {
			const message = document.getElementById("message");
			message.style.color = "red";
			message.innerText = "An error occured";
		}


	});

}




function logout() {

	sendHttpRequest("POST", "controller/logout").then(xhr => {

		if (xhr.status == 200) {
			document.body.innerHTML = loginBodyHtml;
		}
		else {
			message.style.color = "red";
			message.innerText = "An error occured";
		}

	});

}

function loadData() {

	sendHttpRequest("GET", "controller/loaddata").then(xhr => {

		plan = JSON.parse(xhr.responseText);

		// Build the view and select the first task
		buildView();
		selectTask(mainElement.querySelector(".task"));

	});

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

	sendHttpRequest("PUT", "controller/savedata", "application/json", newPlan).then(xhr => {

		if (xhr.status == 405) {
			document.body.innerHTML = loginBodyHtml;
		}

	});

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