package com.nedim.vaktijawatch

import android.content.Context
import android.database.Cursor
import android.util.Log
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper
import java.util.*

/**
 * Created by e on 5/25/15.
 */
class Database(context: Context?) :
    SQLiteAssetHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    val locations: List<Any>
        get() {
            val locations: MutableList<Location> = ArrayList<Location>()
            val c: Cursor = readableDatabase.query(
                TABLE_LOCATIONS,
                null,
                null,
                null,
                null,
                null,
                COLUMN_WEIGHT
            )
            while (c.moveToNext()) {
                locations.add(
                    Location(
                        c.getString(c.getColumnIndex(COLUMN_LOCATION)),
                        c.getInt(c.getColumnIndex(COLUMN_ID))
                    )
                )
            }
            c.close()
            return locations
        }

    fun getPrayerTimesSec(month: Int, day: Int, locationId: Int): IntArray {
        Log.d(
            TAG,
            "getPrayerTimeMins month=$month day=$day locationId=$locationId"
        )
        var monthStr = month.toString() + ""
        var dayStr = day.toString() + ""
        if (month < 10) {
            monthStr = "0$month"
        }
        if (day < 10) {
            dayStr = "0$day"
        }
        val datum = "$monthStr-$dayStr"
        Log.i(TAG, "datum=$datum")
        val prayerTimes: Cursor = getReadableDatabase().query(
            TABLE_SCHEDULE,
            null,
            COLUMN_DATUM + "=?",
            arrayOf(datum),
            null,
            null,
            null
        )
        val offset: Cursor = getReadableDatabase().query(
            TABLE_OFFSET,
            null,
            COLUMN_MONTH + "=? AND " + COLUMN_LOCATION_ID + "=?",
            arrayOf(month.toString() + "", locationId.toString() + ""),
            null,
            null,
            null
        )
        Log.i(TAG, "prayerTimes count=" + prayerTimes.count)
        Log.i(TAG, "offset count=" + offset.count)
        prayerTimes.moveToFirst()
        val fajr = prayerTimes.getString(prayerTimes.getColumnIndex(COLUMN_FAJR))
        val sunrise = prayerTimes.getString(prayerTimes.getColumnIndex(COLUMN_SUNRISE))
        val dhuhr = prayerTimes.getString(prayerTimes.getColumnIndex(COLUMN_DHUHR))
        val asr = prayerTimes.getString(prayerTimes.getColumnIndex(COLUMN_ASR))
        val maghrib = prayerTimes.getString(prayerTimes.getColumnIndex(COLUMN_MAGHRIB))
        val isha = prayerTimes.getString(prayerTimes.getColumnIndex(COLUMN_ISHA))
        Log.i(
            TAG,
            "fajr=$fajr sunrise=$sunrise dhuhr=$dhuhr asr=$asr maghrib=$maghrib isha=$isha"
        )
        var fajrTime = (fajr.split(":".toRegex()).toTypedArray()[0].toInt() * 60
                + fajr.split(":".toRegex()).toTypedArray()[1].toInt())
        var sunriseTime = (sunrise.split(":".toRegex()).toTypedArray()[0].toInt() * 60
                + sunrise.split(":".toRegex()).toTypedArray()[1].toInt())
        var dhuhrTime = (dhuhr.split(":".toRegex()).toTypedArray()[0].toInt() * 60
                + dhuhr.split(":".toRegex()).toTypedArray()[1].toInt())
        var asrTime = (asr.split(":".toRegex()).toTypedArray()[0].toInt() * 60
                + asr.split(":".toRegex()).toTypedArray()[1].toInt())
        var maghribTime = (maghrib.split(":".toRegex()).toTypedArray()[0].toInt() * 60
                + maghrib.split(":".toRegex()).toTypedArray()[1].toInt())
        var ishaTime = (isha.split(":".toRegex()).toTypedArray()[0].toInt() * 60
                + isha.split(":".toRegex()).toTypedArray()[1].toInt())
        offset.moveToFirst()
        val offsetFajr = offset.getInt(offset.getColumnIndex(COLUMN_FAJR))
        val offsetDhuhr = offset.getInt(offset.getColumnIndex(COLUMN_DHUHR))
        val offsetAsr = offset.getInt(offset.getColumnIndex(COLUMN_ASR))
        Log.i(
            TAG,
            "offsetFajr=$offsetFajr offsetDhuhr=$offsetDhuhr offsetAsr=$offsetAsr"
        )
        fajrTime = (fajrTime + offsetFajr) * 60
        sunriseTime = (sunriseTime + offsetFajr) * 60
        dhuhrTime = (dhuhrTime + offsetDhuhr) * 60
        asrTime = (asrTime + offsetAsr) * 60
        maghribTime = (maghribTime + offsetAsr) * 60
        ishaTime = (ishaTime + offsetAsr) * 60
        val times = intArrayOf(fajrTime, sunriseTime, dhuhrTime, asrTime, maghribTime, ishaTime)
        Log.i(TAG, "times=" + Arrays.toString(times))
        prayerTimes.close()
        offset.close()
        return times
    }

    fun getPrayerTimesString(month: Int, day: Int, locationId: Int): List<String> {
        Log.d(
            TAG,
            "getPrayerTimeMins month=$month day=$day locationId=$locationId"
        )
        var monthStr = month.toString() + ""
        var dayStr = day.toString() + ""
        if (month < 10) {
            monthStr = "0$month"
        }
        if (day < 10) {
            dayStr = "0$day"
        }
        val datum = "$monthStr-$dayStr"
        Log.i(TAG, "datum=$datum")
        val prayerTimes: Cursor = getReadableDatabase().query(
            TABLE_SCHEDULE,
            null,
            COLUMN_DATUM + "=?",
            arrayOf(datum),
            null,
            null,
            null
        )
        val offset: Cursor = getReadableDatabase().query(
            TABLE_OFFSET,
            null,
            COLUMN_MONTH + "=? AND " + COLUMN_LOCATION_ID + "=?",
            arrayOf(month.toString() + "", locationId.toString() + ""),
            null,
            null,
            null
        )
        Log.i(TAG, "prayerTimes count=" + prayerTimes.count)
        Log.i(TAG, "offset count=" + offset.count)
        prayerTimes.moveToFirst()
        val fajr = prayerTimes.getString(prayerTimes.getColumnIndex(COLUMN_FAJR))
        val sunrise = prayerTimes.getString(prayerTimes.getColumnIndex(COLUMN_SUNRISE))
        val dhuhr = prayerTimes.getString(prayerTimes.getColumnIndex(COLUMN_DHUHR))
        val asr = prayerTimes.getString(prayerTimes.getColumnIndex(COLUMN_ASR))
        val maghrib = prayerTimes.getString(prayerTimes.getColumnIndex(COLUMN_MAGHRIB))
        val isha = prayerTimes.getString(prayerTimes.getColumnIndex(COLUMN_ISHA))
        Log.i(
            TAG,
            "fajr=$fajr sunrise=$sunrise dhuhr=$dhuhr asr=$asr maghrib=$maghrib isha=$isha"
        )
        var fajrTime = (fajr.split(":".toRegex()).toTypedArray()[0].toInt() * 60
                + fajr.split(":".toRegex()).toTypedArray()[1].toInt())
        var sunriseTime = (sunrise.split(":".toRegex()).toTypedArray()[0].toInt() * 60
                + sunrise.split(":".toRegex()).toTypedArray()[1].toInt())
        var dhuhrTime = (dhuhr.split(":".toRegex()).toTypedArray()[0].toInt() * 60
                + dhuhr.split(":".toRegex()).toTypedArray()[1].toInt())
        var asrTime = (asr.split(":".toRegex()).toTypedArray()[0].toInt() * 60
                + asr.split(":".toRegex()).toTypedArray()[1].toInt())
        var maghribTime = (maghrib.split(":".toRegex()).toTypedArray()[0].toInt() * 60
                + maghrib.split(":".toRegex()).toTypedArray()[1].toInt())
        var ishaTime = (isha.split(":".toRegex()).toTypedArray()[0].toInt() * 60
                + isha.split(":".toRegex()).toTypedArray()[1].toInt())
        offset.moveToFirst()
        val offsetFajr = offset.getInt(offset.getColumnIndex(COLUMN_FAJR))
        val offsetDhuhr = offset.getInt(offset.getColumnIndex(COLUMN_DHUHR))
        val offsetAsr = offset.getInt(offset.getColumnIndex(COLUMN_ASR))
        Log.i(
            TAG,
            "offsetFajr=$offsetFajr offsetDhuhr=$offsetDhuhr offsetAsr=$offsetAsr"
        )
        fajrTime = (fajrTime + offsetFajr) * 60
        sunriseTime = (sunriseTime + offsetFajr) * 60
        dhuhrTime = (dhuhrTime + offsetDhuhr) * 60
        asrTime = (asrTime + offsetAsr) * 60
        maghribTime = (maghribTime + offsetAsr) * 60
        ishaTime = (ishaTime + offsetAsr) * 60
        val times = intArrayOf(fajrTime, sunriseTime, dhuhrTime, asrTime, maghribTime, ishaTime)
        Log.i(TAG, "times=" + Arrays.toString(times))
        prayerTimes.close()
        offset.close()
        return listOf(
            fajr, sunrise, dhuhr, asr, maghrib, isha
        )
    }

    fun getLocationName(locationId: Int): String {
        val c: Cursor = getReadableDatabase().query(
            TABLE_LOCATIONS,
            null,
            COLUMN_ID + "=?",
            arrayOf(locationId.toString() + ""),
            null,
            null,
            null
        )
        c.moveToFirst()
        return c.getString(c.getColumnIndex(COLUMN_LOCATION))
    }

    companion object {
        val TAG = Database::class.java.simpleName
        private const val DATABASE_NAME = "vaktija.db"
        private const val DATABASE_VERSION = 3
        const val TABLE_LOCATIONS = "locations"
        const val TABLE_SCHEDULE = "schedule"
        const val TABLE_OFFSET = "offset"
        const val COLUMN_ID = "_id"
        const val COLUMN_WEIGHT = "weight"
        const val COLUMN_LOCATION = "location"
        const val COLUMN_LOCATION_ID = "location_id"
        const val COLUMN_DATUM = "datum"
        const val COLUMN_MONTH = "month"
        const val COLUMN_FAJR = "fajr"
        const val COLUMN_SUNRISE = "sunrise"
        const val COLUMN_DHUHR = "dhuhr"
        const val COLUMN_ASR = "asr"
        const val COLUMN_MAGHRIB = "maghrib"
        const val COLUMN_ISHA = "isha"
    }

    init {
        setForcedUpgrade()
    }
}