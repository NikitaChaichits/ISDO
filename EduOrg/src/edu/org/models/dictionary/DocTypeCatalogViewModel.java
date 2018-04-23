package edu.org.models.dictionary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.org.models.lineitems.SimpleIntValueLineItem;

public class DocTypeCatalogViewModel implements Serializable {
    private static final long serialVersionUID = -2533211219560445677L;

    private List<SimpleIntValueLineItem> docTypeList = new ArrayList<SimpleIntValueLineItem>();
    private SimpleIntValueLineItem selectedDocType;
    private Integer docTypeID;

    private String editedObjectName;
    private Integer editedObjectID;

    public List<SimpleIntValueLineItem> getDocTypeList() {
        return docTypeList;
    }

    public void setDocTypeList(List<SimpleIntValueLineItem> docTypeList) {
        this.docTypeList = docTypeList;
    }

    public SimpleIntValueLineItem getSelectedDocType() {
        return selectedDocType;
    }

    public void setSelectedDocType(SimpleIntValueLineItem selectedDocType) {
        this.selectedDocType = selectedDocType;
    }

    public Integer getDocTypeID() {
        return docTypeID;
    }

    public void setDocTypeID(Integer docTypeID) {
        this.docTypeID = docTypeID;
    }

    public String getEditedObjectName() {
        return editedObjectName;
    }

    public void setEditedObjectName(String editedObjectName) {
        this.editedObjectName = editedObjectName;
    }

    public Integer getEditedObjectID() {
        return editedObjectID;
    }

    public void setEditedObjectID(Integer editedObjectID) {
        this.editedObjectID = editedObjectID;
    }
}
