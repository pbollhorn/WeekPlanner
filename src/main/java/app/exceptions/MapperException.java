package app.exceptions;

public class MapperException extends Exception {

    public MapperException(String message) {
        super(message);
        System.out.println("MapperException: " + message);
    }

}