package com.github.yamin8000.ppn.util

import java.math.BigInteger

internal object Constants {

    private const val ZERO_ONLY_REGEX = "[0]+"
    private const val DECIMAL_REGEX = "\\d*\\.\\d+"

    internal const val NaN = "NaN"
    internal const val zeroChar = '0'
    internal const val dashChar = '-'
    internal const val dotChar = '.'

    internal val zeroOnlyRegex: Regex by lazy(LazyThreadSafetyMode.NONE) { Regex(ZERO_ONLY_REGEX) }
    internal val decimalRegex: Regex by lazy(LazyThreadSafetyMode.NONE) { Regex(DECIMAL_REGEX) }

    internal val zeroBigInteger: BigInteger by lazy(LazyThreadSafetyMode.NONE) { BigInteger.ZERO }
    internal val oneBigInteger: BigInteger by lazy(LazyThreadSafetyMode.NONE) { BigInteger.ONE }
}