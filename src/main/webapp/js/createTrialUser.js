// This JavaScript function always returns a random number between min and max (both included):
function getRndInteger(min, max) {
	return Math.floor(Math.random() * (max - min + 1)) + min;
}

function createTrialUser() {

	document.body.innerHTML = trialBodyHtml;

	const username = document.getElementById("username");
	const password = document.getElementById("password");

	let randomUsername = "user" + getRndInteger(10, 99);
	let randomPassword = getRndInteger(1000, 9999);

	username.value = randomUsername;
	password.value = randomPassword;

}


function create() {

	credentials = {
		username: document.getElementById("username").value,
		password: document.getElementById("password").value
	};

	sendHttpRequest("POST", "controller/user", "application/json", credentials).then(xhr => {

		if (xhr.status === 200) {

			// User was created so now, lets login
			sendHttpRequest("POST", "controller/session", "application/json", credentials).then(xhr => {
				if (xhr.status === 200) {
					document.body.innerHTML = viewBodyHtml;
					loadData();
				}
			});
		} else if (xhr.status === 409) {

			// Username already taken

		} else {

			// An error occured

		}

	});

}