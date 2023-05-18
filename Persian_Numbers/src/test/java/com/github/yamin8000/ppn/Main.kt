package com.github.yamin8000.ppn

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Main {

    @Test
    fun zeroTest() {
        assertEquals("صفر", PersianDigits.spellToFarsi(0))
    }

    @Test
    fun twelveTest() {
        assertEquals("دوازده", PersianDigits.spellToFarsi(12))
    }

    @Test
    fun negativeTest() {
        assertEquals("منفی یکصد و بیست و چهار", PersianDigits.spellToFarsi(-124))
    }

    @Test
    fun decimalTest() {
        assertEquals("یکصد و بیست و چهار ممیز یک، صدم", PersianDigits.spellToFarsi(124.01))
    }
}