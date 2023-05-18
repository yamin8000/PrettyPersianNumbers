@file:Suppress("unused")

package com.github.yamin8000.ppn

import com.github.yamin8000.ppn.util.Constants.NaN
import com.github.yamin8000.ppn.util.Constants.dashChar
import com.github.yamin8000.ppn.util.Constants.decimalRegex
import com.github.yamin8000.ppn.util.Constants.dotChar
import com.github.yamin8000.ppn.util.Constants.oneBigInteger
import com.github.yamin8000.ppn.util.Constants.zeroBigInteger
import com.github.yamin8000.ppn.util.Constants.zeroChar
import com.github.yamin8000.ppn.util.Constants.zeroDecimal
import com.github.yamin8000.ppn.util.Constants.zeroOnlyRegex
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
     */
    fun spellToFarsi(number: Number) = try {
        when (number) {
            is Byte -> spellToFarsi("${number.toInt()}")
            is Short -> spellToFarsi("${number.toInt()}")
            is Int -> spellToFarsi("$number")
            is Long -> spellToFarsi("$number")
            is Float -> bigDecimalHandler("$number")
            is Double -> bigDecimalHandler("$number")
            is BigInteger -> bigIntegerHandler(number)
            is BigDecimal -> bigDecimalHandler(number)
            else -> NaN
        }
    } catch (e: NumberFormatException) {
        NaN
    }

    /**
     * Spell a number in [String] to its Persian/Farsi equivalent.
     *
     * @see [spellToFarsi]
     */
    fun spellToFarsi(number: String) = try {
        stringHandler(number)
    } catch (e: NumberFormatException) {
        NaN
    }

    private fun stringHandler(number: String): String {
        if (number.isBlank()) return NaN
        when (number.first()) {
            dashChar -> return handleStringsWithMinusPrefix(number)
            zeroChar -> return handleStringsWithZeroPrefix(number)
            //numbers like .0, .14 are decimals too
            dotChar -> if (decimalRegex.matches(number)) return bigDecimalHandler(BigDecimal(number))
            else -> if (decimalRegex.matches(number)) return bigDecimalHandler(BigDecimal(number))
        }

        return when (number.length) {
            1 -> singleDigits[number.toLong()] ?: NaN
            2 -> twoDigitHandler(number)
            3 -> threeDigitsHandler(number)
            else -> digitsHandler(number)
        }
    }

    private fun handleStringsWithMinusPrefix(number: String): String {
        val numberWithoutMinus = number.substring(1)
        return if (numberWithoutMinus.isNotBlank()) {
            //when normal input like -12 -123123 -5612 -0
            if (numberWithoutMinus.isNumberOnly()) {
                return if (zeroOnlyRegex.matches(numberWithoutMinus)) ZERO
                else "$MINUS ${stringHandler(numberWithoutMinus)}"
            }
            //when input contains anything other than numbers 0-9 like --, -., -.5, -1.5
            else {
                if (decimalRegex.matches(numberWithoutMinus)) return bigDecimalHandler(number)
                else NaN
            }
        }
        //when input is only -
        else NaN
    }

    /**
     * Handling numbers that starts with zero and removes starting zeros
     *
     * @param number string that starts with zero
     * @return string with starting zeros removed
     */
    private fun handleStringsWithZeroPrefix(number: String): String {
        if (zeroOnlyRegex.matches(number)) return ZERO
        /**
         * this can probably be replaced with some smart-ass regex with lookahead,
         * but that makes it more complex
         *
         * this loop re-call this method with version of this number that doesn't contain starting zeros
         * or finds first occurrence of a non-zero number and makes that starting index of new string
         * suppose input number is 000012
         * then starting index is 4 and new string is 12
         */
        return number.indices.firstOrNull { number[it] != zeroChar }
            ?.let { stringHandler(number.substring(it)) } ?: ZERO
    }

    /**
     * Two digit numbers handler
     *
     * @param number in String type
     * @return Persian representation of that number
     */
    private fun twoDigitHandler(number: String): String {
        if (number.length < 2) return stringHandler(number)
        //return if number is exactly from twoDigits list
        twoDigits[number.toLong()]?.let { return it }
        val oneNotation = "${number[1]}".toLong()
        val tenNotation = ("${number[0]}".toLong()) * 10
        return "${twoDigits[tenNotation]} $AND ${singleDigits[oneNotation]}"
    }

    /**
     * Three digits number handler
     *
     * @param number in String type
     * @return persian representation of that number
     */
    private fun threeDigitsHandler(number: String): String {
        //return if number is exactly from threeDigits list
        threeDigits[number.toLong()]?.let { return it }
        val oneNotation = "${number[2]}".toLong()
        val tenNotation = (("${number[1]}".toLong()) * 10) + oneNotation
        val hundredNotation = ("${number[0]}".toLong()) * 100
        return "${threeDigits[hundredNotation]} $AND ${twoDigitHandler("$tenNotation")}"
    }

    /**
     * Digits handler
     *
     * @param number in String type
     * @return persian representation of that number
     * @see Long.MAX_VALUE
     */
    private fun digitsHandler(number: String): String {
        val bigInteger = number.toBigInteger()
        return when (bigInteger.compareTo(BigInteger.valueOf(Long.MAX_VALUE))) {
            0, -1 -> longHandler(number.toLong())
            1 -> bigIntegerHandler(bigInteger)
            else -> NaN
        }
    }

    /**
     * handle numbers that can be fitted in a long variable
     *
     * @param longNumber input in long format
     * @return persian representation of that number
     */
    private fun longHandler(longNumber: Long): String {
        singleDigits[longNumber]?.let { return it }
        twoDigits[longNumber]?.let { return it }
        threeDigits[longNumber]?.let { return it }
        tenPowers[longNumber]?.let { return "${PersianNumber.ONE} $it" }

        //biggest ten power before input number
        val biggestTenPower = findBiggestTenPowerBeforeInputNumber(longNumber)
        if (biggestTenPower == 0L) return stringHandler("$longNumber")

        val tenPowerDivisor = stringHandler("${longNumber / biggestTenPower}")
        val tenPowerName = tenPowers[biggestTenPower] ?: ""
        val remainderNumber = longNumber % biggestTenPower
        if (remainderNumber == 0L) return "$tenPowerDivisor $tenPowerName"
        val remainderNumberName = stringHandler("$remainderNumber")
        return "$tenPowerDivisor $tenPowerName و $remainderNumberName"
    }

    /**
     * Find biggest ten power before input number,
     * biggest ten power before 2220 is 1000
     *
     * @param longNumber
     * @return
     */
    private fun findBiggestTenPowerBeforeInputNumber(longNumber: Long): Long {
        var biggestTenPower = 0L
        if (longNumber >= 1_000L) {
            for (power in tenPowers)
                if (longNumber / power.key in 1 until longNumber)
                    biggestTenPower = power.key
        }
        return biggestTenPower
    }

    private fun bigIntegerHandler(input: BigInteger): String {
        val biggestTenPower = findBiggestTenPowerBeforeInputNumber(input)

        if (biggestTenPower == zeroBigInteger) return longHandler(input.toLong())

        val tenPowerDivisor = stringHandler("${input.divide(biggestTenPower)}")

        var tenPowerName = NaN
        bigIntegerTenPowers[biggestTenPower]?.let { tenPowerName = it }
        tenPowers[biggestTenPower.toLong()]?.let { tenPowerName = it }

        val remainderNumber = input.mod(biggestTenPower)
        if (remainderNumber == zeroBigInteger) return "$tenPowerDivisor $tenPowerName"
        val remainderNumberName = stringHandler("$remainderNumber")
        return "$tenPowerDivisor $tenPowerName $AND $remainderNumberName"
    }

    private fun findBiggestTenPowerBeforeInputNumber(input: BigInteger): BigInteger {
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

    /**
     * Big decimal handler
     *
     * convenient method to avoid calling bigDecimalHandler(BigDecimal(number))
     *
     * @param bigDecimalString string that is probably can be parsed to BigDecimal
     */
    private fun bigDecimalHandler(bigDecimalString: String): String {
        return bigDecimalHandler(BigDecimal(bigDecimalString))
    }

    /**
     * Big decimal handler,
     * handle conversion of decimal numbers like 3.14, 0.0, 0.1, 0.0002, 1.0002
     *
     * @param bigDecimal big decimal input number
     * @return string representation of given decimal number in farsi
     */
    private fun bigDecimalHandler(bigDecimal: BigDecimal): String {
        when (bigDecimal.compareTo(zeroDecimal)) {
            -1 -> return "$MINUS ${bigDecimalHandler(bigDecimal.abs())}"
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
        if (isIntegerOnly) return bigIntegerHandler(integerPart)
        //if bigDecimal is 3.14 then decimals is 14
        val decimals = fraction.scaleByPowerOfTen(fraction.scale())
        //if bigDecimal is 3.14 then ten power is 100 or صدم
        val tenPower = bigTen.pow(fraction.scale())
        //add م to صد so it becomes صدم
        var tenPowerName = "${bigIntegerHandler(tenPower)}${PersianNumber.TH}"
        tenPowerName = normalizeTenPowerName(tenPowerName)
        //if input is only fraction like 0.5, 0.0002
        val isFractionOnly = integerPart.isFractionOnly()
        val fractionName = bigIntegerHandler(BigInteger("$decimals"))
        if (isFractionOnly) return "$fractionName $tenPowerName"
        //if input is normal like 3.14, 3.121323, 15.00001
        val integerName = bigIntegerHandler(integerPart)
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

    /**
     * Extension method to spell a [Number] to word representation of it for Farsi,
     * example: 12 => دوازده
     */
    fun Number.spell() = spellToFarsi(this)

    /**
     * Extension method to spell a number in [String] to word representation of it for Farsi,
     * example: 12 => دوازده
     */
    fun String.spell() = spellToFarsi(this)
}