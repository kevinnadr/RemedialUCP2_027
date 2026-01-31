package com.example.remedialucp2_027

import android.content.Context
import com.example.remedialucp2_027.data.PerpustakaanDatabase
import com.example.remedialucp2_027.repository.OfflineRepositoriPerpustakaan
import com.example.remedialucp2_027.repository.RepositoriPerpustakaan


interface AppContainer {
    val repositoriPerpustakaan: RepositoriPerpustakaan
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    override val repositoriPerpustakaan: RepositoriPerpustakaan by lazy {
        OfflineRepositoriPerpustakaan(PerpustakaanDatabase.getDatabase(context).perpustakaanDao())
    }
}