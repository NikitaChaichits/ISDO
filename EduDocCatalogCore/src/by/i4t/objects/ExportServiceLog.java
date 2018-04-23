package by.i4t.objects;

import by.i4t.dao.interfaces.DBEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Created by roman on 28.11.2016.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name="export_service_log")
public class ExportServiceLog implements DBEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    @Type(type = "pg-uuid")
    private UUID id;
    private boolean isOn;
    private String log;
    private Date date;

    public ExportServiceLog(Boolean isOn, String log, Date date) {
        super();
        this.setOn(true);
        this.setLog(log);
        this.setDate(date);
    }

}
