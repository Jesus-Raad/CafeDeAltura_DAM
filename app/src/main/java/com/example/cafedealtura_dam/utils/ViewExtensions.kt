package com.example.cafedealtura_dam.utils

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

fun View.applyTopInsets() {

    val initialPaddingTop = paddingTop

    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->

        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

        view.setPadding(
            view.paddingLeft,
            initialPaddingTop + systemBars.top,
            view.paddingRight,
            view.paddingBottom
        )

        insets
    }

    requestApplyInsets()
}