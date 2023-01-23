package com.vylo.common.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TimeAgoConverter {

    private val SECOND_MILLIS = 1000
    private val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private val DAY_MILLIS = 24 * HOUR_MILLIS

    private fun currentDate(): Date {
        val calendar: Calendar = Calendar.getInstance()
        return calendar.time
    }

    fun getTimeAgo(date: String): String {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        format.timeZone = TimeZone.getTimeZone("UTC")

        try {
            var time: Long = format.parse(date).time
            if (time < 1000000000000L) {
                time *= 1000
            }
            val now: Long = currentDate().time
            if (time > now || time <= 0) return "in the future"

            val diff = now - time
            return when {
                diff < MINUTE_MILLIS -> { "moments ago" }
                diff < 2 * MINUTE_MILLIS -> { "a minute ago" }
                diff < 60 * MINUTE_MILLIS -> { "${diff / MINUTE_MILLIS} minutes ago" }
                diff < 2 * HOUR_MILLIS -> { "an hour ago" }
                diff < 24 * HOUR_MILLIS -> { "${diff / HOUR_MILLIS} hours ago" }
                diff < 48 * HOUR_MILLIS -> { "yesterday" }
                else -> { "${diff / DAY_MILLIS} days ago" }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            return ""
        }
    }
}


