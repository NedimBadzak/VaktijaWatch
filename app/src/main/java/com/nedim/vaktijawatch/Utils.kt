package com.nedim.vaktijawatch

import android.util.Log
import java.util.*

class Utils {
    companion object {

        fun getCorrectList(list : IntArray, year: Int, month: Int, day: Int) : List<String?> {
            var novaLista : MutableList<Int> = mutableListOf()
            for (i in list.indices) {
                novaLista.add(getDstRespectingPrayerTime(list[i], year, month, day))
            }
            var stringLista : MutableList<String> = mutableListOf()
            for(i in novaLista.indices) {
                stringLista.add(getTimeStringDots(novaLista[i]).toString())
            }
            return stringLista
        }

        fun getTimeStringDots(totalSecs: Int): String? {
            // MyLog.d("FormattingUtils", "getTimeString totalSecs="+totalSecs+" ceil="+ceil);
            //		int seconds = (int) (milliseconds / 1000) % 60 ;
            val seconds = totalSecs % 60
            val minutes = totalSecs / 60 % 60
            val hours = totalSecs / 3600 % 24
            return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes)
        }



        fun getDstRespectingPrayerTime(
            defaultPrayerTime: Int,
            year: Int,
            month: Int,
            day: Int
        ): Int {
            var month = month
            Log.d(
                "TAGIC",
                "[getDstRespectingPrayerTime defaultPrayerTime=$defaultPrayerTime year=$year month=$month day=$day]"
            )

            // beacuse months have index 0 in java Calendar
            --month
            val summerTimeOn = isSummerTimeOn(year, month, day)
            Log.i("TAGIC", "summer time on: $summerTimeOn")
            if (summerTimeOn && (month == Calendar.MARCH || month == Calendar.OCTOBER)) {
                if (day >= 25 && day <= 30) {
                    Log.i("TAGIC", "adding one hour to default prayer time")
                    return defaultPrayerTime + 3600
                    //return defaultPrayerTime + 60;
                }
            }
            return defaultPrayerTime
        }

        fun isSummerTimeOn(year: Int, month: Int, day: Int): Boolean {
            val calendar = Calendar.getInstance(Locale.getDefault())
            calendar[Calendar.YEAR] = year
            calendar[Calendar.MONTH] = month
            calendar[Calendar.DAY_OF_MONTH] = day
            return TimeZone.getDefault().inDaylightTime(Date(calendar.timeInMillis))
        }
    }
}