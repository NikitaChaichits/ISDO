package edu.org.controllers;

import by.i4t.helper.EduDocsStatus;
import by.i4t.objects.EduOrganization;
import edu.org.auth.SecurityManager;
import edu.org.models.EduDocsStatViewModel;
import edu.org.models.lineitems.AdministratorStatisticsLineItem;
import edu.org.models.lineitems.SimpleIntValueLineItem;
import edu.org.service.EduDocStatService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import java.util.*;
import java.util.Map.Entry;

@ManagedBean(name = "eduDocsStatCtrl")
@SessionScoped
public class EduDocsStatCtrl extends EduDocCommonCtrl<EduDocsStatViewModel> {
    private EduDocStatService statService;

    /**
     * Init ViewModel and preload data.
     */
    @PostConstruct
    public void init() {
        super.init();
        statService = (EduDocStatService) getServiceFactory().getService("statService");
        for (Integer i = Calendar.getInstance().get(Calendar.YEAR); i > 1979; --i)
            getViewModel().getHighEduDocsYearList().add(new SimpleIntValueLineItem(i.toString(), i));

        if (SecurityManager.isAdmin()) {
            getViewModel().setHighEduDocsSelectedYear(Calendar.getInstance().get(Calendar.YEAR));
            loadEduDocsStatByYear();
            loadEduDocsStatByLevel();
        } else {
            loadEduDocsStatByEduOrg();
            loadEduDocsStatByLevel();
        }
    }

    /**
     * Action controller, provides functionality to load high EduDocs
     * statistics.
     *
     * @param actionEvent
     */
    public void updateHighEduDocsStatistics(ActionEvent actionEvent) {
        if (SecurityManager.isAdmin()) {
            loadEduDocsStatByYear();
            loadEduDocsStatByLevel();
        } else {
            loadEduDocsStatByEduOrg();
            loadEduDocsStatByLevel();
        }
    }

    private void loadEduDocsStatByYear() {
        getViewModel().getHighEduDocsStatList().clear();
        Set<Integer> eduOrgCodeSet = new HashSet<>();

//        List<Integer> statusList = new ArrayList<Integer>();
//        statusList.add(EduDocsStatus.VALIDATED.getCode());
//        statusList.add(EduDocsStatus.EXPORTED.getCode());
        //Map<Integer, Integer> correctDocsStatMap = statService.getEduDocsStatByStatusAndYear(statusList, getViewModel().getHighEduDocsSelectedYear());
        Map<Integer, Integer> correctDocsStatMap = statService.getEduDocsStatByYear(getViewModel().getHighEduDocsSelectedYear());
        eduOrgCodeSet.addAll(correctDocsStatMap.keySet());

//	statusList.clear();
//	statusList.add(EduDocsStatus.VALIDATION_ERROR.getCode());
//	statusList.add(EduDocsStatus.EXPORT_ERROR.getCode());
//	statusList.add(EduDocsStatus.DATA_WITHOUT_PERSONAL_ID.getCode());
//	Map<Integer, Integer> incorrectDocsStatMap = statService.getEduDocsStatByStatusAndYear(statusList, getViewModel().getHighEduDocsSelectedYear());
//	eduOrgCodeSet.addAll(incorrectDocsStatMap.keySet());

        for (Integer eduOrgCode : eduOrgCodeSet) {
            Integer correctDocsCount = 0;
            Integer incorrectDocsCount = 0;

            if (correctDocsStatMap.containsKey(eduOrgCode))
                correctDocsCount = correctDocsStatMap.get(eduOrgCode);

//	    if (incorrectDocsStatMap.containsKey(eduOrgCode))
//		incorrectDocsCount = incorrectDocsStatMap.get(eduOrgCode);

            getViewModel().getHighEduDocsStatList().add(
                    new AdministratorStatisticsLineItem(getAppCache().getActualEduOrg(eduOrgCode).getName(), correctDocsCount, incorrectDocsCount));
        }

        for (EduOrganization eduorg : getAppCache().getActualEduOrgList())
            if (!eduOrgCodeSet.contains(eduorg.getCode()))
                getViewModel().getHighEduDocsStatList().add(new AdministratorStatisticsLineItem(eduorg.getName(), 0, 0));
    }

    private void loadEduDocsStatByEduOrg() {
        getViewModel().getHighEduDocsStatList().clear();
        Set<Integer> yearSet = new HashSet<Integer>();

//        List<Integer> statusList = new ArrayList<Integer>();
//	statusList.add(EduDocsStatus.VALIDATED.getCode());
//	statusList.add(EduDocsStatus.EXPORTED.getCode());
        Map<Integer, Integer> correctDocsStatMap = statService.getEduDocsStatByEduOrg(SecurityManager.getUser().getEduOrganization().getCode());
        yearSet.addAll(correctDocsStatMap.keySet());

//	statusList.clear();
//	statusList.add(EduDocsStatus.VALIDATION_ERROR.getCode());
//	statusList.add(EduDocsStatus.EXPORT_ERROR.getCode());
//	statusList.add(EduDocsStatus.DATA_WITHOUT_PERSONAL_ID.getCode());
//	Map<Integer, Integer> incorrectDocsStatMap = statService.getEduDocsStatByEduOrg(statusList, SecurityManager.getUser().getEduOrganization().getCode());
//	yearSet.addAll(incorrectDocsStatMap.keySet());

        for (Integer year : yearSet) {
            Integer correctDocsCount = 0;
            Integer incorrectDocsCount = 0;

            if (correctDocsStatMap.containsKey(year))
                correctDocsCount = correctDocsStatMap.get(year);

//	    if (incorrectDocsStatMap.containsKey(year))
//		incorrectDocsCount = incorrectDocsStatMap.get(year);

            getViewModel().getHighEduDocsStatList().add(new AdministratorStatisticsLineItem(year.toString(), correctDocsCount, incorrectDocsCount));
        }
    }

    private void loadEduDocsStatByLevel() {
        getViewModel().getEduDocsStatByLevelList().clear();
        Map<String, Integer> statMap = statService.getEduDocsStatByLevel(getViewModel().getHighEduDocsSelectedYear());
        for (Entry<String, Integer> entry : statMap.entrySet())
            getViewModel().getEduDocsStatByLevelList().add(new SimpleIntValueLineItem(entry.getKey(), entry.getValue()));
    }
}
