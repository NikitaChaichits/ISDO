package by.i4t.objects;

import by.i4t.dao.interfaces.DBEntity;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "specialty_catalog")
public class Specialty implements DBEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    @Type(type = "pg-uuid")
    private UUID ID;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code")
    @Type(type = "integer")
    private Integer code;

    @Column(name = "okrb_code", nullable = false)
    private String OKRBCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "specialty_group")
    @Fetch(FetchMode.SELECT)
    private SpecialtyGroup group;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "qualification")
    @Fetch(FetchMode.SELECT)
    private Qualification qualification;


}
