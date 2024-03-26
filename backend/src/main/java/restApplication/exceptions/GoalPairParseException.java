package restApplication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import parser.ParseException;

/**
 * Exception thrown when the parsing of the goal pair fails.
 */
public class GoalPairParseException extends Exception {
    public GoalPairParseException() {
        super();
    }

    public GoalPairParseException(String msg) {
        super(msg);
    }

    public HttpStatus getHTTPStatus() {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }
}
