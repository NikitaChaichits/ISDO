package edu.org.models.dictionary;

import edu.org.models.lineitems.SimpleStringValueLineItem;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class SpecialtyCatalogViewModel implements Serializable {
    private static final long serialVersionUID = -6563388284718461419L;

    private List<SimpleStringValueLineItem> eduLevelList = new ArrayList<SimpleStringValueLineItem>();
    private SimpleStringValueLineItem selectedEduLevel;
    private String selectedEduLevelID;

    private List<SimpleStringValueLineItem> specialtyDirectionList = new ArrayList<SimpleStringValueLineItem>();
    private SimpleStringValueLineItem selectedSpecialtyDirection;
    private String selectedSpecialtyDirectionID;

    private List<SimpleStringValueLineItem> specialtyGroupList = new ArrayList<SimpleStringValueLineItem>();
    private SimpleStringValueLineItem selectedSpecialtyGroup;
    private String specialtyGroupID;

    private List<SimpleStringValueLineItem> specialtyList = new ArrayList<SimpleStringValueLineItem>();
    private SimpleStringValueLineItem selectedSpec;
    private String specialtyID;

    private String editedObjectName;
    private String editedObjectCode;
    private String editedObjectID;

}