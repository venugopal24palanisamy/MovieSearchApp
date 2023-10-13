package com.venu.moviesearchapp


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.venu.moviesearchapp.api.ApiService
import com.venu.moviesearchapp.model.moviesData.MoviesData
import com.venu.moviesearchapp.model.moviesData.Search
import com.venu.moviesearchapp.utils.Constants.API_KEY
import com.venu.moviesearchapp.utils.Constants.TYPE
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel : ViewModel() {

    private val _movieDetails = MutableLiveData<List<Search>>()
    val movieDetails: LiveData<List<Search>>
        get() = _movieDetails

    private val _movieDetailsError = MutableLiveData<String?>()
    val movieDetailsError: LiveData<String?>
        get() = _movieDetailsError

    var movieSearchName by mutableStateOf("")
    var movieSearchNameError by mutableStateOf("")
    var isMovieSearchNameError by mutableStateOf(false)

    var isLoading by mutableStateOf(false)

    fun validate(): Boolean {
        if (movieSearchName.isEmpty()) {
            movieSearchNameError = "Enter Movie Name"
            isMovieSearchNameError = true
            return false
        } else {
            movieSearchNameError = ""
            isMovieSearchNameError = false
        }
        return true
    }

    fun getMovieDetails() {
        viewModelScope.launch {
            isLoading = true
            val call: Call<MoviesData> =
                ApiService.getInstance().getMoviesBySearch(TYPE, API_KEY, movieSearchName)
            call.enqueue(object : Callback<MoviesData> {
                override fun onResponse(call: Call<MoviesData>, response: Response<MoviesData>) {
                    isLoading = false
                    if (response.isSuccessful) {
                        val moviesData: MoviesData? = response.body()
                        _movieDetails.value = moviesData?.Search
                        //Log.d("onResponse", Gson().toJson(response.body()))
                    } else {
                        _movieDetailsError.value = response.errorBody().toString()
                    }
                }
                override fun onFailure(call: Call<MoviesData>, t: Throwable) {
                    _movieDetailsError.value = t.localizedMessage
                    isLoading = false
                }

            })
        }
    }
}