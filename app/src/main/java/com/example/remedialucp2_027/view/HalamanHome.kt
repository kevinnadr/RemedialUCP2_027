package com.example.remedialucp2_027.view


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.remedialucp2_027.model.Buku
import com.example.remedialucp2_027.model.Kategori
import com.example.remedialucp2_027.navigasi.DestinasiNavigasi
import com.example.remedialucp2_027.viewmodel.HomeViewModel
import com.example.remedialucp2_027.viewmodel.PenyediaViewModel

object DestinasiHome : DestinasiNavigasi {
    override val route = "home"
    override val titleRes = "Home"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanHome(
    navigateToItemEntry: () -> Unit,
    onDetailClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val listKategori by viewModel.listKategori.collectAsState()
    val listBuku by viewModel.listBuku.collectAsState()


    var showDialog by remember { mutableStateOf(false) }
    var selectedKategoriId by remember { mutableStateOf(0) }


    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Hapus Kategori?") },
            text = { Text("Pilih aksi untuk buku di dalam kategori ini:") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteKategori(selectedKategoriId, deleteBooks = true)
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) { Text("Hapus Beserta Buku") }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        viewModel.deleteKategori(selectedKategoriId, deleteBooks = false)
                        showDialog = false
                    }
                ) { Text("Simpan Buku (Tanpa Kategori)") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Perpustakaan UCP 2") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Data")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(text = "Daftar Kategori (Klik untuk Filter)", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Button(onClick = { viewModel.filterBuku(null) }, modifier = Modifier.fillMaxWidth()) {
                Text("Tampilkan Semua Buku")
            }

            LazyColumn(modifier = Modifier.fillMaxWidth().height(180.dp)) {
                items(listKategori) { kategori ->
                    CardKategori(
                        kategori = kategori,
                        onClick = { viewModel.filterBuku(kategori.id) },
                        onDelete = {
                            selectedKategoriId = kategori.id
                            showDialog = true
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Text(text = "Daftar Buku", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(top = 8.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(listBuku) { buku ->
                    CardBuku(buku = buku, onClick = { onDetailClick(buku.id) })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardKategori(
    kategori: Kategori,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().padding(4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = kategori.nama, fontWeight = FontWeight.Bold)
                if (kategori.parentId != null) Text("Sub-Kategori ID: ${kategori.parentId}", fontSize = 12.sp)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color.Red)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardBuku(buku: Buku, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().padding(4.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(buku.judul, fontWeight = FontWeight.Bold)
            Text("Status: ${buku.status}")
        }
    }
}
