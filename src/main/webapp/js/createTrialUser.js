"use strict";


function create() {

	const credentials = {
		username: document.getElementById("username").value,
		password: document.getElementById("password").value
	};

	sendHttpRequest("POST", "api/user", "application/json", credentials).then(xhr => {

		if (xhr.status === 200) {

			// User was created so now, lets login
			sendHttpRequest("POST", "api/session", "application/json", credentials).then(xhr => {
				if (xhr.status === 200) {
					window.location.href = "plan";
				}
			});
		} else if (xhr.status === 409) {

			// Username already taken

		} else {

			// An error occured

		}

	});

}