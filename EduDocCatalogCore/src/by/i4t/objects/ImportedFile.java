package by.i4t.objects;

import by.i4t.dao.interfaces.DBEntity;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "imported_files")
public class ImportedFile implements DBEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    @Type(type = "pg-uuid")
    private UUID ID;

    @Column(name = "upload_date", nullable = false)
    private Date uploadDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "users")
    @Fetch(FetchMode.SELECT)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "edu_org")
    @Fetch(FetchMode.SELECT)
    private EduOrganization eduOrg;

    @Column(name = "success_row_count")
    private Integer successRowCount;

    @Column(name = "error_count")
    private Integer errorCount;

    @Column(name = "imported_file_name", nullable = false)
    private String importedFileName;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "imported_file_content")
    @Type(type = "pg-uuid")
    private UUID importedFileContent;

    @Column(name = "error_file_content")
    @Type(type = "pg-uuid")
    private UUID errorFileContent;

    @Column(name = "status", nullable = false)
    private Integer status;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "exception")
    private ExceptionInfo exceptionInfo;
}
