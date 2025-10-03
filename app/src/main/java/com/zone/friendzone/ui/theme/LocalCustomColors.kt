package com.zone.friendzone.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider


@Composable
fun ColorsProvider(
    customColors: CustomColors,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalCustomColors provides customColors) {
        content()
    }
}
