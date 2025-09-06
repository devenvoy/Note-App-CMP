package com.devansh.noteapp.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import kotlinx.serialization.Serializable
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import kotlinx.datetime.*
import kotlinx.datetime.format.*

@Stable
@Serializable
data class SharedContent(val fileName: String = "", val content: String = "")

fun findAllIndices(text: String, word: String): List<Pair<Int, Int>> {
    if (word.isBlank()) return emptyList()

    return buildList {
        var index = text.indexOf(word)
        while (index != -1) {
            add(index to (index + word.length))
            index = text.indexOf(word, index + 1)
        }
    }
}

@Composable
fun rememberDateTimeFormatter(): DateTimeFormat<LocalDateTime> {
    return remember {
        LocalDateTime.Format {
            date(LocalDate.Formats.ISO)
            char(' ')
            time(LocalTime.Formats.ISO)
        }
    }
}

@Composable
fun rememberSimpleDateTimeFormatter(): DateTimeFormat<LocalDateTime> {
    return remember {
        LocalDateTime.Formats.ISO // Uses built-in ISO format
    }
}
@Composable
fun rememberShortDateTimeFormatter(): DateTimeFormat<LocalDateTime> {
    return remember {
        LocalDateTime.Format {
            monthNumber(padding = Padding.ZERO)
            char('/')
            day(padding = Padding.ZERO)
            char('/')
            year()
            char(' ')
            hour(padding = Padding.ZERO)
            char(':')
            minute(padding = Padding.ZERO)
        }
    }
}

@Composable
fun rememberUrlLauncher(): UriLauncher {
    val uriHandler = LocalUriHandler.current
    return remember { UriLauncher(uriHandler) }
}

class UriLauncher(private val uriHandler: UriHandler) {
    fun launch(url: String) {
        uriHandler.openUri(url)
    }
}

/*
@Composable
fun rememberCustomTabsIntent(): CustomTabsIntent {
    return remember {
        CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()
    }
}
*/

fun Int.toHexColor(): String {
    val value = 0xFFFFFF and this
    return buildString {
        append('#')
        append(value.toString(16).padStart(6, '0').uppercase())
    }
}

fun IntRange.overlaps(other: IntRange): Boolean {
    return this.first <= other.last && other.first <= this.last
}