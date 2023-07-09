function sendLoginRequest() {

	credentials = {
		username: document.getElementById("username").value,
		password: document.getElementById("password").value
	};

	console.log(credentials);

	const xhr = new XMLHttpRequest();
	xhr.responseType = "text"; // should this be text/HTML???

	xhr.open("POST", "controller?action=login");



	xhr.onload = () => {
		

		if (xhr.status == 200) {
			// THIS LINE MANAGES THE SHOW view.html WITHOUT CHANGING THE URL
			// BUT DOES ITS JAVASCRIPT RUN ONLOAD? BECAUSE THAT IS IMPORTANT TO ACTUALLY BUILD view.html
			// I NEED TO FIGURE OUT A WAY TO GET THE JAVASCRIPT TO RUN
			// PERHAPS PUT ALL MY JAVASCRIPT INTO A FEW FILES AND HAVE BOTH login.html AND view.html REFERENCE THOSE FILES,
			// SO ALL MY JAVASCRIPT IS ALWAYS AVAILABLE.
			console.log(xhr.response);
			document.documentElement.innerHTML=xhr.responseText;
			loadData();
		}
	}

	xhr.send(JSON.stringify(credentials));

}




function sendLogoutRequest() {

	const xhr = new XMLHttpRequest();
	xhr.responseType = "text";

	xhr.open("POST", "controller?action=logout");


	xhr.onload = () => {
		console.log(xhr.response);

		if (xhr.status == 200) {
			document.documentElement.innerHTML=xhr.responseText;
		}
	}

	xhr.send();

}
