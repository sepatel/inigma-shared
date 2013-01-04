package org.inigma.shared.tracking;


/**
 * <p>
 * Implementation based off of http://answers.google.com/answers/threadview/id/207899.html
 * </p>
 * <p>
 * Also off of http://www.logicalside.com/carrier_detection
 * </p>
 */
public abstract class TrackingUtil {
    public static TrackingType getTrackingType(String trackingNumber) {
        if (isFedExExpress(trackingNumber) || isFedExGround(trackingNumber)) {
            return TrackingType.FedEx;
        } else if (isUps(trackingNumber)) {
            return TrackingType.UPS;
        } else if (isAirborne(trackingNumber)) {
            return TrackingType.Airborne;
        } else if (isUsps(trackingNumber)) {
            return TrackingType.USPS;
        }
        return null;
    }

    private static int differenceFromNextHighestMultipleOf10(int sum) {
        return ((sum / 10 * 10) + 10 - sum) % 10;
    }

    /**
     * <pre>
     * Airborne Express utilizes the stanard MOD 7 method for their tracking
     * numbers.  The check digit is the last digit of the tracking number.
     * 
     * [208-914-2901 x Val in Boise Call Center]
     * </pre>
     */
    private static boolean isAirborne(String trackingNumber) {
        return false;
    }

    /**
     * <pre>
     * EXPRESS SHIPMENTS:
     * 
     * For example, using tracking number: 012345678983
     * 
     * Take the first 11 digits of tracking number.  Starting with the 11th
     * position, take the digits 1, 3, and 7, and assign them to each digit
     * [repeatedly].
     *  
     * 012345678983
     * |||||||||||
     * 31731731731
     * 
     * Multiply each assigned number to its corresponding tracking number
     * digit:
     * 
     * 0 1 14 9 4 35 18 7 56 27 8
     * 
     * Add the products together [= 179 in this instance]
     * 
     * Divide the sum by 11.  You get 16 remainder 3.If the remainder is 10,
     * then the check digit is 0.  If there is no remainder, the check digit
     * is 0.  The remainder is the check digit and should equal the 12th
     * digit of the tracking number.
     * </pre>
     */
    private static boolean isFedExExpress(String trackingNumber) {
        if (trackingNumber == null || trackingNumber.length() != 12) {
            return false;
        }
        int hash[] = { 3, 1, 7 };
        int sum = 0;
        for (int i = 0; i < 11; i++) {
            int digit = trackingNumber.charAt(i) - '0';
            if (digit < 0 || digit > 9) {
                return false;
            }
            sum += digit * hash[i % hash.length];
        }
        int remainder = sum % 11;
        if (remainder == 10) {
            remainder = 0;
        }

        return trackingNumber.charAt(11) - '0' == remainder;
    }

    /**
     * <pre>
     * FOR GROUND SHIPMENTS:
     * 
     * See this PDF file on the web:
     * 
     * grd.fedex.com/online/mcode/fedex_ground_label_layout_specification.pdf
     * 
     * under Check Digit Calculation Algorithms section
     * 
     * [Web API dept. - 800-810-9073 [option 1]]
     * [Bruce Clark                            ]
     * [CASE NUMBER: 11016731                  ]
     * 
     * --
     * 
     * Positions are from <b>Right</b> to Left
     * Step 1. Starting from position 2, add up the values of the even numbered positions.
     * Step 2. Multiply the results of step Step 1. By three.
     * Step 3. Starting from position 3, add up the values of the odd numbered positions. Remember – position 1 is the
     *  check digit you are trying to calculate.
     * Step 4. Add the result of step Step 2. To the result of step Step 3.
     * Step 5. Determine the smallest number which when added to the number from Step 4. Results in a multiple of 10.
     *  This is the check digit.
     * </pre>
     */
    private static boolean isFedExGround(String trackingNumber) {
        if (trackingNumber == null) {
            return false;
        }
        trackingNumber = trackingNumber.replaceAll(" ", ""); // remove all whitespace
        if (trackingNumber.length() == 22) { // 96 information ground shipment
            if (!trackingNumber.startsWith("96")) {
                return false;
            }
            trackingNumber = trackingNumber.substring(7); // strip the 96 information
        }

        if (trackingNumber.length() != 15) { // not a valid tracking number
            return false;
        }

        int checkDigit = trackingNumber.charAt(trackingNumber.length() - 1) - '0';
        if (checkDigit < 0 || checkDigit > 9) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < trackingNumber.length() - 1; i++) {
            int digit = trackingNumber.charAt(i) - '0';
            if (digit < 0 || digit > 9) {
                return false;
            }
            if (i % 2 == 0) { // odd numbers logic because it falls on even positions
                sum += digit;
            } else { // evens logic because it falls on odd positions
                sum += digit * 3;
            }
        }

        int calcCheckDigit = differenceFromNextHighestMultipleOf10(sum);

        return checkDigit == calcCheckDigit;
    }

    /**
     * <pre>
     * The 1Z tracking numbers utilize a modified MOD 10 calculation.
     * 
     * 1. Exclude 1Z data identifier from calculation.
     * 2. Convert all alpha characters to their numeric equivalents using
     * chart below.
     * 3. From left, add all odd positions.
     * 4. From left, add all even positions and multiply by two.
     * 5. Add results of steps 3 and 4.
     * 6. Subtract result from next highest multiple of 10.
     * 7. The remainder is your check digit [the last digit].     
     * 
     * Note: If the remainder is 10, the check digit is 0.Alpha to numeric
     * cross reference chart
     * 
     * A=2
     * B=3
     * C=4
     * D=5
     * E=6
     * F=7
     * G=8
     * H=9
     * I=0
     * J=1
     * K=2
     * L=3
     * M=4
     * N=5
     * O=6
     * p=7
     * Q=8
     * R=9
     * S=0
     * T=1
     * U=2
     * V=3
     * W=4
     * X=5
     * Y=6
     * Z=7
     * 
     * ---
     * 
     * For all other tracking numbers, the standard MOD 10 algorithm applies
     * for the 11th check-digit.
     * 
     * [Business Development Dept.                      ]
     * [404-828-6627 x Getty Gidash                     ] 
     * [Mark Lewis is a name that Getty may refer you to]
     * </pre>
     * 
     * <pre>
     * Additional Reference:
     * http://www.codeproject.com/Articles/21224/Calculating-the-UPS-Tracking-Number-Check-Digit?fid=479913&df=90&mpp=25&noise=3&prof=False&sort=Position&view=Quick&spc=Relaxed&fr=8
     * 
     * 1.  The first two characters must be "1Z".
     * 2.  The next 6 characters we fill with our UPS account number "XXXXXX".
     * 3.  The next 2 characters denote the service type:
     *     a.  "01" for Next Day Air shipments.
     *     b.  "02" for Second Day Air shipments.
     *     c.  "03" for Ground shipments.
     * 4.  The next 5 characters is our invoice number (our invoices are 6 digits; we drop the first digit, e.g., the 123456 invoice would yield 23456 characters).
     * 5.  The next 2 digits is the package number, zero filled. E.g., package 1 is "01", 2 is "02".
     * 6.  The last and final character is the check digit.
     * 
     * </pre>
     * 
     * <pre>
     * Additional Reference: http://www.ups.com/content/us/en/tracking/help/tracking/tnh.html
     *  UPS tracking numbers appear in the following formats:
     *     1Z9999999999999999
     *     999999999999
     *     T9999999999
     *     999999999
     * </pre>
     */
    private static boolean isUps(String trackingNumber) {
        if (trackingNumber == null) {
            return false;
        }
        trackingNumber = trackingNumber.toUpperCase().replaceAll(" ", "");
        if (!trackingNumber.startsWith("1Z")) {
            return false;
        }

        int sum = 0;
        for (int i = 2; i < trackingNumber.length() - 1; i++) {
            char digit = trackingNumber.charAt(i);
            int value = digit - '0';
            if (value > 9) { // need to convert letters to numbers accordingly
                value = (digit - 'A' + 2) % 10;
            }
            if (i % 2 == 0) { // even position
                sum += value;
            } else { // odd position
                sum += value * 2;
            }
        }

        int checkdigit = differenceFromNextHighestMultipleOf10(sum);
        return trackingNumber.charAt(trackingNumber.length() - 1) - '0' == checkdigit;
    }

    /**
     * <pre>
     * Please see the following publications for check digit information. 
     * Note: “PIC” is their term for “tracking number.”  In addition to the
     * specific sections/page numbers below, I would advise searching for
     * “check digit” in these documents.
     * 
     * Publication 91 – Delivery and Signature Confirmation numbers
     * Acrobat page 85 [literally page 79]
     * http://www.usps.com/cpim/ftp/pubs/pub91.pdf
     * 
     * Publication 97 – Express Mail Manifesting Technical Guide
     * Acrobat page 57 [literally page 59]
     * http://www.usps.com/cpim/ftp/pubs/pub97.pdf
     * 
     * Publication 109 – Special Services Technical GuideSection 7.6.3
     * http://www.usps.com/cpim/ftp/pubs/pub109.pdf
     * [Charles in Delivery Confirmation: 877-264-9693]
     * [He only has information on the Delivery       ]
     * [and signature confirmation schemes            ]
     * 
     * 
     * Additional link:
     * Mod information
     * http://www.formtechservices.com/dstuff/bookstuf/modnos.html
     * </pre>
     */
    private static boolean isUsps(String trackingNumber) {
        if (trackingNumber == null) {
            return false;
        }
        trackingNumber = trackingNumber.toUpperCase().replaceAll(" ", "");
        int length = trackingNumber.length();
        if (length != 22 && length != 20 && length != 13) {
            return false;
        }
        if (length == 13) {
            if (!trackingNumber.endsWith("US")) {
                return false;
            }
            trackingNumber = trackingNumber.substring(2, 11);
            // mod 11 check which if it fails drop back to mod 10 check as either are valid for express
            int multipliers[] = { 8, 6, 4, 2, 3, 5, 9, 7 };
            int sum = 0;
            for (int i = 0; i < trackingNumber.length() - 1; i++) {
                int value = trackingNumber.charAt(i) - '0';
                sum += value * multipliers[i];
            }
            int remainder = sum % 11;
            int checkdigit;
            if (remainder == 0) {
                checkdigit = 5;
            } else if (remainder == 1) {
                checkdigit = 0;
            } else {
                checkdigit = 11 - remainder;
            }
            if (trackingNumber.charAt(trackingNumber.length() - 1) - '0' == checkdigit) {
                return true;
            }
        }

        int sum = 0;
        for (int i = trackingNumber.length() - 2; i >= 0; i--) {
            int value = trackingNumber.charAt(i) - '0';
            if (i % 2 == trackingNumber.length() % 2) {
                sum += 3 * value;
            } else {
                sum += value;
            }
        }

        int checkdigit = differenceFromNextHighestMultipleOf10(sum);
        return trackingNumber.charAt(trackingNumber.length() - 1) - '0' == checkdigit;
    }
}
