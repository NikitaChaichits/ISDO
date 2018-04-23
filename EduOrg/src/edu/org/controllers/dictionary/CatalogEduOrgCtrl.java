package edu.org.controllers.dictionary;

import by.i4t.objects.EduOrgOwnershipType;
import by.i4t.objects.EduOrganization;
import by.i4t.objects.EduOrganizationType;
import edu.org.controllers.EduDocCommonCtrl;
import edu.org.models.dictionary.CatalogEduOrgViewModel;
import edu.org.models.lineitems.EduOrgLineItem;
import edu.org.service.EduOrgCatalogService;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@ManagedBean(name = "eduOrgCatalogCtrl")
@SessionScoped
public class CatalogEduOrgCtrl extends EduDocCommonCtrl<CatalogEduOrgViewModel> implements Serializable {
    private static final long serialVersionUID = 7939415746369419320L;
    private EduOrgCatalogService service;

    @PostConstruct
    public void init() {
        super.init();
        service = (EduOrgCatalogService) getServiceFactory().getService("eduOrgCatalogService");
        for (EduOrganization eduOrg : getAppCache().getActualEduOrgList())
            getViewModel().getEduOrgList().add(new EduOrgLineItem(eduOrg));
        getViewModel().setEduOrgOwnershipList(getRepositoryService().getEduOrgOwnershipTypeRepository().findAll());
        getViewModel().setEduOrgTypeList(getRepositoryService().getEduOrganizationTypeRepository().findAll());
    }

    public void createEduOrgAction(ActionEvent actionEvent) {
        getViewModel().setCreateEduOrg(new EduOrganization());
    }

    public void saveNewEduOrgAction(ActionEvent actionEvent) {
        if (getViewModel().getCreateEduOrg().getName() != null) {
            EduOrgOwnershipType ownType = getRepositoryService().getEduOrgOwnershipTypeRepository().findFirstByCode(getViewModel().getEduOrgOwnershipId());
            getViewModel().getCreateEduOrg().setOwnershipType(ownType);
            EduOrganizationType orgType = getRepositoryService().getEduOrganizationTypeRepository().findFirstByCode(getViewModel().getEduOrgTypeId());
            getViewModel().getCreateEduOrg().setOrgType(orgType);
            getViewModel().getCreateEduOrg().setNameVersion(1);
            getViewModel().getCreateEduOrg().setRenameDate(new Date());
            Integer maxCode = 0;
            for (EduOrganization eduOrg : getAppCache().getActualEduOrgList())
                if (maxCode < eduOrg.getCode())
                    maxCode = eduOrg.getCode();
            maxCode++;
            getViewModel().getCreateEduOrg().setCode(maxCode);
            getRepositoryService().getEduOrganizationRepository().save(getViewModel().getCreateEduOrg());

            getAppCache().reloadEduOrgData();
            getViewModel().getEduOrgList().clear();
            for (EduOrganization eduOrg : getAppCache().getActualEduOrgList())
                getViewModel().getEduOrgList().add(new EduOrgLineItem(eduOrg));
            getViewModel().setSelectedOrgLine(new EduOrgLineItem(getViewModel().getCreateEduOrg()));
        }
    }

    public void editEduOrgAction(ActionEvent actionEvent) {
        reloadEduOrgHistory();
        getViewModel().setEditedHistoryOrgLine(new EduOrganization());
    }

    public void onHistoryRowSelect(SelectEvent event) {
        getViewModel().setSelectedHistoryOrgLine((EduOrganization) event.getObject());

        if (getViewModel().getSelectedHistoryOrgLine() != null) {
            getViewModel().setName(getViewModel().getSelectedHistoryOrgLine().getName());
            getViewModel().setShortName(getViewModel().getSelectedHistoryOrgLine().getShortName());
            getViewModel().setNameInBel(getViewModel().getSelectedHistoryOrgLine().getNameInBel());
            getViewModel().setShortNameInBel(getViewModel().getSelectedHistoryOrgLine().getShortNameInBel());
            getViewModel().setUNP(getViewModel().getSelectedHistoryOrgLine().getUNP());
            getViewModel().setRenameDate(getViewModel().getSelectedHistoryOrgLine().getRenameDate());
        } else
            getViewModel().clearEditDataFields();
    }

    public void saveEduOrgHistoryLineAction(ActionEvent actionEvent) {
        if (getViewModel().getSelectedHistoryOrgLine() == null) {
            getViewModel().setSelectedHistoryOrgLine(new EduOrganization());
            getViewModel().getSelectedHistoryOrgLine().setCode(getViewModel().getSelectedOrgLine().getCode());
            getViewModel().getSelectedHistoryOrgLine().setNameVersion(getViewModel().getEduOrgHistory().size() + 1);

            EduOrganization eduOrg = getRepositoryService().getEduOrganizationRepository().findOne(UUID.fromString(getViewModel().getSelectedOrgLine().getID()));
            getViewModel().getSelectedHistoryOrgLine().setOrgType(eduOrg.getOrgType());
            getViewModel().getSelectedHistoryOrgLine().setOwnershipType(eduOrg.getOwnershipType());
        }

        getViewModel().getSelectedHistoryOrgLine().setName(getViewModel().getName());
        getViewModel().getSelectedHistoryOrgLine().setShortName(getViewModel().getShortName());
        getViewModel().getSelectedHistoryOrgLine().setNameInBel(getViewModel().getNameInBel());
        getViewModel().getSelectedHistoryOrgLine().setShortNameInBel(getViewModel().getShortNameInBel());
        getViewModel().getSelectedHistoryOrgLine().setUNP(getViewModel().getUNP());
        getViewModel().getSelectedHistoryOrgLine().setRenameDate(getViewModel().getRenameDate());

        if (getViewModel().getSelectedHistoryOrgLine().getName() == null ||
                getViewModel().getSelectedHistoryOrgLine().getName().trim().isEmpty() ||
                getViewModel().getSelectedHistoryOrgLine().getRenameDate() == null)
            return;
        // throws exception!!!
        if (getViewModel().getSelectedHistoryOrgLine().getID() == null) {
            EduOrganization org = getRepositoryService().getEduOrganizationRepository().save(getViewModel().getSelectedHistoryOrgLine());
            if (org != null) {
                getViewModel().getSelectedHistoryOrgLine().setID(org.getID());
                getViewModel().getEduOrgHistory().add(getViewModel().getSelectedHistoryOrgLine());
            }
        } else
            getRepositoryService().getEduOrganizationRepository().save(getViewModel().getSelectedHistoryOrgLine());
        getAppCache().reloadEduOrgData();
    }

    public void deleteEduOrgAction(ActionEvent actionEvent) {
        if (getViewModel().getSelectedOrgLine() != null && getViewModel().getSelectedOrgLine().getName() != null) {
            for (EduOrganization eduOrg : getRepositoryService().getEduOrganizationRepository().findByCode(getViewModel().getSelectedOrgLine().getCode()))
                getRepositoryService().getEduOrganizationRepository().delete(eduOrg);
            getAppCache().reloadEduOrgData();
            getViewModel().getEduOrgList().clear();
            for (EduOrganization eduOrg : getAppCache().getActualEduOrgList())
                getViewModel().getEduOrgList().add(new EduOrgLineItem(eduOrg));
        }
    }

    protected void reloadEduOrgHistory() {
        getViewModel().getEduOrgHistory().clear();
        getViewModel().getEduOrgHistory().addAll(getAppCache().getEduOrgHistory(getViewModel().getSelectedOrgLine().getCode()));
        getViewModel().setSelectedHistoryOrgLine(new EduOrganization());
        getViewModel().clearEditDataFields();
    }

    public StreamedContent downloadCatalog() {
        return new DefaultStreamedContent(service.buildUploadFile(getAppCache().getAllEduOrg()),
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "edu_org_catalog.xlsx");
    }
}
