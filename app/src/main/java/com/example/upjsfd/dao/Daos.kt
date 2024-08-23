package com.example.upjsfd.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.upjsfd.entities.Film
import com.example.upjsfd.entities.Review
import com.example.upjsfd.entities.User

@Dao
interface UserDao{
    @Insert
    fun insertUser(user: User)

    @Query("SELECT * FROM User WHERE meno = :meno AND heslo = :heslo")
    fun getUser(meno: String, heslo: String): User?

    @Query("SELECT * FROM User WHERE meno = :meno LIMIT 1")
    fun getUserByName(meno: String): User?
    @Query("SELECT * FROM User WHERE idUser IN (:userIds)")
    fun getUsersByIds(userIds: List<Int>): List<User>
    @Query("SELECT * FROM User WHERE idUser=:idUser")
    fun getUserById(idUser: Int): User

}
@Dao
interface ReviewDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(review: Review)
    @Query("SELECT * FROM Review WHERE idFilm = :filmId AND rating IS NOT NULL")
    fun getReviewsByFilmId(filmId: Int): List<Review>
    @Query("SELECT * FROM Review WHERE idUser = :userId AND idFilm = :filmId LIMIT 1")
    fun getReviewByUserAndFilm(userId: Int, filmId: Int): Review?
    @Query("UPDATE Review SET rating = :rating, text = :text WHERE idUser = :userId AND idFilm = :filmId")
    fun updateReview(userId: Int, filmId: Int, rating: Int?, text: String?)
    @Query("SELECT COUNT(*) FROM review WHERE idUser = :userId AND rating IS NOT NULL")
    fun getRatedMoviesCountByUserId(userId: Int): Int
    @Query("SELECT COUNT(*) FROM review WHERE idUser = :userId AND text IS NOT NULL AND text != ''")
    fun getReviewedMoviesCountByUserId(userId: Int): Int
    @Query("SELECT * FROM review WHERE idUser = :userId")
    fun getAllReviewsByUserId(userId: Int): List<Review>
    @Query("SELECT AVG(rating) FROM review WHERE idFilm = :filmId AND rating IS NOT NULL")
    fun getAverageRatingForFilm(filmId: Int): Double?





}
@Dao
interface FilmDao {
    @Insert
    fun insertFilm(film: Film)
    @Query("SELECT * FROM film WHERE nazov LIKE :query || '%' ORDER BY nazov")
    fun searchFilmsByTitle(query: String): List<Film>
    @Query("SELECT * FROM film ORDER BY nazov")
    fun getAllFilms(): List<Film>
    @Query("SELECT * FROM film WHERE idFilm = :filmId LIMIT 1")
    fun getFilmById(filmId: Int): Film
    @Query("SELECT * FROM film WHERE idFilm IN (SELECT idFilm FROM review WHERE idUser = :userId)")
    fun getAllFilmsByUserId(userId: Int): List<Film>



}
