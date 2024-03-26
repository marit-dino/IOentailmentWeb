package restApplication.exceptions;

import java.util.List;

/**
 * Is thrown when at least one error occurred during the parsing process.
 */
public class ValidationException extends Exception{
    public List<String[]> errors;
    public ValidationException() {
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(List<String[]> errors) {
        this.errors = errors;
    }
}
