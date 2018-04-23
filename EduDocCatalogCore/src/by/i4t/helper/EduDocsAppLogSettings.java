package by.i4t.helper;

public enum EduDocsAppLogSettings {
    ADD_NEW_USER_ACTION_LOG("Создание новой учетной записи пользователя"),
    BLOCK_USER_ACTION_LOG("Блокирование учётной записи пользователя"),
    EDIT_USER_ACTION_LOG("Модификация учетной записи пользователя"),
    LOGIN_USER_ACTION_LOG("Аутентификация пользователя"),
    EDIT_SECURITY_ACTION_LOG("Изменение настроек безопасности"),
    ADD_NEW_DOC_ACTION_LOG("Внесение данных об образовании физических лиц"),
    EDIT_DOC_ACTION_LOG("Модификация данных об образовании физических лиц"),
    EXPORT_DOC_ACTION_LOG("Экспорт данных об образовании физических лиц в ГИС 'Регистр населения'"),
    REMOVE_DOC_ACTION_LOG("Удаление данных об образовании физических лиц");

    private String name;
    private String description;

    EduDocsAppLogSettings(String description) {
        name = String.valueOf(this);
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
