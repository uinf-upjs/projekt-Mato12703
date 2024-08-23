package com.example.upjsfd

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.upjsfd.entities.Review
import com.example.upjsfd.entities.User

class TextReviewAdapter(
    private val reviews: List<Review>,
    private val usersMap: Map<Int, User>
) : RecyclerView.Adapter<TextReviewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userNameTextView: TextView = itemView.findViewById(R.id.userNameTextView)
        val reviewTextView: TextView = itemView.findViewById(R.id.reviewTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = reviews[position]
        val user = usersMap[review.idUser]
        holder.userNameTextView.text = user?.meno ?: "Neznámy používateľ"
        holder.reviewTextView.text = review.text
    }

    override fun getItemCount(): Int = reviews.size
}