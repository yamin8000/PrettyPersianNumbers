package ir.yamin.digits

import ir.yamin.digits.constants.PersianNumber
import java.math.BigInteger

class Digits {
    
    /**
     * Spell number to Persian/Farsi words
     *
     * @param number in any type like Byte/Short/Int/Long/String
     * @return Persian representation of that number in String type
     */
    fun spellToFarsi(number : Any) : String {
        return when (number) {
            is Byte -> spellToFarsi("${number.toInt()}")
            is Short -> spellToFarsi("${number.toInt()}")
            is Int -> spellToFarsi("$number")
            is Long -> spellToFarsi("$number")
            //todo handling floating numbers
            is Float -> "$number"
            is Double -> "$number"
            is String -> stringHandler(number)
            else -> "$number"
        }
    }
    
    /**
     * handle numbers as a String
     *
     * @param number in String type
     * @return Persian representation of that number
     */
    private fun stringHandler(number : String) : String {
        val zeroOnlyRegex = Regex("[0]+")
        if (number.isEmpty()) return ""
        when (number[0]) {
            '-' -> { //when input starts with - like -a, -12, -, --, -a12, ...
                val numberWithoutMinus = number.substring(1)
                return if (numberWithoutMinus.isNotEmpty()) {
                    if (isNumberOnly(numberWithoutMinus)) { //when normal input like -12 -123123 -5612 -0
                        return if (zeroOnlyRegex.matches(numberWithoutMinus)) PersianNumber.singleDigits[0L]!!
                        else "${PersianNumber.MINUS} ${stringHandler(number.substring(1))}"
                    } else "NaN" //when input contains anything other than numbers 0-9
                } else "NaN" //when input is only -
            }
            '0' -> { // when input starts with 0
                //handling numbers that starts with zero and removes starting zeros
                if (zeroOnlyRegex.matches(number)) return PersianNumber.singleDigits[0L]!!
                for (index in number.indices) {
                    if (number[index] == '0') continue
                    else return stringHandler(number.substring(index))
                }
            }
            else -> if (!isNumberOnly(number)) return "NaN"
        }
        
        return when (number.length) {
            1 -> PersianNumber.singleDigits[number.toLong()] ?: ""
            2 -> twoDigitHandler(number)
            3 -> threeDigitsHandler(number)
            else -> digitsHandler(number)
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
            val oneNotation = number[1].toLong() - 48
            val tenNotation = (number[0].toLong() - 48) * 10
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
            val oneNotation = number[2].toLong() - 48
            val tenNotation = ((number[1].toLong() - 48) * 10) + oneNotation
            val hundredNotation = (number[0].toLong() - 48) * 100
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
        for (tenMultiplier in PersianNumber.tenMultipliers) if (longNumber / tenMultiplier.key in 1 until longNumber) multiplier = tenMultiplier.key
        
        val tenMultiplierDivisor = stringHandler("${longNumber / multiplier}")
        val tenMultiplierName = PersianNumber.tenMultipliers[multiplier]
        if (longNumber % multiplier == 0L) return "$tenMultiplierDivisor $tenMultiplierName"
        val remainder = stringHandler("${longNumber - (longNumber / multiplier) * multiplier}")
        return "$tenMultiplierDivisor $tenMultiplierName و $remainder"
    }
    
    private fun bigIntegerHandler(input : BigInteger) : String {
        var multiplierBig = BigInteger("0")
        for (tenMultiplier in PersianNumber.tenMultipliers) {
            val temp = input.divide(tenMultiplier.key.toBigInteger())
            if (temp >= BigInteger.ONE && temp < input) multiplierBig = tenMultiplier.key.toBigInteger()
        }
        for (tenMultiplier in PersianNumber.bigIntegerMultipliers) {
            val temp = input.divide(tenMultiplier.key)
            if (temp >= BigInteger.ONE && temp < input) multiplierBig = tenMultiplier.key
        }
        
        val tenMultiplierDivisor = stringHandler("${input.divide(multiplierBig)}")
        val tenMultiplierName = when {
            PersianNumber.bigIntegerMultipliers[multiplierBig] != null -> {
                PersianNumber.bigIntegerMultipliers[multiplierBig]!!
            }
            PersianNumber.tenMultipliers[multiplierBig.toLong()] != null -> {
                PersianNumber.tenMultipliers[multiplierBig.toLong()]!!
            }
            else -> "Nan"
        }
        if (input.mod(multiplierBig) == BigInteger.ZERO) return "$tenMultiplierDivisor $tenMultiplierName"
        val remainder = stringHandler("${input.minus(input.divide(multiplierBig).multiply(multiplierBig))}")
        return "$tenMultiplierDivisor $tenMultiplierName و $remainder"
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