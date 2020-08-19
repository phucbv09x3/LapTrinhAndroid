package com.monstar_lab_lifetime.laptrinhandroid.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Account::class, Messenger::class], version = 2, exportSchema = false)
abstract class AccountDatabase : RoomDatabase() {

    abstract fun accountDAO(): AccountDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: AccountDatabase? = null
        private var migration: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE inbox(id Integer not null ,image INTEGER NOT NULL, name TEXT NOT NULL,Primary Key(id) )")
            }
        }

        fun getDatabase(context: Context): AccountDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            //Toán tử ?: Có thể được sử dụng để trả về một giá trị nếu nó không phải là null,
            // nếu không thì trả về giá trị của toán tử. Nó thay thế tất cả các câu lệnh if trong Java.
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AccountDatabase::class.java,
                    "account_f_database"
                ).addMigrations(migration).run { allowMainThreadQueries() }.build()
                //allowMainThreadQueries : tránh exeption . vì nó k cho truy xuât database trên mani thread
                INSTANCE = instance
                return instance
            }
        }
    }
}