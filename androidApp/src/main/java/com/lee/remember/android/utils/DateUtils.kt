package com.lee.remember.android.utils

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class DateUtils {
}

fun parseUtcString(dateString: String): String {
    val localDateTime = dateString.toInstant().toLocalDateTime(TimeZone.UTC)
    val formatter = DateTimeFormatter.ISO_DATE

    return localDateTime.toJavaLocalDateTime().format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateTime(dateString: String): String {
    //change this to match your specific string
    val dateTimePattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"

    val formatter = DateTimeFormatter.ofPattern(dateTimePattern).withZone(
        ZoneOffset.UTC
    )
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    // \u00A0 is used to prevent the view from breaking line between "10:10" and "PM"
    val dateTime =
        ZonedDateTime.parse(dateString, formatter).withZoneSameInstant(ZoneId.systemDefault())
    val eventDateString = dateTime.format(dateFormatter)

    val formattedTime = dateTime.format(timeFormatter).replace(" ", "\u00A0")

    return "$eventDateString $formattedTime"
}