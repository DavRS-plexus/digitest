package com.plexus.example.libs.kotlinutils.extensions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.Locale

class NumberExtensionsTest {

    // ---- formatDecimals ----

    @Test
    fun `GIVEN a double, WHEN formatDecimals is called, THEN uses the comma and the requested decimals`() {
        // Given / When / Then
        assertEquals("1,352", 1.352.formatDecimals(3))
        assertEquals("30,50", 30.5.formatDecimals(2))
        assertEquals("34,47", 34.47.formatDecimals(2))
    }

    @Test
    fun `GIVEN a BigDecimal, WHEN formatDecimals is called, THEN uses the comma and the requested decimals`() {
        // Given / When / Then
        assertEquals("1,598", BigDecimal("1.598").formatDecimals(3))
        assertEquals("30,50", BigDecimal("30.5").formatDecimals(2))
        assertEquals("30", BigDecimal("30.4").formatDecimals(0))
    }

    @Test
    fun `GIVEN a BigDecimal with more decimals than requested, WHEN formatDecimals is called, THEN it is rounded half up`() {
        // Given / When / Then
        assertEquals("0,20", BigDecimal("0.199").formatDecimals(2))
        assertEquals("0,13", BigDecimal("0.125").formatDecimals(2))
        assertEquals("2,99", BigDecimal("2.985").formatDecimals(2))
    }

    @Test
    fun `GIVEN a BigDecimal, WHEN formatDecimals is called with another locale, THEN uses that locale`() {
        // Given
        val value = BigDecimal("1234.5")

        // When
        val result = value.formatDecimals(2, Locale.US)

        // Then
        assertEquals("1234.50", result)
    }

    // ---- toEuros ----

    @Test
    fun `GIVEN an amount, WHEN toEuros is called, THEN it has two decimals and no space before the symbol`() {
        // Given / When / Then
        assertEquals("2,98€", BigDecimal("2.98").toEuros())
        assertEquals("50,01€", BigDecimal("50.01").toEuros())
        assertEquals("9,00€", BigDecimal("9.0").toEuros())
        assertEquals("0,00€", BigDecimal.ZERO.toEuros())
    }

    @Test
    fun `GIVEN a negative amount, WHEN toEuros is called, THEN keeps the sign`() {
        // Given / When / Then
        assertEquals("-1,28€", BigDecimal("-1.28").toEuros())
        assertEquals("-2,00€", BigDecimal("-2.0").toEuros())
    }

    @Test
    fun `GIVEN an amount with more than two decimals, WHEN toEuros is called, THEN it is rounded half up to cents`() {
        // Given / When / Then
        assertEquals("0,20€", BigDecimal("0.199").toEuros())
        assertEquals("1,01€", BigDecimal("1.005").toEuros())
    }

    @Test
    fun `GIVEN a big amount, WHEN toEuros is called, THEN there is no thousands separator`() {
        // Given
        val amount = BigDecimal("12345.6")

        // When
        val result = amount.toEuros()

        // Then
        assertEquals("12345,60€", result)
    }

    @Test
    fun `GIVEN a subtraction of BigDecimals, WHEN toEuros is called, THEN the result is exact`() {
        // Given
        val vatAmount = BigDecimal("50.01") - BigDecimal("41.33")

        // When
        val result = vatAmount.toEuros()

        // Then
        assertEquals("8,68€", result)
    }

    // ---- toPercent ----

    @Test
    fun `GIVEN a whole percentage, WHEN toPercent is called, THEN it has no decimals`() {
        // Given / When / Then
        assertEquals("21%", 21.0.toPercent())
        assertEquals("23%", 23.0.toPercent())
        assertEquals("0%", 0.0.toPercent())
    }

    @Test
    fun `GIVEN a percentage with decimals, WHEN toPercent is called, THEN it has one decimal`() {
        // Given / When / Then
        assertEquals("5,5%", 5.5.toPercent())
        assertEquals("7,3%", 7.25.toPercent())
    }
}
