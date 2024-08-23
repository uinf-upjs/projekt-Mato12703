package com.example.upjsfd

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.upjsfd.dao.FilmDao
import com.example.upjsfd.dao.ReviewDao
import com.example.upjsfd.dao.UserDao
import kotlin.concurrent.thread

class ProfileActivity : AppCompatActivity() {
    private var isLoggedIn: Boolean = false
    private var idUser: Int = -1
    private lateinit var userDao: UserDao
    private lateinit var reviewDao: ReviewDao
    private lateinit var filmDao: FilmDao

    private lateinit var ratedFilmsRecyclerView: RecyclerView
    private lateinit var reviewedFilmsRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        userDao = UPJSFdDatabase.getDatabase(this).userDao()
        reviewDao = UPJSFdDatabase.getDatabase(this).reviewDao()
        filmDao = UPJSFdDatabase.getDatabase(this).filmDao()

        isLoggedIn = intent.getBooleanExtra("isLoggedIn", false)
        idUser = intent.getIntExtra("idUser", -1)

        val nameTextView = findViewById<TextView>(R.id.nameTextView)
        val ratedMoviesTextView = findViewById<TextView>(R.id.ratedMoviesTextView)
        val reviewedMoviesTextView = findViewById<TextView>(R.id.reviewedMoviesTextView)
        val reviewsHeaderTextView = findViewById<TextView>(R.id.reviewsHeaderTextView)


        ratedFilmsRecyclerView = findViewById(R.id.ratedFilmsRecyclerView)
        reviewedFilmsRecyclerView = findViewById(R.id.reviewedFilmsRecyclerView)

        ratedFilmsRecyclerView.layoutManager = LinearLayoutManager(this)
        reviewedFilmsRecyclerView.layoutManager = LinearLayoutManager(this)

        loadUserData(nameTextView, ratedMoviesTextView, reviewedMoviesTextView,reviewsHeaderTextView)

        val searchButton  = findViewById<ImageButton>(R.id.button1)
        searchButton.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            intent.putExtra("idUser", idUser)
            intent.putExtra("isLoggedIn", isLoggedIn)
            startActivity(intent)
        }
        val logOutButton = findViewById<Button>(R.id.logOutButton)
        logOutButton.setOnClickListener {
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Odhlásenie")
            builder.setMessage("Naozaj sa chcete odhlásiť?")


            builder.setPositiveButton("Áno") { dialog, _ ->

                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                Toast.makeText(this, "Boli ste úspešne odhlásený", Toast.LENGTH_SHORT).show()
                finish()
            }

            builder.setNegativeButton("Nie") { dialog, _ ->
                dialog.dismiss()
            }


            builder.show()
        }


    }

    private fun loadUserData(nameTextView: TextView, ratedMoviesTextView: TextView, reviewedMoviesTextView: TextView,reviewsHeaderTextView: TextView) {
        thread {
            idUser = intent.getIntExtra("idUser", -1)
            val user = userDao.getUserById(idUser)
            val ratedMoviesCount = reviewDao.getRatedMoviesCountByUserId(idUser)
            val reviewedMoviesCount = reviewDao.getReviewedMoviesCountByUserId(idUser)


            val ratedFilms = filmDao.getAllFilmsByUserId(idUser)
            val ratedReviews = reviewDao.getAllReviewsByUserId(idUser).associateBy { it.idFilm }

            val reviewedFilms = reviewDao.getAllReviewsByUserId(idUser)
            val reviewedFilmMap = reviewedFilms.associateBy { it.idFilm }.mapValues { filmDao.getFilmById(it.key) }


            runOnUiThread {
                nameTextView.text = "Meno: ${user?.meno}"
                ratedMoviesTextView.text = "Počet Hodnotených filmov: $ratedMoviesCount"
                reviewedMoviesTextView.text = "Počet Recenzovaných filmov: $reviewedMoviesCount"
                if(reviewedMoviesCount !=0){
                    reviewsHeaderTextView.text = "Recenzie: ($reviewedMoviesCount)"
                }else{
                    reviewsHeaderTextView.visibility= View.GONE
                }








                val ratedAdapter = FilmRatingProfileAdapter(ratedFilms, ratedReviews)
                ratedFilmsRecyclerView.adapter = ratedAdapter


                val reviewedAdapter = ProfileReviewAdapter(reviewedFilms, reviewedFilmMap)
                reviewedFilmsRecyclerView.adapter = reviewedAdapter
            }
        }
    }
}

