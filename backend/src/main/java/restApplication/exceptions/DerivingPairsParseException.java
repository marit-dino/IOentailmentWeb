package restApplication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import parser.ParseException;

/**
 * Exception thrown when the parsing of the deriving pairs fails.
 */
public class DerivingPairsParseException extends Exception {
    public DerivingPairsParseException() {
        super();
    }

    public DerivingPairsParseException(String msg) {
        super(msg);
    }

    public HttpStatus getHTTPStatus() {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }
}
