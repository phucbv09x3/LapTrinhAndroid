package com.monstar_lab_lifetime.laptrinhandroid.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AccountDAO {
    @Insert
    suspend fun insertAccount(accountDAO: Account)

    @Query("select * from sign_up ")
    fun getAllAccount():List<Account>

    @Query("SELECT * FROM sign_up WHERE email=:email")
    fun findAccountByMail(email:String) : Account
    @Delete
    fun delete(accountDAO: Account)


    @Insert
    suspend fun insertInbox(messenger: Messenger)
    @Query("select *from inbox  ")
    fun getMes():List<Messenger>

    @Query("SELECT * FROM inbox WHERE name=:name")
    fun findInboxByName(name:String) : Messenger

}