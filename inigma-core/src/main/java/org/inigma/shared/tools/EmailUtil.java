package org.inigma.shared.tools;

import java.util.regex.Pattern;

/**
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 * @since 3/10/14 10:42 PM
 */
public abstract class EmailUtil {
    private static Pattern EMAIL_PATTERN = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@" +
            "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+(?:[a-z]{2}|com|org|net|edu|gov|mil|biz|info|mobi|name|aero|asia|jobs|museum)\\b");

    public static String fixEmail(String email) {
        if (email == null || "".equals(email.trim())) {
            return null;
        }
        return email.trim().toLowerCase();
    }

    public static boolean isValidEmail(String email) {
        if (email != null) {
            return EMAIL_PATTERN.matcher(email.trim().toLowerCase()).matches();
        }
        return true;
    }
}
