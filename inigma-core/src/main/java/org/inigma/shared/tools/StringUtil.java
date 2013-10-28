package org.inigma.shared.tools;

public abstract class StringUtil {

    public static String snakeToCamel(String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }
        StringBuilder builder = new StringBuilder();
        char[] chars = string.toCharArray();
        CHARS:
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != '_') {
                builder.append(Character.toLowerCase(chars[i]));
                continue;
            }
            do {
                i++;
                if (i >= chars.length) {
                    break CHARS;
                }
            } while (chars[i] == '_');
            builder.append(Character.toUpperCase(chars[i]));
        }
        return builder.toString();
    }
}
