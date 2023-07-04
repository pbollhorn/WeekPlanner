let plan;


const xhr = new XMLHttpRequest();

xhr.responseType = "json";

xhr.open("GET", "controller");

xhr.onload = () => {
	
	plan = xhr.response;
	
	console.log(plan);
	
	
	/////// MAIN FUNCTION ////////////////////////
	// Build the view and select the first task
	buildView();
	selectTask(mainElement.querySelector(".task"));
	//////////////////////////////////////////////
}

xhr.send();