package com.plexus.example.libs.kotlinutils.extensions

/**
 * Adds a prefix to the current string.
 *
 * @param prefix the string to prepend.
 * @return a new string with the prefix added at the beginning.
 */
fun String.prefix(prefix: String): String = prefix + this

/**
 * Adds a suffix to the current string.
 *
 * @param suffix the string to append.
 * @return a new string with the suffix added at the end.
 */
fun String.suffix(suffix: String): String = this + suffix
