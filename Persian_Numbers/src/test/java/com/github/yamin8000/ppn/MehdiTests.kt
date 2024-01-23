package com.github.yamin8000.ppn

import com.github.yamin8000.ppn.PersianHelpers.spellToPersian
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private class MehdiTests {

    @Test
    fun firstMehdiTests() {
        assertEquals("یکصد و یک", 101.spellToPersian())
    }

    @Test
    fun otherMehdiTests() {
        assertEquals("دویست و دو", 202.spellToPersian())
        assertEquals("یک هزار و یکصد و یک", 1101.spellToPersian())
        assertEquals("نود و نه هزار و نهصد و نه", 99_909.spellToPersian())
        assertEquals("پانصد و پنجاه و پنج هزار و پانصد و پنج", 555_505.spellToPersian())
        assertEquals("یک هزار و پنج", 1005.spellToPersian())
        assertEquals("یک هزار و پانصد و پنج", 1505.spellToPersian())
    }
}