package edu.org.controllers;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import by.i4t.helper.UserRole;
import edu.org.auth.SecurityManager;
import edu.org.models.EduDocsMainViewModel;

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
}
