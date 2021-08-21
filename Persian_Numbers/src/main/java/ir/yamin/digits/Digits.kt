@file:Suppress("unused")

package ir.yamin.digits

import ir.yamin.digits.constants.IranCurrency
import ir.yamin.digits.constants.PersianNumber
import ir.yamin.digits.constants.PersianNumber.AND
import ir.yamin.digits.constants.PersianNumber.MINUS
import ir.yamin.digits.constants.PersianNumber.RADIX
import ir.yamin.digits.constants.PersianNumber.ZERO
import ir.yamin.digits.constants.PersianNumber.bigIntegerTenProducts
import ir.yamin.digits.constants.PersianNumber.bigTen
import ir.yamin.digits.constants.PersianNumber.singleDigits
import ir.yamin.digits.constants.PersianNumber.tenProducts
import ir.yamin.digits.constants.PersianNumber.threeDigits
import ir.yamin.digits.constants.PersianNumber.twoDigits
import java.math.BigDecimal
import java.math.BigInteger

private const val NaN = "NaN"
private const val ZERO_ONLY_REGEX = "[0]+"
private const val DECIMAL_REGEX = "\\d*\\.\\d+"
private const val zeroChar = '0'
private const val dashChar = '-'
private const val dotChar = '.'

class Digits {
    
    private val zeroOnlyRegex : Regex by lazy(LazyThreadSafetyMode.NONE) { Regex(ZERO_ONLY_REGEX) }
    private val decimalRegex : Regex by lazy(LazyThreadSafetyMode.NONE) { Regex(DECIMAL_REGEX) }
    
    private val zeroBigInteger : BigInteger by lazy(LazyThreadSafetyMode.NONE) { BigInteger.ZERO }
    private val oneBigInteger : BigInteger by lazy(LazyThreadSafetyMode.NONE) { BigInteger.ONE }
    
    /**
     * Spell number to Persian/Farsi words
     *
     * @param number in any type like Byte/Short/Int/Long/String/Big Integer
     * @return Persian representation of that number in String type
     */
    fun spellToFarsi(number : Any) : String {
        return try {
            when (number) {
                is Byte -> spellToFarsi("${number.toInt()}")
                is Short -> spellToFarsi("${number.toInt()}")
                is Int -> spellToFarsi("$number")
                is Long -> spellToFarsi("$number")
                is Float -> bigDecimalHandler("$number")
                is Double -> bigDecimalHandler("$number")
                is String -> stringHandler(number)
                is BigInteger -> bigIntegerHandler(number)
                else -> NaN
            }
        } catch (exception : Exception) {
            NaN
        }
    }
    
    /**
     * Spell number to Persian/Farsi words plus Iran currency name
     *
     * @param number in any type like Byte/Short/Int/Long/String/Big Integer
     * @param currency if no parameter is passed 'ریال' is default
     * @return Persian representation of that number plus currency name in String type
     */
    @JvmOverloads
    fun spellToIranMoney(number : Any, currency : IranCurrency = IranCurrency.RIAL) : String {
        return "${spellToFarsi(number)} ${currency.value}"
    }
    
    /**
     * handle numbers as a String
     *
     * @param number in String type
     * @return Persian representation of that number
     */
    private fun stringHandler(number : String) : String {
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
    
    /**
     * Handle strings with minus prefix
     *
     * when input starts with - like -a, -12, -, --, -a12, ...
     *
     * @param number string that starts with - (minus character)
     * @return
     */
    private fun handleStringsWithMinusPrefix(number : String) : String {
        val numberWithoutMinus = number.substring(1)
        return if (numberWithoutMinus.isNotBlank()) {
            //when normal input like -12 -123123 -5612 -0
            if (isNumberOnly(numberWithoutMinus)) {
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
     * handling numbers that starts with zero and removes starting zeros
     *
     * @param number string that starts with zero
     * @return string with starting zeros removed
     */
    private fun handleStringsWithZeroPrefix(number : String) : String {
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
    private fun twoDigitHandler(number : String) : String {
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
    private fun threeDigitsHandler(number : String) : String {
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
    private fun digitsHandler(number : String) : String {
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
    private fun longHandler(longNumber : Long) : String {
        singleDigits[longNumber]?.let { return it }
        twoDigits[longNumber]?.let { return it }
        threeDigits[longNumber]?.let { return it }
        tenProducts[longNumber]?.let { return "${PersianNumber.ONE} $it" }
        
        //biggest ten product before input number
        val biggestTenProduct = findBiggestTenProductBeforeInputNumber(longNumber)
        if (biggestTenProduct == 0L) return stringHandler("$longNumber")
        
        val tenProductDivisor = stringHandler("${longNumber / biggestTenProduct}")
        val tenProductName = tenProducts[biggestTenProduct] ?: ""
        val remainderNumber = longNumber % biggestTenProduct
        if (remainderNumber == 0L) return "$tenProductDivisor $tenProductName"
        val remainderNumberName = stringHandler("$remainderNumber")
        return "$tenProductDivisor $tenProductName و $remainderNumberName"
    }
    
    /**
     * Find biggest ten product before input number,
     * biggest ten product before 2220 is 1000
     *
     * @param longNumber
     * @return
     */
    private fun findBiggestTenProductBeforeInputNumber(longNumber : Long) : Long {
        var biggestTenProduct = 0L
        if (longNumber >= 1_000L) {
            for (product in tenProducts) if (longNumber / product.key in 1 until longNumber) biggestTenProduct = product.key
        }
        return biggestTenProduct
    }
    
    private fun bigIntegerHandler(input : BigInteger) : String {
        val biggestTenProduct = findBiggestTenProductBeforeInputNumber(input)
        
        if (biggestTenProduct == zeroBigInteger) return longHandler(input.toLong())
        
        val tenProductDivisor = stringHandler("${input.divide(biggestTenProduct)}")
        
        var tenProductName = NaN
        bigIntegerTenProducts[biggestTenProduct]?.let { tenProductName = it }
        tenProducts[biggestTenProduct.toLong()]?.let { tenProductName = it }
        
        val remainderNumber = input.mod(biggestTenProduct)
        if (remainderNumber == zeroBigInteger) return "$tenProductDivisor $tenProductName"
        val remainderNumberName = stringHandler("$remainderNumber")
        return "$tenProductDivisor $tenProductName $AND $remainderNumberName"
    }
    
    private fun findBiggestTenProductBeforeInputNumber(input : BigInteger) : BigInteger {
        var biggestTenProduct = zeroBigInteger
        
        if (input >= BigInteger("1000")) {
            for (product in tenProducts) {
                val temp = input.divide(product.key.toBigInteger())
                if (temp >= oneBigInteger && temp < input) biggestTenProduct = product.key.toBigInteger()
            }
        }
        if (input >= bigTen.pow(21)) {
            for (product in bigIntegerTenProducts) {
                val temp = input.divide(product.key)
                if (temp >= oneBigInteger && temp < input) biggestTenProduct = product.key
            }
        }
        return biggestTenProduct
    }
    
    /**
     * Big decimal handler
     *
     * convenient method to avoid calling bigDecimalHandler(BigDecimal(number))
     *
     * @param bigDecimalString string that is probably can be parsed to BigDecimal
     */
    private fun bigDecimalHandler(bigDecimalString : String) = bigDecimalHandler(BigDecimal(bigDecimalString))
    
    /**
     * Big decimal handler,
     * handle conversion of decimal numbers like 3.14, 0.0, 0.1, 0.0002, 1.0002
     *
     * @param bigDecimal big decimal input number
     * @return string representation of given decimal number in farsi
     */
    private fun bigDecimalHandler(bigDecimal : BigDecimal) : String {
        val zeroDecimal = BigDecimal.ZERO
        when (bigDecimal.compareTo(zeroDecimal)) {
            -1 -> return "$MINUS ${bigDecimalHandler(bigDecimal.abs())}"
            0 -> return ZERO
            1 -> {
                //dividing integer and fraction part from decimal
                val integerPart = bigDecimal.toBigInteger()
                val fraction = bigDecimal.remainder(BigDecimal.ONE)
                //if input only contains integers and no fraction like 1.0, 14.5
                val isIntegerOnly = fraction == zeroDecimal || fraction.compareTo(zeroDecimal) == 0
                if (isIntegerOnly) return bigIntegerHandler(integerPart)
                //if bigDecimal is 3.14 then decimals is 14
                val decimals = fraction.scaleByPowerOfTen(fraction.scale())
                //if bigDecimal is 3.14 then ten product is 100 or صدم
                val tenProduct = bigTen.pow(fraction.scale())
                //add م to صد so it becomes صدم
                var tenProductName = "${bigIntegerHandler(tenProduct)}${PersianNumber.TH}"
                tenProductName = normalizeTenProductName(tenProductName)
                //if input is only fraction like 0.5, 0.0002
                val isFractionOnly = integerPart == zeroBigInteger || integerPart.compareTo(
                        zeroBigInteger) == 0
                val fractionName = bigIntegerHandler(BigInteger("$decimals"))
                if (isFractionOnly) return "$fractionName $tenProductName"
                //if input is normal like 3.14, 3.121323, 15.00001
                val integerName = bigIntegerHandler(integerPart)
                return "$integerName $RADIX $fractionName، $tenProductName"
            }
        }
        return NaN
    }
    
    /**
     * Normalize ten product name,
     *
     * since we don't want return value to be سه ممیز چهارده، یک صدم
     * and be سه ممیز چهارده، صدم
     * then we remove that part
     *
     * @param tenProductName
     * @return
     */
    private fun normalizeTenProductName(tenProductName : String) : String {
        var tenProductName1 = tenProductName
        val persianOne = PersianNumber.ONE
        if (tenProductName1.startsWith(persianOne)) {
            tenProductName1 = tenProductName1.replace(persianOne, "").trim()
        }
        return tenProductName1
    }
    
    /**
     * this method check if input
     * is only containing numbers
     *
     * @param input is number in String type
     * @return true if only contains number characters
     */
    private fun isNumberOnly(input : String) : Boolean {
        var isNumberOnly = true
        input.forEach { char -> isNumberOnly = isNumberOnly && char in '0'..'9' }
        return isNumberOnly
    }
    
    companion object {
        
        /**
         * extension method to for spelling number to farsi
         *
         */
        fun Any.spell() = Digits().spellToFarsi(this)
    }
}