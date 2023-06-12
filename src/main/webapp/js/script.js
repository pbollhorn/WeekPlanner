/**
 * 
 */




console.log('Hello World! Im here, your favorite language, JavaScript');



const saveButton = document.getElementById("save-button");



const sendData = () => {
	console.log("Du har trykket på GET knappen");
	console.log(plan);

	const xhr = new XMLHttpRequest();

	xhr.responseType = "json";

	xhr.open("POST", "controller");


	xhr.onload = () => {
		console.log(xhr.response);

	}


	//xhr.send(JSON.stringify(data));
	xhr.send(JSON.stringify(plan));



	buildView();

};



saveButton.addEventListener("click", sendData);