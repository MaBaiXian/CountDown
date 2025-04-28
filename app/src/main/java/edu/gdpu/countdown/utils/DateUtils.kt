package edu.gdpu.countdown.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    // 将时间戳转换为指定格式的字符串
    fun timestampToString(timestamp: Long, pattern: String = "yyyy-MM-dd HH:mm"): String {
        val date = Date(timestamp)
        val format = SimpleDateFormat(pattern, Locale.getDefault())
        return format.format(date)
    }

    // 计算从当前时间到目标日期剩余的天数
    fun daysUntil(targetDate: Long): Int {
        val currentDate = Calendar.getInstance().timeInMillis
        val diffInMillies = targetDate - currentDate
        return (diffInMillies / (1000 * 60 * 60 * 24)).toInt()
    }

}