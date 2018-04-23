package edu.org.models;

import edu.org.models.lineitems.NotificationDataLineItem;
import edu.org.utils.ColumnModel;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class EduDocsMainViewModel implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5537939916153916018L;
    private Integer pageNumber = 0;
    private String pageLink;
    private Boolean isUser = false;
    private Boolean isAdmin = false;
    private Boolean isAuthOK = false;
    private Boolean isSessionExpired = false;
    private Integer sessionTimeoutInterval;
    private String userName;
    private List<String> dialogs = new ArrayList<>();

    private List<NotificationDataLineItem> userNotifications = new ArrayList<>();
    private List<ColumnModel> userNotificationsColumnList = new ArrayList<>();
    private NotificationDataLineItem selectedUserNotification;

}
