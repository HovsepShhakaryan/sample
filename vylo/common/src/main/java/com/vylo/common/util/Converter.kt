package com.vylo.common.util

import android.annotation.SuppressLint
import android.text.format.DateFormat
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.abs
import kotlin.math.pow


class Converter(date: String?, dateFormat: Int) {

    var mNewDate: String? = null
    var mDate: Date? = null

    init {
        if (date != null) {
            val input = SimpleDateFormat(DATE_FORMAT_1, Locale.ENGLISH)
            input.timeZone = TimeZone.getTimeZone("UTC")
            val output: SimpleDateFormat = when (dateFormat) {
                1 -> SimpleDateFormat(DATE_FORMAT_2, Locale.ENGLISH)
                2 -> SimpleDateFormat(DATE_FORMAT_3, Locale.ENGLISH)
                3 -> SimpleDateFormat(DATE_FORMAT_4, Locale.ENGLISH)
                4 -> SimpleDateFormat(DATE_FORMAT_6, Locale.ENGLISH)
                5 -> SimpleDateFormat(DATE_FORMAT_5, Locale.ENGLISH)
                6 -> SimpleDateFormat(DATE_FORMAT_7, Locale.ENGLISH)
                7 -> SimpleDateFormat(DATE_FORMAT_8, Locale.ENGLISH)
                8 -> SimpleDateFormat(DATE_FORMAT_9, Locale.ENGLISH)
                9 -> SimpleDateFormat(DATE_FORMAT_10, Locale.ENGLISH)
                10 -> SimpleDateFormat(DATE_FORMAT_11, Locale.ENGLISH)
                else -> SimpleDateFormat(DATE_FORMAT_1, Locale.ENGLISH)
            }
            var d: Date? = null
            try {
                d = input.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            mDate = d
            mNewDate = output.format(Objects.requireNonNull(d))
        }
    }

    companion object {

        const val DEFAULT_TIME = "00:00 AM"
        const val DEFAULT_QUANTITY = "1"
        const val DEFAULT_SERVICE_TIME: Long = 1200
        private const val DATE_FORMAT_1 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        private const val DATE_FORMAT_2 = "dd/MM/yyyy"
        private const val DATE_FORMAT_3 = "hh:mm aa"
        private const val DATE_FORMAT_4 = "dd MMM HH:mmaa"
        private const val DATE_FORMAT_5 = "dd/MM/yyyy/HH:mm"
        private const val DATE_FORMAT_6 = "dd MMM./yyyy HH:mm aa"
        private const val DATE_FORMAT_7 = "dd/MM/yyyy hh:mm aa"
        private const val DATE_FORMAT_8 = "yyyy-MM-dd"
        private const val DATE_FORMAT_9 = "HH:mm aa"
        private const val DATE_FORMAT_10 = "dd Mmm"
        private const val DATE_FORMAT_11 = "dd MMM yyyy,HH:mm"
        private const val DATE_FORMAT_12 = "MMMM dd, yyyy"
        private const val DATE_FORMAT_13 = "yyyy"
        private const val DATE_FORMAT_14 = "M"
        private const val DATE_FORMAT_15 = "dd"
        private const val DATE_FORMAT_16 = "mm:ss"

        fun converter(date: String?, dateFormat: Int): Converter {
            return Converter(date, dateFormat)
        }

        fun fromDatePickerToDate(date: String): String {
            val originalFormat = SimpleDateFormat(DATE_FORMAT_2, Locale.ENGLISH)
            val targetFormat = SimpleDateFormat(DATE_FORMAT_12, Locale.ENGLISH)
            val dateFormat = originalFormat.parse(date)

            return dateFormat?.let {
                targetFormat.format(it)
            } ?: run {
                ""
            }
        }

        fun fromDatePickerToDateForServer(date: String): String {
            val originalFormat = SimpleDateFormat(DATE_FORMAT_12, Locale.ENGLISH)
            val targetFormat = SimpleDateFormat(DATE_FORMAT_8, Locale.ENGLISH)
            val dateFormat = originalFormat.parse(date)

            return dateFormat?.let {
                targetFormat.format(it)
            } ?: run {
                ""
            }
        }

        fun makeFormat(size: String, indicator: String) = "$size $indicator"

        fun getDurationString(seconds: Double): String {
            var secondsCount = seconds
            val hours = (secondsCount / 3600).toInt()
            val minutes = (secondsCount % 3600 / 60).toInt()
            secondsCount %= 60
            return twoDigitString(hours) + " : " + twoDigitString(minutes)
        }

        fun getDurationString(seconds: Long): String {
            val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_16, Locale.ENGLISH)
            return LocalTime.MIN.plusSeconds(seconds).format(formatter)
        }

        private fun twoDigitString(number: Int): String {
            if (number == 0) return "00"
            return if (number / 10 == 0) "0$number" else number.toString()
        }

        fun metersToMiles(meters: Double): Double {
            return truncateTo(meters / 1609.3440057765)
        }

        fun minuteToTime(minute: Double): String {
            return (minute.toInt() / 24 / 60).toString() + " day " + minute.toInt() / 60 % 24 + ":" + minute.toInt() % 60
        }

        fun incrementTime(time: Int): Int {
            var timeCount = time
            timeCount++
            var hours = timeCount / 100
            val minutes = (timeCount - hours * 100) % 60
            if (minutes == 0) hours++
            return hours * 100 + minutes
        }

        private fun truncateTo(unRoundedNumber: Double): Double {
            val truncatedNumberInt = (unRoundedNumber * 10.0.pow(4.0)).toInt()
            return truncatedNumberInt / 10.0.pow(4.0)
        }

        fun addSecondsToDate(seconds: Int, beforeTime: Date, isFirstPoint: Boolean): Long {
            val curTimeInSeconds: Long = beforeTime.time / 1000
            val addedTimeInSeconds: Int =
                if (isFirstPoint) (curTimeInSeconds - 3600).toInt() else curTimeInSeconds.toInt() + seconds
            return addedTimeInSeconds.toLong()
        }

        fun convertTimestampToDate(timestamp: Long, dateFormat: Int): String {
            val date: String
            val cal: Calendar = Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis = timestamp * 1000L
            date = when (dateFormat) {
                1 -> DateFormat.format(DATE_FORMAT_2, cal).toString()
                2 -> DateFormat.format(DATE_FORMAT_3, cal).toString()
                3 -> DateFormat.format(DATE_FORMAT_4, cal).toString()
                else -> DateFormat.format(DATE_FORMAT_5, cal).toString()
            }
            return date
        }

        @SuppressLint("SimpleDateFormat")
        fun margeDateTime(dateTime: String?, date: String, time: String, merged: String?): String {
            var margeDT: Date? = null
            val simpleDateFormat = SimpleDateFormat(DATE_FORMAT_7)
            val parse = SimpleDateFormat(DATE_FORMAT_1)
            parse.timeZone = TimeZone.getTimeZone("UTC")
            val margeDateTime: String = merged ?: (dateTime ?: "$date $time")
            try {
                margeDT = simpleDateFormat.parse(margeDateTime)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return parse.format(Objects.requireNonNull(margeDT))
        }

        val currentDateByDate: Date
            get() {
                val calendar: Calendar =
                    Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault())
                return calendar.time
            }

        val currentDate: String
            get() {
                val dateFormat = SimpleDateFormat(DATE_FORMAT_1, Locale.ENGLISH)
                val calendar: Calendar =
                    Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault())
                val currentLocalTime: Date = calendar.time
                dateFormat.timeZone = TimeZone.getDefault()
                return dateFormat.format(currentLocalTime)
            }

        fun amPmConverter(hourOfDay: Int, minute: Int): String {
            var hourOfDayCount = hourOfDay
            val time: String
            val amPm: String
            if (hourOfDayCount < 12) {
                if (hourOfDayCount == 0) hourOfDayCount = 0
                amPm = "AM"
            } else {
                if (hourOfDayCount != 12) hourOfDayCount -= 12
                amPm = "PM"
            }
            var h = hourOfDayCount.toString() + ""
            var m = minute.toString() + ""
            if (h.length == 1) h = "0$h"
            if (m.length == 1) m = "0$m"
            time = "$h:$m $amPm"
            return time
        }

        val currentTimezoneOffset: String
            get() {
                val tz: TimeZone = TimeZone.getDefault()
                val cal: Calendar = GregorianCalendar.getInstance(tz)
                val offsetInMillis: Int = tz.getOffset(cal.timeInMillis)
                var offset = String.format(
                    "%02d:%02d",
                    abs(offsetInMillis / 3600000),
                    abs(offsetInMillis / 60000 % 60)
                )
                offset = (if (offsetInMillis >= 0) "+" else "-") + offset
                return offset
            }

        fun getNowDate(isToday: Boolean): String {
            val today = Date()
            val calendar: Calendar = Calendar.getInstance()
            calendar.time = today
            val year: Int = calendar.get(Calendar.YEAR)
            var month: Int = calendar.get(Calendar.MONTH)
            val dayOfMonth: Int = calendar.get(Calendar.DAY_OF_MONTH)
            return if (isToday) (if (dayOfMonth < 10) "0" else "") + dayOfMonth +
                    "/" + (if (month < 10 && month != 9) "0" else "") + ++month +
                    "/" + year else (if (dayOfMonth < 10) "0" else "") + (dayOfMonth + 1) +
                    "/" + (if (month < 10 && month != 9) "0" else "") + ++month + "/" + year
        }

        fun getDurationStringSep(seconds: Long): String {
            val hours = (seconds / 3600).toInt()
            val minutes = (seconds % 3600 / 60).toInt()
            val second = seconds.toInt() % 60
            return if (hours > 0) "$hours hour $minutes min" else "$minutes min"
        }

        fun getCurrentTimeInMillis(time: String): Long {
            val sdf = SimpleDateFormat(DATE_FORMAT_9, Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
//            sdf.timeZone = TimeZone.getDefault()
            var date: Date? = null
            try {
                date = sdf.parse(time)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return date!!.time
        }

        fun fromBackEndToFrontEndFormat(date: String): String {
            val originalFormat = SimpleDateFormat(DATE_FORMAT_8, Locale.ENGLISH)
            val targetFormat = SimpleDateFormat(DATE_FORMAT_12, Locale.ENGLISH)
            val dateFormat = originalFormat.parse(date)

            return dateFormat?.let {
                targetFormat.format(it)
            } ?: run {
                ""
            }
        }

        fun fromFrontEndToBackEndFormat(date: String): String {
            val originalFormat = SimpleDateFormat(DATE_FORMAT_12, Locale.ENGLISH)
            val targetFormat = SimpleDateFormat(DATE_FORMAT_8, Locale.ENGLISH)
            val dateFormat = originalFormat.parse(date)

            return dateFormat?.let {
                targetFormat.format(it)
            } ?: run {
                ""
            }
        }

        fun fromLongToFrontEndFormat(date: Long): String {
            val format = SimpleDateFormat(DATE_FORMAT_12, Locale.ENGLISH)
            return format.format(date)
        }

        fun fromBackEndToYearFormat(date: String): Int {
            val originalFormat = SimpleDateFormat(DATE_FORMAT_8, Locale.ENGLISH)
            val targetFormat = SimpleDateFormat(DATE_FORMAT_13, Locale.ENGLISH)
            val dateFormat = originalFormat.parse(date)
            return dateFormat?.let {
                targetFormat.format(dateFormat).toInt()
            } ?: run {
                2000
            }
        }

        fun fromBackEndToMonthFormat(date: String): Int {
            val originalFormat = SimpleDateFormat(DATE_FORMAT_8, Locale.ENGLISH)
            val targetFormat = SimpleDateFormat(DATE_FORMAT_14, Locale.ENGLISH)
            val dateFormat = originalFormat.parse(date)
            return dateFormat?.let {
                targetFormat.format(dateFormat).toInt() - 1
            } ?: run {
                1
            }
        }

        fun fromBackEndToDayFormat(date: String): Int {
            val originalFormat = SimpleDateFormat(DATE_FORMAT_8, Locale.ENGLISH)
            val targetFormat = SimpleDateFormat(DATE_FORMAT_15, Locale.ENGLISH)
            val dateFormat = originalFormat.parse(date)
            return dateFormat?.let {
                targetFormat.format(dateFormat).toInt()
            } ?: run {
                1
            }
        }

        fun convertDigitsToLong(text: String): Long {
            val digitsList = Regex("[0-9]+")
                .findAll(text)
                .map(MatchResult::value)
                .toList()

            return if (digitsList.size > 1)
                (digitsList[0].toLong() * 60) + digitsList[1].toLong()
            else digitsList[0].toLong() * 60
        }

        fun convertDigitsToDouble(text: String): Double {
            val digitsList = Regex("[0-9]+")
                .findAll(text)
                .map(MatchResult::value)
                .toList()

            return if (digitsList.size > 1)
                (digitsList[0].toDouble() * 60) + digitsList[1].toDouble()
            else digitsList[0].toDouble() * 60
        }

        fun digitsOnly(name: String): String {
            val regex = Regex("[^0-9]")
            return regex.replace(name, "")
        }

        fun isNumeric(str: String) = str.all { it in '0'..'9' }

        fun jsonToText(jsonString: String, key: String) = JSONObject(jsonString).getString(key)

        fun timeAgo(data: String): String {
            val timeAgo = TimeAgoConverter()
            return timeAgo.getTimeAgo(data)
        }

    }

}