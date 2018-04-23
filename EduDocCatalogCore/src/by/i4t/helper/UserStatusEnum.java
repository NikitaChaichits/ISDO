package by.i4t.helper;

public enum UserStatusEnum {
    ACTIVE("ACTIVE", "Активен"),
    BLOCKED("BLOCKED", "Заблокирован");

    private String name;
    private String code;

    UserStatusEnum(String code, String name) {
        this.name = name;
        this.code = code;
    }

    public static UserStatusEnum valueByCode(String code) {
        UserStatusEnum userStatus = null;
        for (UserStatusEnum status : UserStatusEnum.values())
            if (status.getCode().equals(code))
                userStatus = status;
        return userStatus;
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
