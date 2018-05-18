package edu.org.controllers;

import by.i4t.exceptions.BusinessConditionException;
import by.i4t.exceptions.DataValidationException;
import by.i4t.helper.EduDocsAppLogSettings;
import by.i4t.helper.UserRole;
import by.i4t.helper.UserStatusEnum;
import by.i4t.log.EduDocsDBAppender;
import by.i4t.log.LogMessage;
import by.i4t.objects.*;
import by.i4t.repository.VUZDocumentRepository;
import by.i4t.repository.search.SearchFilter;
import by.i4t.repository.search.SearchRestriction;
import by.i4t.repository.search.SearchSpecificationBuilder;
import by.i4t.service.CertificateParsingService;
import edu.org.auth.SecurityManager;
import edu.org.models.EduDocsAdmViewModel;
import edu.org.models.UserDialogViewModel;
import edu.org.models.lineitems.*;
import edu.org.service.RepositoryService;
import edu.org.service.VUZDocParsingService;
import edu.org.utils.ColumnModel;
import edu.org.validators.VUZEduDocValidator;
import org.bouncycastle.cms.CMSException;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIInput;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.swing.text.html.parser.Element;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

@ManagedBean(name = "eduDocsAdmCtrl")
@SessionScoped
public class EduDocsAdmCtrl extends EduDocCommonCtrl<EduDocsAdmViewModel> implements Serializable {
    private static final long serialVersionUID = -8383509039735740726L;

    private EduDocsDBAppender dbAppender;
    private VUZDocumentRepository vuzDocumentRepository;
    private RepositoryService repositoryService;

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
        for (EduOrganizationType orgType : getRepositoryService().getEduOrganizationTypeRepository().findAll())
            getViewModel().getEduOrgTypeListN().add(new SimpleIntValueLineItem(orgType.getName(), orgType.getCode()));

    /*    List<User> userList;
        userList = getRepositoryService().getUserRepository().findByRole(UserRole.USER.getCode());

        for (User user : userList)
            getViewModel().getUserListN().add(new UserDataLineItem(user));*/

        EduOrganization eduOrgCurrent = SecurityManager.getUser().getEduOrganization();
        if (eduOrgCurrent != null)
            getViewModel().setSelectedEduOrg(new SimpleStringValueLineItem(eduOrgCurrent.getName(), eduOrgCurrent.getCode().toString()));

        for(Notification notification
                : getRepositoryService().getNotificationRepository().findAllBySenderId(SecurityManager.getUser().getID())){
            getViewModel().getNotifications().add(new NotificationDataLineItem(notification));
        }


        /*for(Notification notification
                : getRepositoryService().getNotificationRepository().findAll()){
            getViewModel().getNotifications().add(new NotificationDataLineItem(notification));
            System.out.println(">>>>>>>>>>>>>>>>" + notification.getTheme());
        }*/

        getViewModel().getUserRoleList().add(UserRole.ADMIN);
        getViewModel().getUserRoleList().add(UserRole.USER);
        getViewModel().setSelectedUserRole(UserRole.USER);

        getViewModel().getUserRoleListN().add(UserRole.ADMIN);
        getViewModel().getUserRoleListN().add(UserRole.USER);
        getViewModel().setSelectedUserRoleN(UserRole.USER);

        getViewModel().setDataTableColumnList(new ArrayList<ColumnModel>());
        getViewModel().getDataTableColumnList().add(new ColumnModel("ФИО", "name"));
        getViewModel().getDataTableColumnList().add(new ColumnModel("Организация", "eduOrganizationName"));
        getViewModel().getDataTableColumnList().add(new ColumnModel("Роль", "role"));
        getViewModel().getDataTableColumnList().add(new ColumnModel("Статус", "status"));

        getViewModel().setDataTableColumnListN(new ArrayList<ColumnModel>());
        getViewModel().getDataTableColumnListN().add(new ColumnModel("ФИО", "name"));
        getViewModel().getDataTableColumnListN().add(new ColumnModel("Организация", "eduOrganizationName"));
        getViewModel().getDataTableColumnListN().add(new ColumnModel("Роль", "role"));
        getViewModel().getDataTableColumnListN().add(new ColumnModel("Статус", "status"));

        getViewModel().setNotificationsColumnList(new ArrayList<ColumnModel>());
        getViewModel().getNotificationsColumnList().add(new ColumnModel("Получатель", "receiverName"));
        getViewModel().getNotificationsColumnList().add(new ColumnModel("Тема", "theme"));
        getViewModel().getNotificationsColumnList().add(new ColumnModel("Дата отправки", "sendingDate"));
        getViewModel().getNotificationsColumnList().add(new ColumnModel("Статус", "status"));

        getViewModel().setUserDialogViewModel(new UserDialogViewModel());
        getViewModel().getUserDialogViewModel().getUserRoleList().add(UserRole.ADMIN);
        getViewModel().getUserDialogViewModel().getUserRoleList().add(UserRole.USER);

        getViewModel().setUserDialogViewModelN(new UserDialogViewModel());
        getViewModel().getUserDialogViewModelN().getUserRoleList().add(UserRole.ADMIN);
        getViewModel().getUserDialogViewModelN().getUserRoleList().add(UserRole.USER);

        for (EduOrganization eduOrg : getAppCache().getActualEduOrgList())
            // getDao().getList(EduOrganization.class))
            getViewModel().getUserDialogViewModel().getEduOrgList().add(new SimpleStringValueLineItem(eduOrg.getName(), eduOrg.getID().toString()));

        for (EduOrganization eduOrg : getAppCache().getActualEduOrgList())
            // getDao().getList(EduOrganization.class))
            getViewModel().getUserDialogViewModelN().getEduOrgList().add(new SimpleStringValueLineItem(eduOrg.getName(), eduOrg.getID().toString()));

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

        getViewModel().setNotChecked1("Не проверено");
//        getViewModel().setNotChecked2("Не проверено"); //для проверки 12 лет
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
        if (UserRole.ADMIN.equals(e.getNewValue())) {
            getViewModel().setEduOrgTypeSelectionDisable(true);
        }
        else {
            getViewModel().setEduOrgTypeSelectionDisable(false);
        }
    }

    public void admPanelUserRoleChangeActionN(ValueChangeEvent e) {
        if (UserRole.ADMIN.equals(e.getNewValue())) {
            getViewModel().setEduOrgTypeSelectionDisableN(true);
        }
        else {
            getViewModel().setEduOrgTypeSelectionDisableN(false);
        }
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

    public void loadUserListActionN(ActionEvent actionEvent) {
        getViewModel().getUserListN().clear();
        /* UserDAO dao = new UserDAO(); */

        List<User> userList;
        userList = getRepositoryService().getUserRepository().findByRoleAndEduOrganization(UserRole.USER.getCode(), getViewModel().getSelectedEduOrg().toString());

        for (User user : userList)
            getViewModel().getUserListN().add(new UserDataLineItem(user));

     /*   if (UserRole.USER.equals(getViewModel().getSelectedUserRoleN()))
            userList = getRepositoryService().getUserRepository().findByRoleAndEduOrganization(UserRole.USER.getCode(), getViewModel().getSelectedEduOrg().toString());
            //userList = getRepositoryService().getUserRepository().findByEduOrgTypeAndRole(getViewModel().getSelectedEduOrgTypeN(), UserRole.USER.getCode());
        else

            userList = getRepositoryService().getUserRepository().findByRole(UserRole.ADMIN.getCode());*/

        /*for (User user : userList)
            getViewModel().getUserListN().add(new UserDataLineItem(user));*/
    }

    /**
     * Add new user action listener.
     *
     * @param actionEvent
     */
    public void addUserAction(ActionEvent actionEvent) {
        getViewModel().getUserDialogViewModel().reset();
    }

    public void addUserActionN(ActionEvent actionEvent) {
        getViewModel().getUserDialogViewModelN().reset();
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

    public void certificatUploadActionN(FileUploadEvent event) {
        if (event.getFile() != null) {
            try {
                Map<String, String> params = CertificateParsingService.parse(event.getFile().getInputstream());
                getViewModel().getUserDialogViewModelN().setCertificatId(params.get("certificatId"));
                getViewModel().getUserDialogViewModelN().setName(params.get("userName"));
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

    public void getSelectedUserN(SelectEvent actionEvent) {
        getViewModel().setSelectedUserN((UserDataLineItem)actionEvent.getObject());
        System.out.println(">>>>>>>> " + (((UserDataLineItem) actionEvent.getObject())).getName());
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

    public void userRoleChangeActionN(ValueChangeEvent e) {
        if (e.getNewValue() != null)
            getViewModel().getUserDialogViewModelN().setUserRole((UserRole) e.getNewValue());
        if (UserRole.ADMIN.equals(e.getNewValue()))
            getViewModel().getUserDialogViewModelN().setIsAdmin(true);
        else
            getViewModel().getUserDialogViewModelN().setIsAdmin(false);
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

    public void fileUploadAction(FileUploadEvent event){
        getViewModel().setFile(event.getFile());
    }

    public void sendMessageAction(){ // add validation
        try {
            Notification notification = new Notification();

            notification.setSenderId(SecurityManager.getUser());
            notification.setReceiverId(getRepositoryService().getUserRepository()
                    .findOne(UUID.fromString(getViewModel().getSelectedUserN().getID())));
            notification.setAttachment(getViewModel().getFile().getContents());
            notification.setSendingDate(new Date());
            notification.setMessage(getViewModel().getMessage());
            notification.setTheme(getViewModel().getTheme());
            notification.setRead(false);
            notification.setStatus(false);
            notification.setAttachType(getViewModel().getFile().getContentType());
            getRepositoryService().getNotificationRepository().save(notification);
            getViewModel().getNotifications().add(new NotificationDataLineItem(notification));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * ***** "Check" panel actions *****
     */
    /**
     * ***** Проверка дубликатов *****
     */
    public void checkDocumentNumber () {
        Integer status = Integer.valueOf(getViewModel().getStatusDocNumber());
        List<String> docList = getRepositoryService().getVuzDocumentRepository().getVUZDocumentByDocNumber(status);

        if (!docList.isEmpty()) {
            for (String docNumber : docList){
                List<VUZDocument> vuzDoc = getRepositoryService().getVuzDocumentRepository().findByDocNumberAndDocSeria(docNumber, "А");
                for (VUZDocument vuzDocument : vuzDoc){
//                    EduDocType docType = repositoryService.getEduDocTypeRepository().findOne(vuzDocument.getDocTypeID());
//                    vuzDocument.setDocType(docType);
                    if (vuzDocument.getStatus()!=0 && !vuzDocument.getDocType().getName().contains("дубликат")){
                        vuzDocument.setStatus(0);
                        vuzDocument.setError("Проверьте номер диплома. Ошибка: диплом с таким номер уже есть в БД");
                        getRepositoryService().getVuzDocumentRepository().save(vuzDocument);
                    }
                }
            }
        }
        getViewModel().setNotChecked1("Проверено");
    }
    /**
     * ***** Проверка поступления до 12 лет *****
     */
//    public void check12years () throws DataValidationException {
//        Integer status = Integer.valueOf(getViewModel().getStatus12Years());
//        List<VUZDocument> docList = getRepositoryService().getVuzDocumentRepository().findByStatus(status);
//
//        VUZEduDocValidator vuzEduDocValidator = new VUZEduDocValidator();
//
//        if (!docList.isEmpty()){
//            for (VUZDocument vuzDocument : docList) {
//                if (!vuzEduDocValidator.checkEduStartDate(vuzDocument.getEduStartDate(), vuzDocument.getCitizen().getIdNumber())){
//                        vuzDocument.setStatus(0);
//                        vuzDocument.setError("Проверьте дату подступление в университет. Ошибка: поступление до 12 лет");
//                        getRepositoryService().getVuzDocumentRepository().save(vuzDocument);
//                }
//            }
//        }
//        getViewModel().setNotChecked2("Проверено");
//    }
//
//    Код ниже должен лежать в adm_check.xhtml
//        <p:panel id="check_panel2" header="Проверка поступления после 12 лет">
//        <h:panelGrid columns="3" cellpadding="5">
//        <h:outputLabel value="Статус документа" />
//        <p:inputText value="#{eduDocsAdmCtrl.viewModel.status12Years}" style="width: 50px"/>
//        <p:commandButton value="Проверить"
//    actionListener="#{eduDocsAdmCtrl.check12years}"
//    update=":#{p:component('check_panel2')}"/>
//        <h:outputLabel value="#{eduDocsAdmCtrl.viewModel.notChecked2}"/>
//        </h:panelGrid>
//        </p:panel>
}
