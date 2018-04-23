package edu.org.models.lineitems;

import by.i4t.helper.UserRole;
import by.i4t.helper.UserStatusEnum;
import by.i4t.objects.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDataLineItem {
    private String ID;
    private String name;
    private String role;
    private String status;
    private String eduOrganizationName;

    public UserDataLineItem() {

    }

    public UserDataLineItem(User user) {
        valueOf(user);
    }

    public void valueOf(User user) {
        setID(user.getID().toString());
        setName(user.getName());
        setRole(UserRole.valueByCode(user.getRole()).getName());
        setStatus(UserStatusEnum.valueByCode(user.getStatus()).getName());

        if (user.getEduOrganization() != null)
            setEduOrganizationName(user.getEduOrganization().getName());
    }

}
