package com.goda.domain.repositories

import com.goda.domain.ApiServer
import com.goda.domain.apiServer


val moviesRepository  : MoviesRepository by lazy {
    MoviesRepository()
}

class MoviesRepository(private val api: ApiServer = apiServer){

    fun getTopMoviesList() = api.getTopRatedMovies()
    fun getPopularMoviesList() = api.getPopularMovies()

}