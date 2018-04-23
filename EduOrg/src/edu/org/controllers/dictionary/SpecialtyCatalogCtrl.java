package edu.org.controllers.dictionary;

import by.i4t.exceptions.BusinessConditionException;
import by.i4t.objects.EduLevel;
import by.i4t.objects.Specialty;
import by.i4t.objects.SpecialtyDirection;
import by.i4t.objects.SpecialtyGroup;
import edu.org.controllers.EduDocCommonCtrl;
import edu.org.models.dictionary.SpecialtyCatalogViewModel;
import edu.org.models.lineitems.SimpleStringValueLineItem;
import edu.org.service.SpecialtyCatalogService;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.util.Comparator;
import java.util.UUID;

@ManagedBean(name = "specialtyCatalogCtrl")
@SessionScoped
public class SpecialtyCatalogCtrl extends EduDocCommonCtrl<SpecialtyCatalogViewModel> implements Serializable {
    private static final long serialVersionUID = 1199342210033423141L;

    private SpecialtyCatalogService service;

    @PostConstruct
    public void init() {
        super.init();
        service = (SpecialtyCatalogService) getServiceFactory().getService("specialtyCatalogService");
        for (EduLevel eduLevel : getAppCache().getEduLevels()) {
            getViewModel().getEduLevelList().add(new SimpleStringValueLineItem(eduLevel.getName(), eduLevel.getID().toString()));
        }
        if (!getViewModel().getEduLevelList().isEmpty()) {
            getViewModel().setSelectedEduLevel(getViewModel().getEduLevelList().get(0));
            loadSpecialtyDirections(getViewModel().getEduLevelList().get(0).getValue());
        }

    }

    public void changeEduLevelAction(ValueChangeEvent event) {
        if (event.getNewValue() == null || "".equals(event.getNewValue()))
            return;
        getViewModel().getSpecialtyDirectionList().clear();
        getViewModel().getSpecialtyGroupList().clear();
        getViewModel().getSpecialtyList().clear();
        getViewModel().setSelectedSpecialtyDirection(null);
        getViewModel().setSelectedSpecialtyGroup(null);
        getViewModel().setSelectedSpec(null);
        loadSpecialtyDirections((String) event.getNewValue());
        if (!getViewModel().getSpecialtyDirectionList().isEmpty()) {
            getViewModel().setSelectedSpecialtyDirection(getViewModel().getSpecialtyDirectionList().get(0));
            loadSpecialtyGroups(getViewModel().getSpecialtyDirectionList().get(0).getValue());
        }
    }

    public void changeSpecialtyDirectionAction(ValueChangeEvent event) {
        if (event.getNewValue() == null || "".equals(event.getNewValue()))
            return;
        getViewModel().getSpecialtyGroupList().clear();
        getViewModel().getSpecialtyList().clear();
        loadSpecialtyGroups((String) event.getNewValue());
        if (!getViewModel().getSpecialtyGroupList().isEmpty()) {
            getViewModel().setSelectedSpecialtyGroup(getViewModel().getSpecialtyGroupList().get(0));
            loadSpecialties(getViewModel().getSpecialtyGroupList().get(0).getValue());
        }
    }

    public void loadSpecialtyDirections(String eduLevelID) {
        if (eduLevelID == null || eduLevelID.isEmpty())
            return;
        getViewModel().getSpecialtyDirectionList().clear();
        getViewModel().setSelectedSpecialtyDirection(null);
        for (SpecialtyDirection specDirection : getAppCache().getSpecialtyDirectionListByEduLevel(eduLevelID))
            getViewModel().getSpecialtyDirectionList().add(new SimpleStringValueLineItem(specDirection.getCode() + " - " + specDirection.getName(), specDirection.getID().toString()));

        if (!getViewModel().getSpecialtyDirectionList().isEmpty()) {
            getViewModel().getSpecialtyGroupList().sort(new Comparator<SimpleStringValueLineItem>() {
                @Override
                public int compare(SimpleStringValueLineItem obj1, SimpleStringValueLineItem obj2) {
                    return obj1.getName().compareTo(obj2.getName());
                }
            });
            getViewModel().setSelectedSpecialtyDirection(getViewModel().getSpecialtyDirectionList().get(0));
            loadSpecialtyGroups(getViewModel().getSpecialtyDirectionList().get(0).getValue());
        }


    }

    public void loadSpecialtyGroups(String specDirectionID) {
        if (specDirectionID == null || specDirectionID.isEmpty())
            return;

        getViewModel().getSpecialtyGroupList().clear();
        getViewModel().setSelectedSpecialtyGroup(null);


        for (SpecialtyGroup specialtyGroup : getAppCache().getSpecialtiesGroupByDirection(UUID.fromString(specDirectionID)))
            getViewModel().getSpecialtyGroupList().add(new SimpleStringValueLineItem(specialtyGroup.getCode() + " - " + specialtyGroup.getName(), specialtyGroup.getID().toString()));
        if (!getViewModel().getSpecialtyGroupList().isEmpty()) {
            getViewModel().getSpecialtyGroupList().sort(new Comparator<SimpleStringValueLineItem>() {
                @Override
                public int compare(SimpleStringValueLineItem obj1, SimpleStringValueLineItem obj2) {
                    return obj1.getName().compareTo(obj2.getName());
                }
            });
            loadSpecialties(getViewModel().getSpecialtyGroupList().get(0).getValue());
        }


    }

    public void onSpecialtyGroupSelect(SelectEvent event) {
        if (event.getObject() == null)
            return;
        getViewModel().getSpecialtyList().clear();
        loadSpecialties(((SimpleStringValueLineItem) event.getObject()).getValue());
    }

    private void loadSpecialties(String specGroupID) {
        if (specGroupID == null || specGroupID.isEmpty())
            return;

        getViewModel().getSpecialtyList().clear();
        getViewModel().setSelectedSpec(null);

        for (Specialty specialty : getAppCache().getSpecialtiesByGroup(UUID.fromString(specGroupID)))
            getViewModel().getSpecialtyList().add(new SimpleStringValueLineItem(specialty.getOKRBCode() + " - " + specialty.getName(), specialty.getID().toString()));
    }

    public void addSpecialtyGroup() {
        resetEditedData();
        RequestContext.getCurrentInstance().update("catalog_specialty_group_dlg_id");
        RequestContext.getCurrentInstance().execute("PF('catalog_specialty_group_dlg').show()");
    }

    public void editSpecialtyGroup() {
        resetEditedData();

        if (getViewModel().getSelectedSpecialtyGroup() == null)
            return;

        SpecialtyGroup specialtyGroup = service.getSpecialtyGroup(getViewModel().getSelectedSpecialtyGroup().getValue());
        if (specialtyGroup != null) {
            getViewModel().setEditedObjectID(specialtyGroup.getID().toString());
            getViewModel().setEditedObjectCode(specialtyGroup.getCode());
            getViewModel().setEditedObjectName(specialtyGroup.getName());
        }

        RequestContext.getCurrentInstance().update("catalog_specialty_group_dlg_id");
        RequestContext.getCurrentInstance().execute("PF('catalog_specialty_group_dlg').show()");
    }

    public void saveSpecialtyGroup() {
        try {
            SpecialtyGroup specialtyGroup = service.saveSpecialtyGroup(getViewModel().getEditedObjectID(), getViewModel().getSelectedSpecialtyDirectionID(),
                    getViewModel().getEditedObjectCode(), getViewModel().getEditedObjectName());

            getAppCache().reloadSpecialtyGroup();

            if (getViewModel().getEditedObjectID() == null) {
                SimpleStringValueLineItem item = new SimpleStringValueLineItem(specialtyGroup.getCode() + " - " + specialtyGroup.getName(), specialtyGroup.getID().toString());
                getViewModel().getSpecialtyGroupList().add(item);
                getViewModel().setSelectedSpecialtyGroup(item);
            } else
                getViewModel().getSelectedSpecialtyGroup().setName(specialtyGroup.getCode() + " - " + specialtyGroup.getName());

            loadSpecialties(specialtyGroup.getID().toString());
            RequestContext.getCurrentInstance().execute("PF('catalog_specialty_group_dlg').hide()");
        } catch (BusinessConditionException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка!", e.getMessage()));
        }
    }

    public void addSpecialty() {
        resetEditedData();
        RequestContext.getCurrentInstance().update("catalog_specialty_dlg_id");
        RequestContext.getCurrentInstance().execute("PF('catalog_specialty_dlg').show()");
    }

    public void editSpecialty() {
        resetEditedData();

        if (getViewModel().getSelectedSpec() == null)
            return;

        Specialty specialty = service.getSpecialty(getViewModel().getSelectedSpec().getValue());
        if (specialty != null) {
            getViewModel().setEditedObjectID(specialty.getID().toString());
            getViewModel().setEditedObjectCode(specialty.getOKRBCode());
            getViewModel().setEditedObjectName(specialty.getName());
        }

        RequestContext.getCurrentInstance().update("catalog_specialty_dlg_id");
        RequestContext.getCurrentInstance().execute("PF('catalog_specialty_dlg').show()");
    }

    public void saveSpecialty() {
        try {
            Specialty specialty = service.saveSpecialty(getViewModel().getEditedObjectID(), getViewModel().getSelectedSpecialtyGroup(),
                    getViewModel().getEditedObjectCode(), getViewModel().getEditedObjectName());

            if (getViewModel().getEditedObjectID() == null) {
                SimpleStringValueLineItem item = new SimpleStringValueLineItem(specialty.getOKRBCode() + " - " + specialty.getName(), specialty.getID().toString());
                getViewModel().getSpecialtyList().add(item);
                getViewModel().setSelectedSpec(item);
            } else
                getViewModel().getSelectedSpec().setName(specialty.getOKRBCode() + " - " + specialty.getName());

            getAppCache().reloadSpecialties();
            RequestContext.getCurrentInstance().execute("PF('catalog_specialty_dlg').hide()");
        } catch (BusinessConditionException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка!", e.getMessage()));
        }
    }

    public void addSpecialtyDirection() {
        resetEditedData();
        RequestContext.getCurrentInstance().update("catalog_specialty_direction_dlg_id");
        RequestContext.getCurrentInstance().execute("PF('catalog_specialty_direction_dlg').show()");
    }

    public void saveSpecialtyDirection() {
        try {
            SpecialtyDirection direction = service.saveSpecialtyDirection(getViewModel().getEditedObjectCode(), getViewModel().getEditedObjectName(), getViewModel().getSelectedEduLevelID());
            getAppCache().reloadSpecialtyDirection();
            loadSpecialtyDirections(getViewModel().getSelectedEduLevelID());
            getViewModel().setSelectedSpecialtyDirectionID(direction.getID().toString());
            RequestContext.getCurrentInstance().execute("PF('catalog_specialty_direction_dlg').hide()");
        } catch (BusinessConditionException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка!", e.getMessage()));
        }
    }

    public StreamedContent downloadCatalog() {
        return new DefaultStreamedContent(service.buildUploadFile(),
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "specialty_catalog.xlsx");
    }

    private void resetEditedData() {
        getViewModel().setEditedObjectID(null);
        getViewModel().setEditedObjectCode(null);
        getViewModel().setEditedObjectName(null);
    }
}
