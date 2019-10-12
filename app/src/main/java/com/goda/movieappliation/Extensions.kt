package com.goda.movieappliation

import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.goda.entities.MovieEntity
import com.goda.movieappliation.core.CropSquareTransformation
import com.goda.movieappliation.features.details.DetailsActivity
import com.goda.movieappliation.features.home.EXTRA_MOVIE
import com.goda.movieappliation.features.home.MainActivity
import com.goda.movieappliation.features.home.MoviesAdapter
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main_activity.*
import java.util.concurrent.TimeUnit



 fun MainActivity.showMessage(message: String) {
    Snackbar.make(coordinator_layout, message, Snackbar.LENGTH_SHORT).show()
}


 fun MainActivity.bindViews() = kotlin.with(viewModel) {

     isLoading.observe(this@bindViews,
         Observer { isLoading -> home_progress_bar.visibility = if (isLoading) View.VISIBLE else View.GONE })


     errorLiveData.observe(this@bindViews,
         Observer { showMessage(it) })

     kotlin.with(home_movies_recycler_view) {
         setHasFixedSize(true)
         layoutManager = LinearLayoutManager(this@bindViews, LinearLayoutManager.HORIZONTAL, false)
         adapter = MoviesAdapter(this@bindViews, topRatedMoviesListLiveData, this@bindViews)
     }
     kotlin.with(popular_movies_recycler_view) {
         setHasFixedSize(true)
         layoutManager = LinearLayoutManager(this@bindViews, LinearLayoutManager.HORIZONTAL, false)
         adapter = MoviesAdapter(this@bindViews, popularMoviesListLiveData, this@bindViews)
     }

     showMovieDetails
         .debounce(500, TimeUnit.MILLISECONDS)
         .observeOn(AndroidSchedulers.mainThread())
         .subscribe { movie -> startDetailsScreen(movie as MovieEntity) }
         .also { disposables.add(it) }

 }!!


private fun MainActivity.startDetailsScreen(movieEntity: MovieEntity) {
    Intent(this, DetailsActivity::class.java)
        .putExtra(EXTRA_MOVIE, movieEntity)
        .also { startActivity(it) }
}


 fun DetailsActivity.bindViews() = kotlin.with(detailsViewModel) {

    titleLiveData.observe(this@bindViews,
        Observer {
            toolbar_details.title = it
            toolbar_layout.title = it
        })

    releaseDateLiveData.observe(this@bindViews, Observer {
        release_date.text = "Release Date: $it"
    })
    voteAverageDateLiveData.observe(this@bindViews, Observer {
        vote_avg.text = "$it"
    })
    overViewLiveData.observe(this@bindViews, Observer {
        overView.text = it
    })
    posterPathLiveData.observe(this@bindViews, Observer {
        Picasso.get()
            .load(com.goda.domain.getImageURL(it, com.goda.domain.IMAGE_SIZE))
            .transform(CropSquareTransformation(10, 0))
            .noFade()
            .into(detail_img_movie, object : Callback {
                override fun onSuccess() {
                    supportStartPostponedEnterTransition()
                }

                override fun onError(e: Exception?) {
                    supportStartPostponedEnterTransition()
                }

            })
    })

    backDropLiveData.observe(this@bindViews, Observer {
        Picasso.get()
            .load(com.goda.domain.getImageURL(it))
            .into(detail_img_cover)
    })

}