@file:Suppress("unused")

package com.github.yamin8000.ppn

import com.github.yamin8000.ppn.util.Constants.NaN
import com.github.yamin8000.ppn.util.Constants.dashChar
import com.github.yamin8000.ppn.util.Constants.dotChar
import com.github.yamin8000.ppn.util.Constants.oneBigInteger
import com.github.yamin8000.ppn.util.Constants.zeroBigInteger
import com.github.yamin8000.ppn.util.Constants.zeroChar
import com.github.yamin8000.ppn.util.Constants.zeroDecimal
import com.github.yamin8000.ppn.util.PersianNumber
import com.github.yamin8000.ppn.util.PersianNumber.AND
import com.github.yamin8000.ppn.util.PersianNumber.MINUS
import com.github.yamin8000.ppn.util.PersianNumber.RADIX
import com.github.yamin8000.ppn.util.PersianNumber.ZERO
import com.github.yamin8000.ppn.util.PersianNumber.bigIntegerTenPowers
import com.github.yamin8000.ppn.util.PersianNumber.bigTen
import com.github.yamin8000.ppn.util.PersianNumber.singleDigits
import com.github.yamin8000.ppn.util.PersianNumber.tenPowers
import com.github.yamin8000.ppn.util.PersianNumber.threeDigits
import com.github.yamin8000.ppn.util.PersianNumber.twoDigits
import java.math.BigDecimal
import java.math.BigInteger

object PersianDigits {

    /**
     * Spell a number to its Persian/Farsi equivalent.
     *
     * @param number the number to spell
     * @throws NumberFormatException
     */
    fun spellToPersian(number: Number) = try {
        val numberString = "$number".trim()
        when (number) {
            is Byte -> spellToPersian("${numberString.toInt()}")
            is Short -> spellToPersian("${numberString.toInt()}")
            is Int -> spellToPersian(numberString)
            is Long -> spellToPersian(numberString)
            is Float -> spellBigDecimalString(numberString)
            is Double -> spellBigDecimalString(numberString)
            is BigInteger -> spellBigInteger(number)
            is BigDecimal -> spellBigDecimal(number)
            else -> NaN
        }
    } catch (e: NumberFormatException) {
        NaN
    }

    /**
     * Spell a number to its Persian/Farsi equivalent.
     *
     * @param number the number to spell
     * @throws NumberFormatException
     */
    fun spellToPersian(number: String): String = try {
        spellUnknownNumber(number)
    } catch (e: NumberFormatException) {
        NaN
    }

    private fun spellUnknownNumber(number: String) = when (number.firstOrNull()) {
        dashChar -> spellNegativeNumber(number.drop(1))
        zeroChar -> spellNumberStartingWithZero(number)
        dotChar -> spellBigDecimal(BigDecimal(number))
        null -> NaN
        else -> spellNumber(number)
    }

    private fun spellNumber(number: String): String {
        return if (number.contains(dotChar)) spellBigDecimalString(number)
        else spellBareNumber(number)
    }

    private fun spellNegativeNumber(unsignedNumber: String): String = when {
        unsignedNumber.isBlank() -> NaN
        unsignedNumber.isNumberOnly() && unsignedNumber.all { it == zeroChar } -> ZERO
        else -> "$MINUS ${spellUnknownNumber(unsignedNumber)}"
    }

    private fun spellBareNumber(number: String) = when (number.length) {
        0 -> NaN
        1 -> singleDigits[number.toLongOrNull()] ?: NaN
        2 -> spellTwoDigitsNumber(number)
        3 -> threeDigits[number.toLongOrNull()] ?: processThreeDigitsNumber(number)
        else -> spellFourDigitsAndMoreNumber(number)
    }

    private fun spellNumberStartingWithZero(number: String): String {
        return if (number.isNotBlank() && number.all { it == zeroChar }) ZERO
        else spellUnknownNumber(number.removePrefix("$zeroChar"))
    }

    private fun spellTwoDigitsNumber(number: String): String {
        return twoDigits[number.toLongOrNull()] ?: processTwoDigitsNumber(number)
    }

    private fun processTwoDigitsNumber(number: String): String {
        val oneNotation = "${number[1]}".toLong()
        val tenNotation = ("${number[0]}".toLong()) * 10
        return "${twoDigits[tenNotation]} $AND ${singleDigits[oneNotation]}"
    }

    private fun processThreeDigitsNumber(number: String): String {
        val oneNotation = "${number[2]}".toLong()
        val tenNotation = (("${number[1]}".toLong()) * 10) + oneNotation
        val hundredNotation = ("${number[0]}".toLong()) * 100
        return "${threeDigits[hundredNotation]} $AND ${spellTwoDigitsNumber("$tenNotation")}"
    }

    private fun spellFourDigitsAndMoreNumber(number: String): String {
        val bigInteger = number.toBigInteger()
        return when (bigInteger.compareTo(BigInteger.valueOf(Long.MAX_VALUE))) {
            0, -1 -> spellPositiveLongNumber(number.toLong())
            1 -> spellBigInteger(bigInteger)
            else -> NaN
        }
    }

    private fun spellPositiveLongNumber(number: Long): String {
        singleDigits[number]?.let { return it }
        twoDigits[number]?.let { return it }
        threeDigits[number]?.let { return it }
        tenPowers[number]?.let { return "${PersianNumber.ONE} $it" }

        //biggest ten power before input number
        val biggestTenPower = findBiggestTenPowerBeforeNumber(number)
        if (biggestTenPower == 0L) return spellUnknownNumber("$number")

        val tenPowerDivisor = spellUnknownNumber("${number / biggestTenPower}")
        val tenPowerName = tenPowers[biggestTenPower] ?: ""
        val remainderNumber = number % biggestTenPower
        if (remainderNumber == 0L) return "$tenPowerDivisor $tenPowerName"
        val remainderNumberName = spellUnknownNumber("$remainderNumber")
        return "$tenPowerDivisor $tenPowerName و $remainderNumberName"
    }

    private fun findBiggestTenPowerBeforeNumber(number: Long): Long {
        var biggestTenPower = 0L
        if (number >= 1_000L) {
            for (power in tenPowers)
                if (number / power.key in 1 until number)
                    biggestTenPower = power.key
        }
        return biggestTenPower
    }

    private fun spellBigInteger(input: BigInteger): String {
        val biggestTenPower = findBiggestTenPowerBeforeBigDecimal(input)

        if (biggestTenPower == zeroBigInteger) return spellPositiveLongNumber(input.toLong())

        val tenPowerDivisor = spellUnknownNumber("${input.divide(biggestTenPower)}")

        var tenPowerName = NaN
        bigIntegerTenPowers[biggestTenPower]?.let { tenPowerName = it }
        tenPowers[biggestTenPower.toLong()]?.let { tenPowerName = it }

        val remainderNumber = input.mod(biggestTenPower)
        if (remainderNumber == zeroBigInteger) return "$tenPowerDivisor $tenPowerName"
        val remainderNumberName = spellUnknownNumber("$remainderNumber")
        return "$tenPowerDivisor $tenPowerName $AND $remainderNumberName"
    }

    private fun findBiggestTenPowerBeforeBigDecimal(input: BigInteger): BigInteger {
        var biggestTenPower = zeroBigInteger

        if (input >= BigInteger("1000")) {
            for (power in tenPowers) {
                val temp = input.divide(power.key.toBigInteger())
                if (temp >= oneBigInteger && temp < input)
                    biggestTenPower = power.key.toBigInteger()
            }
        }
        if (input >= bigTen.pow(21)) {
            for (power in bigIntegerTenPowers) {
                val temp = input.divide(power.key)
                if (temp >= oneBigInteger && temp < input) biggestTenPower = power.key
            }
        }
        return biggestTenPower
    }

    private fun spellBigDecimalString(bigDecimalString: String): String {
        return spellBigDecimal(BigDecimal(bigDecimalString))
    }

    private fun spellBigDecimal(bigDecimal: BigDecimal): String {
        when (bigDecimal.compareTo(zeroDecimal)) {
            -1 -> return "$MINUS ${spellBigDecimal(bigDecimal.abs())}"
            0 -> return ZERO
            1 -> return positiveBigDecimalHandler(bigDecimal)
        }
        return NaN
    }

    private fun positiveBigDecimalHandler(bigDecimal: BigDecimal): String {
        //dividing integer and fraction part from decimal
        val integerPart = bigDecimal.toBigInteger()
        val fraction = bigDecimal.remainder(BigDecimal.ONE)
        //if input only contains integers and no fraction like 1.0, 14.5
        val isIntegerOnly = fraction == zeroDecimal || fraction.compareTo(zeroDecimal) == 0
        if (isIntegerOnly) return spellBigInteger(integerPart)
        //if bigDecimal is 3.14 then decimals is 14
        val decimals = fraction.scaleByPowerOfTen(fraction.scale())
        //if bigDecimal is 3.14 then ten power is 100 or صدم
        val tenPower = bigTen.pow(fraction.scale())
        //add م to صد so it becomes صدم
        var tenPowerName = "${spellBigInteger(tenPower)}${PersianNumber.TH}"
        tenPowerName = normalizeTenPowerName(tenPowerName)
        //if input is only fraction like 0.5, 0.0002
        val isFractionOnly = integerPart.isFractionOnly()
        val fractionName = spellBigInteger(BigInteger("$decimals"))
        if (isFractionOnly) return "$fractionName $tenPowerName"
        //if input is normal like 3.14, 3.121323, 15.00001
        val integerName = spellBigInteger(integerPart)
        return "$integerName $RADIX $fractionName، $tenPowerName"
    }

    /**
     * Normalize ten power name,
     *
     * since we don't want return value to be سه ممیز چهارده، یک صدم
     * and be سه ممیز چهارده، صدم
     * then we remove that part
     *
     * @param input un-normal ten power name
     * @return normalized ten power name
     */
    private fun normalizeTenPowerName(input: String): String {
        var tenPowerName = input
        val persianOne = PersianNumber.ONE
        if (tenPowerName.startsWith(persianOne)) {
            tenPowerName = tenPowerName.replace(persianOne, "").trim()
        }
        return tenPowerName
    }

    private fun BigInteger.isFractionOnly(): Boolean {
        return this == zeroBigInteger || this.compareTo(zeroBigInteger) == 0
    }

    private fun String.isNumberOnly(): Boolean {
        return if (this.isBlank()) false
        else this.all { it.isDigit() }
    }
}