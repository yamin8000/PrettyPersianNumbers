package com.github.yamin8000.ppn.util

import java.math.BigDecimal
import java.math.BigInteger

internal object Constants {

    internal const val NaN = "NaN"
    internal const val zeroChar = '0'
    internal const val dashChar = '-'
    internal const val dotChar = '.'

    internal val zeroBigInteger: BigInteger by lazy(LazyThreadSafetyMode.NONE) { BigInteger.ZERO }
    internal val oneBigInteger: BigInteger by lazy(LazyThreadSafetyMode.NONE) { BigInteger.ONE }

    internal val zeroDecimal: BigDecimal = BigDecimal.ZERO
}