package ir.yamin.digits

import ir.yamin.digits.constants.IranCurrency
import ir.yamin.digits.constants.PersianNumber
import java.math.BigDecimal
import java.math.BigInteger

private const val NaN = "NaN"

class Digits {
    
    /**
     * Spell number to Persian/Farsi words
     *
     * @param number in any type like Byte/Short/Int/Long/String/Big Integer
     * @return Persian representation of that number in String type
     */
    fun spellToFarsi(number : Any) : String {
        val result : String = when (number) {
            is Byte -> spellToFarsi("${number.toInt()}")
            is Short -> spellToFarsi("${number.toInt()}")
            is Int -> spellToFarsi("$number")
            is Long -> spellToFarsi("$number")
            is Float -> bigDecimalHandler(BigDecimal(number.toDouble()))
            is Double -> bigDecimalHandler(BigDecimal(number))
            is String -> stringHandler(number)
            is BigInteger -> bigIntegerHandler(number)
            else -> "$number"
        }
        return result.trim()
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
            val zeroOnlyRegex = Regex("[0]+")
            val decimalRegex = Regex("\\d*\\.\\d+")
            
            if (number.isEmpty()) return ""
            when (number[0]) {
                '-' -> { //when input starts with - like -a, -12, -, --, -a12, ...
                    val numberWithoutMinus = number.substring(1)
                    return if (numberWithoutMinus.isNotEmpty()) {
                        if (isNumberOnly(numberWithoutMinus)) { //when normal input like -12 -123123 -5612 -0
                            return if (zeroOnlyRegex.matches(
                                        numberWithoutMinus)
                            ) PersianNumber.singleDigits[0L]!!
                            else "${PersianNumber.MINUS} ${stringHandler(numberWithoutMinus)}"
                        } else { //when input contains anything other than numbers 0-9 like --, -., -.5, -1.5
                            if (decimalRegex.matches(numberWithoutMinus)) {
                                return bigDecimalHandler(BigDecimal(number))
                            } else NaN
                        }
                    } else NaN //when input is only -
                }
                '0' -> { // when input starts with 0
                    //handling numbers that starts with zero and removes starting zeros
                    if (zeroOnlyRegex.matches(number)) return PersianNumber.singleDigits[0L]!!
                    for (index in number.indices) {
                        if (number[index] == '0') continue
                        else return stringHandler(number.substring(index))
                    }
                }
                '.' -> if (decimalRegex.matches(number)) return bigDecimalHandler(BigDecimal(number))
                else -> if (decimalRegex.matches(number)) return bigDecimalHandler(BigDecimal(number))
            }
            
            return when (number.length) {
                1 -> PersianNumber.singleDigits[number.toLong()] ?: ""
                2 -> twoDigitHandler(number)
                3 -> threeDigitsHandler(number)
                else -> digitsHandler(number)
            }
        } catch (exception : Exception) {
            return NaN
        }
    }
    
    /**
     * Two digit numbers handler
     *
     * @param number in String type
     * @return Persian representation of that number
     */
    private fun twoDigitHandler(number : String) : String {
        if (number.length < 2) return stringHandler(number)
        val temp = PersianNumber.twoDigits[number.toLong()]
        return if (temp != null) temp
        else {
            val oneNotation = "${number[1]}".toLong()
            val tenNotation = ("${number[0]}".toLong()) * 10
            "${PersianNumber.twoDigits[tenNotation]} و ${PersianNumber.singleDigits[oneNotation]}"
        }
    }
    
    /**
     * Three digits number handler
     *
     * @param number in String type
     * @return persian representation of that number
     */
    private fun threeDigitsHandler(number : String) : String {
        val temp = PersianNumber.threeDigits[number.toLong()]
        return if (temp != null) temp
        else {
            val oneNotation = "${number[2]}".toLong()
            val tenNotation = (("${number[1]}".toLong()) * 10) + oneNotation
            val hundredNotation = ("${number[0]}".toLong()) * 100
            "${PersianNumber.threeDigits[hundredNotation]} و ${twoDigitHandler("$tenNotation")}"
        }
    }
    
    /**
     * Digits handler
     *
     * @param number in String type
     * @return persian representation of that number
     */
    private fun digitsHandler(number : String) : String {
        return if (number.length > 18) {
            val bigInteger = number.toBigInteger()
            bigIntegerHandler(bigInteger)
        } else longHandler(number.toLong())
    }
    
    private fun longHandler(longNumber : Long) : String {
        if (PersianNumber.singleDigits[longNumber] != null) return PersianNumber.singleDigits[longNumber]!!
        if (PersianNumber.twoDigits[longNumber] != null) return PersianNumber.twoDigits[longNumber]!!
        if (PersianNumber.threeDigits[longNumber] != null) return PersianNumber.threeDigits[longNumber]!!
        if (PersianNumber.tenMultipliers[longNumber] != null) return "${PersianNumber.ONE} ${PersianNumber.tenMultipliers[longNumber]}"
        
        var multiplier = 0L
        if (longNumber >= 1_000L) {
            for (tenMultiplier in PersianNumber.tenMultipliers) if (longNumber / tenMultiplier.key in 1 until longNumber) multiplier = tenMultiplier.key
        }
        if (multiplier == 0L) return stringHandler("$longNumber")
        val tenMultiplierDivisor = stringHandler("${longNumber / multiplier}")
        val tenMultiplierName = PersianNumber.tenMultipliers[multiplier] ?: ""
        if (longNumber % multiplier == 0L) return "$tenMultiplierDivisor $tenMultiplierName"
        val remainder = stringHandler("${longNumber - (longNumber / multiplier) * multiplier}")
        return "$tenMultiplierDivisor $tenMultiplierName و $remainder"
    }
    
    private fun bigIntegerHandler(input : BigInteger) : String {
        var multiplierBig = BigInteger("0")
        
        if (input >= BigInteger("1000")) {
            for (tenMultiplier in PersianNumber.tenMultipliers) {
                val temp = input.divide(tenMultiplier.key.toBigInteger())
                if (temp >= BigInteger.ONE && temp < input) multiplierBig = tenMultiplier.key.toBigInteger()
            }
        }
        if (input >= BigInteger("10").pow(21)) {
            for (tenMultiplier in PersianNumber.bigIntegerMultipliers) {
                val temp = input.divide(tenMultiplier.key)
                if (temp >= BigInteger.ONE && temp < input) multiplierBig = tenMultiplier.key
            }
        }
        
        if (multiplierBig == BigInteger.ZERO) return longHandler(input.toLong())
        
        val tenMultiplierDivisor = stringHandler("${input.divide(multiplierBig)}")
        val tenMultiplierName = when {
            PersianNumber.bigIntegerMultipliers[multiplierBig] != null -> {
                PersianNumber.bigIntegerMultipliers[multiplierBig]!!
            }
            PersianNumber.tenMultipliers[multiplierBig.toLong()] != null -> {
                PersianNumber.tenMultipliers[multiplierBig.toLong()]!!
            }
            else -> NaN
        }
        if (input.mod(multiplierBig) == BigInteger.ZERO) return "$tenMultiplierDivisor $tenMultiplierName"
        val remainder = stringHandler("${input.minus(input.divide(multiplierBig).multiply(multiplierBig))}")
        return "$tenMultiplierDivisor $tenMultiplierName و $remainder"
    }
    
    private fun bigDecimalHandler(bigDecimal : BigDecimal) : String {
        try {
            when (bigDecimal.compareTo(BigDecimal.ZERO)) {
                -1 -> return "${PersianNumber.MINUS} ${bigDecimalHandler(bigDecimal.abs())}"
                0 -> return PersianNumber.ZERO
                1 -> {
                    val integerPart = bigDecimal.toBigInteger()
                    val fraction = bigDecimal.remainder(BigDecimal.ONE)
                    val zero = BigDecimal.ZERO
                    val isIntegerOnly = fraction == zero || fraction.compareTo(zero) == 0
                    if (isIntegerOnly) return bigIntegerHandler(integerPart)
                    
                    val decimals = fraction.scaleByPowerOfTen(fraction.scale())
                    val multiplier = BigInteger("10").pow(fraction.scale())
                    var multiplierName = "${bigIntegerHandler(multiplier)}${PersianNumber.TH}"
                    if (multiplierName.startsWith(PersianNumber.ONE)) {
                        multiplierName = multiplierName.replace(PersianNumber.ONE, "").trim()
                    }
                    
                    val integerName = bigIntegerHandler(integerPart)
                    val fractionName = bigIntegerHandler(BigInteger("$decimals"))
                    return "$integerName ${PersianNumber.RADIX} $fractionName $multiplierName"
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
}