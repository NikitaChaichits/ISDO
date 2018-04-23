package by.i4t.helper;

public class CompareObjectsUtils {
    public static Boolean compareStrings(String str1, String str2) {
        if (str1 == null && str2 == null)
            return true;

        return str1 != null && str1.equals(str2);

    }
}
