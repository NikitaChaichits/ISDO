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

@Entity
@Table(name = "notifications")
@Data
public class Notification implements DBEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    @Type(type = "pg-uuid")
    private UUID ID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "senderId")
    @Fetch(FetchMode.SELECT)
    private User senderId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receiverId")
    @Fetch(FetchMode.SELECT)
    private User receiverId;

    @Column(name = "theme")
    private String theme;

    @Column(name = "message")
    private String message;

    @Column(name = "attachment")
    private byte[] attachment;

    @Column(name = "sendingDate")
    private Date sendingDate;

    @Column(name = "read")
    private boolean read;

    @Column(name = "status")
    private boolean status;

    @Column(name = "attach_type")
    private String attachType;
}
