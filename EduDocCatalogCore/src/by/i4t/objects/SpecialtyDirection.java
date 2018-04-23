package by.i4t.objects;

import by.i4t.dao.interfaces.DBEntity;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "specialty_direction")
public class SpecialtyDirection implements DBEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    @Type(type = "pg-uuid")
    private UUID ID;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false)
    private String code;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "profile")
//    @Fetch(FetchMode.SELECT)
//    private SpecialtyProfile profile;

    @ManyToOne
    private EduLevel eduLevel;
}
