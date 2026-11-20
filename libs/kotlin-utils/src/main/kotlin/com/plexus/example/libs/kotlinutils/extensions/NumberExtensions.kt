package com.plexus.example.libs.kotlinutils.extensions

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

// TODO: We must create a class "Amount" and its "Formatter or Converter" to handle all the number formatting
//  in a single place, with a default locale and currency.
//  This will avoid having to repeat the same code in multiple places.

// "1,352" with 3 decimals. Spanish by default: same comma decimal as Portuguese.
@Deprecated("Think a class to handle all the number formatting in a single place, with a default locale and currency")
fun Double.formatDecimals(decimals: Int, locale: Locale = SPANISH): String =
    String.format(locale, "%.${decimals}f", this)

@Deprecated("Think a class to handle all the number formatting in a single place, with a default locale and currency")
fun Double?.formatGowBalance(): String {
    val df = DecimalFormat("###,###,##0.00")
    df.roundingMode = RoundingMode.HALF_UP
    df.isGroupingUsed = true
    this?.let {
        return String.format(Locale.getDefault(), "%s€", df.format(it))
    }
    return ""
}

@Deprecated("Think a class to handle all the number formatting in a single place, with a default locale and currency")
fun BigDecimal.formatDecimals(decimals: Int, locale: Locale = SPANISH): String =
    String.format(locale, "%.${decimals}f", this)

// "2,98€": no space before the symbol, as in the Figma
@Deprecated("Think a class to handle all the number formatting in a single place, with a default locale and currency")
fun BigDecimal.toEuros(locale: Locale = SPANISH): String = "${formatDecimals(2, locale)}€"

// "21%" for whole numbers, "5,5%" otherwise
@Deprecated("Think a class to handle all the number formatting in a single place, with a default locale and currency")
fun Double.toPercent(locale: Locale = SPANISH): String =
    if (this % 1.0 == 0.0) "${toInt()}%" else "${formatDecimals(1, locale)}%"

private val SPANISH: Locale = Locale.forLanguageTag("es-ES")

@Deprecated("Think a class to handle all the number formatting in a single place, with a default locale and currency")
fun BigDecimal.formatWithCurrency(): String {
    return NumberFormat.getCurrencyInstance(Locale.getDefault()).apply {
        currency = Currency.getInstance("EUR")
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }.format(this)
}
