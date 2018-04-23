package by.i4t.exceptions;

/**
 * Import data from file exception.
 *
 * @author SM
 */
public class ImportDataException extends ISDEBaseException {
    private static final long serialVersionUID = 2399666524491139453L;

    public ImportDataException(String message) {
        super(message);
    }

    public ImportDataException(String message, Exception ex) {
        super(message, ex);
    }
}
