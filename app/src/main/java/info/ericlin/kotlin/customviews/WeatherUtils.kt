@file:JvmName("WeatherUtilJava")

package info.ericlin.kotlin.customviews

fun List<Weather>.toDisplayText(): String {
    return this.sortedBy { it.main }
            .map { "${it.main} - ${it.description}" }
            .firstOrNull() ?: "unknown weather"
}

