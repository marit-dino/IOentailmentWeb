package restApplication.exceptions;

import parser.ParseException;
public class GoalPairParseException extends ParseException {
    public GoalPairParseException() {
        super();
    }

    public GoalPairParseException(String msg) {
        super(msg);
    }
}
