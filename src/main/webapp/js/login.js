function sendLoginRequest() {

	credentials = {
		username: document.getElementById("username").value,
		password: document.getElementById("password").value
	};

	console.log(credentials);

	const xhr = new XMLHttpRequest();
	xhr.responseType = "json";

	xhr.open("POST", "controller?action=login");



	xhr.onload = () => {
		console.log(xhr.response);

		if (xhr.status == 200) {
			window.location.href = "view.html";
		}
	}

	xhr.send(JSON.stringify(credentials));

}




function sendLogoutRequest() {

	const xhr = new XMLHttpRequest();
	xhr.responseType = "json";

	xhr.open("POST", "controller?action=logout");


	xhr.onload = () => {
		console.log(xhr.response);

		if (xhr.status == 200) {
			window.location.href = "login.html";
		}
	}

	xhr.send();

}
