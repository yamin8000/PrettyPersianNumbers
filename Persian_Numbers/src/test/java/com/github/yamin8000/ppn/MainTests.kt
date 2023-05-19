package com.github.yamin8000.ppn

import com.github.yamin8000.ppn.PersianHelpers.spellToPersian
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MainTests {


    @Test
    fun singleDigitTest() {
        assertEquals("دوازده", PersianDigits.spellToPersian(12))
        assertEquals("صفر", PersianDigits.spellToPersian(0))
        assertEquals("دو", "02".spellToPersian())
    }

    @Test
    fun negativeTest() {
        assertEquals("منفی یکصد و بیست و چهار", PersianDigits.spellToPersian(-124))
        assertEquals("صفر", "-00".spellToPersian())
        assertEquals("NaN", "-".spellToPersian())
        assertEquals("منفی یک دهم", "-.1".spellToPersian())
    }

    @Test
    fun decimalTest() {
        assertEquals("یکصد و بیست و چهار ممیز یک، صدم", PersianDigits.spellToPersian(124.01))
    }
}