package weekplanner;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Debugger {

	public static void printRequestInfo(HttpServletRequest request) {

		System.out.println("--- REQUEST OBJECT --------------------------------");
		System.out.println("REQUEST METHOD: " + request.getMethod() + "\n");

		// Print all request header information to screen
		System.out.println("HEADER INFORMATION:");
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			String headerValue = request.getHeader(headerName);
			System.out.println(headerName + ": " + headerValue);
		}
		System.out.println();

		// Print all request parameters to screen
		System.out.println("PARAMETERS:");
		Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			String paramValue = request.getParameter(paramName);
			System.out.println(paramName + ": " + paramValue);
		}
		System.out.println();

		// Print all request attributes to screen
		System.out.println("ATTRIBUTES:");
		Enumeration<String> attributeNames = request.getAttributeNames();
		while (attributeNames.hasMoreElements()) {
			String attributeName = attributeNames.nextElement();
			Object attributeValue = request.getAttribute(attributeName);
			System.out.println(attributeName + ": " + attributeValue);
		}
		System.out.println("---------------------------------------------------");

	}

	public static void printResponseInfo(HttpServletResponse response) {

		// Write the response object information to the console
		System.out.println("--- RESONSE OBJECT INFORMATION---------------------");
		System.out.println("Status Code: " + response.getStatus());
		System.out.println("Content Type: " + response.getContentType());
		System.out.println("Headers:");
		response.getHeaderNames()
				.forEach(headerName -> System.out.println(headerName + ": " + response.getHeader(headerName)));
		System.out.println("Body: " + response.toString());
		System.out.println("---------------------------------------------------");

	}
}
