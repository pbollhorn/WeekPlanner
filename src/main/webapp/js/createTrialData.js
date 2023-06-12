/**
 * JavaScript object which is a plan with start data for the trial user
 */

const monday = {
	name: "Monday",
	tasks: [{ description: "Wake up", done: true },
	{ description: "Drink a cup of Java", done: true },
	{ description: "Code Java", done: false },
	{ description: "Debug Java", done: false },
	{ description: "Go to bed", done: false },
	{ description: "Dream of Java", done: false }]
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
	tasks: [{ description: "f is for friday", done: false }]
};

const saturday = {
	name: "Saturday",
	tasks: [{ description: "s is for saturday", done: false }]
};

const sunday = {
	name: "Sunday",
	tasks: [{ description: "s is also for sunday", done: false }]
};

const week = {
	name: "Next Week",
	tasks: [{ description: "stuff to do next week", done: false }]
};

const month = {
	name: "Within a Month",
	tasks: [{ description: "stuff to do next week", done: false }]
};

const year = {
	name: "Within a Year",
	tasks: [{ description: "stuff to do next week", done: false }]
};





const plan = {
	username: "egon",
	lists: [monday, tuesday, wednesday, thursday, friday, saturday, sunday, week, month, year]
};