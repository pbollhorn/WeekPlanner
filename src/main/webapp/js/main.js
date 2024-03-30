/**
 *	main.js
 *	All global variables should be put in this file
 */


// From manipulateDOM.js
const myGreen = "rgb(144, 238, 144)";
const myYellow = "rgb(255,255,153)";
const myBlack = "rgb(0,0,0)";
const myTransparent = "rgb(0,0,0,0)";
let task_id = 0; // Only used for debugging purposes
let selectedTask;
let mainElement;


// From requests.js 
let loginBodyHtml;
let trialBodyHtml;
let viewBodyHtml;
loadSite();

