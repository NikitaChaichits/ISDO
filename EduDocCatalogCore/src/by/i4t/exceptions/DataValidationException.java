package by.i4t.exceptions;

public class DataValidationException extends ISDEBaseException {
    private static final long serialVersionUID = 8298961309073861801L;
    private String fieldName;

    public DataValidationException(String fieldName, String message) {
        super(message);
        setFieldName(fieldName);
    }

    public DataValidationException(String message) {
        super(message);
    }

    /**
     * @return the fieldName
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * @param fieldName the fieldName to set
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
