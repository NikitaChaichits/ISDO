package by.i4t.objects;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import by.i4t.dao.interfaces.DBEntity;

@Entity
@Table(name = "users")
@Data
public class User implements DBEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    @Type(type = "pg-uuid")
    private UUID ID;

    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(name = "certificat_id", nullable = false)
    private String certificatId;

    @Column(name = "user_role", nullable = false)
    private String role;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "ext_login")
    private String extLogin;

    @Column(name = "ext_pswrd")
    private String extPswrd;

    @Column(name = "email")
    private String email;

    @Column(name = "office_phone")
    private String officePhone;

    @Column(name = "mobile_phone")
    private String mobilePhone;

    @Column(name = "note")
    private String note;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "edu_org")
    @Fetch(FetchMode.SELECT)
    private EduOrganization eduOrganization;

    @Column(name = "edu_org_type")
    private Integer eduOrgType;

}
