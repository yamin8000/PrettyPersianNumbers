@file:Suppress("unused")

package ir.yamin.digits

import ir.yamin.digits.constants.IranCurrency
import ir.yamin.digits.constants.PersianNumber
import ir.yamin.digits.constants.PersianNumber.AND
import ir.yamin.digits.constants.PersianNumber.MINUS
import ir.yamin.digits.constants.PersianNumber.RADIX
import ir.yamin.digits.constants.PersianNumber.ZERO
import ir.yamin.digits.constants.PersianNumber.bigIntegerMultipliers
import ir.yamin.digits.constants.PersianNumber.bigTen
import ir.yamin.digits.constants.PersianNumber.singleDigits
import ir.yamin.digits.constants.PersianNumber.tenMultipliers
import ir.yamin.digits.constants.PersianNumber.threeDigits
import ir.yamin.digits.constants.PersianNumber.twoDigits
import java.math.BigDecimal
import java.math.BigInteger

private const val NaN = "NaN"
private const val ZERO_ONLY_REGEX = "[0]+"
private const val DECIMAL_REGEX = "\\d*\\.\\d+"

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
        try {
            val zeroChar = '0'
            if (number.isBlank()) return NaN
            when (number[0]) {
                '-' -> return handleStringsWithMinusPrefix(number)
                //when input starts with 0
                //handling numbers that starts with zero and removes starting zeros
                zeroChar -> {
                    if (zeroOnlyRegex.matches(number)) return ZERO
                    /**
                     * this can probably be replaced with some smart-ass regex,
                     * but that makes it more complex
                     *
                     * this loop re-call this method with version of this number that doesn't contain starting zeros
                     */
                    for (index in number.indices) {
                        if (number[index] == zeroChar) continue
                        else return stringHandler(number.substring(index))
                    }
                }
                //numbers like .0, .14 are decimals too
                '.' -> if (decimalRegex.matches(number)) return bigDecimalHandler(BigDecimal(number))
                else -> if (decimalRegex.matches(number)) return bigDecimalHandler(BigDecimal(number))
            }
            
            return when (number.length) {
                1 -> singleDigits[number.toLong()] ?: NaN
                2 -> twoDigitHandler(number)
                3 -> threeDigitsHandler(number)
                else -> digitsHandler(number)
            }
        } catch (exception : Exception) {
            return NaN
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
        return if (number.length > 18) {
            val bigInteger = number.toBigInteger()
            bigIntegerHandler(bigInteger)
        } else longHandler(number.toLong())
    }
    
    private fun longHandler(longNumber : Long) : String {
        singleDigits[longNumber]?.let { return it }
        twoDigits[longNumber]?.let { return it }
        threeDigits[longNumber]?.let { return it }
        tenMultipliers[longNumber]?.let { return "${PersianNumber.ONE} $it" }
        
        var multiplier = 0L
        if (longNumber >= 1_000L) {
            for (tenMultiplier in tenMultipliers) if (longNumber / tenMultiplier.key in 1 until longNumber) multiplier = tenMultiplier.key
        }
        if (multiplier == 0L) return stringHandler("$longNumber")
        val tenMultiplierDivisor = stringHandler("${longNumber / multiplier}")
        val tenMultiplierName = tenMultipliers[multiplier] ?: ""
        if (longNumber % multiplier == 0L) return "$tenMultiplierDivisor $tenMultiplierName"
        val remainder = stringHandler("${longNumber - (longNumber / multiplier) * multiplier}")
        return "$tenMultiplierDivisor $tenMultiplierName و $remainder"
    }
    
    // TODO: 2021-07-29 reducing cognitive complexity 
    private fun bigIntegerHandler(input : BigInteger) : String {
        var multiplierBig = zeroBigInteger
        
        if (input >= BigInteger("1000")) {
            for (tenMultiplier in tenMultipliers) {
                val temp = input.divide(tenMultiplier.key.toBigInteger())
                if (temp >= oneBigInteger && temp < input) multiplierBig = tenMultiplier.key.toBigInteger()
            }
        }
        if (input >= bigTen.pow(21)) {
            for (tenMultiplier in bigIntegerMultipliers) {
                val temp = input.divide(tenMultiplier.key)
                if (temp >= oneBigInteger && temp < input) multiplierBig = tenMultiplier.key
            }
        }
        
        if (multiplierBig == zeroBigInteger) return longHandler(input.toLong())
        
        val tenMultiplierDivisor = stringHandler("${input.divide(multiplierBig)}")
        
        var tenMultiplierName = NaN
        bigIntegerMultipliers[multiplierBig]?.let { tenMultiplierName = it }
        tenMultipliers[multiplierBig.toLong()]?.let { tenMultiplierName = it }
        
        if (input.mod(multiplierBig) == zeroBigInteger) return "$tenMultiplierDivisor $tenMultiplierName"
        val remainder = stringHandler("${input.minus(input.divide(multiplierBig).multiply(multiplierBig))}")
        return "$tenMultiplierDivisor $tenMultiplierName $AND $remainder"
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
        try {
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
                    //if bigDecimal is 3.14 then multiplier is 100 or صدم
                    val multiplier = bigTen.pow(fraction.scale())
                    //add م to صد so it becomes صدم
                    var multiplierName = "${bigIntegerHandler(multiplier)}${PersianNumber.TH}"
                    /*
                    since we don't want return value to be سه ممیز چهاردم، یک صدم
                    then we remove that part
                    */
                    val persianOne = PersianNumber.ONE
                    if (multiplierName.startsWith(persianOne)) {
                        multiplierName = multiplierName.replace(persianOne, "").trim()
                    }
                    //if input is only fraction like 0.5, 0.0002
                    val isFractionOnly = integerPart == zeroBigInteger || integerPart.compareTo(
                            zeroBigInteger) == 0
                    val fractionName = bigIntegerHandler(BigInteger("$decimals"))
                    if (isFractionOnly) return "$fractionName $multiplierName"
                    //if input is normal like 3.14, 3.121323, 15.00001
                    val integerName = bigIntegerHandler(integerPart)
                    return "$integerName $RADIX $fractionName، $multiplierName"
                }
            }
        } catch (exception : Exception) {
            return NaN
        }
        return NaN
    }
    
    /**
     * this method check if input
     * is only contains numbers
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