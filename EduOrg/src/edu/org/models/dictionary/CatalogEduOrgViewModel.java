package edu.org.models.dictionary;

import by.i4t.objects.EduOrgOwnershipType;
import by.i4t.objects.EduOrganization;
import by.i4t.objects.EduOrganizationType;
import edu.org.models.lineitems.EduOrgLineItem;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class CatalogEduOrgViewModel implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * FILTER DATA SECTION.
     */
    private List<EduOrgLineItem> eduOrgList = new ArrayList<EduOrgLineItem>();
    /**
     * DATA TABLE SECTION.
     */
    private EduOrgLineItem selectedOrgLine;
    /**
     * EDIT TABLE SECTION.
     */
    private List<EduOrganization> eduOrgHistory = new ArrayList<EduOrganization>();
    private EduOrganization selectedHistoryOrgLine;
    private EduOrganization editedHistoryOrgLine;
    private String name;
    private String shortName;
    private String nameInBel;
    private String shortNameInBel;
    private String UNP;
    private Date renameDate;
    /**
     * CREATE TABLE SECTION.
     */
    private Integer eduOrgTypeId;
    private Integer eduOrgOwnershipId;
    private EduOrganization createEduOrg = new EduOrganization();
    private List<EduOrganizationType> eduOrgTypeList = new ArrayList<EduOrganizationType>();
    private List<EduOrgOwnershipType> eduOrgOwnershipList = new ArrayList<EduOrgOwnershipType>();

    public void clearEditDataFields() {
        setName(null);
        setShortName(null);
        setNameInBel(null);
        setShortNameInBel(null);
        setUNP(null);
        setRenameDate(null);
    }


    public boolean filterByName(Object value, Object filter, java.util.Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim();
        if (filterText == null || filterText.equals("")) {
            return true;
        }

        if (value == null) {
            return false;
        }

        String carName = value.toString().toUpperCase();
        filterText = filterText.toUpperCase();

        return carName.contains(filterText);
    }

    public int sortByName(Object o1, Object o2) {
        return o1.toString().compareToIgnoreCase(o2.toString());
    }


}
