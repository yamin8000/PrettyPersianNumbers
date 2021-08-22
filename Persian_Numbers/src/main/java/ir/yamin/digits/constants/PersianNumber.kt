package ir.yamin.digits.constants

import java.math.BigInteger

/**
 * numbers representation in Formal Persian
 */
internal object PersianNumber {
    
    //صدم، میلیونم، دهم و ...
    internal const val TH = "م"
    internal const val MINUS = "منفی"
    internal const val RADIX = "ممیز"
    internal const val ZERO = "صفر"
    internal const val ONE = "یک"
    internal const val AND = "و"
    private const val TWO = "دو"
    private const val THREE = "سه"
    private const val FOUR = "چهار"
    private const val FIVE = "پنج"
    private const val SIX = "شش"
    private const val SEVEN = "هفت"
    private const val EIGHT = "هشت"
    private const val NINE = "نه"
    private const val TEN = "ده"
    private const val HUNDRED = "صد"
    private const val THOUSAND = "هزار"
    private const val MILLION = "میلیون"
    private const val MILLIARD = "میلیارد"
    private const val TRILLION = "تریلیون"
    private const val QUADRILLION = "کوآدریلیون"
    private const val QUINTILLION = "کوینتیلیون"
    private const val SEXTILLION = "سکستیلیون"
    private const val SEPTILLION = "سپتیلیون"
    private const val OCTILLION = "اکتیلیون"
    private const val NONILLION = "نانیلیون"
    private const val DECILLION = "دسیلیون"
    private const val UNDECILLION = "آندسیلیون"
    private const val DUODECILLION = "دیودسیلیون"
    private const val TREDECILLION = "تریدسیلیون"
    private const val QUATTUORDECILLION = "کواتیوردسیلیون"
    private const val QUINDECILLION = "کویندسیلیون"
    private const val SEXDECILLION = "سکسدسیلیون"
    private const val SEPTENDECILLION = "سپتدسیلیون"
    private const val OCTODECILLION = "اُکتودسیلیون"
    private const val NOVEMDECILLION = "نومدسیلیون"
    private const val VIGINTILLION = "ویجینتیلیون"
    
    /**
     * Single digits numbers representation
     */
    val singleDigits = mapOf(
            0L to ZERO, 1L to ONE, 2L to TWO, 3L to THREE, 4L to FOUR, 5L to FIVE, 6L to SIX,
            7L to SEVEN, 8L to EIGHT, 9L to NINE,
                            )
    
    /**
     * Two digits numbers representation
     */
    val twoDigits = mapOf(
            10L to TEN, 11L to "یاز$TEN", 12L to "دواز$TEN", 13L to "سیز$TEN",
            14L to "$FOUR$TEN", 15L to "پانز$TEN", 16L to "شانز$TEN", 17L to "هف$TEN",
            18L to "هج$TEN", 19L to "نوز$TEN", 20L to "بیست", 30L to "سی", 40L to "چهل",
            50L to "پنجاه", 60L to "شصت", 70L to "هفتاد", 80L to "هشتاد", 90L to "نود",
                         )
    
    /**
     * Three digits numbers representation
     */
    val threeDigits = mapOf(
            100L to "یک$HUNDRED", 200L to "دویست", 300L to "سی$HUNDRED",
            400L to "$FOUR$HUNDRED", 500L to "پان$HUNDRED", 600L to "$SIX$HUNDRED",
            700L to "$SEVEN$HUNDRED", 800L to "$EIGHT$HUNDRED", 900L to "$NINE$HUNDRED",
                           )
    
    /**
     * Products of Ten representation
     */
    val tenPowers = mapOf(
            1_000L to THOUSAND, 1_000_000L to MILLION, 1_000_000_000L to MILLIARD,
            1_000_000_000_000L to TRILLION, 1_000_000_000_000_000L to QUADRILLION,
            1_000_000_000_000_000_000L to QUINTILLION,
                         )
    
    /**
     * Big integer Products of Ten representation
     */
    internal val bigTen = BigInteger("10")
    val bigIntegerTenPowers = mapOf(
            bigTen.pow(21) to SEXTILLION, bigTen.pow(24) to SEPTILLION,
            bigTen.pow(27) to OCTILLION, bigTen.pow(30) to NONILLION,
            bigTen.pow(33) to DECILLION, bigTen.pow(36) to UNDECILLION,
            bigTen.pow(39) to DUODECILLION, bigTen.pow(42) to TREDECILLION,
            bigTen.pow(45) to QUATTUORDECILLION, bigTen.pow(48) to QUINDECILLION,
            bigTen.pow(51) to SEXDECILLION, bigTen.pow(54) to SEPTENDECILLION,
            bigTen.pow(57) to OCTODECILLION, bigTen.pow(60) to NOVEMDECILLION,
            bigTen.pow(63) to VIGINTILLION,
                                   )
}