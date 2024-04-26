package controllers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.security.Key;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import model.Database;

public class Session {

	private static final int SESSION_TIMEOUT_SECONDS = 86400;
	private static final String SECRET_KEY = "ThisIsMySecretKeyIhopeitislongenoughThisIsMySecretKeyIhopeitislongenoughThisIsMySecretKeyIhopeitislongenoughThisIsMySecretKeyIhopeitislongenough";
	private static final String COOKIE_NAME = "JWT";
	
	/**
	 * Gets userId from session, or 0 if there is no session.<br>
	 * Also renews expiration time of JSESSIONID cookie.
	 * 
	 * @param request  The HttpServletRequest object
	 * @param response The HttpServletResponse object
	 * @return userId for session, or 0 if no session
	 */
	public static int getUserId(HttpServletRequest request, HttpServletResponse response) {

		// Get the JWT cookie.
		// And renew the expiration of the cookie, so it is the same as the session timeout.
		Cookie[] cookies = request.getCookies();
		Cookie jwtCookie = null;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (COOKIE_NAME.equals(cookie.getName())) {
					jwtCookie = cookie;
					break;
				}
			}
		}
//		if (jsessionidCookie != null) {
//			jsessionidCookie.setMaxAge(SESSION_TIMEOUT_SECONDS);
//			jsessionidCookie.setPath(request.getContextPath());
//			response.addCookie(jsessionidCookie);
//		}
		
		if(jwtCookie != null) {
			String jwt = jwtCookie.getValue();
			String userIdString = getUserIdFromToken(jwt);
			return Integer.parseInt(userIdString);
		}

		return 0;
	}

	/**
	 * API endpoint POST session. Used for login.<br>
	 * Returns JSESSIONID cookie that expires when browser closes.
	 * 
	 * @param request  The HttpServletRequest object
	 * @param response The HttpServletResponse object
	 */
	public static void post(HttpServletRequest request, HttpServletResponse response) throws IOException {

		// Read JSON data from the request's input stream
		BufferedReader reader = request.getReader();
		JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

		// Use Gson to deserialize the JSON into a Java credentials object
		Credentials credentials = new Gson().fromJson(jsonObject, Credentials.class);

//		// LETS TEST ENCRYPTION
//		try {
//			MessageDigest md = MessageDigest.getInstance("SHA-512");
//			byte[] hashBytes = md.digest(credentials.password.getBytes());
//			byte[] hashBytes2 = md.digest(credentials.password.getBytes());
//			hashBytes2[0] = 5;
//			String hashedPassword = Base64.getEncoder().encodeToString(hashBytes);
//			String hashedPassword2 = Base64.getEncoder().encodeToString(hashBytes);
//			System.out.println(credentials.password);
//			System.out.println(hashedPassword);
//			System.out.println(hashedPassword.length());
//			System.out.println(hashedPassword2);
//			if (Arrays.equals(hashBytes, hashBytes2))
//				System.out.println("de er ens");
//			else
//				System.out.println("de er IKKE ens");
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//			// Handle NoSuchAlgorithmException
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			return;
//		}
//		// LETS TEST ENCRYPTION (END)
		

		// Get userId from database
		int userId = Database.getUserId(credentials);
		if (userId == 0) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		
		// Build the token
        String jwt = Jwts.builder()
                .setSubject(""+userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // Token valid for 1 day
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
        
		// Put token in cookie
		Cookie cookie = new Cookie(COOKIE_NAME, jwt);
		cookie.setPath(request.getContextPath());
		cookie.setMaxAge(60 * 60 * 24 * 365);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		
		response.addCookie(cookie);
		response.setStatus(HttpServletResponse.SC_OK);

	}

	/**
	 * API endpoint DELETE session. Used for logout.
	 * 
	 * @param request  The HttpServletRequest object
	 * @param response The HttpServletResponse object
	 */
	public static void delete(HttpServletRequest request, HttpServletResponse response) {

		
		Cookie cookie = new Cookie(COOKIE_NAME, "");
		cookie.setPath(request.getContextPath());
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		
		
		response.setStatus(HttpServletResponse.SC_OK);

	}
	
	// TEMPORARY
	 public static String getUserIdFromToken(String token) {
	        try {
	            Jws<Claims> claims = Jwts.parser()
	                    .setSigningKey(SECRET_KEY)
	                    .build()
	                    .parseClaimsJws(token);

	            Date expiration = claims.getBody().getExpiration();
	            if (expiration != null && expiration.after(new Date())) {
	                return claims.getBody().getSubject(); // Subject is assumed to be the userId
	            }
	        } catch (Exception e) {
	            // Token validation failed
	            e.printStackTrace();
	        }
	        return null;
	        
	        
	        
	    }

//	    private static SecretKey getSecretKey() {
//	        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
//	    }
	
	


}