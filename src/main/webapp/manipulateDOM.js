

function manipDOM() {


	const mainElement = document.querySelector("main");


	const inputElement = document.createElement("input");
	inputElement.type = "text";


	mainElement.appendChild(inputElement);



}






function buildView() {

	const mainElement = document.querySelector("main");

	// Loop through lists in plan
	for (let i = 0; i < plan.lists.length; i++) {

		const list = plan.lists[i];

		// Write name of list in h3 element
		const h3Element = document.createElement("h3");
		h3Element.innerText = list.name;
		mainElement.append(h3Element);

		// Loop through tasks in list
		for (let j = 0; j < list.tasks.length; j++) {
			
			const task = list.tasks[j];
			
			// Write task description in input element
			const inputElement = document.createElement("input");
			inputElement.type = "text";
			inputElement.value= task.description;	
			
			// Set background color of input element depending on if the task is done or not
			if(task.done === true) {
				inputElement.style.backgroundColor = "LightGreen";	
			}
			else {
				inputElement.style.backgroundColor = "#FFFF99";
			}
			
			mainElement.append(inputElement);	

		}

	}

}