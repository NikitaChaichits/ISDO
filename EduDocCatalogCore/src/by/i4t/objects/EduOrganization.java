package by.i4t.objects;

import java.util.Date;
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

@Data
@Entity
@Table(name = "edu_org")
public class EduOrganization implements DBEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    @Type(type = "pg-uuid")
    private UUID ID;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "name_in_bel")
    private String nameInBel;

    @Column(name = "short_name_in_bel")
    private String shortNameInBel;

    @Column(name = "unp")
    private String UNP;

    @Column(name = "rename_date", nullable = false)
    private Date renameDate;

    @Column(name = "code", nullable = false)
    private Integer code;

    @Column(name = "name_version", nullable = false)
    private Integer nameVersion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ownership_type")
    @Fetch(FetchMode.SELECT)
    private EduOrgOwnershipType ownershipType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "org_type")
    @Fetch(FetchMode.SELECT)
    private EduOrganizationType orgType;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ID == null) ? 0 : ID.hashCode());
        result = prime * result + ((UNP == null) ? 0 : UNP.hashCode());
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((nameVersion == null) ? 0 : nameVersion.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EduOrganization other = (EduOrganization) obj;
        if (ID == null) {
            if (other.ID != null)
                return false;
        } else if (!ID.equals(other.ID))
            return false;
        if (UNP == null) {
            if (other.UNP != null)
                return false;
        } else if (!UNP.equals(other.UNP))
            return false;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (nameVersion == null) {
            if (other.nameVersion != null)
                return false;
        } else if (!nameVersion.equals(other.nameVersion))
            return false;
        return true;
    }

}
