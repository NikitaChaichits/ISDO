package by.i4t.objects;

import by.i4t.dao.interfaces.DBEntity;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "gisun_process")
public class GisunProcess implements DBEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    @Type(type = "pg-uuid")
    private UUID ID;

    @Column(name = "status")
    private Integer status;

    @Column(name = "personal_id")
    private String personalID;

}
