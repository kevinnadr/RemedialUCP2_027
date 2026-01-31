package com.example.remedialucp2_027.data


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.remedialucp2_027.model.AuditLog
import com.example.remedialucp2_027.model.Buku
import com.example.remedialucp2_027.model.BukuPengarangCrossRef
import com.example.remedialucp2_027.model.Kategori
import com.example.remedialucp2_027.model.Pengarang
import kotlinx.coroutines.flow.Flow

@Dao
interface PerpustakaanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKategori(kategori: Kategori)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBuku(buku: Buku)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuditLog(auditLog: AuditLog)

    @Query("SELECT * FROM tblKategori WHERE isDeleted = 0")
    fun getAllKategori(): Flow<List<Kategori>>

    @Query("SELECT * FROM tblBuku WHERE isDeleted = 0")
    fun getAllBuku(): Flow<List<Buku>>

    @Query("SELECT * FROM tblBuku WHERE kategoriId = :kategoriId AND isDeleted = 0")
    suspend fun getBukuByKategori(kategoriId: Int): List<Buku>

    @Query("UPDATE tblKategori SET isDeleted = 1 WHERE id = :id")
    suspend fun softDeleteKategori(id: Int)

    @Query("UPDATE tblBuku SET isDeleted = 1 WHERE kategoriId = :kategoriId")
    suspend fun softDeleteBukuByKategori(kategoriId: Int)

    @Query("UPDATE tblBuku SET kategoriId = NULL WHERE kategoriId = :kategoriId")
    suspend fun setBukuNoCategory(kategoriId: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPengarang(pengarang: Pengarang): Long

    @Query("SELECT * FROM tblPengarang WHERE nama = :nama LIMIT 1")
    suspend fun getPengarangByName(nama: String): Pengarang?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBukuPengarang(crossRef: BukuPengarangCrossRef)
    @Transaction
    suspend fun deleteKategoriWithValidation(kategoriId: Int, deleteBooks: Boolean) {
        val bukuList = getBukuByKategori(kategoriId)

        val isDipinjam = bukuList.any { it.status == "Dipinjam" }

        if (isDipinjam) {
            throw IllegalStateException("GAGAL: Ada buku yang sedang dipinjam dalam kategori ini. Rollback dilakukan.")
        }

        softDeleteKategori(kategoriId)
        insertAuditLog(
            AuditLog(
                action = "SOFT_DELETE",
                tableName = "tblKategori",
                recordId = kategoriId,
                oldData = "Active",
                newData = "Deleted"
            )
        )

        if (deleteBooks) {
            softDeleteBukuByKategori(kategoriId)
            bukuList.forEach { buku ->
                insertAuditLog(
                    AuditLog(
                        action = "SOFT_DELETE_CASCADE",
                        tableName = "tblBuku",
                        recordId = buku.id
                    )
                )
            }
        } else {
            setBukuNoCategory(kategoriId)
            bukuList.forEach { buku ->
                insertAuditLog(
                    AuditLog(
                        action = "UPDATE_NO_CATEGORY",
                        tableName = "tblBuku",
                        recordId = buku.id
                    )
                )
            }
        }
    }

    @Query("""
        WITH RECURSIVE CategoryHierarchy AS (
            SELECT id, parentId FROM tblKategori WHERE id = :rootId AND isDeleted = 0
            UNION ALL
            SELECT k.id, k.parentId FROM tblKategori k
            INNER JOIN CategoryHierarchy ch ON k.parentId = ch.id
            WHERE k.isDeleted = 0
        )
        SELECT b.* FROM tblBuku b
        INNER JOIN CategoryHierarchy ch ON b.kategoriId = ch.id
        WHERE b.isDeleted = 0
    """)
    fun getBukuRecursive(rootId: Int): Flow<List<Buku>>

    @Query("SELECT * FROM tblKategori WHERE id = :id")
    suspend fun getKategoriById(id: Int): Kategori?

    suspend fun validateCyclicReference(childId: Int, newParentId: Int): Boolean {
        var currentParentId: Int? = newParentId
        while (currentParentId != null) {
            if (currentParentId == childId) return true
            val parent = getKategoriById(currentParentId)
            currentParentId = parent?.parentId
        }
        return false
    }

    @Query("SELECT * FROM tblBuku WHERE id = :id")
    fun getBuku(id: Int): Flow<Buku>

    @Update
    suspend fun updateBuku(buku: Buku)
}