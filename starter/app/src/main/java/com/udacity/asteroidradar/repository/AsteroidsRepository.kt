package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.asDatabaseModel
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.network.AsteroidsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

const val START_DATE = 0
const val END_DATE = 1

class AsteroidsRepository(private val database: AsteroidsDatabase) {

    private val TAG = "AsteroidsRepository"

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val dates = getCurrentStartAndEndDates()
                val stringResponse = AsteroidsApi.retrofitService
                    .getAsteroids(dates[START_DATE], dates[END_DATE], Constants.API_KEY)
                val jsonResponse = JSONObject(stringResponse)
                val asteroidList = parseAsteroidsJsonResult(jsonResponse)

                database.asteroidDao.insertAll(asteroidList.asDatabaseModel())
            } catch (e: Exception) {
                Log.i(TAG, "Exception: " + e.message)
            }
        }
    }

    suspend fun deletePastAsteroids() {
        withContext(Dispatchers.IO) {
            val currentDay = getCurrentStartAndEndDates()[START_DATE]
            database.asteroidDao.deletePastEntries(currentDay)
        }
    }

    suspend fun getPictureOfDay(): PictureOfDay? {
        return try {
            AsteroidsApi.retrofitService.getPictureOfDay(Constants.API_KEY)
        } catch (e: Exception) {
            Log.i(TAG,"exception " + e.message)
            null
        }
    }
}

private fun getCurrentStartAndEndDates(): Array<String> {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())

    val startTime = dateFormat.format(calendar.time)
    calendar.add(Calendar.DAY_OF_YEAR, 7)
    val endTime = dateFormat.format(calendar.time)

    return listOf(startTime, endTime).toTypedArray()
}