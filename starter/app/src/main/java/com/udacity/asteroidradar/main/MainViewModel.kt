package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch

private const val TAG = "MainViewModel"

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(database)

    private val _navigateToDetailScreen = MutableLiveData<Asteroid>()
    val navigateToDetailScreen : LiveData<Asteroid>
        get() = _navigateToDetailScreen

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay : LiveData<PictureOfDay>
        get() = _pictureOfDay

    init {
        viewModelScope.launch {
            asteroidsRepository.deletePastAsteroids()
            Log.i(TAG,"Before refresh asteroids")
            asteroidsRepository.refreshAsteroids()
            Log.i(TAG,"After refresh asteroids")
            _pictureOfDay.value = asteroidsRepository.getPictureOfDay()
        }
    }

    val asteroids = asteroidsRepository.asteroids

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToDetailScreen.value = asteroid
    }

    fun onDoneNavigating() {
        _navigateToDetailScreen.value = null
    }
}