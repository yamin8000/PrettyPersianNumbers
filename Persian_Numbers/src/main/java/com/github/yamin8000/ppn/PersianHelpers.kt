package com.github.yamin8000.ppn

/**
 * Helper [object] for spelling Persian numbers
 */
object PersianHelpers {

    /**
     * Extension method to spell a [Number] to word representation of it for Farsi,
     * example: 12 => دوازده
     */
    fun Number.spellToPersian() = PersianDigits.spellToPersian(this)

    /**
     * Extension method to spell a number in [String] to word representation of it for Farsi,
     * example: 12 => دوازده
     */
    fun String.spellToPersian() = PersianDigits.spellToPersian(this)
}