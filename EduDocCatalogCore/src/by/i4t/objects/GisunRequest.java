package by.i4t.objects;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import by.i4t.dao.interfaces.DBEntity;

@Data
@Entity
@Table(name = "gisun_request")
public class GisunRequest implements DBEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    @Type(type = "pg-uuid")
    private UUID ID;

    @Column(name = "messageid", nullable = false)
    @Type(type = "pg-uuid")
    private UUID messageId;

    @Column(name = "request_data", nullable = false)
    private String requestData;

    @Column(name = "status")
    private Integer status;
    @Column(name = "personal_id")
    private String personalID;
    @Column(name = "education_record_number")
    private Integer recordNumber;
    @Column(name = "export_date")
    private Date exportDate;
    @Column(name = "doc_number")
    private String docNumber;
    @Column(name = "doc_seria")
    private String docSeria;
    @Column(name = "doc_type")
    private String docType;
    @Column(name = "edu_org")
    private String eduOrg;
    @Column(name = "specialty")
    private String specialty;
    @Column(name = "date_of_issue")
    private Date dateOfIssue;
    @Column(name = "education_begin_data")
    private Date educationBeginDate;
    @Column(name = "education_end_data")
    private Date educationEndDate;
    @Column(name = "active")
    private Boolean active;
    @Column(name = "log")
    private String log;
}
