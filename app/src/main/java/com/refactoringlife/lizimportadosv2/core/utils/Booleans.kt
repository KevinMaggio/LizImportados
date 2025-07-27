package com.refactoringlife.lizimportadosv2.core.utils

inline fun <R> Boolean?.isTrue(block: (Boolean) -> R): R? =
    if (this == true) block(true) else null

inline fun <R> Boolean?.isFalse(block: (Boolean) -> R): R? =
    if (this == false) block(true) else null