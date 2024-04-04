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


function loadSite() {

	sendHttpRequest("GET", "trial-body.html").then(xhr => {

		if (xhr.status == 200) {
			trialBodyHtml = xhr.responseText;
		} else {
			trialBodyHtml = "An error occured";
		}

		return sendHttpRequest("GET", "view-body.html");

	}).then(xhr => {

		if (xhr.status == 200) {
			viewBodyHtml = xhr.responseText;
		} else {
			viewBodyHtml = "An error occured";
		}

		return sendHttpRequest("GET", "login-body.html");

	}).then(xhr => {

		if (xhr.status == 200) {
			loginBodyHtml = xhr.responseText;
		} else {
			loginBodyHtml = "An error occured";
		}

		return sendHttpRequest("GET", "controller/session");

	}).then(xhr => {

		if (xhr.status == 200) {
			document.body.innerHTML = viewBodyHtml;
			loadData();
		}
		else {
			document.body.innerHTML = loginBodyHtml;
		}

	});

}

function loadData() {

	sendHttpRequest("GET", "controller/data").then(xhr => {

		if (xhr.status == 200) {
			const data = JSON.parse(xhr.responseText);
			buildViewFromData(data);
		}
		else if (xhr.status == 401) {
			document.body.innerHTML = loginBodyHtml;
		}
		else {
			setMessage("Error: Could not load data", true);
		}

	});

}


function saveData() {

	const data = buildDataFromView();

	sendHttpRequest("PUT", "controller/data", "application/json", data).then(xhr => {

		if (xhr.status == 200) {
			setMessage("", false);
		} else if (xhr.status == 401) {
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

	sendHttpRequest("POST", "controller/session", "application/json", credentials).then(xhr => {

		if (xhr.status == 200) {
			document.body.innerHTML = viewBodyHtml;
			loadData();
		}
		else if (xhr.status == 401) {
			setMessage("Wrong username or password", false);
		}
		else {
			setMessage("Error: An error occured", true);
		}


	});

}


function logout() {

	sendHttpRequest("DELETE", "controller/session").then(xhr => {

		if (xhr.status == 200) {
			document.body.innerHTML = loginBodyHtml;
		}
		else {
			setMessage("Error: Could not log out", true);
		}

	});

}