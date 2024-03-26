package restApplication.exceptions;


import org.springframework.http.HttpStatus;

/**
 * Exception thrown when an unexpected logic is encountered.
 */
public class IllegalLogicException extends Exception {
    public IllegalLogicException() {
        super();
    }

    public IllegalLogicException(String msg) {
        super(msg);
    }

    public HttpStatus getHTTPStatus() {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }
}
