package edu.org.service;

import by.i4t.objects.EduLevel;
import by.i4t.objects.EduOrganization;
import edu.org.auth.SecurityManager;
import edu.org.models.lineitems.SimpleStringValueLineItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("statService")
public class EduDocStatService {

    @Autowired
    private RepositoryService repositoryService;
    private List<SimpleStringValueLineItem> eduOrgList = new ArrayList<>();

    public Map<Integer, Integer> getEduDocsStatByStatusAndYear(List<Integer> statusList, Integer year) {
        Map<Integer, Integer> statmap = new HashMap<Integer, Integer>();
        if (statusList == null || statusList.isEmpty())
            return statmap;
        Calendar calendar = Calendar.getInstance();
        Date lo;
        Date hi;
        if (year != null) {
            calendar.clear();
            calendar.set(Calendar.YEAR, year);
            lo = calendar.getTime();
            calendar.add(Calendar.YEAR, 1);
            hi = calendar.getTime();
        } else {
            calendar.set(Calendar.YEAR, 1900);
            lo = calendar.getTime();
            calendar.add(Calendar.YEAR, 2050);
            hi = calendar.getTime();
        }

        List resultList = repositoryService.getVuzDocumentRepository().getCountListByStatusAndPeriodGroupByOrg(statusList.toArray(new Integer[]{}), lo, hi);

        for (int i = 0; i < resultList.size(); ++i) {
            Object[] objArray = (Object[]) resultList.get(i);
            if (statmap.containsKey(((EduOrganization) objArray[0]).getCode()))
                statmap.put(((EduOrganization) objArray[0]).getCode(), ((Long) objArray[1]).intValue() + statmap.get(((EduOrganization) objArray[0]).getCode()));
            else
                statmap.put(((EduOrganization) objArray[0]).getCode(), ((Long) objArray[1]).intValue());
        }
        return statmap;
    }

    public Map<Integer, Integer>  getEduDocsStatByYear(Integer year) {
        Map<Integer, Integer> statmap = new HashMap<Integer, Integer>();

        if (year == null)
            return statmap;

        List resultList = repositoryService.getVuzDocumentRepository().getCountListByYearGroupByOrg(year);

        for (int i = 0; i < resultList.size(); ++i) {
            Object[] objArray = (Object[]) resultList.get(i);
            if (statmap.containsKey(((EduOrganization) objArray[0]).getCode()))
                statmap.put(((EduOrganization) objArray[0]).getCode(), ((Long) objArray[1]).intValue() + statmap.get(((EduOrganization) objArray[0]).getCode()));
            else
                statmap.put(((EduOrganization) objArray[0]).getCode(), ((Long) objArray[1]).intValue());
        }
        return statmap;
    }

    public Map<Integer, Integer> getEduDocsStatByEduOrg(Integer eduOrgCode) {
        Map<Integer, Integer> statmap = new HashMap<Integer, Integer>();
        if (eduOrgCode == null)
            return statmap;
        repositoryService.getVuzDocumentRepository().getStatByOrgGroupByYear(eduOrgCode).forEach(record -> {
            Long year = record.get("year").longValue();
            Long count = record.get("count").longValue();
            if (year != null && count != null)
                statmap.put(year.intValue(), count.intValue());
        });
        return statmap;
    }

    public Map<String, Integer> getEduDocsStatByLevel(Integer year) {
        Map<String, Integer> statmap = new HashMap<String, Integer>();

        if (year == null)
            return statmap;

        //List resultList = repositoryService.getVuzDocumentRepository().getStatByYearGroupByEduLevel(year);

        List resultList = null;

        if (SecurityManager.isAdmin()){
            resultList = repositoryService.getVuzDocumentRepository().getStatByYearGroupByEduLevel(year);
        }else if (!SecurityManager.isAdmin()){
            resultList = repositoryService.getVuzDocumentRepository().getStatByYearGroupByEduLevelAndOrg(
                    year, SecurityManager.getUser().getEduOrganization().getCode());
        }

        for (int i = 0; i < resultList.size(); ++i) {
            Object[] objArray = (Object[]) resultList.get(i);
            //statmap.put(((EduLevel) objArray[0]).getName(), ((Long) objArray[1]).intValue());
            statmap.put((String) objArray[0], ((Long) objArray[1]).intValue());
        }
        return statmap;
    }

    public Map<String, Integer>  getEduDocsStatByPeriod(Date startDate, Date endDate, String code) {
        Map<String, Integer> statmap = new HashMap<String, Integer>();
        if (startDate == null || endDate == null)
            return statmap;

        List resultList = repositoryService.getVuzDocumentRepository().getCountListByPeriod(startDate, endDate, Integer.valueOf(code));
        for (int i = 0; i < resultList.size(); ++i) {
            Object[] objArray = (Object[]) resultList.get(i);
            statmap.put((String) objArray[0], ((Long) objArray[1]).intValue());
        }
        return statmap;
    }

    public Map<String, Integer>  getEduDocsStatByPeriodForAll(Date startDate, Date endDate) {
        Map<String, Integer> statmap = new HashMap<String, Integer>();
        if (startDate == null || endDate == null)
            return statmap;

        List resultList = repositoryService.getVuzDocumentRepository().getCountListByPeriodForAll(startDate, endDate);
        for (int i = 0; i < resultList.size(); ++i) {
            Object[] objArray = (Object[]) resultList.get(i);
            statmap.put((String) objArray[0], ((Long) objArray[1]).intValue());
        }
        return statmap;
    }
}
