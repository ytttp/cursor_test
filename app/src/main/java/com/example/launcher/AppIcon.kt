package com.example.launcher

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

data class AppIcon(
    val id: Long,
    val label: String,
    @DrawableRes val iconRes: Int,
    @ColorRes val backgroundColorRes: Int
)
