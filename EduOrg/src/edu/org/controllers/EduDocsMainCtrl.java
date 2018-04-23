package edu.org.controllers;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import by.i4t.exceptions.ImportDataException;
import by.i4t.helper.UserRole;
import by.i4t.objects.Notification;
import edu.org.auth.SecurityManager;
import edu.org.models.EduDocsMainViewModel;
import edu.org.models.lineitems.NotificationDataLineItem;
import edu.org.utils.ColumnModel;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

@ManagedBean(name = "eduDocsMainCtrl")
@SessionScoped
public class EduDocsMainCtrl extends EduDocCommonCtrl<EduDocsMainViewModel> {
    /**
     * Init ViewModel and preload data.
     */
    @PostConstruct
    public void init() {
        super.init();

        getViewModel().setSessionTimeoutInterval(900000);

        if (SecurityManager.isSessionTimeout())
            SecurityManager.resetSessionTimeout();

        if (SecurityManager.isUserAuth()) {
            getViewModel().setIsAuthOK(true);
            getViewModel().setUserName(SecurityManager.getUser().getName());

            if (UserRole.ADMIN.getCode().equals(SecurityManager.getUser().getRole())) {
                getViewModel().setIsAdmin(true);
            } else
                getViewModel().setIsUser(true);
            eduDocsMenuItemAction();
        } else
            getViewModel().setPageLink("/pages/accessDenied.xhtml");

        initNotificationsList();

        getViewModel().getUserNotificationsColumnList().add(new ColumnModel("Статус", "read"));
        getViewModel().getUserNotificationsColumnList().add(new ColumnModel("Отправитель", "senderName"));
        getViewModel().getUserNotificationsColumnList().add(new ColumnModel("Тема", "theme"));
        getViewModel().getUserNotificationsColumnList().add(new ColumnModel("Сообщение", "message"));
        getViewModel().getUserNotificationsColumnList().add(new ColumnModel("Дата отправки", "sendingDate"));
    }

    public void eduDocsMenuItemAction() {
        getViewModel().setPageNumber(0);
        getViewModel().setPageLink("/pages/edu_docs_data.xhtml");
        getViewModel().getDialogs().clear();
        getViewModel().getDialogs().add("/pages/edu_doc_details.xhtml");
        getViewModel().getDialogs().add("/pages/edu_docs_import.xhtml");
    }

    public void eduDocsStatMenuItemAction() {
        getViewModel().setPageNumber(1);
        getViewModel().setPageLink("/pages/docs_stat.xhtml");
        getViewModel().getDialogs().clear();
    }

    public void eduDocsAdmMenuItemAction() {
        getViewModel().setPageNumber(2);
        getViewModel().setPageLink("/pages/adm.xhtml");
        getViewModel().getDialogs().clear();
        getViewModel().getDialogs().add("/pages/adm_user_details.xhtml");
    }

    public void eduDocsExportMenuItemAction() {
        getViewModel().setPageNumber(3);
        getViewModel().setPageLink("/pages/gisun_export.xhtml");
        getViewModel().getDialogs().clear();
        getViewModel().getDialogs().add("/pages/gisun_error_info.xhtml");
    }

    public void dictionaryMenuItemAction() {
        getViewModel().setPageNumber(4);
        getViewModel().setPageLink("/pages/catalogs/catalog.xhtml");
        getViewModel().getDialogs().clear();
        getViewModel().getDialogs().add("/pages/catalogs/catalog_edu_org_dlg.xhtml");
        getViewModel().getDialogs().add("/pages/catalogs/catalog_specialties_dlg.xhtml");
        getViewModel().getDialogs().add("/pages/catalogs/catalog_doc_type_dlg.xhtml");
    }

    public void eduDocsHelpMenuItemAction() {
        getViewModel().setPageNumber(5);
        getViewModel().setPageLink("/pages/help.xhtml");
        getViewModel().getDialogs().clear();
    }

    public void eduExit(){
        SecurityManager.sessionTerminate();
        getViewModel().setPageNumber(6);
        getViewModel().setPageLink("/pages/close_session.xhtml");
        getViewModel().setIsSessionExpired(true);
        getViewModel().getDialogs().clear();
        }

    public void sessionIdleListener() {
        if (!SecurityManager.isSessionTimeout()) {
            SecurityManager.sessionTerminate();
            getViewModel().setPageLink("/pages/empty_page.xhtml");
            getViewModel().setIsSessionExpired(true);
            getViewModel().getDialogs().clear();
        }
    }

    public StreamedContent getImportedFile(NotificationDataLineItem n){
        byte[] attachment = null;

        try {
            attachment = getRepositoryService().getNotificationRepository().findAttachmentByNotificationId(UUID.fromString(n.getId()));
        }catch(NullPointerException e){
            return null;
        }

        String in = new String (n.getReceiverName() + ".xlsx");
        String out = null;

        try {
            out = new String(in.getBytes("UTF-8"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        DefaultStreamedContent content = new DefaultStreamedContent(new ByteArrayInputStream(attachment),
                n.getAttachType(), out);
        return content;
    }

    public void readNotification(NotificationDataLineItem n){
        Notification notification = getRepositoryService().getNotificationRepository().findOne(UUID.fromString(n.getId()));
        notification.setStatus(true);
        notification.setRead(true);
        getRepositoryService().getNotificationRepository().save(notification);
        initNotificationsList();
    }

    private void initNotificationsList(){
        getViewModel().setUserNotifications(new ArrayList<>());
        for (Notification n:
                getRepositoryService().getNotificationRepository().findAllByReceiverId(SecurityManager.getUser().getID())) {
            getViewModel().getUserNotifications().add(new NotificationDataLineItem(n));
        }
    }
}
