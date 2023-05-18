package com.github.yamin8000.ppn

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Main {

    @Test
    fun zeroTest() {
        assertEquals("صفر", PersianDigits.spellToPersian(0))
    }

    @Test
    fun twelveTest() {
        assertEquals("دوازده", PersianDigits.spellToPersian(12))
    }

    @Test
    fun negativeTest() {
        assertEquals("منفی یکصد و بیست و چهار", PersianDigits.spellToPersian(-124))
    }

    @Test
    fun decimalTest() {
        assertEquals("یکصد و بیست و چهار ممیز یک، صدم", PersianDigits.spellToPersian(124.01))
    }
}