// Make sure we are logged out, then login with valid credentials, then checked we are logged in,
// the logout and check everything along the way
function testB1() {

	let credentials = {
		username: "testuser1",
		password: "1111"
	};

	sendHttpRequest("DELETE", "controller/session").then(xhr => {
		assertEquals(200, xhr.status, "Logout");

		return sendHttpRequest("GET", "controller/session");
	}).then(xhr => {
		assertEquals(401, xhr.status, "Check login status");

		return sendHttpRequest("POST", "controller/session", "application/json", credentials);
	}).then(xhr => {
		assertEquals(200, xhr.status, "Login");

		return sendHttpRequest("GET", "controller/session");
	}).then(xhr => {
		assertEquals(200, xhr.status, "Check login status");

		return sendHttpRequest("DELETE", "controller/session");
	}).then(xhr => {
		assertEquals(200, xhr.status, "Logout");

		return sendHttpRequest("GET", "controller/session");
	}).then(xhr => {
		assertEquals(401, xhr.status, "Check login status");

	});

}


// Run all the tests
function testsB() {

	testB1();

}