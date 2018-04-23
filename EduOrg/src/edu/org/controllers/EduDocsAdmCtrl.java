package edu.org.controllers;

import by.i4t.helper.EduDocsAppLogSettings;
import by.i4t.helper.UserRole;
import by.i4t.helper.UserStatusEnum;
import by.i4t.log.EduDocsDBAppender;
import by.i4t.log.LogMessage;
import by.i4t.objects.EduOrganization;
import by.i4t.objects.EduOrganizationType;
import by.i4t.objects.Logs;
import by.i4t.objects.User;
import by.i4t.repository.search.SearchFilter;
import by.i4t.repository.search.SearchRestriction;
import by.i4t.repository.search.SearchSpecificationBuilder;
import by.i4t.service.CertificateParsingService;
import edu.org.auth.SecurityManager;
import edu.org.models.EduDocsAdmViewModel;
import edu.org.models.UserDialogViewModel;
import edu.org.models.lineitems.LogSettingsTextItem;
import edu.org.models.lineitems.SimpleIntValueLineItem;
import edu.org.models.lineitems.SimpleStringValueLineItem;
import edu.org.models.lineitems.UserDataLineItem;
import edu.org.utils.ColumnModel;
import org.bouncycastle.cms.CMSException;
import org.primefaces.event.FileUploadEvent;
import org.springframework.data.domain.Sort;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

@ManagedBean(name = "eduDocsAdmCtrl")
@SessionScoped
public class EduDocsAdmCtrl extends EduDocCommonCtrl<EduDocsAdmViewModel> implements Serializable {
    private static final long serialVersionUID = -8383509039735740726L;

    private EduDocsDBAppender dbAppender;

    @PostConstruct
    public void init() {
        super.init();
        dbAppender = (EduDocsDBAppender) getServiceFactory().getService("dbAppender");
        initAppLogSettings();
        /*
         * EduOrganizationTypeDAO dao = new EduOrganizationTypeDAO(); for
         * (EduOrganizationType orgType : dao.getAll())
         */
        for (EduOrganizationType orgType : getRepositoryService().getEduOrganizationTypeRepository().findAll())
            getViewModel().getEduOrgTypeList().add(new SimpleIntValueLineItem(orgType.getName(), orgType.getCode()));

        getViewModel().getUserRoleList().add(UserRole.ADMIN);
        getViewModel().getUserRoleList().add(UserRole.USER);
        getViewModel().setSelectedUserRole(UserRole.USER);

        getViewModel().setDataTableColumnList(new ArrayList<ColumnModel>());
        getViewModel().getDataTableColumnList().add(new ColumnModel("ФИО", "name"));
        getViewModel().getDataTableColumnList().add(new ColumnModel("Организация", "eduOrganizationName"));
        getViewModel().getDataTableColumnList().add(new ColumnModel("Роль", "role"));
        getViewModel().getDataTableColumnList().add(new ColumnModel("Статус", "status"));

        getViewModel().setUserDialogViewModel(new UserDialogViewModel());
        getViewModel().getUserDialogViewModel().getUserRoleList().add(UserRole.ADMIN);
        getViewModel().getUserDialogViewModel().getUserRoleList().add(UserRole.USER);

        for (EduOrganization eduOrg : getAppCache().getActualEduOrgList())
            // getDao().getList(EduOrganization.class))
            getViewModel().getUserDialogViewModel().getEduOrgList().add(new SimpleStringValueLineItem(eduOrg.getName(), eduOrg.getID().toString()));

        for (EduDocsAppLogSettings item : EduDocsAppLogSettings.values())
            getViewModel().getEduDocLogTypeList().add(item);

        /*
         * UserDAO userDAO = new UserDAO(); for (User user: userDAO.getAll())
         */
        for (User user : getRepositoryService().getUserRepository().findAll())
            getViewModel().getLogUserList().add(new SimpleStringValueLineItem(user.getName(), user.getID().toString()));

        // add sorting
        Comparator<SimpleStringValueLineItem> comparator = (SimpleStringValueLineItem a, SimpleStringValueLineItem b) ->{
            return a.getName().compareTo(b.getName());
        };

        Collections.sort(getViewModel().getLogUserList(), comparator);

        getViewModel().getLogTableColumnList().add(new ColumnModel("Дата", "actionDate"));
        getViewModel().getLogTableColumnList().add(new ColumnModel("Пользователь", "userInfo"));
        getViewModel().getLogTableColumnList().add(new ColumnModel("Тип события", "description"));
        getViewModel().getLogTableColumnList().add(new ColumnModel("Запись журнала", "message"));
    }

    /**
     * ***** "Users" panel actions *****
     */

    /**
     * UserRole rbtn change item event listener.
     *
     * @param e
     */
    public void admPanelUserRoleChangeAction(ValueChangeEvent e) {
        if (UserRole.ADMIN.equals(e.getNewValue()))
            getViewModel().setEduOrgTypeSelectionDisable(true);
        else
            getViewModel().setEduOrgTypeSelectionDisable(false);
    }

    /**
     * Load users by parameters action listener.
     *
     * @param actionEvent
     */
    public void loadUserListAction(ActionEvent actionEvent) {
        getViewModel().getUserList().clear();
        /* UserDAO dao = new UserDAO(); */

        List<User> userList;
        if (UserRole.USER.equals(getViewModel().getSelectedUserRole()))

            userList = getRepositoryService().getUserRepository().findByEduOrgTypeAndRole(getViewModel().getSelectedEduOrgType(), UserRole.USER.getCode());
        else

            userList = getRepositoryService().getUserRepository().findByRole(UserRole.ADMIN.getCode());

        for (User user : userList)
            getViewModel().getUserList().add(new UserDataLineItem(user));
    }

    /**
     * Add new user action listener.
     *
     * @param actionEvent
     */
    public void addUserAction(ActionEvent actionEvent) {
        getViewModel().getUserDialogViewModel().reset();
    }

    /**
     * Upload personal certificate action listener.
     *
     * @param event
     */
    public void certificatUploadAction(FileUploadEvent event) {
        if (event.getFile() != null) {
            try {
                Map<String, String> params = CertificateParsingService.parse(event.getFile().getInputstream());
                getViewModel().getUserDialogViewModel().setCertificatId(params.get("certificatId"));
                getViewModel().getUserDialogViewModel().setName(params.get("userName"));
            } catch (CMSException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // public void onRowSelect(SelectEvent event)
    // {
    // if (event.getObject() != null)
    // System.out.println(((UserDataLineItem) event.getObject()).getName());
    // else
    // System.out.println("!!!! onRowSelect-NULL !!!!!");
    // }
    //
    // public void unSelectRow(SelectEvent event)
    // {
    // if (event.getObject() != null)
    // System.out.println(((UserDataLineItem) event.getObject()).getName());
    // else
    // System.out.println("!!!! unSelectRow-NULL !!!!!");
    // }

    /**
     * Inits and shows edit user dialog.
     *
     * @param actionEvent
     */
    public void editUserDataAction(ActionEvent actionEvent) {
        getViewModel().getUserDialogViewModel().reset();
        if (getViewModel().getSelectedUser() != null) {
            /*
             * UserDAO dao = new UserDAO();
             * getViewModel().getUserDialogViewModel
             * ().updateFromUser(dao.findByID
             * (UUID.fromString(getViewModel().getSelectedUser().getID())));
             */
            getViewModel().getUserDialogViewModel().updateFromUser(getRepositoryService().getUserRepository().findOne(UUID.fromString(getViewModel().getSelectedUser().getID())));
        }
    }

    /**
     * Save user data action.
     *
     * @param actionEvent
     */
    public void saveUserDataAction(ActionEvent actionEvent) {
        User user;
        if (getViewModel().getUserDialogViewModel().getID() == null)
            user = new User();
        else
            user = getRepositoryService().getUserRepository().findOne(getViewModel().getUserDialogViewModel().getID());

        user.setCertificatId(getViewModel().getUserDialogViewModel().getCertificatId());
        user.setName(getViewModel().getUserDialogViewModel().getName());
        if (getViewModel().getUserDialogViewModel().getEmail() != null && !getViewModel().getUserDialogViewModel().getEmail().trim().isEmpty())
            user.setEmail(getViewModel().getUserDialogViewModel().getEmail());
        if (getViewModel().getUserDialogViewModel().getOfficePhone() != null && !getViewModel().getUserDialogViewModel().getOfficePhone().trim().isEmpty())
            user.setOfficePhone(getViewModel().getUserDialogViewModel().getOfficePhone());
        if (getViewModel().getUserDialogViewModel().getMobilePhone() != null && !getViewModel().getUserDialogViewModel().getMobilePhone().trim().isEmpty())
            user.setMobilePhone(getViewModel().getUserDialogViewModel().getMobilePhone());
        if (getViewModel().getUserDialogViewModel().getNote() != null && !getViewModel().getUserDialogViewModel().getNote().trim().isEmpty())
            user.setNote(getViewModel().getUserDialogViewModel().getNote());
        user.setRole(getViewModel().getUserDialogViewModel().getUserRole().getCode());
        if (getViewModel().getUserDialogViewModel().isUserStatus())
            user.setStatus(UserStatusEnum.BLOCKED.getCode());
        else
            user.setStatus(UserStatusEnum.ACTIVE.getCode());
        if (getViewModel().getUserDialogViewModel().getEduOrg() != null && !"".equals(getViewModel().getUserDialogViewModel().getEduOrg().getValue())) {
            user.setEduOrganization(getRepositoryService().getEduOrganizationRepository().findOne(UUID.fromString(getViewModel().getUserDialogViewModel().getEduOrg().getValue())));
            if (user.getEduOrganization() != null)
                user.setEduOrgType(user.getEduOrganization().getOrgType().getCode());
        }
        if (UserRole.ADMIN.getCode().equals(user.getRole())) {
            user.setExtLogin(getViewModel().getUserDialogViewModel().getExtLogin());
            user.setExtPswrd(getViewModel().getUserDialogViewModel().getExtPswrd());
        }

        getRepositoryService().getUserRepository().save(user);

        if (getViewModel().getUserDialogViewModel().getID() == null) {
            getViewModel().getUserList().add(new UserDataLineItem(user));
            log.info(new LogMessage(SecurityManager.getUser(), EduDocsAppLogSettings.ADD_NEW_USER_ACTION_LOG, user.getName() + " (" + user.getID() + ")"));
        } else {
            getViewModel().getSelectedUser().valueOf(user);
            log.info(new LogMessage(SecurityManager.getUser(), EduDocsAppLogSettings.EDIT_USER_ACTION_LOG, user.getName() + " (" + user.getID() + ")"));
        }
        getViewModel().getUserDialogViewModel().reset();
    }

    /**
     * @param e
     */
    public void userRoleChangeAction(ValueChangeEvent e) {
        if (e.getNewValue() != null)
            getViewModel().getUserDialogViewModel().setUserRole((UserRole) e.getNewValue());
        if (UserRole.ADMIN.equals(e.getNewValue()))
            getViewModel().getUserDialogViewModel().setIsAdmin(true);
        else
            getViewModel().getUserDialogViewModel().setIsAdmin(false);
    }

    /**
     * ***** "Settings" panel actions *****
     */
    public void saveLogSettingsAction(ActionEvent actionEvent) {
        for (LogSettingsTextItem item : getViewModel().getSettings())
            dbAppender.updateAppLogSetting(item.getType(), item.getValue());
        log.info(new LogMessage(SecurityManager.getUser(), EduDocsAppLogSettings.EDIT_SECURITY_ACTION_LOG, EduDocsDBAppender.appLogSettingsToString()));
        /*
         * EduDocDBLogger.getInstance().standardLog( SecurityManager.getUser(),
         * EduDocsAppLogType.EDIT_SECURITY_ACTION.getCode(),
         * EduDocsAppLogType.EDIT_SECURITY_ACTION.getName() + ": " +
         * EduDocsAppLogSettings.valuesToString());
         */
    }

    /**
     * ***** "Log" panel actions *****
     */

    /**
     * Load logs by parameters action listener.
     *
     * @param actionEvent
     */
    public void loadLogAction(ActionEvent actionEvent) {
        getViewModel().getLogTableData().clear();
        List<SearchRestriction> filters = new ArrayList<>();
        if (getViewModel().getSelectedEduDocLogType() != null && !"".equals(getViewModel().getSelectedEduDocLogType()))
            filters.add(new SearchRestriction("actionType", "equals", getViewModel().getSelectedEduDocLogType(), null, null));
        if (getViewModel().getSelectedLogUser() != null && !"".equals(getViewModel().getSelectedLogUser()))
            filters.add(new SearchRestriction("userInfo", "contains", getViewModel().getSelectedLogUser(), null, null));
        Date logDate = getViewModel().getLogDate();
        if (logDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(logDate);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date beginDate = calendar.getTime();

            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            Date endDate = calendar.getTime();
            filters.add(new SearchRestriction("actionDate", "between", null, beginDate, endDate));
        }
        SearchFilter searchFilter = new SearchFilter("AND", filters);
        getViewModel().setLogTableData(getRepositoryService().getLogsRepository().findAll(new SearchSpecificationBuilder<Logs>(searchFilter).build(), new Sort(Sort.Direction.ASC, "actionDate")));
    }

    public void initAppLogSettings() {
        getViewModel().getSettings().add(
                new LogSettingsTextItem(EduDocsAppLogSettings.ADD_NEW_DOC_ACTION_LOG, EduDocsDBAppender.getAppLogSettings().get(EduDocsAppLogSettings.ADD_NEW_DOC_ACTION_LOG)));
        getViewModel().getSettings().add(
                new LogSettingsTextItem(EduDocsAppLogSettings.ADD_NEW_USER_ACTION_LOG, EduDocsDBAppender.getAppLogSettings().get(EduDocsAppLogSettings.ADD_NEW_USER_ACTION_LOG)));
        getViewModel().getSettings().add(
                new LogSettingsTextItem(EduDocsAppLogSettings.EDIT_DOC_ACTION_LOG, EduDocsDBAppender.getAppLogSettings().get(EduDocsAppLogSettings.EDIT_DOC_ACTION_LOG)));
        getViewModel().getSettings().add(
                new LogSettingsTextItem(EduDocsAppLogSettings.BLOCK_USER_ACTION_LOG, EduDocsDBAppender.getAppLogSettings().get(EduDocsAppLogSettings.BLOCK_USER_ACTION_LOG)));
        getViewModel().getSettings().add(
                new LogSettingsTextItem(EduDocsAppLogSettings.EXPORT_DOC_ACTION_LOG, EduDocsDBAppender.getAppLogSettings().get(EduDocsAppLogSettings.EXPORT_DOC_ACTION_LOG)));
        getViewModel().getSettings().add(
                new LogSettingsTextItem(EduDocsAppLogSettings.EDIT_USER_ACTION_LOG, EduDocsDBAppender.getAppLogSettings().get(EduDocsAppLogSettings.EDIT_USER_ACTION_LOG)));
        getViewModel().getSettings().add(
                new LogSettingsTextItem(EduDocsAppLogSettings.REMOVE_DOC_ACTION_LOG, EduDocsDBAppender.getAppLogSettings().get(EduDocsAppLogSettings.REMOVE_DOC_ACTION_LOG)));
        getViewModel().getSettings().add(
                new LogSettingsTextItem(EduDocsAppLogSettings.LOGIN_USER_ACTION_LOG, EduDocsDBAppender.getAppLogSettings().get(EduDocsAppLogSettings.LOGIN_USER_ACTION_LOG)));
        getViewModel().getSettings().add(
                new LogSettingsTextItem(EduDocsAppLogSettings.EDIT_SECURITY_ACTION_LOG, EduDocsDBAppender.getAppLogSettings().get(EduDocsAppLogSettings.EDIT_SECURITY_ACTION_LOG)));
    }
}
