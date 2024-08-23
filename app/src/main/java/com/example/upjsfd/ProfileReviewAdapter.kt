package com.example.upjsfd

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.upjsfd.entities.Film
import com.example.upjsfd.entities.Review

class ProfileReviewAdapter(
    private val reviews: List<Review>,
    private val films: Map<Int, Film>
) : RecyclerView.Adapter<ProfileReviewAdapter.FilmReviewViewHolder>() {

    class FilmReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val filmTitleTextView: TextView = view.findViewById(R.id.filmTitleTextView)
        val reviewTextView: TextView = view.findViewById(R.id.reviewTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review_profile, parent, false)
        return FilmReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilmReviewViewHolder, position: Int) {
        val review = reviews[position]
        val film = films[review.idFilm]

        if (review.text.isNullOrEmpty()) {

            holder.itemView.visibility = View.GONE
        } else {

            holder.filmTitleTextView.text = film?.nazov ?: "Neznámy film"
            holder.reviewTextView.text = review.text
            holder.itemView.visibility = View.VISIBLE
        }
    }

    override fun getItemCount() = reviews.size
}
