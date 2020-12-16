package com.jessy.shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.row_movie.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.uiThread
import java.net.URL

class MovieActivity : AppCompatActivity() , AnkoLogger{
    private val TAG  = MovieActivity::class.java.simpleName
    var movies:List<MovieItem>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)
        doAsync {
            val json = URL("https://gist.githubusercontent.com/saniyusuf/406b843afdfb9c6a86e25753fe2761f4/raw/523c324c7fcc36efab8224f9ebb7556c09b69a14/Film.JSON").readText()
            movies = Gson().fromJson<List<MovieItem>>(json,
            object : TypeToken<List<MovieItem>>(){}.type)
            movies?.forEach {
                info("${it.Title} ${it.imdbRating}")
            }
            uiThread {
                recycler.layoutManager = LinearLayoutManager(this@MovieActivity)
                recycler.setHasFixedSize(true)
                recycler.adapter = MovieAdapter()
            }
        }

    }

    inner class MovieAdapter() : RecyclerView.Adapter<MovieHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.row_movie, parent, false)
            return MovieHolder(view)
        }

        override fun onBindViewHolder(holder: MovieHolder, position: Int){
            val movie = movies?.get(position)
            holder.bindMovie(movie!!)
        }

        override fun getItemCount(): Int {
            val size = movies?.size?: 0
            return size
        }

    }

    inner class MovieHolder(view: View) : RecyclerView.ViewHolder(view){
        val titleText: TextView = view.movie_title
        val imdbText: TextView = view.moovie_imdb
        val directorText: TextView = view.movie_director
        val posterImage: ImageView = view.movie_poster
        fun bindMovie(movie: MovieItem) {
            titleText.text = movie.Title
            imdbText.text = movie.imdbRating
            directorText.text = movie.Director
            Glide.with(this@MovieActivity)
                    .load(movie.Poster)
                    .override(300)
                    .into(posterImage)
        }
    }
}

//class Movie : ArrayList<MovieItem>()

data class MovieItem(
    val Actors: String,
    val Awards: String,
    val ComingSoon: Boolean,
    val Country: String,
    val Director: String,
    val Genre: String,
    val Images: List<String>,
    val Language: String,
    val Metascore: String,
    val Plot: String,
    val Poster: String,
    val Rated: String,
    val Released: String,
    val Response: String,
    val Runtime: String,
    val Title: String,
    val Type: String,
    val Writer: String,
    val Year: String,
    val imdbID: String,
    val imdbRating: String,
    val imdbVotes: String,
    val totalSeasons: String
)