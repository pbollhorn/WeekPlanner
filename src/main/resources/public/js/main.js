/**
 *	This file is the entry point for the JavaScript code.
 *	This file is the only place where global variables are allowed.
 */
"use strict";

// From manipulateDOM.js
const myGreen = "rgb(144, 238, 144)";
const myYellow = "rgb(255,255,153)";
const myBlack = "rgb(0,0,0)";
const myTransparent = "rgb(0,0,0,0)";
let unsavedChanges = false;
let selectedTask;
let mainElement;


// From requests.js
let loginBodyHtml;
let trialBodyHtml;
let viewBodyHtml;
let menuBodyHtml;
let accountBodyHtml;
loadSite();