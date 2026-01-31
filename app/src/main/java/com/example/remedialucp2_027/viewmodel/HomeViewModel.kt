package com.example.remedialucp2_027.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remedialucp2_027.model.Buku
import com.example.remedialucp2_027.model.Kategori
import com.example.remedialucp2_027.repository.RepositoriPerpustakaan
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(private val repositori: RepositoriPerpustakaan) : ViewModel() {

    val listKategori: StateFlow<List<Kategori>> = repositori.getAllKategori()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    private val _filterKategoriId = MutableStateFlow<Int?>(null)

    val listBuku: StateFlow<List<Buku>> = _filterKategoriId
        .flatMapLatest { id ->
            if (id == null) repositori.getAllBuku()
            else repositori.getBukuRecursive(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun filterBuku(kategoriId: Int?) {
        _filterKategoriId.value = kategoriId
    }


    fun deleteKategori(id: Int, deleteBooks: Boolean) {
        viewModelScope.launch {
            try {
                repositori.deleteKategori(id, deleteBooks)
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
        }
    }
}