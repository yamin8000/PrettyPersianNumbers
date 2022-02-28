package com.github.yamin8000.ppn

import com.github.yamin8000.ppn.Digits.Companion.spell
import java.math.BigDecimal
import java.math.BigInteger

private fun main() {
    val x = 12L
    println(BigDecimal("1.4").spell())
    println((-124).spell())
    val big = BigInteger("150")
    println(Digits().spellToFarsi(big))
    //for (number in 0..Long.MAX_VALUE) println(Digits().spellToFarsi(number))

    println(12.spell())
    println("....".spell())
    println("124.01".spell())
}