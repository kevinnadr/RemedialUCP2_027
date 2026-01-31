package com.example.remedialucp2_027.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.remedialucp2_027.navigasi.DestinasiNavigasi
import com.example.remedialucp2_027.viewmodel.BukuEvent
import com.example.remedialucp2_027.viewmodel.EntryViewModel
import com.example.remedialucp2_027.viewmodel.KategoriEvent
import com.example.remedialucp2_027.viewmodel.PenyediaViewModel
import kotlinx.coroutines.launch

object DestinasiEntry : DestinasiNavigasi {
    override val route = "item_entry"
    override val titleRes = "Entry Data"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanEntry(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EntryViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = DestinasiEntry.titleRes) },
                navigationIcon = {
                    Button(onClick = navigateBack) {
                        Text("Kembali")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            Text(text = "Form Tambah Kategori", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            FormKategori(
                kategoriEvent = viewModel.kategoriUiState.kategoriEvent,
                onValueChange = viewModel::updateKategoriState,
                onSaveClick = {
                    coroutineScope.launch {
                        viewModel.saveKategori()
                        navigateBack()
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
            Divider(thickness = 2.dp)
            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Form Tambah Buku", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            FormBuku(
                bukuEvent = viewModel.bukuUiState.bukuEvent,
                namaPengarang = viewModel.namaPengarang,
                onValueChange = viewModel::updateBukuState,
                onPengarangChange = { viewModel.namaPengarang = it },
                onSaveClick = {
                    coroutineScope.launch {
                        viewModel.saveBuku()
                        navigateBack()
                    }
                }
            )
        }
    }
}

@Composable
fun FormKategori(
    kategoriEvent: KategoriEvent,
    onValueChange: (KategoriEvent) -> Unit,
    onSaveClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = kategoriEvent.nama,
            onValueChange = { onValueChange(kategoriEvent.copy(nama = it)) },
            label = { Text("Nama Kategori") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = kategoriEvent.parentId,
            onValueChange = { onValueChange(kategoriEvent.copy(parentId = it)) },
            label = { Text("ID Parent Kategori (Opsional)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onSaveClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan Kategori")
        }
    }
}

@Composable
fun FormBuku(
    bukuEvent: BukuEvent,
    namaPengarang: String = "",
    onValueChange: (BukuEvent) -> Unit,
    onPengarangChange: (String) -> Unit = {},
    onSaveClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        OutlinedTextField(
            value = bukuEvent.judul,
            onValueChange = { onValueChange(bukuEvent.copy(judul = it)) },
            label = { Text("Judul Buku") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))


        Text(text = "Status Buku:", style = MaterialTheme.typography.labelLarge)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf("Tersedia", "Dipinjam").forEach { statusOption ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    RadioButton(
                        selected = (bukuEvent.status == statusOption),
                        onClick = { onValueChange(bukuEvent.copy(status = statusOption)) }
                    )
                    Text(text = statusOption)
                }
            }
        }


        OutlinedTextField(
            value = bukuEvent.kategoriId,
            onValueChange = { onValueChange(bukuEvent.copy(kategoriId = it)) },
            label = { Text("ID Kategori") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )


        OutlinedTextField(
            value = namaPengarang,
            onValueChange = onPengarangChange,
            label = { Text("Nama Pengarang (Wajib Diisi)") },
            placeholder = { Text("Contoh: Tere Liye, Andrea Hirata") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = onSaveClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan Buku")
        }
    }
}