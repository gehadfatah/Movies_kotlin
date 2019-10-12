package com.goda.movieappliation.features

import android.app.Application

class MoviesApp :Application(){

    override fun onCreate() {
        super.onCreate()
        com.goda.domain.Domain.integrateWith(this)
    }
}