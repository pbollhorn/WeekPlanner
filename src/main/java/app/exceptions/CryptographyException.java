package app.exceptions;

public class CryptographyException extends Exception {

    public CryptographyException(String message) {
        super(message);
        System.out.println("CryptographyException: " + message);
    }

}