package com.plexus.example.libs.kotlinutils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object DateConverter {

    enum class DatePattern(val pattern: String) {
        YYYY_MM_DD("yyyy-MM-dd"),
        DD_MM_YYYY("dd/MM/yyyy"),
        HH_MM_SS("HH:mm:ss")
        // ... Add more date formats as needed
    }

    /**
     * Converts epoch milliseconds (in UTC time zone) to a [LocalDate] using the system's default
     * time zone.
     *
     * The conversion assumes the epoch milliseconds represent a point in time (UTC), which is then
     * converted to the system's time zone and truncated to a date (time component discarded).
     *
     * @return the [LocalDate] corresponding to this epoch milliseconds in the system's time zone.
     */
    fun Long.toLocalDate(): LocalDate =
        Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()

    /**
     * Converts epoch milliseconds (in UTC) to a [LocalDateTime] using the system's default time zone.
     *
     * @return the [LocalDateTime] corresponding to this instant in the system's default time zone.
     */
    fun Long.toLocalDateTime(): LocalDateTime =
        Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDateTime()

    /**
     * Formats this epoch milliseconds value as a system time zone date string using the provided
     * [DatePattern].
     *
     * The conversion is performed in the system default time zone and the resulting string follows
     * the pattern defined by [DatePattern.pattern].
     *
     * @param format the date format to apply.
     * @return the formatted system time zone date string.
     */
    fun Long.toLocalFormattedDate(format: DatePattern): String =
        Instant.ofEpochMilli(this)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern(format.pattern))

    /**
     * Formats this epoch milliseconds value as a UTC date string using the provided [DatePattern].
     *
     * The conversion is performed in UTC and the resulting string follows the pattern defined by
     * [DatePattern.pattern].
     *
     * @param format the date format to apply.
     * @return the formatted UTC date string.
     */
    fun Long.toUtcFormattedDate(format: DatePattern): String =
        Instant.ofEpochMilli(this)
            .atZone(ZoneOffset.UTC)
            .format(DateTimeFormatter.ofPattern(format.pattern))

    /**
     * Converts a [LocalDate] to epoch milliseconds using UTC time zone.
     *
     * The conversion represents the start of day (00:00:00) in UTC.
     *
     * @return epoch milliseconds representing the start of this date in UTC.
     */
    fun LocalDate.toUtcMillis(): Long = atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

    /**
     * Converts an ISO-8601 formatted date string (for example, yyyy-MM-dd'T'HH:mm:ss'Z') to epoch
     * milliseconds using UTC time zone.
     *
     * @return epoch milliseconds corresponding to this date string in UTC.
     */
    fun String.toUtcMillis(): Long = Instant.parse(this).toEpochMilli()

    /**
     * Formats a [LocalDate] using the provided [DatePattern].
     *
     * @param format the date format to apply.
     * @return the formatted date string.
     */
    fun LocalDate.toFormattedString(format: DatePattern): String = this.format(DateTimeFormatter.ofPattern(format.pattern))

    /**
     * Formats a [LocalDateTime] using the provided [DatePattern].
     *
     * @param format the date format to apply.
     * @return the formatted date-time string.
     */
    fun LocalDateTime.toFormattedString(format: DatePattern): String = this.format(DateTimeFormatter.ofPattern(format.pattern))
}
