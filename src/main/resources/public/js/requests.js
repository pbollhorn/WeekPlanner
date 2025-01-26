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
            buildDOMFromData(data);
        } else if (xhr.status === 401) {
            window.location.href = "login";
        } else {
            setMessage("Error: Could not load data", true);
        }

    });

}


function saveData() {

    const data = buildDataFromDOM();

    setMessage("Saving...", false);

    sendHttpRequest("PUT", "api/data", "application/json", data).then(xhr => {

        if (xhr.status === 200) {
            unsavedChanges = false;
            setMessage("", false);
        } else if (xhr.status === 401) {
            unsavedChanges = false;
            window.location.href = "login";
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
            window.location.href = "plan";
        } else if (xhr.status === 401) {
            setMessage("Wrong username or password", false);
        } else {
            setMessage("Error: An error occured", true);
        }

    });

}


function logout() {

    sendHttpRequest("DELETE", "api/session").then(xhr => {

        if (xhr.status === 200) {
            //unsavedChanges = false;
            window.location.href = "login";
        } else {
            setMessage("Error: Could not log out", true);
        }

    });

}

function createAccount() {

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

    if (password !== confirmPassword) {
        setMessage("Passwords do not match", true);
        return;
    }

    const credentials = {
        username: username,
        password: password
    };

    sendHttpRequest("POST", "api/user", "application/json", credentials).then(xhr => {

        if (xhr.status !== 200) {
            setMessage("An error occured", true);
            return;
        }

        // User was created so now, lets login
        sendHttpRequest("POST", "api/session", "application/json", credentials).then(xhr => {
            if (xhr.status === 200) {
                window.location.href = "plan";
            }
        });

    });

}

function changeUsername() {

    const newUsername = document.getElementById("newUsername").value;
    const password = document.getElementById("password").value;

    const wrapper = {
        newUsername: newUsername,
        password: password
    };


    sendHttpRequest("PUT", "api/user", "application/json", wrapper).then(xhr => {

        if (xhr.status === 200) {


        } else {

            // An error occured

        }

    });


}


// FOR TESTING STUFF
function changePassword() {

    const password = document.getElementById("password").value;
    const newPassword = document.getElementById("newPassword").value;
    const confirmNewPassword = document.getElementById("confirmNewPassword").value;

    const wrapper = {
        password: password,
        newPassword: newPassword
    };

    sendHttpRequest("PUT", "api/user", "application/json", wrapper).then(xhr => {

        if (xhr.status === 200) {


        } else {

            // An error occured

        }

    });

}


function deleteAccount() {

    const password = document.getElementById("password").value;

    const wrapper = {
        password: password
    };

    sendHttpRequest("DELETE", "api/user", "application/json", wrapper).then(xhr => {

        if (xhr.status === 200) {


        } else {

            // An error occured

        }

    });

}