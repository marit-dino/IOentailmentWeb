package restApplication.exceptions;

/**
 * Representation of a thrown exception in the parsing process.
 */
public class ErrorDTO {
    public String message;
    public String cause;
    public String time;

    public ErrorDTO() {
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
