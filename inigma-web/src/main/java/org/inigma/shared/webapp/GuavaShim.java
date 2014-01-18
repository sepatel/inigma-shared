package org.inigma.shared.webapp;

class GuavaShim {
    static Integer ints_tryParse(String string) {
        if (string == null) {
            throw new NullPointerException();
        }
        int radix = 10;
        int length = string.length(), i = 0;
        if (length == 0) {
            return null;
        }
        boolean negative = string.charAt(i) == '-';
        if (negative && ++i == length) {
            return null;
        }
        int offset = i;
        int max = Integer.MIN_VALUE / radix;
        int result = 0, length1 = string.length();
        while (offset < length1) {
            int digit = Character.digit(string.charAt(offset++), radix);
            if (digit == -1) {
                return null;
            }
            if (max > result) {
                return null;
            }
            int next = result * radix - digit;
            if (next > result) {
                return null;
            }
            result = next;
        }
        if (!negative) {
            result = -result;
            if (result < 0) {
                return null;
            }
        }
        return result;
    }
}
