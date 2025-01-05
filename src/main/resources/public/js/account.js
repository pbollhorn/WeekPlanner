"use strict";


function changeUsername() {

    const newUsername = document.getElementById("newUsername").value;
    const password = document.getElementById("password").value;

    const wrapper = {
        newUsername: newUsername,
        password: password
    };


    sendHttpRequest("PUT", "api/user", "application/json", wrapper).then(xhr => {

        if (xhr.status === 200) {


        }
        else {

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


        }
        else {

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


        }
        else {

            // An error occured

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

        if (xhr.status === 409) {
            setMessage("Username already in use", true);
            return;
        }
        if (xhr.status !== 200) {
            setMessage("An error occured", true);
            return;
        }

        //data = createTrialData();

        // User was created so now, lets login
        sendHttpRequest("POST", "api/session", "application/json", credentials).then(xhr => {
            if (xhr.status === 200) {
                window.location.href = "plan";
            }
        });



    });

}