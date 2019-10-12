package com.goda.domain



fun getImageURL(moviePoster :String , size :String = BIG_IMAGE_SIZE)
        ="$BASE_IMAGE_URL$size$moviePoster"

