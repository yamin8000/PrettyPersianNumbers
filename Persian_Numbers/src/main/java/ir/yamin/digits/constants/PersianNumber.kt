package ir.yamin.digits.constants

internal object PersianNumber {
    
    internal const val MINUS = "منفی"
    internal const val RADIX = "ممیز"
    private const val ZERO = "صفر"
    internal const val ONE = "یک"
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
    private const val BILLION = "میلیارد"
    private const val TRILLION = "تریلیون"
    
    val singleDigits = mutableMapOf(0L to ZERO, 1L to ONE, 2L to TWO, 3L to THREE, 4L to FOUR, 5L to FIVE,
                                    6L to SIX, 7L to SEVEN, 8L to EIGHT, 9L to NINE)
    
    val twoDigits = mutableMapOf(10L to TEN, 11L to "یاز$TEN", 12L to "دواز$TEN", 13L to "سیز$TEN",
                                 14L to "$FOUR$TEN", 15L to "پانز$TEN", 16L to "شانز$TEN", 17L to "هف$TEN",
                                 18L to "هج$TEN", 19L to "نوز$TEN", 20L to "بیست", 30L to "سی", 40L to "چهل",
                                 50L to "پنجاه", 60L to "شصت", 70L to "هفتاد", 80L to "هشتاد", 90L to "نود")
    
    val threeDigits = mutableMapOf(100L to "یک$HUNDRED", 200L to "دویست", 300L to "سی$HUNDRED",
                                   400L to "$FOUR$HUNDRED", 500L to "پان$HUNDRED", 600L to "$SIX$HUNDRED",
                                   700L to "$SEVEN$HUNDRED", 800L to "$EIGHT$HUNDRED",
                                   900L to "$NINE$HUNDRED")
    
    val tenMultipliers = mutableMapOf(1_000L to THOUSAND, 1_000_000L to MILLION, 1_000_000_000L to BILLION,
                                      1_000_000_000_000L to TRILLION)
}