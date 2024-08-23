package com.example.upjsfd.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class User(
    @PrimaryKey(autoGenerate = true) val idUser: Int,
    val meno: String,
    val heslo: String
)

@Entity(
    tableName = "Film"
)
data class Film(
    @PrimaryKey(autoGenerate = true) val idFilm: Int,
    val nazov: String,
    val rok: Int,
    val popis: String,
    val fotoPath: String
)

@Entity(
    tableName = "review",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["idUser"],
            childColumns = ["idUser"],
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = Film::class,
            parentColumns = ["idFilm"],
            childColumns = ["idFilm"],
            onDelete = ForeignKey.NO_ACTION
        )
    ]
)
data class Review(
    @PrimaryKey(autoGenerate = true) val idReview: Int,
    val idUser: Int,
    val idFilm: Int,
    val rating: Int?,
    val text: String?
)
