package by.i4t.helper;

public enum EduDocsStatus {
    VALIDATION_ERROR(0, "Неккоректные данные"),
    DATA_WITHOUT_PERSONAL_ID(1, "Данные не содержат идентификатора физлица"),
    VALIDATED(2, "Корректные данные"),
    EXPORT_ERROR(3, "Ошибка экспорта"),
    EXPORTED(4, "Экспортирован"),
    FOREIGN_STUDENT(5, "Иностранный студент");

    private String name;
    private Integer code;

    EduDocsStatus(Integer code, String name) {
        this.name = name;
        this.code = code;
    }

    public static EduDocsStatus valueByCode(Integer code) {
        if (code == null)
            return null;
        for (EduDocsStatus status : EduDocsStatus.values())
            if (status.getCode().equals(code))
                return status;
        return null;
    }

    public String getName() {
        return name;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String toString() {
        return getCode() + " - " + getName();
    }
}
