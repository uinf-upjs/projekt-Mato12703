package com.example.upjsfd.repository

import com.example.upjsfd.dao.FilmDao
import com.example.upjsfd.dao.ReviewDao
import com.example.upjsfd.dao.UserDao

class AppRepository(
    private val userDao: UserDao,
    private val reviewDao: ReviewDao,
    private val filmDao: FilmDao
) {

}