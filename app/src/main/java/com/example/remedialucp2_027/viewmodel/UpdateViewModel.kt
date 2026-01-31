package com.example.remedialucp2_027.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remedialucp2_027.model.Buku
import com.example.remedialucp2_027.navigasi.DestinasiUpdate
import com.example.remedialucp2_027.repository.RepositoriPerpustakaan
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UpdateViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositori: RepositoriPerpustakaan
) : ViewModel() {

    var updateUiState by mutableStateOf(BukuUiState())
        private set

    private val _bukuId: Int = checkNotNull(savedStateHandle[DestinasiUpdate.bukuIdArg])

    init {
        viewModelScope.launch {
            updateUiState = repositori.getBuku(_bukuId)
                .filterNotNull()
                .first()
                .toUiStateBuku()
        }
    }

    fun updateState(bukuEvent: BukuEvent) {
        updateUiState = updateUiState.copy(bukuEvent = bukuEvent)
    }

    suspend fun updateBuku() {
        repositori.updateBuku(updateUiState.bukuEvent.toBuku().copy(id = _bukuId))
    }
}


fun Buku.toUiStateBuku(): BukuUiState = BukuUiState(
    bukuEvent = BukuEvent(
        judul = this.judul,
        status = this.status,
        kategoriId = this.kategoriId?.toString() ?: ""
    )
)