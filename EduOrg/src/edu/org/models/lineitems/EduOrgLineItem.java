package edu.org.models.lineitems;

import by.i4t.objects.EduOrganization;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class EduOrgLineItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private String ID;
    private String name;
    private String shortName;
    private String nameInBel;
    private String shortNameInBel;
    private String unp;
    private Integer code;
    private Integer nameVersion; //for editing datatable rowKey

    public EduOrgLineItem() {

    }

    public EduOrgLineItem(EduOrganization eduOrg) {
        this.ID = eduOrg.getID().toString();
        this.name = eduOrg.getName();
        this.nameInBel = eduOrg.getNameInBel();
        this.shortName = eduOrg.getShortName();
        this.shortNameInBel = eduOrg.getShortNameInBel();
        this.unp = eduOrg.getUNP();
        this.code = eduOrg.getCode();
        this.nameVersion = eduOrg.getNameVersion();
    }


}
