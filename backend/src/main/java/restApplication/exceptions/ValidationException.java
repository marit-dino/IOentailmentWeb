package restApplication.exceptions;

import java.util.List;

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
