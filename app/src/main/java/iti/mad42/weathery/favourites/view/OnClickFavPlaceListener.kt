package iti.mad42.weathery.favourites.view

import iti.mad42.weathery.model.pojo.FavoriteWeather

interface OnClickFavPlaceListener {
    fun onClickFavPlace(favPlace : FavoriteWeather)
}