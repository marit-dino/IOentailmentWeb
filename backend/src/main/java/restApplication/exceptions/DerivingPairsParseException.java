package restApplication.exceptions;

import parser.ParseException;
public class DerivingPairsParseException extends ParseException {
    public DerivingPairsParseException() {
        super();
    }

    public DerivingPairsParseException(String msg) {
        super(msg);
    }
}
