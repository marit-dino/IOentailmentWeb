package restApplication.exceptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a response to the client in the case of an error.
 */
public class ErrorResponseDTO {
    public List<ErrorDTO> errors;
    public String time;

    public ErrorResponseDTO() {
        this.errors = new ArrayList<>();
    }

    public List<ErrorDTO> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorDTO> errors) {
        this.errors = errors;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void addError(ErrorDTO e) {
        this.errors.add(e);
    }
}
