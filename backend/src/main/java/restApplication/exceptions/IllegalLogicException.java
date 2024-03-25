package restApplication.exceptions;


import org.springframework.http.HttpStatus;

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
