/**
 * JavaScript object which is a plan with start data for the trial user
 */

function createTrialData() {

	const monday = {
		name: "Monday",
		tasks: [{ description: "Wake up", done: true },
		{ description: "Drink a cup of Java ☕", done: true },
		{ description: "Code Java", done: false }]
	};

	const tuesday = {
		name: "Tuesday",
		tasks: [{ description: "t is for tuesday", done: false }]
	};

	const wednesday = {
		name: "Wednesday",
		tasks: [{ description: "w is for wednesday", done: false }]
	};

	const thursday = {
		name: "Thursday",
		tasks: [{ description: "t is also for thursday", done: false }]
	};

	const friday = {
		name: "Friday",
		tasks: [{ description: "abcdefghijklmnopqrstuvxyzæøå", done: false }]
	};

	const saturday = {
		name: "Saturday",
		tasks: [{ description: "ABCDEFGHIJKLMNOPQRSTUVXYZÆØÅ", done: false }]
	};

	const sunday = {
		name: "Sunday",
		tasks: [{ description: 'That&apos;s my fun day', done: false },
		{ description: "My I don&apos;t have to run day", done: false }]
	};

	const week = {
		name: "Next Week",
		tasks: [{ description: "stuff to do next week", done: false }]
	};

	const month = {
		name: "Within a Month",
		tasks: [{ description: "Shower day", done: false }]
	};

	const year = {
		name: "Within a Year",
		tasks: [{ description: "stuff to do this year", done: false }]
	};

	const shopping = {
		name: "Shopping",
		tasks: [{ description: "Stuff to buy", done: false }]
	};


	const plan = {
		lists: [monday, tuesday, wednesday, thursday, friday, saturday, sunday, week, month, year, shopping]
	};
	
	console.log(JSON.stringify(plan));

}