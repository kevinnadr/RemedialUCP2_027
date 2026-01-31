package com.example.remedialucp2_027.repository

import com.example.remedialucp2_027.data.PerpustakaanDao
import com.example.remedialucp2_027.model.Buku
import com.example.remedialucp2_027.model.BukuPengarangCrossRef
import com.example.remedialucp2_027.model.Kategori
import com.example.remedialucp2_027.model.Pengarang
import kotlinx.coroutines.flow.Flow

interface RepositoriPerpustakaan {
    fun getAllKategori(): Flow<List<Kategori>>
    fun getAllBuku(): Flow<List<Buku>>
    fun getBukuRecursive(kategoriId: Int): Flow<List<Buku>>
    fun getBuku(id: Int): Flow<Buku>

    suspend fun insertKategori(kategori: Kategori)
    suspend fun insertBuku(buku: Buku)
    suspend fun updateBuku(buku: Buku)

    suspend fun deleteKategori(kategoriId: Int, deleteBooks: Boolean)
    suspend fun getKategoriById(id: Int): Kategori?

    suspend fun insertBukuWithPengarang(buku: Buku, namaPengarang: String)
}

class OfflineRepositoriPerpustakaan(
    private val perpustakaanDao: PerpustakaanDao
) : RepositoriPerpustakaan {

    override fun getAllKategori(): Flow<List<Kategori>> =
        perpustakaanDao.getAllKategori()

    override fun getAllBuku(): Flow<List<Buku>> =
        perpustakaanDao.getAllBuku()

    override fun getBukuRecursive(kategoriId: Int): Flow<List<Buku>> =
        perpustakaanDao.getBukuRecursive(kategoriId)

    override fun getBuku(id: Int): Flow<Buku> =
        perpustakaanDao.getBuku(id)

    override suspend fun insertKategori(kategori: Kategori) {
        if (kategori.parentId != null) {
            val isCyclic = perpustakaanDao.validateCyclicReference(
                kategori.id,
                kategori.parentId
            )
            if (isCyclic) {
                throw IllegalArgumentException(
                    "GAGAL: Cyclic Reference terdeteksi! Kategori tidak bisa menjadi induk bagi leluhurnya sendiri."
                )
            }
        }
        perpustakaanDao.insertKategori(kategori)
    }

    override suspend fun insertBuku(buku: Buku) =
        perpustakaanDao.insertBuku(buku)

    override suspend fun updateBuku(buku: Buku) =
        perpustakaanDao.updateBuku(buku)

    override suspend fun deleteKategori(
        kategoriId: Int,
        deleteBooks: Boolean
    ) {
        perpustakaanDao.deleteKategoriWithValidation(
            kategoriId,
            deleteBooks
        )
    }

    override suspend fun getKategoriById(id: Int): Kategori? =
        perpustakaanDao.getKategoriById(id)

    override suspend fun insertBukuWithPengarang(
        buku: Buku,
        namaPengarang: String
    ) {
        perpustakaanDao.insertBuku(buku)

        val bukuList =
            perpustakaanDao.getBukuByKategori(buku.kategoriId ?: 0)
        val bukuBaru =
            bukuList.maxByOrNull { it.id } ?: return

        val daftarNama =
            namaPengarang.split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }

        daftarNama.forEach { nama ->
            var pengarang =
                perpustakaanDao.getPengarangByName(nama)

            if (pengarang == null) {
                perpustakaanDao.insertPengarang(
                    Pengarang(nama = nama)
                )
                pengarang =
                    perpustakaanDao.getPengarangByName(nama)
            }

            if (pengarang != null) {
                perpustakaanDao.insertBukuPengarang(
                    BukuPengarangCrossRef(
                        bukuId = bukuBaru.id,
                        pengarangId = pengarang.id
                    )
                )
            }
        }
    }
}
