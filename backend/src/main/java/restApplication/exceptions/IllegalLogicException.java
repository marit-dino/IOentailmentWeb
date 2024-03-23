package restApplication.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class IllegalLogicException extends RuntimeException{
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
