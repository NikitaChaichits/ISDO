package by.i4t.objects;

import by.i4t.dao.interfaces.DBEntity;
import lombok.Data;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "vuz_doc")
public class VUZDocument implements DBEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    @Type(type = "pg-uuid")
    private UUID ID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "citizen")
    @Fetch(FetchMode.SELECT)
    private Citizen citizen;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "edu_org")
    @Fetch(FetchMode.SELECT)
    private EduOrganization eduOrganization;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "specialty")
    @Fetch(FetchMode.SELECT)
    private Specialty specialty;

    @Column(name = "specialization")
    @Type(type = "pg-uuid")
    private UUID specialization;

    @Column(name = "specialization_txt")
    private String specializationTXT;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "qualification")
    @Fetch(FetchMode.SELECT)
    private Qualification qualification;

    @Column(name = "qualification_txt")
    private String qualificationTXT;

    @Column(name = "edu_start_date")
    private Date eduStartDate;

    @Column(name = "edu_stop_date")
    private Date eduStopDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doc_type")
    @Fetch(FetchMode.SELECT)
    private EduDocType docType;

    @Column(name = "doc_seria")
    private String docSeria;

    @Column(name = "doc_number", nullable = false)
    private String docNumber;

    @Column(name = "doc_reg_number", nullable = false)
    private String docRegNumber;

    @Column(name = "doc_issue_date", nullable = false)
    private Date docIssueDate;

    @Column(name = "change_date")
    private Date changeDate;

    @Column(name = "status")
    private Integer status;

    @Column(name = "gisun_id", columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Generated(GenerationTime.INSERT)
    private Integer gisunID;

//    @OneToMany
//    private List<GisunExportInfo> exportInfoList;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((citizen == null) ? 0 : citizen.hashCode());
        result = prime * result + ((docIssueDate == null) ? 0 : docIssueDate.hashCode());
        result = prime * result + ((docNumber == null) ? 0 : docNumber.hashCode());
        result = prime * result + ((docRegNumber == null) ? 0 : docRegNumber.hashCode());
        result = prime * result + ((docSeria == null) ? 0 : docSeria.hashCode());
        result = prime * result + ((docType == null) ? 0 : docType.hashCode());
        result = prime * result + ((eduOrganization == null) ? 0 : eduOrganization.hashCode());
        result = prime * result + ((eduStartDate == null) ? 0 : eduStartDate.hashCode());
        result = prime * result + ((eduStopDate == null) ? 0 : eduStopDate.hashCode());
        result = prime * result + ((qualification == null) ? 0 : qualification.hashCode());
        result = prime * result + ((specialization == null) ? 0 : specialization.hashCode());
        result = prime * result + ((specializationTXT == null) ? 0 : specializationTXT.hashCode());
        result = prime * result + ((specialty == null) ? 0 : specialty.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((gisunID == null) ? 0 : gisunID.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VUZDocument other = (VUZDocument) obj;
        if (citizen == null) {
            if (other.citizen != null)
                return false;
        } else if (!citizen.equals(other.citizen))
            return false;
        if (docIssueDate == null) {
            if (other.docIssueDate != null)
                return false;
        } else if (!docIssueDate.equals(other.docIssueDate))
            return false;
        if (docNumber == null) {
            if (other.docNumber != null)
                return false;
        } else if (!docNumber.equals(other.docNumber))
            return false;
        if (docRegNumber == null) {
            if (other.docRegNumber != null)
                return false;
        } else if (!docRegNumber.equals(other.docRegNumber))
            return false;
        if (docSeria == null) {
            if (other.docSeria != null)
                return false;
        } else if (!docSeria.equals(other.docSeria))
            return false;
        if (docType == null) {
            if (other.docType != null)
                return false;
        } else if (!docType.equals(other.docType))
            return false;
        if (eduOrganization == null) {
            if (other.eduOrganization != null)
                return false;
        } else if (!eduOrganization.equals(other.eduOrganization))
            return false;
        if (eduStartDate == null) {
            if (other.eduStartDate != null)
                return false;
        } else if (!eduStartDate.equals(other.eduStartDate))
            return false;
        if (eduStopDate == null) {
            if (other.eduStopDate != null)
                return false;
        } else if (!eduStopDate.equals(other.eduStopDate))
            return false;
        if (qualification == null) {
            if (other.qualification != null)
                return false;
        } else if (!qualification.equals(other.qualification))
            return false;
        if (qualificationTXT == null) {
            if (other.qualificationTXT != null)
                return false;
        } else if (!qualificationTXT.equals(other.qualificationTXT))
            return false;
        if (specialization == null) {
            if (other.specialization != null)
                return false;
        } else if (!specialization.equals(other.specialization))
            return false;
        if (specializationTXT == null) {
            if (other.specializationTXT != null)
                return false;
        } else if (!specializationTXT.equals(other.specializationTXT))
            return false;
        if (specialty == null) {
            if (other.specialty != null)
                return false;
        } else if (!specialty.equals(other.specialty))
            return false;
        if (gisunID == null) {
            if (other.gisunID != null)
                return false;
        } else if (!gisunID.equals(other.gisunID))
            return false;
        return true;
    }

}