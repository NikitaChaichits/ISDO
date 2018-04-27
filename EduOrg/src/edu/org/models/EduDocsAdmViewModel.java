package edu.org.models;

import by.i4t.helper.EduDocsAppLogSettings;
import by.i4t.helper.UserRole;
import by.i4t.objects.Logs;
import edu.org.models.lineitems.*;
import edu.org.utils.ColumnModel;
import lombok.Data;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class EduDocsAdmViewModel implements Serializable, Converter {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    // "Users" panel
    private List<SimpleIntValueLineItem> eduOrgTypeList = new ArrayList<SimpleIntValueLineItem>();
    private Integer selectedEduOrgType;
    private Boolean eduOrgTypeSelectionDisable = false;
    private UserRole selectedUserRole;
    private List<UserRole> userRoleList = new ArrayList<UserRole>();
    private List<UserDataLineItem> userList = new ArrayList<UserDataLineItem>();
    private UserDataLineItem selectedUser;
    private List<ColumnModel> dataTableColumnList;
    private UserDialogViewModel userDialogViewModel;

    // "Settings" panel
    private List<LogSettingsTextItem> settings = new ArrayList<LogSettingsTextItem>();

    // "Log" panel
    private List<EduDocsAppLogSettings> eduDocLogTypeList = new ArrayList<>();
    private String selectedEduDocLogType;
    private List<SimpleStringValueLineItem> logUserList = new ArrayList<>();
    private String selectedLogUser;
    private Date logDate;
    private List<Logs> logTableData = new ArrayList<>();
    private List<ColumnModel> logTableColumnList = new ArrayList<>();

    // "User Notifications" panel
    private List<SimpleIntValueLineItem> eduOrgTypeListN = new ArrayList<SimpleIntValueLineItem>();
    private Integer selectedEduOrgTypeN;
    private Boolean eduOrgTypeSelectionDisableN = false;
    private UserRole selectedUserRoleN;
    private List<UserRole> userRoleListN = new ArrayList<UserRole>();
    private List<UserDataLineItem> userListN = new ArrayList<UserDataLineItem>();
    private UserDataLineItem selectedUserN;
    private List<ColumnModel> dataTableColumnListN;
    private UserDialogViewModel userDialogViewModelN;

    //Sending Message Dialog
    private UploadedFile file;
    private String theme;
    private String message;

    //Notifications Story
    private List<NotificationDataLineItem> notifications = new ArrayList<>();
    private List<ColumnModel> notificationsColumnList;
    private NotificationDataLineItem selectedNotification;



    public boolean getSettingValue(EduDocsAppLogSettings setting) {
        for (LogSettingsTextItem item : settings) {
            if (item.getType() == setting)
                return item.getValue();
        }
        return false;
    }

    public LogSettingsTextItem getLogSettingItemByType(
            EduDocsAppLogSettings setting) {
        if (setting != null) {
            for (LogSettingsTextItem item : settings) {
                if (item.getType() == setting)
                    return item;
            }
            return null;
        } else
            return null;
    }

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                for (SimpleStringValueLineItem item : getUserDialogViewModel()
                        .getEduOrgList())
                    if (item.getValue().equals(value) || item.getName().equals(value))
                        return item;
                return null;
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, "Conversion Error",
                        "Not a valid item."));
            }
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(object);
        } else {
            return null;
        }
    }
}
