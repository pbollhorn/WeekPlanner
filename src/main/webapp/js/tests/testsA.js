// Test "nonexisting" URL endpoint
function testA0() {

	sendHttpRequest("GET", "nonexisting").then(xhr => {
		assertTrue(xhr.status >= 400 && xhr.status <= 499, "GET nonexisting");

		return sendHttpRequest("POST", "nonexisting");
	}).then(xhr => {
		assertTrue(xhr.status >= 400 && xhr.status <= 499, "POST nonexisting");

		return sendHttpRequest("PUT", "nonexisting");
	}).then(xhr => {
		assertTrue(xhr.status >= 400 && xhr.status <= 499, "PUT nonexisting");

		return sendHttpRequest("DELETE", "nonexisting");
	}).then(xhr => {
		assertTrue(xhr.status >= 400 && xhr.status <= 499, "DELETE nonexisting");

	});

}


// Test "controller/nonexisting" URL endpoint
function testA1() {

	sendHttpRequest("GET", "controller/nonexisting").then(xhr => {
		assertEquals(404, xhr.status, "GET controller/nonexisting");

		return sendHttpRequest("POST", "controller/nonexisting");
	}).then(xhr => {
		assertEquals(404, xhr.status, "POST controller/nonexisting");

		return sendHttpRequest("PUT", "controller/nonexisting");
	}).then(xhr => {
		assertEquals(404, xhr.status, "PUT controller/nonexisting");

		return sendHttpRequest("DELETE", "controller/nonexisting");
	}).then(xhr => {
		assertEquals(404, xhr.status, "DELETE controller/nonexisting");

	});

}

// Test login with valid username and valid password 
function testA2() {

	let credentials = {
		username: "testuser1",
		password: "1111"
	};

	sendHttpRequest("POST", "controller/session", "application/json", credentials).then(xhr => {
		assertEquals(200, xhr.status, "Login with valid username and valid password");
	});

}


// Test login with valid username and invalid password
function testA3() {

	credentials = {
		username: "testuser1",
		password: "0000"
	};

	sendHttpRequest("POST", "controller/session", "application/json", credentials).then(xhr => {
		assertEquals(401, xhr.status, "Login with valid username and invalid password");
	});

}


// Test login with invalid username and valid password
function testA4() {

	credentials = {
		username: "testuser0",
		password: "1111"
	};

	sendHttpRequest("POST", "controller/session", "application/json", credentials).then(xhr => {
		assertEquals(401, xhr.status, "Login with invalid username and valid password");
	});

}


// Test login with invalid username and invalid password
function testA5() {

	credentials = {
		username: "testuser0",
		password: "0000"
	};

	sendHttpRequest("POST", "controller/session", "application/json", credentials).then(xhr => {
		assertEquals(401, xhr.status, "Login with invalid username and invalid password");
	});

}


// Test login with inadequate json object
function testA6() {

	credentials = {
	};

	sendHttpRequest("POST", "controller/session", "application/json", credentials).then(xhr => {
		assertEquals(401, xhr.status, "Login with inadequate json object");
	});

}


// Run all the tests
function testsA() {

	testA0();
	testA1();
	testA2();
	testA3();
	testA4();
	testA5();
	testA6();

}

