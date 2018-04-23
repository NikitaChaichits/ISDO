package by.i4t.objects;

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
@Table(name = "citizen")
public class Citizen implements DBEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    @Type(type = "pg-uuid")
    private UUID ID;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "second_name", nullable = false)
    private String secondName;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "id_number")
    private String idNumber;

    @Column(name = "memberOfBel")
    private String memberOfBel;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((idNumber == null) ? 0 : idNumber.hashCode());
        result = prime * result + ((patronymic == null) ? 0 : patronymic.hashCode());
        result = prime * result + ((secondName == null) ? 0 : secondName.hashCode());
        result = prime * result + ((memberOfBel == null) ? 0 : memberOfBel.hashCode());
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
        Citizen other = (Citizen) obj;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (idNumber == null) {
            if (other.idNumber != null)
                return false;
        } else if (!idNumber.equals(other.idNumber))
            return false;
        if (patronymic == null) {
            if (other.patronymic != null)
                return false;
        } else if (!patronymic.equals(other.patronymic))
            return false;
        if (secondName == null) {
            if (other.secondName != null)
                return false;
        } else if (!secondName.equals(other.secondName))
            return false;
        if (memberOfBel == null) {
            if (other.memberOfBel != null)
                return false;
        } else if (!memberOfBel.equals(other.memberOfBel))
            return false;
        return true;
    }


}
