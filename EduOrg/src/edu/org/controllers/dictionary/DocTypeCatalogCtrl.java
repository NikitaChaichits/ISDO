package edu.org.controllers.dictionary;

import by.i4t.exceptions.BusinessConditionException;
import by.i4t.objects.EduDocType;
import edu.org.controllers.EduDocCommonCtrl;
import edu.org.models.dictionary.DocTypeCatalogViewModel;
import edu.org.models.lineitems.SimpleIntValueLineItem;
import edu.org.service.DocTypeCatalogService;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean(name = "docTypeCatalogCtrl")
@SessionScoped
public class DocTypeCatalogCtrl extends EduDocCommonCtrl<DocTypeCatalogViewModel> implements Serializable {
    private static final long serialVersionUID = 4333833076947620078L;
    private DocTypeCatalogService service;

    @PostConstruct
    public void init() {
        super.init();
        service = (DocTypeCatalogService) getServiceFactory().getService("docTypeCatalogService");

        loadDocTypes();
    }

    public void loadDocTypes() {
        getViewModel().getDocTypeList().clear();
        getViewModel().getDocTypeList().addAll(service.loadEduDocTypes());
    }

    public void addDocType() {
        resetEditedData();
        RequestContext.getCurrentInstance().update("catalog_doc_type_dlg_id");
        RequestContext.getCurrentInstance().execute("PF('catalog_doc_type_dlg').show()");
    }

    public void editDocType() {
        resetEditedData();

        if (getViewModel().getSelectedDocType() == null)
            return;

        EduDocType eduDocType = service.getEduDocType(getViewModel().getSelectedDocType().getValue());
        if (eduDocType != null) {
            getViewModel().setEditedObjectID(eduDocType.getID());
            getViewModel().setEditedObjectName(eduDocType.getName());
        }

        RequestContext.getCurrentInstance().update("catalog_doc_type_dlg_id");
        RequestContext.getCurrentInstance().execute("PF('catalog_doc_type_dlg').show()");
    }

    public void saveDocType() {
        try {
            EduDocType eduDocType = service.saveEduDocType(getViewModel().getEditedObjectID(), getViewModel().getEditedObjectName());

            if (getViewModel().getEditedObjectID() == null) {
                SimpleIntValueLineItem item = new SimpleIntValueLineItem(eduDocType.getName(), eduDocType.getID());
                getViewModel().getDocTypeList().add(item);
                getViewModel().setSelectedDocType(item);
            } else
                getViewModel().getSelectedDocType().setName(eduDocType.getName());

            loadDocTypes();
            RequestContext.getCurrentInstance().execute("PF('catalog_doc_type_dlg').hide()");
        } catch (BusinessConditionException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка!", e.getMessage()));
        }
    }

    private void resetEditedData() {
        getViewModel().setEditedObjectID(null);
        getViewModel().setEditedObjectName(null);
    }
}
