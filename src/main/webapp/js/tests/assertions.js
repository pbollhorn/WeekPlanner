// assertEquals function inspired by JUnit
function assertEquals(expected, actual, message) {

	if (expected === actual) {
		console.log("Test successful: " + message + " ==> expected <" + expected + "> was <" + actual + ">");
	} else {
		console.log("Test failed: " + message + " ==> expected <" + expected + "> was <" + actual + ">");
	}

}


// assertTrue function inspired by JUnit
function assertTrue(condition, message) {

	if (condition) {
		console.log("Test successful: " + message + " ==> expected <true> was <true>");
	} else {
		console.log("Test failed: " + message + " ==> expected <true> was <false>");
	}

}