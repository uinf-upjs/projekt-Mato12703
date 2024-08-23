package com.example.upjsfd

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.upjsfd.entities.Film
import com.example.upjsfd.entities.Review
import android.view.LayoutInflater

class FilmRatingProfileAdapter(
    private val films: List<Film>,
    private val reviews: Map<Int, Review>
) : RecyclerView.Adapter<FilmRatingProfileAdapter.FilmRatingViewHolder>() {

    class FilmRatingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val filmTitleTextView: TextView = itemView.findViewById(R.id.filmTitleTextView)
        val filmRatingTextView: TextView = itemView.findViewById(R.id.filmRatingTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmRatingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rating_by_user, parent, false)
        return FilmRatingViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilmRatingViewHolder, position: Int) {
        val film = films[position]
        val review = reviews[film.idFilm]

        holder.filmTitleTextView.text = film.nazov
        holder.filmRatingTextView.text = review?.rating?.toString() + "/10"
    }

    override fun getItemCount(): Int {
        return films.size
    }
}