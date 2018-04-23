package by.i4t.exceptions;


/**
 * Abstract exception with base functionality
 *
 * @author SM
 */
public class ISDEBaseException extends Exception {
    private static final long serialVersionUID = -1735228660416335817L;
    private String message;

    public ISDEBaseException(String message) {
        setMessage(message);
    }

    public ISDEBaseException(String message, Exception ex) {
        setMessage(message + "\r\n" + ex.getMessage());
        setStackTrace(ex.getStackTrace());
    }

    @Override
    public String getLocalizedMessage() {
        return getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
