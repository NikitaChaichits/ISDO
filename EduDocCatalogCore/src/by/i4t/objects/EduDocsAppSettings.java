package by.i4t.objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import by.i4t.dao.interfaces.DBEntity;
import lombok.Data;

@Data
@Entity
@Table(name = "app_settings")
public class EduDocsAppSettings implements DBEntity {
    @Id
    @Column(name = "setting_name", nullable = false)
    private String name;

    @Column(name = "setting_value", nullable = false)
    private String value;


}
