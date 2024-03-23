package restApplication.exceptions;

public class IllegalLogicException extends IllegalArgumentException{
    public IllegalLogicException() {
        super();
    }

    public IllegalLogicException(String msg) {
        super(msg);
    }
}
