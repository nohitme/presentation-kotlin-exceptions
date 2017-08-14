package info.ericlin.kotlin.customviews

import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import retrofit2.http.GET

interface WeatherService {

    @GET("/data/2.5/weather?q=London&appid=b1b15e88fa797225412429c1c50c122a1")
    fun get(): Single<WeatherResponse>
}

data class WeatherResponse(val name: String, @SerializedName("weathers") val weather: List<Weather>, val wind: Wind)

data class Weather(val main: String, val description: String)

data class Wind(val speed: Double, val deg: Int)