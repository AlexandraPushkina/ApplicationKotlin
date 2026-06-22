package com.example.homework6.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    fun formatPostDate(timestamp: Long): String {
       val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("ru"))
        val date = Date(timestamp)
        return sdf.format(date)
    }
}
