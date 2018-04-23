package edu.org.models.lineitems;

import by.i4t.objects.VUZDocument;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
public class EduDocLineItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private String documentID;
    private String name;
    private String idNumber;
    private String memberOfBel;
    private String eduOrgName;
    private Date eduStartDate;
    private Date eduStopDate;
    private String docType;
    private String docSeriaNumber;
    private Date docIssueDate;
    private String docRegNumber;
    private String specialty;
    private String specialization;
    private String qualification;
    private String importErrorMsg;

    public EduDocLineItem() {

    }

    public EduDocLineItem(VUZDocument doc) {
        updateFrom(doc);
    }


    public void updateFrom(VUZDocument doc) {
        if (doc == null)
            return;

        setDocumentID(doc.getID().toString());
        if (doc.getCitizen() != null) {
            setName((doc.getCitizen().getSecondName() != null ? doc.getCitizen().getSecondName() : "") + " " +
                    (doc.getCitizen().getFirstName() != null ? doc.getCitizen().getFirstName() : "") + " " +
                    (doc.getCitizen().getPatronymic() != null ? doc.getCitizen().getPatronymic() : ""));
            setIdNumber(doc.getCitizen().getIdNumber());
            setMemberOfBel(doc.getCitizen().getMemberOfBel());
        }
        if (doc.getEduOrganization() != null)
            setEduOrgName(doc.getEduOrganization().getName());
        setEduStartDate(doc.getEduStartDate());
        setEduStopDate(doc.getEduStopDate());
        if (doc.getDocType() != null)
            setDocType(doc.getDocType().getName());
        if (doc.getDocSeria() != null && !"".equals(doc.getDocSeria()))
            setDocSeriaNumber(doc.getDocSeria() + "-" + doc.getDocNumber());
        else
            setDocSeriaNumber(doc.getDocNumber());
        setDocIssueDate(doc.getDocIssueDate());
        setDocRegNumber(doc.getDocRegNumber());
        if (doc.getSpecialty() != null)
            setSpecialty(doc.getSpecialty().getName());
        setSpecialization(doc.getSpecializationTXT());
        if (doc.getQualification() != null)
            setQualification(doc.getQualification().getName());
    }
}
