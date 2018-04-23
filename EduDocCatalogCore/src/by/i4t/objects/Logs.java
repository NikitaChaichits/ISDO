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
@Table(name = "logs")
public class Logs implements DBEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    @Type(type = "pg-uuid")
    private UUID ID;
    @Column(name = "action_date", nullable = false)
    private Date actionDate;
    @Column(name = "action_type", nullable = false)
    private String actionType;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "message", nullable = false)
    private String message;
    @Column(name = "user_info", nullable = false)
    private String userInfo;
}
