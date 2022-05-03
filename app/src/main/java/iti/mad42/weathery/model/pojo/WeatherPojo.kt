package iti.mad42.weathery.model.pojo

data class WeatherPojo(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezoneOffset: Long,
    val current: CurrentWeather,
    val hourly: List<CurrentWeather>,
    val daily: List<DailyWeather>
)
