package edu.org.controllers;

import by.i4t.helper.EduDocsAppLogSettings;
import by.i4t.helper.GisunExportErrors;
import by.i4t.log.LogMessage;
import by.i4t.objects.*;
import edu.org.auth.SecurityManager;
import edu.org.models.GisunExportViewModel;
import edu.org.models.lineitems.BadExportedEduDocLI;
import edu.org.models.lineitems.SimpleIntValueLineItem;
import edu.org.models.lineitems.SimpleStringValueLineItem;
import edu.org.service.VUZDocParsingService;
import edu.org.utils.ColumnModel;
import org.hibernate.cfg.NotYetImplementedException;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.chart.PieChartModel;
import org.springframework.data.domain.PageRequest;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean(name = "gisunExportCtrl")
@SessionScoped
public class GisunExportCtrl extends EduDocCommonCtrl<GisunExportViewModel> {
    private VUZDocParsingService vuzDocParsingService;

    @PostConstruct
    public void init() {
        super.init();

        vuzDocParsingService = (VUZDocParsingService) getServiceFactory().getService("vuzDocParsingService");
        getViewModel().setServerStatus("работает");
        getViewModel().setExportDataChartModel(new PieChartModel());
        getViewModel().setExportErrorChartModel(new PieChartModel());
        getViewModel().setErrorsByEduOrgList(new ArrayList<SimpleIntValueLineItem>());
        getViewModel().setErrorTypes(new ArrayList<SimpleStringValueLineItem>());
        initExportExceptionTable();
        initBadExportedEduDocs();
        updateDataModel();
    }

    /**
     * Action controller, provides functionality to load info (with error description)
     * about not exported EduDocs.
     *
     * @param actionEvent
     */
    public void loadBadExportedEduDocs(ActionEvent actionEvent) {
        loadBadExportedEduDocs();
    }

    /**
     * Action controller, provides functionality to update info on page.
     */
    public void updateDataModel() {
        loadGisunExportStatistics();
        loadGisunExportErrorStatistics();
        loadExportLog();
        loadExportServiceStatus();
        getViewModel().setExportStatLastUpdate(new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").format(new Date()));
    }

    public void startServerAction(ActionEvent actionEvent) {
        getViewModel().setServerStatus("работает");
        log.info(new LogMessage(SecurityManager.getUser(), EduDocsAppLogSettings.EXPORT_DOC_ACTION_LOG, "старт сервер"));
//		EduDocDBLogger.getInstance().standardLog(
//                SecurityManager.getUser(),
//				EduDocsAppLogType.EXPORT_DOC_ACTION.getCode(),
//				EduDocsAppLogType.EXPORT_DOC_ACTION.getName() + ": старт сервер");
    }

    public void stopServerAction(ActionEvent actionEvent) {
        getViewModel().setServerStatus("остановлен");
        log.info(new LogMessage(SecurityManager.getUser(), EduDocsAppLogSettings.EXPORT_DOC_ACTION_LOG, "стоп сервер"));
/*		EduDocDBLogger.getInstance().standardLog(
                SecurityManager.getUser(),
				EduDocsAppLogType.EXPORT_DOC_ACTION.getCode(), 
				EduDocsAppLogType.EXPORT_DOC_ACTION.getName() + ": стоп сервер");*/
    }

    private void loadGisunExportStatistics() {
        getViewModel().getExportDataChartModel().clear();
        //VUZDocumentDAO dao = new VUZDocumentDAO();
        //Integer tmp = dao.getEduDocsCountByStatus(4);

        Long tmp = getRepositoryService().getVuzDocumentRepository().countByStatus(4);
        getViewModel().getExportDataChartModel().set("Экспортировано (" + tmp + ")", tmp);
        //tmp = dao.getEduDocsCountByStatus(3);
        tmp = getRepositoryService().getVuzDocumentRepository().countByStatus(3);
        getViewModel().getExportDataChartModel().set("Ошибки экспорта (" + tmp + ")", tmp);
        //tmp = dao.getEduDocsCountByStatus(2);
        tmp = getRepositoryService().getVuzDocumentRepository().countByStatus(2);
        getViewModel().getExportDataChartModel().set("Не экспортировано (" + tmp + ")", tmp);
        getViewModel().getExportDataChartModel().setTitle("Текущее состояние экспорта данных");
        getViewModel().getExportDataChartModel().setLegendPosition("e");
        getViewModel().getExportDataChartModel().setShowDataLabels(true);
        getViewModel().getExportDataChartModel().setDataFormat("value");
    }

    private void loadGisunExportErrorStatistics() {
        getViewModel().getExportErrorChartModel().clear();
        getViewModel().getErrorTypes().clear();
        /*GisunExportInfoDAO dao = new GisunExportInfoDAO();
		List<String> errorList = dao.findUniqueExportError();*/
        List<String> errorList = getRepositoryService().getGisunExportInfoRepository().getUniqueExportErrors();
        for (String error : errorList) {
			/*Integer tmp = dao.getErrorsCount(error);*/
            Long tmp = getRepositoryService().getGisunExportInfoRepository().countByErrorCode(error);
            if (GisunExportErrors.valueByCode(error) != null) {
                getViewModel().getExportErrorChartModel().set(GisunExportErrors.valueByCode(error).getName() + " (" + tmp + ")", tmp);
                getViewModel().getErrorTypes().add(new SimpleStringValueLineItem(GisunExportErrors.valueByCode(error).getName() + " (" + tmp + ")", error));
            } else {
                getViewModel().getExportErrorChartModel().set(error + " (" + tmp + ")", tmp);
                getViewModel().getErrorTypes().add(new SimpleStringValueLineItem(error + " (" + tmp + ")", error));
            }
        }
        getViewModel().getExportErrorChartModel().setTitle("Ошибки экспорта данных");
        getViewModel().getExportErrorChartModel().setLegendPosition("e");
        getViewModel().getExportErrorChartModel().setShowDataLabels(true);
        getViewModel().getExportErrorChartModel().setDataFormat("value");
    }

    private void initBadExportedEduDocs() {
        getViewModel().setBadExportedEduDocsTableColumnList(new ArrayList<ColumnModel>());
        getViewModel().getBadExportedEduDocsTableColumnList().add(new ColumnModel("Идентификационный номер", "idNumber"));
        getViewModel().getBadExportedEduDocsTableColumnList().add(new ColumnModel("Учреждение образования", "eduOrgName"));
        getViewModel().getBadExportedEduDocsTableColumnList().add(new ColumnModel("Год начала обучения", "eduStartDate"));
        getViewModel().getBadExportedEduDocsTableColumnList().add(new ColumnModel("Год окончания обучения", "eduStopDate"));
        getViewModel().getBadExportedEduDocsTableColumnList().add(new ColumnModel("Тип документа", "docType"));
        getViewModel().getBadExportedEduDocsTableColumnList().add(new ColumnModel("Регистрационный номер документа", "docRegNumber"));
        getViewModel().getBadExportedEduDocsTableColumnList().add(new ColumnModel("Серия и номер документа", "docSeriaNumber"));
        getViewModel().getBadExportedEduDocsTableColumnList().add(new ColumnModel("Дата выдачи документа", "docIssueDate"));
        getViewModel().getBadExportedEduDocsTableColumnList().add(new ColumnModel("Специальность", "specialty"));
        getViewModel().getBadExportedEduDocsTableColumnList().add(new ColumnModel("Дата экспорта", "exportDate"));
        getViewModel().getBadExportedEduDocsTableColumnList().add(new ColumnModel("Описание ошибки", "error"));

        getViewModel().setBadExportedEduDocsLIList(new ArrayList<BadExportedEduDocLI>());
    }

    private void loadBadExportedEduDocs() {
		/*GisunExportInfoDAO dao = new GisunExportInfoDAO();
		List<GisunExportInfo> docList = dao.getBadExportedEduDocInfo(100);*/
        List<GisunExportInfo> docList = getRepositoryService().getGisunExportInfoRepository().findByStatus("ERROR", new PageRequest(0, 100));
        for (GisunExportInfo doc : docList) {
            BadExportedEduDocLI item = new BadExportedEduDocLI();
            item.updateFrom(doc.getVuzDocument());
            item.setError(doc.getNote());
            item.setExportDate(doc.getExportDate());
            getViewModel().getBadExportedEduDocsLIList().add(item);
        }
    }

    private void initExportExceptionTable() {
        getViewModel().setExportExceptionTableColumns(new ArrayList<ColumnModel>());
        getViewModel().getExportExceptionTableColumns().add(new ColumnModel("Дата и время", "exceptionDate"));
        getViewModel().getExportExceptionTableColumns().add(new ColumnModel("Описание", "message"));

        getViewModel().setExportExceptionTableData(new ArrayList<ExceptionInfo>());
    }

    private void loadExportLog() {
		/*ExceptionInfoDAO dao = new ExceptionInfoDAO();
		List<ExceptionInfo> infoList = dao.findLastExceptions(200);*/
        List<ExceptionInfo> infoList = getRepositoryService().getExceptionInfoRepository().findTop200ByOrderByExceptionDateDesc();
        getViewModel().setExportLog(new StringBuffer());
        for (ExceptionInfo info : infoList)
            getViewModel().getExportLog().append(info.getExceptionDate() + " - " + info.getMessage() + "<br/>");
    }

    //this method is to listen itemSelect event for chart
    public void loadGisunInfoByEduOrgs(ItemSelectEvent event) {
        //here we get label which looks like "EduOrg (error count)"
        String selectedErrorType = (String) getViewModel().getExportErrorChartModel().getData().keySet().toArray()[event.getItemIndex()];
        //here we get we get rid of " (error count)" in label
        String errorName = selectedErrorType.split(" \\([\\d]+\\)")[0];
        String errorCode = null;
        if (GisunExportErrors.valueByName(errorName) != null)
            errorCode = GisunExportErrors.valueByName(errorName).getCode();
        else
            errorCode = errorName;
        loadGisunInfoByEduOrgs(errorCode);

        for (SimpleStringValueLineItem type : getViewModel().getErrorTypes())
            if (type.getName().equalsIgnoreCase(selectedErrorType)) {
                getViewModel().setSelectedErrorType(type.getValue());
                break;
            }
    }

    //this method is to listen submition of the form in gisun info dialog
    public void loadGisunInfoByEduOrgs(ActionEvent actionEvent) {
        //here we get we get rid of " (error count)" in label
        String errorCode = getViewModel().getSelectedErrorType();
        loadGisunInfoByEduOrgs(errorCode);
    }

    public void loadGisunInfoByEduOrgs(String errorCode) {
        getViewModel().getErrorsByEduOrgList().clear();
        //list is sorted by eduOrg name
        List exportInfoList = getRepositoryService().getGisunExportInfoRepository().getCountListByErrorCode(errorCode);
        List<SimpleIntValueLineItem> actualExportInfoList = new ArrayList<SimpleIntValueLineItem>();
        for (Object info : exportInfoList) {
            EduOrganization eduOrg = getAppCache().getActualEduOrg((Integer) ((Object[]) info)[0]);
            if (eduOrg != null)
                actualExportInfoList.add(new SimpleIntValueLineItem(eduOrg.getName(), ((Long) ((Object[]) info)[1]).intValue()));
        }
        getViewModel().setErrorsByEduOrgList(actualExportInfoList);
        //getViewModel().setErrorCodeForCollection(errorCode);
    }

    public void exportFileAction(ActionEvent actionEvent) {
        String fileName = null;
        try {
            fileName = URLEncoder.encode(GisunExportErrors.valueByCode(getViewModel().getSelectedErrorType()).getName(), "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        getViewModel().setExportFileName(fileName);
    }

    public StreamedContent downloadErrorInfo(SimpleIntValueLineItem error) {
        String eduOrgName = error.getName().split(" \\([\\d]+\\)")[0];
        Integer eduOrgCode = getAppCache().getActualEduOrg(eduOrgName).getCode();
        List<VUZDocument> docs = getRepositoryService().getVuzDocumentRepository().getByGisunErrorCodeAndEduOrgCode(getViewModel().getSelectedErrorType(), eduOrgCode);
        String fileName = null;
        try {
            fileName = URLEncoder.encode(GisunExportErrors.valueByCode(getViewModel().getSelectedErrorType()).getName() +
                    " - " + eduOrgName + ".xlsx", "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new DefaultStreamedContent(vuzDocParsingService.bulidXLSXDocument(docs), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", fileName);
    }

    public void retryExportAction(ActionEvent actionEvent) {
        throw new NotYetImplementedException("Пересмотреть экспорт в МВД!!!");
//		getDao().deleteGisunInfoWithCodeUpdateVuzDocWithStatus(getViewModel().getSelectedErrorType(), EduDocsStatus.VALIDATED.getCode());
//		getViewModel().getErrorsByEduOrgList().clear();
//
//		for (SimpleStringValueLineItem item : getViewModel().getErrorTypes())
//			if (item.getValue().equals(getViewModel().getSelectedErrorType())) {
//				String errorName = item.getName().split(" \\([\\d]+\\)")[0];
//				item.setName(errorName.concat(" (0)"));
//				break;
//			}
    }

    public void loadExportServiceStatus() {
        List<ExportServiceLog> exportServiceLogs = getRepositoryService().getExportServiceLogRepository().findAll();
        if (exportServiceLogs.size() == 1) {
            ExportServiceLog status = exportServiceLogs.get(0);
            Date date = new Date();
            if(((date.getTime() - exportServiceLogs.get(0).getDate().getTime()) > 1000 * 20)&&status.isOn()) {
                getViewModel().setIsExportServiceOK(false);
                getViewModel().setIsExportServiceOn(false);
            }
            else {
                getViewModel().setIsExportServiceOK(true);
                getViewModel().setIsExportServiceOn(status.isOn());
            }
            getViewModel().setServerStatus(status.getLog());
        }
//        else
//            throw new ISDEBaseException("Ошибка получения статуса сервиса экспорта");
    }

    public void launchExportService() {
        List<ExportServiceLog> exportServiceLogs = getRepositoryService().getExportServiceLogRepository().findAll();
        if (exportServiceLogs.size() == 1) {
            exportServiceLogs.get(0).setOn(true);
            getRepositoryService().getExportServiceLogRepository().save(exportServiceLogs);
            getViewModel().setIsExportServiceOn(exportServiceLogs.get(0).isOn());
        }
        //        else
//            throw new ISDEBaseException("Ошибка получения статуса сервиса экспорта");
    }

    public void stopExportService() {
        List<ExportServiceLog> exportServiceLogs = getRepositoryService().getExportServiceLogRepository().findAll();
        if (exportServiceLogs.size() == 1) {
            exportServiceLogs.get(0).setOn(false);
            getRepositoryService().getExportServiceLogRepository().save(exportServiceLogs);
            getViewModel().setIsExportServiceOn(exportServiceLogs.get(0).isOn());
        }
        //        else
//            throw new ISDEBaseException("Ошибка получения статуса сервиса экспорта");
    }
}
