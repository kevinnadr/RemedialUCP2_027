package com.example.remedialucp2_027.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.remedialucp2_027.MainApp

object PenyediaViewModel {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(aplikasi().container.repositoriPerpustakaan)
        }

        initializer {
            EntryViewModel(aplikasi().container.repositoriPerpustakaan)
        }

        initializer {
            UpdateViewModel(
                this.createSavedStateHandle(),
                aplikasi().container.repositoriPerpustakaan
            )
        }
    }
}

fun CreationExtras.aplikasi(): MainApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MainApp)