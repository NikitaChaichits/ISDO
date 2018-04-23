package edu.org.models.lineitems;

import by.i4t.helper.EduDocsAppLogSettings;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LogSettingsTextItem {
    private EduDocsAppLogSettings type;
    private Boolean value;
    private String text;

    public LogSettingsTextItem(EduDocsAppLogSettings type, Boolean value) {
        this.type = type;
        this.value = value;
        setTextByType(type);
    }

    public void setTextByType(EduDocsAppLogSettings type) {
        switch (type) {
            case ADD_NEW_DOC_ACTION_LOG:
                setText("Добавление новых документов об образовании");
                break;
            case ADD_NEW_USER_ACTION_LOG:
                setText("Добавление нового пользователя");
                break;
            case EDIT_DOC_ACTION_LOG:
                setText("Редактирование информации о документах об образовании");
                break;
            case EDIT_SECURITY_ACTION_LOG:
                setText("Редактирование настроек безопасности");
                break;
            case EDIT_USER_ACTION_LOG:
                setText("Редактирование данных пользователя");
                break;
            case EXPORT_DOC_ACTION_LOG:
                setText("Экспорт информации о документах об образовании");
                break;
            case LOGIN_USER_ACTION_LOG:
                setText("Вход пользователя в систему");
                break;
            case REMOVE_DOC_ACTION_LOG:
                setText("Удаление информации о документах об образовании");
                break;
            case BLOCK_USER_ACTION_LOG:
                setText("Блокировка учётной записи пользователя");
                break;
            default:
                break;

        }
    }
}
