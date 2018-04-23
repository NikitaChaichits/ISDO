package by.i4t.exceptions;

public class BusinessConditionException extends ISDEBaseException {
    private static final long serialVersionUID = -3911023971589903384L;

    public BusinessConditionException(String message) {
        super(message);
    }

    public BusinessConditionException(String message, Exception exception) {
        super(message, exception);
    }
}
