package weekplanner;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class Authenticator {

	public static boolean checkForLogin(HttpServletRequest request) {

		String username = null;
		String password = null;

		Cookie[] theCookies = request.getCookies();

		if (theCookies != null) {
			for (Cookie tempCookie : theCookies) {
				if ("WeekPlannerUsername".equals(tempCookie.getName())) {
					username = tempCookie.getValue();
				} else if ("WeekPlannerPassword".equals(tempCookie.getName())) {
					password = tempCookie.getValue();
				}
			}
		}

		if (username != null && password != null) {
			return true;
		} else {
			return false;
		}

	}

}
