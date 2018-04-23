package edu.org.models;

import by.i4t.objects.VUZDocument;
import edu.org.models.lineitems.SimpleIntValueLineItem;
import edu.org.models.lineitems.SimpleStringValueLineItem;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.io.Serializable;
import java.util.*;

@Data
public class EduDocDetailsDialogViewModel implements Serializable, Converter {
    private static final long serialVersionUID = -7366056120777695827L;

    private String ID;
    private String citizenID;
    private String firstName;
    private String secondName;
    private String patronymic;
    private String memberOfBel;
    private List<String> memberList = new ArrayList<>();
    @Setter(AccessLevel.NONE)
    private String personIdNumber;
    private List<SimpleStringValueLineItem> eduOrgList = new ArrayList<SimpleStringValueLineItem>();
    private SimpleStringValueLineItem eduOrg;
    private List<SimpleStringValueLineItem> specialtyGroupList = new ArrayList<SimpleStringValueLineItem>();
    private String specialtyGroupID;
    private List<SimpleStringValueLineItem> specialtyList = new ArrayList<SimpleStringValueLineItem>();
    private SimpleStringValueLineItem selectedSpec;
    private String specialtyID;
    private String specialization;
    private String qualification;
    private Date eduStartDate;
    private Date eduStopDate;
    private List<SimpleIntValueLineItem> eduDocTypeList = new ArrayList<SimpleIntValueLineItem>();
    private Integer docTypeID;
    private String docSeria;
    private String docNumber;
    private Date docIssueDate;
    private String docRegNumber;
    private Boolean isEditable = true;

    public void reset() {
        setID(null);
        setCitizenID(null);
        setFirstName(null);
        setSecondName(null);
        setPatronymic(null);
        setMemberOfBel(null);
        setPersonIdNumber(null);
        setEduOrg(null);
        getSpecialtyList().clear();
        setSpecialtyID(null);
        setSpecialization(null);
        setQualification(null);
        setEduStartDate(null);
        setEduStopDate(null);
        setDocTypeID(null);
        setDocSeria(null);
        setDocNumber(null);
        setDocIssueDate(null);
        setDocRegNumber(null);
        setIsEditable(true);

        memberList.clear();
        memberList.add(0, "да");
        memberList.add(1, "нет");
    }

    public void updateFrom(VUZDocument doc) {
        setID(doc.getID().toString());
        setCitizenID(doc.getCitizen().getID().toString());
        setFirstName(doc.getCitizen().getFirstName());
        setSecondName(doc.getCitizen().getSecondName());
        setPatronymic(doc.getCitizen().getPatronymic());
        setMemberOfBel(doc.getCitizen().getMemberOfBel());
        setPersonIdNumber(doc.getCitizen().getIdNumber());
        setEduOrg(new SimpleStringValueLineItem(doc.getEduOrganization().getName(), doc.getEduOrganization().getID().toString()));
        setSpecialtyGroupID(doc.getSpecialty().getGroup().getID().toString());
        setSpecialtyID(doc.getSpecialty().getID().toString());
        setSpecialization(doc.getSpecializationTXT());
        setQualification(doc.getQualificationTXT());
        setEduStartDate(doc.getEduStartDate());
        setEduStopDate(doc.getEduStopDate());
        setDocTypeID(doc.getDocType().getID());
        setDocSeria(doc.getDocSeria());
        setDocNumber(doc.getDocNumber());
        setDocIssueDate(doc.getDocIssueDate());
        setDocRegNumber(doc.getDocRegNumber());
    }


    public void setPersonIdNumber(String personIdNumber) {
        if (personIdNumber != null)
            this.personIdNumber = personIdNumber.toUpperCase();
        else
            this.personIdNumber = null;
    }


    public List<SimpleStringValueLineItem> completeTextSpec(String query) {
        List<SimpleStringValueLineItem> results = new ArrayList<SimpleStringValueLineItem>();
        String regex = ".*";
        for (int i = 0; i < query.length(); i++)
            regex += "[" + Character.toUpperCase(query.charAt(i))
                    + Character.toLowerCase(query.charAt(i)) + "]";
        regex += ".*";
        for (SimpleStringValueLineItem item : this.specialtyList)
            if (item.getName().matches(regex))
                results.add(item);
        Collections.sort(results, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((SimpleStringValueLineItem) o1).getName().compareTo(
                        ((SimpleStringValueLineItem) o2).getName());
            }
        });
        return results;
    }

    public List<SimpleStringValueLineItem> completeTextEduOrg(String query) {
        List<SimpleStringValueLineItem> results = new ArrayList<SimpleStringValueLineItem>();
        String regex = ".*";
        for (int i = 0; i < query.length(); i++)
            regex += "[" + Character.toUpperCase(query.charAt(i))
                    + Character.toLowerCase(query.charAt(i)) + "]";
        regex += ".*";
        for (SimpleStringValueLineItem item : this.eduOrgList)
            if (item.getName().matches(regex))
                results.add(item);
        Collections.sort(results, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((SimpleStringValueLineItem) o1).getName().compareTo(
                        ((SimpleStringValueLineItem) o2).getName());
            }
        });
        return results;
    }

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                for (SimpleStringValueLineItem item : eduOrgList) {
                    if (item.getValue().equals(value) || item.getName().equals(value))
                        return item;
                }
                return null;
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid item."));
            }
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(object);
        } else {
            return null;
        }
    }
}
