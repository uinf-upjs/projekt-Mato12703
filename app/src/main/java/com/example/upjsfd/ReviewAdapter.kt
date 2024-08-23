package com.example.upjsfd

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.upjsfd.entities.Review
import com.example.upjsfd.entities.User

class ReviewAdapter(private val reviews: List<Review>, private val users: Map<Int, User>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userNameTextView: TextView = view.findViewById(R.id.userNameTextView)
        val userRatingTextView: TextView = view.findViewById(R.id.userRatingTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rating_list_item, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        val user = users[review.idUser]

        holder.userNameTextView.text = user?.meno ?: "Neznámy"
        holder.userRatingTextView.text = "${review.rating}/10"
    }

    override fun getItemCount() = reviews.size
}
