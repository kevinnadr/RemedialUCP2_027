package com.example.remedialucp2_027.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.remedialucp2_027.model.AuditLog
import com.example.remedialucp2_027.model.Buku
import com.example.remedialucp2_027.model.BukuPengarangCrossRef
import com.example.remedialucp2_027.model.Kategori
import com.example.remedialucp2_027.model.Pengarang

@Database(
    entities = [Buku::class, Kategori::class, Pengarang::class, BukuPengarangCrossRef::class, AuditLog::class],
    version = 1,
    exportSchema = false
)
abstract class PerpustakaanDatabase : RoomDatabase() {
    abstract fun perpustakaanDao(): PerpustakaanDao

    companion object {
        @Volatile
        private var Instance: PerpustakaanDatabase? = null

        fun getDatabase(context: Context): PerpustakaanDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    PerpustakaanDatabase::class.java,
                    "perpustakaan_db"
                ).build().also { Instance = it }
            }
        }
    }
}