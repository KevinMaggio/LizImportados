package com.refactoringlife.lizimportadosv2.core.utils

inline fun <R> String?.isValid(block: (String) -> R): R? {
    return if (!this.isNullOrBlank()) block(this) else null
}

fun String.capitalizeWords(): String =
    this.lowercase()
        .split(" ")
        .filter { it.isNotBlank() }
        .joinToString(" ") { word ->
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }