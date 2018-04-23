package by.i4t.objects;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import by.i4t.dao.interfaces.DBEntity;

@Data
@Entity
@Table(name = "gisun_export_info")
public class GisunExportInfo implements DBEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    @Type(type = "pg-uuid")
    private UUID ID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doc")
    @Fetch(FetchMode.SELECT)
    private VUZDocument vuzDocument;

    @Column(name = "messageid", nullable = false)
    @Type(type = "pg-uuid")
    private UUID messageId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "export_date", nullable = false)
    private Date exportDate;

    @Column(name = "note", nullable = false)
    private String note;

    @Column(name = "error_code", nullable = false)
    private String errorCode;

    @Column(name = "error_description", nullable = false)
    private String errorDescription;

    @Column(name = "wrong_value", nullable = false)
    private String wrongValue;


}
