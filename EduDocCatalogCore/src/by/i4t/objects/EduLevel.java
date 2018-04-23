package by.i4t.objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import by.i4t.dao.interfaces.DBEntity;

@Data
@Entity
@Table(name = "edu_level")
public class EduLevel implements DBEntity {
    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    @Column(name = "id", columnDefinition = "serial")
    private Integer ID;

    @Column(name = "name", nullable = false)
    private String name;

}
