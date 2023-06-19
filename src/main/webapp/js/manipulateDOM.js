var idLastClicked = null;
var mainElement = document.querySelector("main");
var myGreen = "rgb(144, 238, 144)";  // Not a web safe color
var myYellow = "rgb(255,255,153)";
//var myYellow = "rgb(240,240,240)";
var myTransparent = "rgb(0,0,0,0)";


// Object to keep track of the ids clicked
const idsClicked = {

	// array to store the ids clicked
	array: [null, null],

	// return the last element of the array
	lastClicked: function() {
		return this.array[1];
	},

	// register latest click
	registerClick: function(id) {
		this.array[0] = this.array[1];
		this.array[1] = id;
	},
};


function deleteTask() {
	
	
	const ls = idsClicked.lastClicked();
	
	if( ls ) {
		document.getElementById(ls).remove();	
	}

}


function markTaskDone() {
	
	const ls = idsClicked.lastClicked();
	
	if( ls ) {
		
		divElement = document.getElementById(ls);
		
		const doneStatus = divElement.getAttribute("data-done-status");
		
		if( doneStatus === "false" ) {
			divElement.setAttribute("data-done-status","true");
			divElement.querySelector("div").style.backgroundColor = myGreen;
		}
		else {
			divElement.setAttribute("data-done-status","false");
			divElement.querySelector("div").style.backgroundColor = myYellow;
		}
		
	}
	
}



function clickTask(element) {

	// Put transparent border around the task clicked last
	const lastElement = document.getElementById(idsClicked.lastClicked());
	if (lastElement) {
		lastElement.querySelector("div").style.borderColor = myTransparent;
	}

	// Put black border around the task clicked now
	element.querySelector("div").style.borderColor = "black";

	// If task clicked now is also the task clicked last, then put focus on its inputElement
	if (element.id === idsClicked.lastClicked()) {
		const inputElement = element.querySelector("input");
		const length = inputElement.value.length;
		inputElement.setSelectionRange(length, length);
		inputElement.focus();
	}

	// Register this task as the task clicked last
	idsClicked.registerClick(element.id);

}







function buildView() {

	let task_id = 0;

	//const mainElement = document.querySelector("main");

	// Loop through lists in plan
	for (const list of plan.lists) {

		// Write name of list in h1 element
		const h1Element = document.createElement("h1");
		h1Element.innerText = list.name;
		mainElement.appendChild(h1Element);

		// Loop through tasks in list
		for (const task of list.tasks) {

			// Create the special task div
			const divElement = document.createElement("div");
			divElement.className = "task";
			divElement.id = "task_" + task_id;
			task_id++;
			divElement.onclick = function fun() { clickTask(this) };
			divElement.setAttribute("data-select-status","0");

			// Create the two child divs on top of each other
			divElement.innerHTML = "<div><input type='text' value='" + task.description + "'></div><div></div>";

			// Set background color of most backward div depending on if the task is done or not
			if (task.done === true) {
				divElement.setAttribute("data-done-status","true");
				divElement.querySelector("div").style.backgroundColor = myGreen;
			}
			else {
				divElement.setAttribute("data-done-status","false");
				divElement.querySelector("div").style.backgroundColor = myYellow;
			}
			
			// Append the special task div to DOM
			mainElement.appendChild(divElement);

		}

	}

}


buildView();