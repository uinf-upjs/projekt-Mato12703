package com.example.upjsfd

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView

import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.upjsfd.dao.FilmDao
import com.example.upjsfd.dao.ReviewDao
import com.example.upjsfd.dao.UserDao
import com.example.upjsfd.entities.Film
import com.example.upjsfd.entities.Review
import kotlin.concurrent.thread

class FilmDetailActivity : AppCompatActivity() {
    private var isLoggedIn: Boolean = false
    private var idUser:Int =-1
    private lateinit var filmDao: FilmDao
    private lateinit var reviewDao: ReviewDao
    private lateinit var userDao: UserDao
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var reviewsRecyclerView: RecyclerView
    private var filmId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_detail)
        filmDao = UPJSFdDatabase.getDatabase(this).filmDao()
        reviewDao=UPJSFdDatabase.getDatabase(this).reviewDao()
        userDao=UPJSFdDatabase.getDatabase(this).userDao()
        isLoggedIn = intent.getBooleanExtra("isLoggedIn", false)
        idUser=intent.getIntExtra("idUser",-1)
        val ratingBar = findViewById<SeekBar>(R.id.ratingBar)

        val filmId = intent.getIntExtra("film_id", -1)
        thread{
            val averageRating = reviewDao.getAverageRatingForFilm(filmId)
            val averageRatingTextView = findViewById<TextView>(R.id.averageRating)



            averageRating?.let {
                val roundedRating = Math.round(it)
                averageRatingTextView.text = "Priemerné hodnotenie: $roundedRating/10"

            }?: run {
                averageRatingTextView.text = "Priemerné hodnotenie: ?/10"
            }
        }

        val profileButton = findViewById<ImageButton>(R.id.button2)
        val searchButton  = findViewById<ImageButton>(R.id.button1)
        val reviewText = findViewById<EditText>(R.id.reviewText)
        val saveReviewButton = findViewById<Button>(R.id.saveReviewButton)


        reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView)
        reviewsRecyclerView.layoutManager = LinearLayoutManager(this)
        thread {
            val existingReview = reviewDao.getReviewByUserAndFilm(idUser, filmId)
            runOnUiThread {
                existingReview?.let {
                    ratingBar.progress = it.rating ?: 0
                    reviewText.setText(it.text ?: "")
                }
            }
        }


        loadFilmDetails(filmId)
        loadReviewsAndUsers()



        if (!isLoggedIn) {
            ratingBar.isEnabled = false

            saveReviewButton.isEnabled=false;
            reviewText.isEnabled=false;
        } else {
            saveReviewButton.setOnClickListener {
                val rating = ratingBar.progress
                val reviewContent = reviewText.text.toString()


                if (rating >= 0 || reviewContent.isNotEmpty()) {
                    saveReview(filmId, rating, reviewContent)
                }
            }

        }


        profileButton.setOnClickListener {
            if (isLoggedIn) {
                val intent = Intent(this, ProfileActivity::class.java)
                intent.putExtra("idUser",idUser)
                intent.putExtra("isLoggedIn",isLoggedIn)
                startActivity(intent)
            } else {
                showLoginDialog()
            }
        }
        searchButton.setOnClickListener{
            val intent = Intent(this,SearchActivity::class.java)
            intent.putExtra("idUser",idUser)
            intent.putExtra("isLoggedIn",isLoggedIn)
            startActivity(intent)

        }

    }

    private fun loadFilmDetails(filmId: Int) {
        thread {
            val film = filmDao.getFilmById(filmId)
            runOnUiThread {
                settingFilm(film)
            }
        }
    }

    private fun settingFilm(film: Film?) {
        film?.let {
            val titleTextView = findViewById<TextView>(R.id.filmTitle)
            val yearTextView = findViewById<TextView>(R.id.filmYear)
            val descriptionTextView = findViewById<TextView>(R.id.filmDescription)
            val imageView = findViewById<ImageView>(R.id.filmImage)


            titleTextView.text = it.nazov
            yearTextView.text = it.rok.toString()
            descriptionTextView.text = it.popis
            //toto je externa kniznica https://github.com/bumptech/glide
            Glide.with(this)
                .load(it.fotoPath)
                .centerCrop()
                .into(imageView)
        }
    }
    private fun showLoginDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Nie ste prihlásený")
        builder.setMessage("Chcete sa vrátiť na login stránku?")

        builder.setPositiveButton("Áno") { dialog, _ ->
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            dialog.dismiss()
        }

        builder.setNegativeButton("Nie") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }
    private fun saveReview(filmId: Int, rating: Int, reviewContent: String) {
        thread {
            val userId = idUser

            val existingReview = reviewDao.getReviewByUserAndFilm(userId, filmId)

            if (existingReview != null) {

                reviewDao.updateReview(userId, filmId, rating, reviewContent)
            } else {

                val review = Review(
                    idReview = 0,
                    idUser = userId,
                    idFilm = filmId,
                    rating = rating,
                    text = reviewContent
                )
                reviewDao.insert(review)
            }
            runOnUiThread {
                loadReviewsAndUsers()
            }






        }
    }
    private fun loadReviewsAndUsers() {
        thread {

            val filmId = intent.getIntExtra("film_id", -1)

            val reviews: List<Review> = reviewDao.getReviewsByFilmId(filmId)
            val userIds = reviews.map { it.idUser }.distinct()
            val users = userDao.getUsersByIds(userIds)



            val usersMap = users.associateBy { it.idUser }
            val reviewsWithText = reviews.filter { !it.text.isNullOrEmpty()}




            runOnUiThread {
                if (reviews.isNotEmpty()) {

                    findViewById<TextView>(R.id.userTextView).visibility = View.VISIBLE
                    findViewById<TextView>(R.id.ratingTextView).visibility = View.VISIBLE

                } else {

                    findViewById<TextView>(R.id.userTextView).visibility = View.GONE
                    findViewById<TextView>(R.id.ratingTextView).visibility = View.GONE
                }
                if (reviewsWithText.isNotEmpty()) {
                    val reviewsHeaderTextView = findViewById<TextView>(R.id.reviewsHeaderTextView)
                    val textReviewsRecyclerView = findViewById<RecyclerView>(R.id.textReviewsRecyclerView)

                    reviewsHeaderTextView.text = "Recenzie: (${reviewsWithText.size})"
                    reviewsHeaderTextView.visibility = View.VISIBLE
                    textReviewsRecyclerView.visibility = View.VISIBLE

                    textReviewsRecyclerView.layoutManager = LinearLayoutManager(this@FilmDetailActivity)
                    textReviewsRecyclerView.adapter = TextReviewAdapter(reviewsWithText, usersMap)
                } else {
                    findViewById<TextView>(R.id.reviewsHeaderTextView).visibility = View.GONE
                    findViewById<RecyclerView>(R.id.textReviewsRecyclerView).visibility = View.GONE
                }
                reviewAdapter = ReviewAdapter(reviews, usersMap)
                reviewsRecyclerView.adapter = reviewAdapter
            }
        }
    }
}
