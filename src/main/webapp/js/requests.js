"use strict";

// Wrapper function for XMLHttpRequest:
// if contentType is "application/json" then body is converted to json and sent.
// if contentType is something else then body is sent as is.
// if contentType is undefined then body is not sent.
// Return value: The XMLHttpRequest object is returned both onload and onerror.
// onload: server responded and status is whatever status code the server responded with.
// onerror: something went wrong and server never responded so status is 0.
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


function loadSite() {

	// If any unsaved changes,
	// then show warning message to user before reloading or leaving page
	window.addEventListener("beforeunload", function(event) {
		if (unsavedChanges) {
			event.preventDefault();
			event.returnValue = "";
		}
	});

	loadData();



//	// Request HTML assets one by one
//	sendHttpRequest("GET", "account-body.html").then(xhr => {
//		if (xhr.status === 200) { accountBodyHtml = xhr.responseText; }
//
//		return sendHttpRequest("GET", "menu-body.html");
//	}).then(xhr => {
//		if (xhr.status === 200) { menuBodyHtml = xhr.responseText; }
//
//		return sendHttpRequest("GET", "trial-body.html");
//	}).then(xhr => {
//		if (xhr.status === 200) { trialBodyHtml = xhr.responseText; }
//
//		return sendHttpRequest("GET", "view-body.html");
//	}).then(xhr => {
//		if (xhr.status === 200) { viewBodyHtml = xhr.responseText; }
//
//		return sendHttpRequest("GET", "login-body.html");
//	}).then(xhr => {
//		if (xhr.status === 200) { loginBodyHtml = xhr.responseText; }
//
//		// If all requests for HTML assets were succesful, then continue loading site
//		// else display error message
//		if (accountBodyHtml !== undefined && menuBodyHtml !== undefined && trialBodyHtml !== undefined && viewBodyHtml !== undefined && loginBodyHtml !== undefined) {
//
//			sendHttpRequest("GET", "api/session").then(xhr => {
//
//				if (xhr.status === 200) {
//					document.body.innerHTML = viewBodyHtml;
//					loadData();
//				}
//				else {
//					document.body.innerHTML = loginBodyHtml;
//				}
//
//			});
//
//		}
//		else {
//			document.body.innerHTML = "<div id='message'></div>";
//			setMessage("Error: Error loading site", true);
//		}
//
//	});
}

function loadData() {

	sendHttpRequest("GET", "api/data").then(xhr => {

		unsavedChanges = false;
		setMessage("", false);

		if (xhr.status === 200) {
			const data = JSON.parse(xhr.responseText);
			buildViewFromData(data);
		}
		else if (xhr.status === 401) {
			document.body.innerHTML = loginBodyHtml;
		}
		else {
			setMessage("Error: Could not load data", true);
		}

	});

}


function saveData() {

	const data = buildDataFromView();

	sendHttpRequest("PUT", "api/data", "application/json", data).then(xhr => {

		if (xhr.status === 200) {
			unsavedChanges = false;
			setMessage("", false);
		} else if (xhr.status === 401) {
			unsavedChanges = false;
			document.body.innerHTML = loginBodyHtml;
		} else {
			setMessage("Error: Could not save data", true);
		}

	});

}


function login() {

	const credentials = {
		username: document.getElementById("username").value,
		password: document.getElementById("password").value
	};

	sendHttpRequest("POST", "api/session", "application/json", credentials).then(xhr => {

		if (xhr.status === 200) {
			//document.body.innerHTML = viewBodyHtml;
			//loadData();

			window.location.href = "plan";
		}
		else if (xhr.status === 401) {
			setMessage("Wrong username or password", false);
		}
		else {
			setMessage("Error: An error occured", true);
		}


	});

}


function logout() {

	// If any unsaved changes,
	// then show warning message to user before logging out
//	if (unsavedChanges) {
//
//		const answer = window.confirm("You have unsaved changes.\nAre you sure you want to logout?");
//		if (answer === false) {
//			return;
//		}
//
//	}

	sendHttpRequest("DELETE", "api/session").then(xhr => {

		if (xhr.status === 200) {
			//unsavedChanges = false;
			window.location.href = "login";
		}
		else {
			setMessage("Error: Could not log out", true);
		}

	});

}


function menu() {
	window.location.href = "menu";
}