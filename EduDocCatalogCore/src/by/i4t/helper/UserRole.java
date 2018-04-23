package by.i4t.helper;

public enum UserRole {
    ADMIN("ADMIN", "Администратор"),
    USER("USER", "Пользователь");

    private String name;
    private String code;

    UserRole(String code, String name) {
        this.name = name;
        this.code = code;
    }

    public static UserRole valueByCode(String code) {
        UserRole userRole = null;
        for (UserRole role : UserRole.values())
            if (role.getCode().equals(code))
                userRole = role;
        return userRole;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return getName() + " (" + getCode() + ")";
    }
}
