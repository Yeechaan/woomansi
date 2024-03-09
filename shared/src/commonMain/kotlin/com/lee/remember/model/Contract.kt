package com.lee.remember.model

data class Contract(
    val id: String,
    val name: String,
    val number: String,
    var isChecked: Boolean = false,
)