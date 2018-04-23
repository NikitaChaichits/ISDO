package edu.org.models;

import by.i4t.helper.UserRole;
import by.i4t.helper.UserStatusEnum;
import by.i4t.objects.User;
import edu.org.models.lineitems.SimpleStringValueLineItem;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

@Data
public class UserDialogViewModel implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -8121421497039351970L;

    private UUID ID;
    private String name;
    private String certificatId;
    private List<UserRole> userRoleList = new ArrayList<UserRole>();
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.PUBLIC)
    private UserRole userRole = UserRole.USER;
    private boolean userStatus = false;
    private String extLogin;
    private String extPswrd;
    private String email;
    private String officePhone;
    private String mobilePhone;
    private String note;
    private List<SimpleStringValueLineItem> eduOrgList = new ArrayList<SimpleStringValueLineItem>();
    private SimpleStringValueLineItem eduOrg;

    private Boolean isAdmin = false;

    public void reset() {
        setCertificatId(null);
        setID(null);
        setName(null);
        setEduOrg(null);
        setUserRole(UserRole.USER);
        setIsAdmin(false);
        setUserStatus(false);
        setExtLogin(null);
        setExtPswrd(null);
        setEmail(null);
        setOfficePhone(null);
        setMobilePhone(null);
        setNote(null);
    }

    public void updateFromUser(User user) {
        reset();
        if (user != null) {
            setCertificatId(user.getCertificatId());
            setID(user.getID());
            setName(user.getName());
            if (user.getEduOrganization() != null)
                setEduOrg(new SimpleStringValueLineItem(user.getEduOrganization().getName(), user.getEduOrganization().getID().toString()));
            setUserRole(UserRole.valueByCode(user.getRole()));
            setIsAdmin(UserRole.ADMIN.equals(getUserRole()));
            setUserStatus(UserStatusEnum.BLOCKED.getCode().equals(user.getStatus()));
            setExtLogin(user.getExtLogin());
            setExtPswrd(user.getExtPswrd());
            setEmail(user.getEmail());
            setOfficePhone(user.getOfficePhone());
            setMobilePhone(user.getMobilePhone());
            setNote(user.getNote());
        }
    }

    public List<SimpleStringValueLineItem> completeTextEduOrg(String query) {
        List<SimpleStringValueLineItem> results = new ArrayList<SimpleStringValueLineItem>();
        String regex = ".*";
        for (int i = 0; i < query.length(); i++)
            regex += "[" + Character.toUpperCase(query.charAt(i)) + Character.toLowerCase(query.charAt(i)) + "]";
        regex += ".*";
        for (SimpleStringValueLineItem item : this.eduOrgList) {

            if (item.getName().matches(regex)) {
                results.add(item);
            }
        }
        Collections.sort(results, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((SimpleStringValueLineItem) o1).getName().compareTo(((SimpleStringValueLineItem) o2).getName());
            }
        });
        return results;
    }

    public void setUserRole(UserRole userRole) {
        if (userRole != null)
            this.userRole = userRole;
    }

}
