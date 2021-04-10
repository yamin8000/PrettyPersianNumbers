package ir.yamin.digits

import android.util.Log
import ir.yamin.digits.constants.PersianNumber

object Digits {
    
    fun spellToFarsi(number : Any) : String {
        return try {
            when (number) {
                is Byte -> spellToFarsi("${number.toInt()}")
                is Short -> spellToFarsi("${number.toInt()}")
                is Int -> {
                    if (number < 0) "${PersianNumber.MINUS} ${spellToFarsi(number * -1)}"
                    else spellToFarsi("$number")
                }
                is Long -> spellToFarsi("$number")
                is Float -> "$number"
                is Double -> "$number"
                is String -> stringHandler(number)
                else -> "$number"
            }
        } catch (exception : Exception) {
            Log.d("", "Number not supported")
            "Not Supported"
        }
    }
    
    private fun stringHandler(number : String) : String {
        if (number.isEmpty()) return ""
        number.forEach { char -> if (char !in '0'..'9') return "NaN" }
        
        //Handling inputs that starts with zero like 00001 or 01 or 0501
        if (number[0] == '0') {
            for (index in number.indices) {
                if (number[index] == '0') continue
                else return stringHandler(number.substring(index))
            }
        }
        
        return when (number.length) {
            1 -> PersianNumber.singleDigits[number.toLong()] ?: ""
            2 -> twoDigitHandler(number)
            3 -> threeDigitsHandler(number)
            else -> digitsHandler(number)
        }
    }
    
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
    
    private fun digitsHandler(number : String) : String {
        //number format exception
        //numbers bigger than 999999999999999 is out of long reach
        //todo using big integer
        val longNumber = number.toLong()
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
}