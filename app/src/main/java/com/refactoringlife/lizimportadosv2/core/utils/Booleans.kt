package com.refactoringlife.lizimportadosv2.core.utils

inline fun <R> Boolean?.isTrue(block: (Boolean) -> R): R? =
    if (this == true) block(true) else null