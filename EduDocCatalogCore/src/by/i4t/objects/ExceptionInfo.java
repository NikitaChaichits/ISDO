package by.i4t.objects;

import by.i4t.dao.interfaces.DBEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "exception_info")
public class ExceptionInfo implements DBEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    @Type(type = "pg-uuid")
    private UUID ID;

    @Column(name = "exception_date", nullable = false)
    private Date exceptionDate;

    @Column(name = "message", columnDefinition = "text")
    private String message;

    @Column(name = "stacktrace", columnDefinition = "text")
    private String stackTrace;

    public ExceptionInfo(Exception e){
        stackTrace = ExceptionUtils.getStackTrace(e);
        message = e.getMessage();
        exceptionDate = new Date();
    }


}
