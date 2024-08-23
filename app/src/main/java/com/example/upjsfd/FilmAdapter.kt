package com.example.upjsfd

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.upjsfd.entities.Film

class FilmAdapter(private var films: List<Film>) : RecyclerView.Adapter<FilmAdapter.FilmViewHolder>() {

    private var onItemClickListener: ((Film) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.film_item, parent, false)
        return FilmViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val film = films[position]
        holder.titleTextView.text = film.nazov
        holder.yearTextView.text = film.rok.toString()


        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(film)
        }
    }

    override fun getItemCount(): Int = films.size

    fun updateFilms(newFilms: List<Film>) {
        films = newFilms
        notifyDataSetChanged()

    }

    fun setOnItemClickListener(listener: (Film) -> Unit) {
        onItemClickListener = listener
    }

    class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.filmTitle)
        val yearTextView: TextView = itemView.findViewById(R.id.filmYear)
    }
}
