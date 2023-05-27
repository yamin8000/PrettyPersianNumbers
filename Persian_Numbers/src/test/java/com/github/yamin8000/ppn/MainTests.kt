package com.github.yamin8000.ppn

import com.github.yamin8000.ppn.PersianHelpers.spellToPersian
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MainTests {

    @Test
    fun normalTests() {
        assertEquals("دوازده", PersianDigits.spellToPersian(12))
        assertEquals("صفر", PersianDigits.spellToPersian(0))
        assertEquals("دو", "02".spellToPersian())
        assertEquals("یک", 1.spellToPersian())
        assertEquals("نه", 9.spellToPersian())
    }

    @Test
    fun negativeTest() {
        assertEquals("منفی یکصد و بیست و چهار", PersianDigits.spellToPersian(-124))
        assertEquals("صفر", "-00".spellToPersian())
        assertEquals("منفی یک دهم", "-.1".spellToPersian())
    }

    @Test
    fun decimalTest() {
        assertEquals("یکصد و بیست و چهار ممیز یک، صدم", PersianDigits.spellToPersian(124.01))
        assertEquals("سه ممیز چهارده، صدم", 3.14.spellToPersian())
    }

    @Test
    fun edgeCases() {
        assertEquals(
            "دوازده میلیارد و یکصد و بیست و سه میلیون و سیصد و دوازده هزار و یکصد و بیست و سه",
            12_123_312_123.spellToPersian()
        )
        assertEquals(
            "پنج میلیون و یکصد و بیست و یک هزار و سیصد و بیست و یک",
            PersianDigits.spellToPersian("5121321")
        )
        assertEquals("NaN", "-".spellToPersian())
    }
}