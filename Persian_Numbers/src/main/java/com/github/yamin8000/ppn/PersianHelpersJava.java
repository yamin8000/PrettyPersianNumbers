package com.github.yamin8000.ppn;

/**
 * A wrapper class for Java
 *
 * @see PersianHelpers
 */
public class PersianHelpersJava {

    /**
     * @param number input number
     * @return Persian representation of input number in String
     */
    public static String spellToFarsi(String number) {
        return PersianHelpers.INSTANCE.spellToPersian(number);
    }

    /**
     * @param number input number
     * @return Persian representation of input number in String
     */
    public static String spellToFarsi(Number number) {
        return PersianHelpers.INSTANCE.spellToPersian(number);
    }
}
