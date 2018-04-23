package edu.org.service;

import by.i4t.objects.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.Map.Entry;

/**
 * Internal application cache.
 *
 * @author SM
 */
// @ManagedBean (eager=true, name="appCache")
// @ApplicationScoped
@Service("appCache")
public class ApplicationCache {
    @Autowired
    RepositoryService repositoryService;
    // EduOrganizations cache
    private Map<Integer, List<EduOrganization>> eduOrgMap = new HashMap<Integer, List<EduOrganization>>();
    private List<EduOrganization> actualEduOrgList = new ArrayList<EduOrganization>();

    // Specialty cache
    private List<EduLevel> eduLevels = new ArrayList<>();
    //    private List<SpecialtyProfile> specProfileList = new ArrayList<SpecialtyProfile>();
//    private List<SpecialtyDirection> specialtyDirectionList = new ArrayList<SpecialtyDirection>();
//    private Map<UUID, List<SpecialtyDirection>> specDirectionMap = new HashMap<UUID, List<SpecialtyDirection>>();
    private Map<UUID, List<SpecialtyGroup>> specGroupMap = new HashMap<UUID, List<SpecialtyGroup>>();
    private Map<UUID, List<Specialty>> specialtyMap = new HashMap<UUID, List<Specialty>>();
    private Map<String, Specialty> specialtyByNameMap = new HashMap<String, Specialty>();
    private Map<Integer, Set<SpecialtyDirection>> specialtyDirectionByEduLevel = new HashMap<>();


    public ApplicationCache() {

    }

    @PostConstruct
    public void init() {
        loadEduOrgData();
        loadSpecialtyDirections();
        loadSpecialtyGroups();
        loadSpecialties();
    }

    private void loadSpecialtyDirections() {
//        specProfileList.clear();
//        specDirectionMap.clear();
//        specialtyDirectionList.clear();

        for (SpecialtyDirection specialtyDirection : repositoryService.getSpecialtyDirectionRepository().findAll()) {
//            if (!specDirectionMap.containsKey(specialtyDirection.getProfile().getID())) {
//                specDirectionMap.put(specialtyDirection.getProfile().getID(), new ArrayList<SpecialtyDirection>());
//                specProfileList.add(specialtyDirection.getProfile());
//            }
//            specDirectionMap.get(specialtyDirection.getProfile().getID()).add(specialtyDirection);
            specialtyDirectionByEduLevel.get(specialtyDirection.getEduLevel().getID()).add(specialtyDirection);
//            if (!specialtyDirectionList.contains(specialtyDirection))
//                specialtyDirectionList.add(specialtyDirection);
        }

//        specialtyDirectionList.sort((o1, o2) -> {
//            if (o1.getCode() != null)
//                return o1.getCode().compareTo(o2.getCode());
//            else if (o2.getCode() != null)
//                return o2.getCode().compareTo(o1.getCode());
//            else return 0;
//        });
    }

    private void loadSpecialtyGroups() {
        specGroupMap.clear();

        for (SpecialtyGroup group : repositoryService.getSpecialtyGroupRepository().findAll()) {
            if (!specGroupMap.containsKey(group.getDirection().getID()))
                specGroupMap.put(group.getDirection().getID(), new ArrayList<SpecialtyGroup>());
            specGroupMap.get(group.getDirection().getID()).add(group);
        }
    }

    private void loadSpecialties() {
        specialtyByNameMap.clear();
        specialtyMap.clear();

        for (Specialty specialty : repositoryService.getSpecialtyRepository().findAll()) {
            specialtyByNameMap.put(specialty.getName().toLowerCase(), specialty);

            if (specialty.getGroup() != null) {
                if (!specialtyMap.containsKey(specialty.getGroup().getID()))
                    specialtyMap.put(specialty.getGroup().getID(), new ArrayList<Specialty>());
                specialtyMap.get(specialty.getGroup().getID()).add(specialty);
            }
        }
    }

    //    public List<SpecialtyDirection> getSpecialtyDirectionList() {
//        List<SpecialtyDirection> list = new ArrayList<>();
//        list.addAll(specialtyDirectionList);
//        return list;
//    }
    public List<SpecialtyDirection> getSpecialtyDirectionListByEduLevel(String eduLevelID) {
        List<SpecialtyDirection> list = new ArrayList<>();
        list.addAll(specialtyDirectionByEduLevel.get(Integer.parseInt(eduLevelID)));
        list.sort((o1, o2) -> {
            if (o1.getCode() != null)
                return o1.getCode().compareTo(o2.getCode());
            else if (o2.getCode() != null)
                return o2.getCode().compareTo(o1.getCode());
            else return 0;
        });
        return list;
    }

    private void loadEduOrgData() {
        eduOrgMap.clear();
        actualEduOrgList.clear();
        eduLevels.clear();

        for (EduOrganization eduOrg : repositoryService.getEduOrganizationRepository().findAll()) {
            if (!eduOrgMap.containsKey(eduOrg.getCode()))
                eduOrgMap.put(eduOrg.getCode(), new ArrayList<EduOrganization>());
            eduOrgMap.get(eduOrg.getCode()).add(eduOrg);
        }

        for (Entry<Integer, List<EduOrganization>> entry : eduOrgMap.entrySet()) {
            EduOrganization actualEduOrg = null;
            for (EduOrganization eduOrg : entry.getValue())
                if (actualEduOrg == null || eduOrg.getNameVersion() > actualEduOrg.getNameVersion())
                    actualEduOrg = eduOrg;

            if (actualEduOrg != null)
                actualEduOrgList.add(actualEduOrg);
        }
        for (EduLevel eduLevel : repositoryService.getEduLevelRepository().findAll()) {
            eduLevels.add(eduLevel);
            specialtyDirectionByEduLevel.put(eduLevel.getID(), new HashSet<>());
        }

    }

    public EduOrganization getActualEduOrg(Integer code) {
        if (code == null)
            return null;
        for (EduOrganization eduOrg : actualEduOrgList)
            if (Objects.equals(eduOrg.getCode(), code))
                return eduOrg;
        return null;
    }

    public EduOrganization getActualEduOrg(String name) {
        for (EduOrganization eduOrg : actualEduOrgList)
            if (eduOrg.getName().equals(name))
                return eduOrg;
        return null;
    }

    public List<EduOrganization> getActualEduOrgList() {
        List<EduOrganization> list = new ArrayList<>();
        list.addAll(actualEduOrgList);
        return list;
    }

    public List<EduOrganization> getEduOrgHistory(Integer eduOrgCode) {
        if (eduOrgCode != null)
            return eduOrgMap.get(eduOrgCode);
        else
            return new ArrayList<EduOrganization>();
    }

    public List<EduOrganization> getAllEduOrg() {
        List<EduOrganization> list = new ArrayList<EduOrganization>();
        for (Entry<Integer, List<EduOrganization>> entry : eduOrgMap.entrySet())
            list.addAll(entry.getValue());
        return list;
    }

    public List<SpecialtyGroup> getSpecialtyGroupList() {
        List<SpecialtyGroup> list = new ArrayList<SpecialtyGroup>();
        for (Entry<UUID, List<SpecialtyGroup>> entry : specGroupMap.entrySet())
            list.addAll(entry.getValue());

        list.sort(Comparator.comparing(SpecialtyGroup::getCode));

        return list;
    }

    public List<EduLevel> getEduLevels() {
        ArrayList<EduLevel> list = new ArrayList<>();
        list.addAll(eduLevels);
        return list;
    }

    public List<Specialty> getSpecialtiesByGroup(UUID specGroupID) {
        if (specGroupID != null && specialtyMap.containsKey(specGroupID))
            return specialtyMap.get(specGroupID);
        else
            return new ArrayList<Specialty>();
    }

    public List<SpecialtyGroup> getSpecialtiesGroupByDirection(UUID specDirectionID) {
        if (specGroupMap != null && specGroupMap.containsKey(specDirectionID))
            return specGroupMap.get(specDirectionID);
        else
            return new ArrayList<SpecialtyGroup>();
    }

    public Specialty getSpecialtiesByName(String specialtyName) {
        if (specialtyName != null && specialtyByNameMap.containsKey(specialtyName.toLowerCase()))
            return specialtyByNameMap.get(specialtyName.toLowerCase());
        else
            return null;
    }

    public void reloadSpecialtyDirection(){
        loadSpecialtyDirections();
    }
    public void reloadSpecialtyGroup() {
        loadSpecialtyGroups();
    }

    public void reloadSpecialties() {
        loadSpecialties();
    }

    public void reloadEduOrgData() {
        loadEduOrgData();
    }
}
