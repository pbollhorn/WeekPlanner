// Login, GET data request, PUT data request, Logout, GET data request, PUT data request 
function testC1() {

	let credentials = {
		username: "testuser1",
		password: "1111"
	};
	let data;

	sendHttpRequest("POST", "controller/session", "application/json", credentials).then(xhr => {
		assertEquals(200, xhr.status, "Login");

		return sendHttpRequest("GET", "controller/data");
	}).then(xhr => {
		assertEquals(200, xhr.status, "GET controller/data");

		data = JSON.parse(xhr.responseText);
		console.log(data);

		return sendHttpRequest("PUT", "controller/data", "application/json", data);
	}).then(xhr => {
		assertEquals(200, xhr.status, "PUT controller/data");

		return sendHttpRequest("DELETE", "controller/session");
	}).then(xhr => {
		assertEquals(200, xhr.status, "Logout");

		return sendHttpRequest("GET", "controller/data");
	}).then(xhr => {
		assertEquals(401, xhr.status, "GET controller/data");

		return sendHttpRequest("PUT", "controller/data", "application/json", data);
	}).then(xhr => {
		assertEquals(401, xhr.status, "PUT controller/data");

		return sendHttpRequest("DELETE", "controller/session");
	})

}


// Run all the tests
function testsC() {

	testC1();

}