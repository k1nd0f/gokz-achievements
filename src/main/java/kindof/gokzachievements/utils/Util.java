package kindof.gokzachievements.utils;

public class Util {

    public static String bracketsValid(String string) {
        return string.replaceAll("\\[", "\\\\[").replaceAll("]", "\\]");
    }

    public static String asteriskValid(String string) {
        return string.replaceAll("\\*", "\\\\*");
    }

    public static String doubleValid(String string) {
        return asteriskValid(bracketsValid(string));
    }
}
