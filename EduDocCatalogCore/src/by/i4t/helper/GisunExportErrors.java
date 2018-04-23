package by.i4t.helper;


public enum GisunExportErrors {
    UNKNOWN_ERROR("01", "Неизвестная ошибка"),
    DATA_VALIDATION_ERROR("02", "Ошибка проверки данных"),
    DATE_FORMAT_ERROR("03", "Неверный формат даты"),
    CLASSIFICATOR_VALUE_NOT_FOUND("04", "Значение классификатора не найдено"),
    PERSONAL_NOT_FOUND("05", "Данные по лицу не найдены"),
    REQUIRED_DATA_NOT_FOUND("06", "Отсутствуют обязательные данные"),
    DATA_DUPLICATION_ERROR("07", "Дублирование данных"),
    AUTH_ERROR("08", "Ошибка авторизации"),
    SIGN_VERFICATION_ERROR("10", "Ошибка проверки ЭЦП");

    private String name;
    private String code;

    GisunExportErrors(String code, String name) {
        this.name = name;
        this.code = code;
    }

    public static GisunExportErrors valueByCode(String code) {
        GisunExportErrors gisunExportError = null;
        for (GisunExportErrors error : GisunExportErrors.values())
            if (error.getCode().equals(code))
                gisunExportError = error;
        return gisunExportError;
    }

    public static GisunExportErrors valueByName(String name) {
        GisunExportErrors gisunExportError = null;
        for (GisunExportErrors error : GisunExportErrors.values())
            if (error.getName().equals(name))
                gisunExportError = error;
        return gisunExportError;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return getCode() + " - " + getName();
    }
}
