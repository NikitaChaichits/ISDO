package by.i4t.objects.bo;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "diplom")
public class VUZEduDocLineItem {
    @XmlElement(name = "Level")
    private String educationLevelField;
    @XmlElement(name = "DocType")
    private String docTypeField;
    @XmlElement(name = "I")
    private String firstNameField;
    @XmlElement(name = "F")
    private String secondNameField;
    @XmlElement(name = "O")
    private String patronymicField;
    @XmlElement(name = "Mem")
    private String memberOfBelField;
    @XmlElement(name = "Start")
    private String eduStartDateStringField;
    @XmlElement(name = "Stop")
    private String eduStopDateStringField;
    @XmlAttribute(name = "RegNum")
    private String eduDocRegNumberField;
    @XmlElement(name = "Create")
    private String eduDocIssueDateField;
    @XmlElement(name = "Ser")
    private String eduDocSeriaField;
    @XmlElement(name = "Num")
    private String eduDocNumberField;
    @XmlElement(name = "Pasp")
    private String personalIDField;
    @XmlElement(name = "Spec")
    private String specialtyField;
    @XmlElement(name = "Spz")
    private String specializationField;
    @XmlElement(name = "Kw")
    private String qualificationField;
    @XmlElement(name = "School")
    private String eduOrgField;
    @XmlTransient
    private String importErrorMsg;
    @XmlTransient
    private Date eduStartDateField;
    @XmlTransient
    private Date eduStopDateField;

    /**
     * @return the educationLevel
     */
    public String getEducationLevel() {
        return educationLevelField;
    }

    /**
     * @param educationLevel the educationLevel to set
     */
    public void setEducationLevel(String educationLevel) {
        this.educationLevelField = educationLevel;
    }

    /**
     * @return the docType
     */
    public String getDocType() {
        return docTypeField;
    }

    /**
     * @param docType the docType to set
     */
    public void setDocType(String docType) {
        this.docTypeField = docType;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstNameField;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstNameField = firstName;
    }

    /**
     * @return the secondName
     */
    public String getSecondName() {
        return secondNameField;
    }

    /**
     * @param secondName the secondName to set
     */
    public void setSecondName(String secondName) {
        this.secondNameField = secondName;
    }

    /**
     * @return the patronymic
     */
    public String getPatronymic() {
        return patronymicField;
    }

    /**
     * @param patronymic the patronymic to set
     */
    public void setPatronymic(String patronymic) {
        this.patronymicField = patronymic;
    }

    /**
     * @return the memberOfBel
     */
    public String getMemberOfBel() {
        return memberOfBelField;
    }

    /**
     * @param memberOfBel the memberOfBel to set
     */
    public void setMemberOfBel(String memberOfBel) {
        this.memberOfBelField = memberOfBel;
    }
    /**
     * @return the eduStartDate
     */
    public Date getEduStartDate() {
        return eduStartDateField;
    }

    /**
     * @param eduStartDate the eduStartDate to set
     */
    public void setEduStartDate(Date eduStartDate) {
        this.eduStartDateField = eduStartDate;
    }

    /**
     * @return the eduStopDate
     */
    public Date getEduStopDate() {
        return eduStopDateField;
    }

    /**
     * @param eduStopDate the eduStopDate to set
     */
    public void setEduStopDate(Date eduStopDate) {
        this.eduStopDateField = eduStopDate;
    }

    /**
     * @return the eduDocRegNumber
     */
    public String getEduDocRegNumber() {
        return eduDocRegNumberField;
    }

    /**
     * @param eduDocRegNumber the eduDocRegNumber to set
     */
    public void setEduDocRegNumber(String eduDocRegNumber) {
        this.eduDocRegNumberField = eduDocRegNumber;
    }

    /**
     * @return the eduDocIssueDate
     */
    public String getEduDocIssueDate() {
        return eduDocIssueDateField;
    }

    /**
     * @param eduDocIssueDate the eduDocIssueDate to set
     */
    public void setEduDocIssueDate(String eduDocIssueDate) {
        this.eduDocIssueDateField = eduDocIssueDate;
    }

    /**
     * @return the eduDocSeria
     */
    public String getEduDocSeria() {
        return eduDocSeriaField;
    }

    /**
     * @param eduDocSeria the eduDocSeria to set
     */
    public void setEduDocSeria(String eduDocSeria) {
        this.eduDocSeriaField = eduDocSeria;
    }

    /**
     * @return the eduDocNumber
     */
    public String getEduDocNumber() {
        return eduDocNumberField;
    }

    /**
     * @param eduDocNumber the eduDocNumber to set
     */
    public void setEduDocNumber(String eduDocNumber) {
        this.eduDocNumberField = eduDocNumber;
    }

    /**
     * @return the personalID
     */
    public String getPersonalID() {
        return personalIDField;
    }

    /**
     * @param personalID the personalID to set
     */
    public void setPersonalID(String personalID) {
        this.personalIDField = personalID;
    }

    /**
     * @return the specialty
     */
    public String getSpecialty() {
        return specialtyField;
    }

    /**
     * @param specialty the specialty to set
     */
    public void setSpecialty(String specialty) {
        this.specialtyField = specialty;
    }

    /**
     * @return the specialization
     */
    public String getSpecialization() {
        return specializationField;
    }

    /**
     * @param specialization the specialization to set
     */
    public void setSpecialization(String specialization) {
        this.specializationField = specialization;
    }

    /**
     * @return the qualification
     */
    public String getQualification() {
        return qualificationField;
    }

    /**
     * @param qualification the qualification to set
     */
    public void setQualification(String qualification) {
        this.qualificationField = qualification;
    }

    /**
     * @return the eduOrg
     */
    public String getEduOrg() {
        return eduOrgField;
    }

    /**
     * @param eduOrg the eduOrg to set
     */
    public void setEduOrg(String eduOrg) {
        this.eduOrgField = eduOrg;
    }

    /**
     * @return the importErrorMsg
     */
    public String getImportErrorMsg() {
        return importErrorMsg;
    }

    /**
     * @param importErrorMsg the importErrorMsg to set
     */
    public void setImportErrorMsg(String importErrorMsg) {
        this.importErrorMsg = importErrorMsg;
    }

    public String getEduStartDateString() {
        return eduStartDateStringField;
    }

    public void setEduStartDateString(String eduStartDateStringField) {
        this.eduStartDateStringField = eduStartDateStringField;
    }

    public String getEduStopDateString() {
        return eduStopDateStringField;
    }

    public void setEduStopDateString(String eduStopDateStringField) {
        this.eduStopDateStringField = eduStopDateStringField;
    }
}
