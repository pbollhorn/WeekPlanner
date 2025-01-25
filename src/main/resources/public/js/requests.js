"use strict";

// Wrapper function for XMLHttpRequest:
// if contentType is "application/json" then body is converted to json and sent.
// if contentType is something else then body is sent as is.
// if contentType is undefined then body is not sent.
// Return value: The XMLHttpRequest object is returned both onload and onerror.
// onload: server responded and status is whatever status code the server responded with.
// onerror: something went wrong and server never responded so status is 0.
function sendHttpRequest(method, url, contentType, body) {

    const promise = new Promise((resolve) => {

        const xhr = new XMLHttpRequest();
        xhr.open(method, url);
        xhr.onload = () => {
            resolve(xhr);
        }
        xhr.onerror = () => {
            resolve(xhr);
        }

        if (contentType === "application/json") {
            xhr.setRequestHeader("Content-Type", "application/json");
            xhr.send(JSON.stringify(body));
        } else if (contentType !== undefined) {
            xhr.setRequestHeader("Content-Type", contentType);
            xhr.send(body);
        } else if (contentType === undefined) {
            xhr.send();
        }

    });
    return promise;
}


function loadSite() {

    // If any unsaved changes,
    // then show warning message to user before reloading or leaving page
    window.addEventListener("beforeunload", function (event) {
        if (unsavedChanges) {
            event.preventDefault();
            event.returnValue = "";
        }
    });

    loadData();

}

function loadData() {

    sendHttpRequest("GET", "api/data").then(xhr => {

        unsavedChanges = false;
        setMessage("", false);

        if (xhr.status === 200) {
            const data = JSON.parse(xhr.responseText);
            buildViewFromData(data);
        } else if (xhr.status === 401) {
            document.body.innerHTML = loginBodyHtml;
        } else {
            setMessage("Error: Could not load data", true);
        }

    });

}


function saveData() {

    const data = buildDataFromView();

    setMessage("Saving...", false);

    sendHttpRequest("PUT", "api/data", "application/json", data).then(xhr => {

        if (xhr.status === 200) {
            unsavedChanges = false;
            setMessage("", false);
        } else if (xhr.status === 401) {
            unsavedChanges = false;
            document.body.innerHTML = loginBodyHtml;
        } else {
            setMessage("Error: Could not save data", true);
        }

    });

}


function login() {

    const credentials = {
        username: document.getElementById("username").value,
        password: document.getElementById("password").value
    };

    sendHttpRequest("POST", "api/session", "application/json", credentials).then(xhr => {

        if (xhr.status === 200) {
            //document.body.innerHTML = viewBodyHtml;
            //loadData();

            window.location.href = "plan";
        } else if (xhr.status === 401) {
            setMessage("Wrong username or password", false);
        } else {
            setMessage("Error: An error occured", true);
        }


    });

}


function logout() {

    // If any unsaved changes,
    // then show warning message to user before logging out
//	if (unsavedChanges) {
//
//		const answer = window.confirm("You have unsaved changes.\nAre you sure you want to logout?");
//		if (answer === false) {
//			return;
//		}
//
//	}

    sendHttpRequest("DELETE", "api/session").then(xhr => {

        if (xhr.status === 200) {
            //unsavedChanges = false;
            window.location.href = "login";
        } else {
            setMessage("Error: Could not log out", true);
        }

    });

}


// This function writes to div with id "message"
// Both viewBodyHtml and loginBodyHtml have such a div
function setMessage(text, error) {

    const message = document.getElementById("message");

    if (error === true) {
        message.style.color = "red";
    } else {
        message.style.color = "black";
    }

    message.innerText = text;

}