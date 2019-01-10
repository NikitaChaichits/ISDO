package edu.org.controllers;

import by.i4t.exceptions.BusinessConditionException;
import by.i4t.exceptions.DataValidationException;
import by.i4t.exceptions.ImportDataException;
import by.i4t.helper.EduDocsAppLogSettings;
import by.i4t.helper.EduDocsStatus;
import by.i4t.log.LogMessage;
import by.i4t.objects.*;
import by.i4t.repository.search.SearchFilter;
import by.i4t.repository.search.SearchRestriction;
import by.i4t.repository.search.SearchSpecificationBuilder;
import edu.org.auth.SecurityManager;
import edu.org.models.EduDocDetailsDialogViewModel;
import edu.org.models.EduDocImportedFilesViewModel;
import edu.org.models.EduDocsDataViewModel;
import edu.org.models.lineitems.ImportedFileLineItem;
import edu.org.models.lineitems.SimpleIntValueLineItem;
import edu.org.models.lineitems.SimpleStringValueLineItem;
import edu.org.models.lineitems.NotificationDataLineItem;
import edu.org.service.EduDocDataService;
import edu.org.service.VUZDocParsingService;
import edu.org.service.VUZDocumentTransformer;
import edu.org.utils.ColumnModel;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.data.domain.Sort;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@ManagedBean(name = "eduDocsDataCtrl")@SessionScoped
public class EduDocsDataCtrl extends EduDocCommonCtrl<EduDocsDataViewModel> implements Serializable {
    private static final long serialVersionUID = 1L;
    private VUZDocParsingService vuzDocParsingService;
    private VUZDocumentTransformer vuzDocTransformer;
    private EduDocDetailsDialogViewModel docDetailsViewModel = new EduDocDetailsDialogViewModel();
    private EduDocImportedFilesViewModel importedFilesViewModel = new EduDocImportedFilesViewModel();
    private EduDocDataService docService;
    private List<EduLevel> eduLevels;

    private String sortField = null;
    private Sort.Direction sortOrder = null;

    @PostConstruct
    public void init() {
        super.init();
        eduLevels = getRepositoryService().getEduLevelRepository().findAll();
        vuzDocTransformer = (VUZDocumentTransformer) getServiceFactory().getService("vuzDocTransformer");
        vuzDocParsingService = (VUZDocParsingService) getServiceFactory().getService("vuzDocParsingService");
        setDocService(new EduDocDataService(this));
        if (!SecurityManager.isUserAuth()) {
            getViewModel().setIsAuthOK(false);
            return;
        }

        EduOrganization eduOrg = SecurityManager.getUser().getEduOrganization();
        if (eduOrg != null)
            getViewModel().setSelectedEduOrg(new SimpleStringValueLineItem(eduOrg.getName(), eduOrg.getCode().toString()));

        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ResourceBundle text = context.getApplication().evaluateExpressionGet(context, "#{labels}", ResourceBundle.class);
            getViewModel().getDataTableColumnList().add(new ColumnModel(text.getString("name"), "name"));
            getViewModel().getDataTableColumnList().add(new ColumnModel(text.getString("idNumber"), "idNumber"));
            getViewModel().getDataTableColumnList().add(new ColumnModel(text.getString("eduOrgName"), "eduOrgName"));
            getViewModel().getDataTableColumnList().add(new ColumnModel(text.getString("eduStartDate"), "eduStartDate"));
            getViewModel().getDataTableColumnList().add(new ColumnModel(text.getString("eduStopDate"), "eduStopDate"));
            getViewModel().getDataTableColumnList().add(new ColumnModel(text.getString("docType"), "docType"));
            getViewModel().getDataTableColumnList().add(new ColumnModel(text.getString("docRegNumber"), "docRegNumber"));
            getViewModel().getDataTableColumnList().add(new ColumnModel(text.getString("docSeriaNumber"), "docSeriaNumber"));
            getViewModel().getDataTableColumnList().add(new ColumnModel(text.getString("docIssueDate"), "docIssueDate"));
            getViewModel().getDataTableColumnList().add(new ColumnModel(text.getString("specialty"), "specialty"));
            getViewModel().getDataTableColumnList().add(new ColumnModel(text.getString("specialization"), "specialization"));
            getViewModel().getDataTableColumnList().add(new ColumnModel(text.getString("qualification"), "qualification"));
            getViewModel().getDataTableColumnList().add(new ColumnModel(text.getString("error"), "error"));
        } catch (Exception e) {
            System.err.println("\n\n\n\n\n" + e + "\n\n\n\n\n");
        }

        initDocDetailsViewModel();
    }

    private void initDocDetailsViewModel() {
        for (EduDocType docType : getRepositoryService().getEduDocTypeRepository().findAll())
            getDocDetailsViewModel().getEduDocTypeList().add(new SimpleIntValueLineItem(docType.getName(), docType.getID()));

        if (SecurityManager.isAdmin()) {
            for (EduOrganization eduOrg : getAppCache().getAllEduOrg())
                getDocDetailsViewModel().getEduOrgList().add(new SimpleStringValueLineItem(eduOrg.getName(), eduOrg.getID().toString()));
        } else {
            for (EduOrganization eduOrg : getAppCache().getEduOrgHistory(SecurityManager.getUser().getEduOrganization().getCode()))
                getDocDetailsViewModel().getEduOrgList().add(new SimpleStringValueLineItem(eduOrg.getName(), eduOrg.getID().toString()));
        }

        for (SpecialtyGroup specGroup : getAppCache().getSpecialtyGroupList())
            getDocDetailsViewModel().getSpecialtyGroupList().add(new SimpleStringValueLineItem(specGroup.getCode() + " - " + specGroup.getName(), specGroup.getID().toString()));

        getDocDetailsViewModel().getSpecialtyList().clear();
        if (!getDocDetailsViewModel().getSpecialtyGroupList().isEmpty())
            changeSpecialtyGroup(getDocDetailsViewModel().getSpecialtyGroupList().get(0).getValue());
    }

    /**
     * "Search" action.
     */
    public void loadDataAction() {
        reloadData();
    }

    private void reloadData() {
        if (getDocDetailsViewModel().getSelectedSpec() != null && !"".equals(getDocDetailsViewModel().getSelectedSpec()))
            getViewModel().setSelectedSpec(getDocDetailsViewModel().getSelectedSpec());
    }

    /**
     * "Create" action.
     */
    public void createDataAction() {
        getDocDetailsViewModel().reset();
        if (!getDocDetailsViewModel().getEduOrgList().isEmpty())
            getDocDetailsViewModel().setEduOrg(getDocDetailsViewModel().getEduOrgList().get(0));
        if (!getDocDetailsViewModel().getSpecialtyGroupList().isEmpty())
            changeSpecialtyGroup(getDocDetailsViewModel().getSpecialtyGroupList().get(0).getValue());
        if (!getDocDetailsViewModel().getSpecialtyGroupList().isEmpty())
            getDocDetailsViewModel().setSpecialtyID(getDocDetailsViewModel().getSpecialtyGroupList().get(0).getValue());
    }

    /**
     * "Copy" action.
     */
    public void copyDataAction() {
        if (getViewModel().getSelectedDocLine() == null || getViewModel().getSelectedDocLine().getDocumentID() == null)
            return;

        getDocDetailsViewModel().reset();
        getDocDetailsViewModel().updateFrom(getRepositoryService().getVuzDocumentRepository().findOne(UUID.fromString(getViewModel().getSelectedDocLine().getDocumentID())));
        getDocDetailsViewModel().setCitizenID(null);
        getDocDetailsViewModel().setDocIssueDate(null);
        getDocDetailsViewModel().setDocNumber(null);
        getDocDetailsViewModel().setDocRegNumber(null);
        getDocDetailsViewModel().setDocSeria(null);
        getDocDetailsViewModel().setID(null);
        getDocDetailsViewModel().setFirstName(null);
        getDocDetailsViewModel().setSecondName(null);
        getDocDetailsViewModel().setPatronymic(null);
        getDocDetailsViewModel().setPersonIdNumber(null);
        getDocDetailsViewModel().setMemberOfBel(null);
        if (getDocDetailsViewModel().getSpecialtyGroupID() != null && !"".equals(getDocDetailsViewModel().getSpecialtyGroupID()))
            changeSpecialtyGroup(getDocDetailsViewModel().getSpecialtyGroupID());
    }

    /**
     * "Edit" action.
     */
    public void editDataAction() {
        if (getViewModel().getSelectedDocLine() == null || getViewModel().getSelectedDocLine().getDocumentID() == null)
            return;

        getDocDetailsViewModel().reset();

        VUZDocument doc = getRepositoryService().getVuzDocumentRepository().findOne(UUID.fromString(getViewModel().getSelectedDocLine().getDocumentID()));

//         if (EduDocsStatus.EXPORTED.getCode().equals(doc.getStatus()))
//         {
//         getDocDetailsViewModel().setIsEditable(false);
//         log.info(new LogMessage(SecurityManager.getUser(), EduDocsAppLogSettings.EDIT_DOC_ACTION_LOG,
//            "Модифицирование запрещено " + doc.getCitizen().getIdNumber() + " - "  + doc.getDocSeria() + " " + doc.getDocNumber()));
//         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Редактирование данной записи запрещено!",
//            "Информация экспортирована в 'Регистр населения'."));
//         }

        getDocDetailsViewModel().updateFrom(doc);
        if (getDocDetailsViewModel().getSpecialtyGroupID() != null && !"".equals(getDocDetailsViewModel().getSpecialtyGroupID()))
            changeSpecialtyGroup(getDocDetailsViewModel().getSpecialtyGroupID());

    }

    /**
     * "Save" action.
     */

    public void saveEduDocAction(ActionEvent actionEvent) {
        try {
            if (getDocDetailsViewModel().getID() == null || "".equals(getDocDetailsViewModel().getID())) {
                VUZDocument doc = new VUZDocument();
                vuzDocTransformer.valueOf(doc, getDocDetailsViewModel());
                if (doc.getCitizen().getMemberOfBel().equalsIgnoreCase("нет") || doc.getDocSeria().equalsIgnoreCase("ДИ")){
                    doc.setStatus(EduDocsStatus.FOREIGN_STUDENT.getCode());
                } else
                    doc.setStatus(2);
                vuzDocParsingService.saveEduDoc(doc);
                log.info(new LogMessage(SecurityManager.getUser(), EduDocsAppLogSettings.ADD_NEW_DOC_ACTION_LOG, "Добавлены данные " + doc.getCitizen().getIdNumber() + " - "
                        + doc.getDocSeria() + " " + doc.getDocNumber()));

            } else {
                VUZDocument doc = getRepositoryService().getVuzDocumentRepository().findOne(UUID.fromString(getDocDetailsViewModel().getID()));
                vuzDocTransformer.valueOf(doc, getDocDetailsViewModel());
                if (doc.getCitizen().getMemberOfBel().equalsIgnoreCase("нет") || doc.getDocSeria().equalsIgnoreCase("ДИ")){
                    doc.setStatus(EduDocsStatus.FOREIGN_STUDENT.getCode());
                } else
                    doc.setStatus(2);
                vuzDocParsingService.saveEduDoc(doc);
                getViewModel().getSelectedDocLine().updateFrom(doc);
                log.info(new LogMessage(SecurityManager.getUser(), EduDocsAppLogSettings.EDIT_DOC_ACTION_LOG, "Данные модифицированы " + doc.getCitizen().getIdNumber() + " - "
                        + doc.getDocSeria() + " " + doc.getDocNumber()));
            }

            getDocDetailsViewModel().reset();
            // hiding dialog panel
            RequestContext.getCurrentInstance().execute("PF('edu_doc_dlg').hide()");
        } catch (BusinessConditionException | DataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка сохранения данных", ex.getLocalizedMessage()));
        }
    }

    /**
     * "Delete" action.
     */
    public void deleteEduDocAction() {
        if (getViewModel().getSelectedDocLine() == null || getViewModel().getSelectedDocLine().getDocumentID() == null
                || "".equals(getViewModel().getSelectedDocLine().getDocumentID()))
            return;

        VUZDocument doc = getRepositoryService().getVuzDocumentRepository().findOne(UUID.fromString(getViewModel().getSelectedDocLine().getDocumentID()));


//        if (EduDocsStatus.EXPORTED.getCode().equals(doc.getStatus())) {
//            log.info(new LogMessage(SecurityManager.getUser(), EduDocsAppLogSettings.REMOVE_DOC_ACTION_LOG, "Удаление запрещено " + doc.getCitizen().getIdNumber() + " - "
//                    + doc.getDocSeria() + " " + doc.getDocNumber()));
//            FacesContext.getCurrentInstance().addMessage(null,
//                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Удаление данной записи запрещено!", "Информация экспортирована в ГИС 'Регистр населения'."));
//            return;
//        }

        List<GisunExportInfo> gisun = getRepositoryService().getGisunExportInfoRepository().findByVuzDocument(UUID.fromString(getViewModel().getSelectedDocLine().getDocumentID()));

        if (gisun.size()!=0){
            for (int i=0; i<gisun.size(); i++){
                getRepositoryService().getGisunExportInfoRepository().delete(gisun.get(i).getUUID());
            }
        }
        getRepositoryService().getVuzDocumentRepository().delete(UUID.fromString(getViewModel().getSelectedDocLine().getDocumentID()));
        getRepositoryService().getCitizenRepository().delete(doc.getCitizen().getID());

        log.info(new LogMessage(SecurityManager.getUser(), EduDocsAppLogSettings.REMOVE_DOC_ACTION_LOG, "Запись удалена " + getViewModel().getSelectedDocLine().getIdNumber()
                + " - " + getViewModel().getSelectedDocLine().getDocSeriaNumber()));

        getViewModel().setSelectedDocLine(null);
    }

    /**
     * "Import" action.
     */

    public void importDataAction() {
        loadImportedFiles();
    }


    public void importEduDocsAction(FileUploadEvent event) {
        if (event.getFile() != null) {
            try {
                ImportedFile file = new ImportedFile();
                file.setUploadDate(new Date());
                file.setUser(SecurityManager.getUser());
                file.setStatus(0);

                if (SecurityManager.isAdmin())
                    throw new ImportDataException("File imported by Admin!");
                else
                    file.setEduOrg(SecurityManager.getUser().getEduOrganization());

                if (event.getFile().getFileName() != null) {
                    byte[] nameBytes = event.getFile().getFileName().getBytes("UTF8");
                    file.setImportedFileName(new String(nameBytes, "UTF8"));
                } else
                    file.setImportedFileName("Imported file");

                file.setContentType(event.getFile().getContentType());

                FileContent importedFileContent = new FileContent();
                importedFileContent.setID(UUID.randomUUID());
                importedFileContent.setContent(event.getFile().getContents());
                getRepositoryService().getFileContentRepository().save(importedFileContent);
                file.setImportedFileContent(importedFileContent.getID());

                getRepositoryService().getImportedFileRepository().save(file);

                loadImportedFiles();
            } catch (ImportDataException e) {
                showErrorMsg(e);
            } catch (UnsupportedEncodingException e) {
                showErrorMsg(e);
            }
        }
    }

    public void changeSpecialtyGroupAction(ValueChangeEvent event) {
        if (event.getNewValue() == null || "".equals(event.getNewValue()))
            return;

        changeSpecialtyGroup((String) event.getNewValue());
    }

    private void changeSpecialtyGroup(String specialtyGroupID) {
        getDocDetailsViewModel().getSpecialtyList().clear();
        for (Specialty specialty : getAppCache().getSpecialtiesByGroup(UUID.fromString(specialtyGroupID)))
            getDocDetailsViewModel().getSpecialtyList().add(new SimpleStringValueLineItem(specialty.getOKRBCode() + " - " + specialty.getName(), specialty.getID().toString()));

        getDocDetailsViewModel().getSpecialtyList().sort(new Comparator<SimpleStringValueLineItem>() {
            @Override
            public int compare(SimpleStringValueLineItem obj1, SimpleStringValueLineItem obj2) {
                return obj1.getName().compareTo(obj2.getName());
            }
        });
    }

    private void loadImportedFiles() {
        getImportedFilesViewModel().getImportedFilesList().clear();

        EduOrganization eduOrg = SecurityManager.getUser().getEduOrganization();
        if (eduOrg == null)
            for (ImportedFile impFile : getRepositoryService().getImportedFileRepository().findAll())
                getImportedFilesViewModel().getImportedFilesList().add(new ImportedFileLineItem(impFile));
        else
            for (ImportedFile impFile : getRepositoryService().getImportedFileRepository().findByEduOrg(eduOrg))
                getImportedFilesViewModel().getImportedFilesList().add(new ImportedFileLineItem(impFile));
    }

    public EduDocDetailsDialogViewModel getDocDetailsViewModel() {
        return docDetailsViewModel;
    }

    public void setDocDetailsViewModel(EduDocDetailsDialogViewModel docDetailsViewModel) {
        this.docDetailsViewModel = docDetailsViewModel;
    }

    public EduDocImportedFilesViewModel getImportedFilesViewModel() {
        return importedFilesViewModel;
    }

    public void setImportedFilesViewModel(EduDocImportedFilesViewModel importedFilesViewModel) {
        this.importedFilesViewModel = importedFilesViewModel;
    }

    public StreamedContent getImportedFile(ImportedFileLineItem file) {
        ImportedFile impFile = getRepositoryService().getImportedFileRepository().findOne(file.getDbId());
        FileContent impFileContent = getRepositoryService().getFileContentRepository().findOne(impFile.getImportedFileContent());
        String fileName = "";
        try {
            fileName = URLEncoder.encode(impFile.getImportedFileName(), "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new DefaultStreamedContent(new ByteArrayInputStream(impFileContent.getContent()), impFile.getContentType(), fileName);
    }

    public StreamedContent getErrorsFile(ImportedFileLineItem file) {
        ImportedFile impFile = getRepositoryService().getImportedFileRepository().findOne(file.getDbId());
        FileContent impFileContent = getRepositoryService().getFileContentRepository().findOne(impFile.getErrorFileContent());
        String fileName = "";
        try {
            fileName = URLEncoder.encode(file.getErrorsFileName(), "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // xls file
        return new DefaultStreamedContent(new ByteArrayInputStream(impFileContent.getContent()), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", fileName);
    }


    public SearchFilter getSearchFilter() {

        List<SearchRestriction> filters = new ArrayList<>();

        String secondName = getViewModel().getSecondName();
        if (secondName != null && !secondName.isEmpty()) {
            filters.add(new SearchRestriction("citizen.secondName", "equals", secondName, null, null));
        }

        String firstName = getViewModel().getFirstName();
        if (firstName != null && !firstName.isEmpty()) {
            filters.add(new SearchRestriction("citizen.firstName", "equals", firstName, null, null));
        }

        String patronimyc = getViewModel().getPatronimyc();
        if (patronimyc != null && !patronimyc.isEmpty()) {
            filters.add(new SearchRestriction("citizen.patronymic", "equals", patronimyc, null, null));
        }

        String id = getViewModel().getPersonalIDNumber();
        if (id != null && !id.isEmpty()) {
            filters.add(new SearchRestriction("citizen.idNumber", "equals", id, null, null));
        }


        SimpleStringValueLineItem org = getViewModel().getSelectedEduOrg();
        if (org != null && org.getValue() != null) {
            try {
                List<EduOrganization> orgs = getAppCache().getEduOrgHistory(Integer.parseInt(org.getValue()));
                List<UUID> ids = new ArrayList<UUID>();
                for (EduOrganization o : orgs) {
                    ids.add(o.getID());
                }
                filters.add(new SearchRestriction("eduOrganization.ID", "in", ids, null, null));
            } catch (NumberFormatException e) {
            }
        }

        String eduLevelName = getViewModel().getSelectedEduLevel();
        if (eduLevelName != null) {
            int i;
            for (i = 0; i < eduLevels.size(); ++i) {
                if (eduLevels.get(i).getName().equals(eduLevelName))
                    break;
            }
            filters.add(new SearchRestriction("docType.eduLevel", "equals", eduLevels.get(i), null, null));
        }

        String docSeria = getViewModel().getDocSeria();
        if (docSeria != null && !docSeria.isEmpty()) {
            filters.add(new SearchRestriction("docSeria", "equals", docSeria, null, null));
        }

        String docNumber = getViewModel().getDocNumber();
        if (docNumber != null && !docNumber.isEmpty()) {
            filters.add(new SearchRestriction("docNumber", "equals", docNumber, null, null));
        }

        Integer year = getViewModel().getSelectedEduDocsDate();
        if (year != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(Calendar.YEAR, year);
            Date lo = calendar.getTime();
            calendar.add(Calendar.YEAR, 1);
            Date hi = calendar.getTime();
            filters.add(new SearchRestriction("docIssueDate", "period", null, lo, hi));
        }

        String docRegNumber = getViewModel().getDocRegNumber();
        if (docRegNumber != null && !docRegNumber.isEmpty()) {
            filters.add(new SearchRestriction("docRegNumber", "equals", docRegNumber, null, null));
        }


        EduDocsStatus status = getViewModel().getSelectedEduDocsStatus();
        if (status != null) {
            filters.add(new SearchRestriction("status", "equals", status.getCode(), null, null));
        }

        String specialtyName = getViewModel().getSpecName();
        if (specialtyName != null && !specialtyName.isEmpty()) {
            filters.add(new SearchRestriction("specialty.name", "equals", specialtyName, null, null));
        }

        return new SearchFilter("AND", filters);
    }

    public EduDocsStatus[] getStatuses() {
        return EduDocsStatus.values();
    }

    public String[] getEduLevels() {
        String[] simpleStringValueLineItems = new String[eduLevels.size()];
        for (int i = 0; i < eduLevels.size(); ++i) {
            simpleStringValueLineItems[i] = eduLevels.get(i).getName();
        }
        return simpleStringValueLineItems;
    }

    public EduDocDataService getDocService() {
        return docService;
    }

    public void setDocService(EduDocDataService docService) {
        this.docService = docService;
    }

    /**
     * "Export" action.
     */

    public StreamedContent exportVuzDocDataToXlsxAction() {
        List<VUZDocument> docs = getRepositoryService().getVuzDocumentRepository().findAll(
                new SearchSpecificationBuilder<VUZDocument>(this.getSearchFilter()).build(), new Sort(sortOrder, sortField));
        String fileName = null;
        try {
            fileName = URLEncoder.encode("Docs" + ".xls", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new DefaultStreamedContent(vuzDocParsingService.bulidXLSXDocument(docs), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", fileName);
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public void setSortOrder(Sort.Direction sortOrder) {
        this.sortOrder = sortOrder;
    }
}
