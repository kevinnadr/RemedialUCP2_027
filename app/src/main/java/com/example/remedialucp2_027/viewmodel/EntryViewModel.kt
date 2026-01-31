package com.example.remedialucp2_027.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.remedialucp2_027.model.Buku
import com.example.remedialucp2_027.model.Kategori
import com.example.remedialucp2_027.repository.RepositoriPerpustakaan

class EntryViewModel(
    private val repositori: RepositoriPerpustakaan
) : ViewModel() {

    var kategoriUiState by mutableStateOf(KategoriUiState())
        private set

    var bukuUiState by mutableStateOf(BukuUiState())
        private set

    var namaPengarang by mutableStateOf("")

    fun updateKategoriState(kategoriEvent: KategoriEvent) {
        kategoriUiState = KategoriUiState(kategoriEvent = kategoriEvent)
    }

    fun updateBukuState(bukuEvent: BukuEvent) {
        bukuUiState = BukuUiState(bukuEvent = bukuEvent)
    }

    suspend fun saveKategori() {
        if (validasiKategori()) {
            try {
                repositori.insertKategori(
                    kategoriUiState.kategoriEvent.toKategori()
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun saveBuku() {
        if (validasiBuku()) {
            try {
                repositori.insertBukuWithPengarang(
                    bukuUiState.bukuEvent.toBuku(),
                    namaPengarang
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun validasiKategori(): Boolean {
        return kategoriUiState.kategoriEvent.nama.isNotBlank()
    }

    private fun validasiBuku(): Boolean {
        return bukuUiState.bukuEvent.judul.isNotBlank() &&
                bukuUiState.bukuEvent.status.isNotBlank()
    }
}

data class KategoriUiState(
    val kategoriEvent: KategoriEvent = KategoriEvent()
)

data class KategoriEvent(
    val nama: String = "",
    val parentId: String = ""
) {
    fun toKategori(): Kategori = Kategori(
        nama = nama,
        parentId = if (parentId.isBlank()) null else parentId.toIntOrNull()
    )
}

data class BukuUiState(
    val bukuEvent: BukuEvent = BukuEvent()
)

data class BukuEvent(
    val judul: String = "",
    val status: String = "",
    val kategoriId: String = ""
) {
    fun toBuku(): Buku = Buku(
        judul = judul,
        status = status,
        kategoriId = if (kategoriId.isBlank()) null else kategoriId.toIntOrNull()
    )
}
