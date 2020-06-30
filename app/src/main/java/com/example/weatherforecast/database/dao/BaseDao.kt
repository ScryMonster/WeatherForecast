package com.example.weatherforecast.database.dao

import androidx.room.*
import io.reactivex.Completable

@Dao
interface BaseDao<T> {



    @Insert()
    fun insert(item: T) : Completable

    @Insert()
    fun insertList(items: List<T>): Completable

    @Delete
    fun delete(item: T): Completable

    @Delete
    fun deleteList(items: List<T>): Completable

    @Update
    fun update(obj: T?): Completable
}