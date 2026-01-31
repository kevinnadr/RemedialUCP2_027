package com.example.remedialucp2_027.model


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tblKategori")
data class Kategori(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nama: String,
    val parentId: Int? = null,
    val isDeleted: Boolean = false
)

@Entity(
    tableName = "tblBuku",
    foreignKeys = [
        ForeignKey(
            entity = Kategori::class,
            parentColumns = ["id"],
            childColumns = ["kategoriId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("kategoriId")]
)
data class Buku(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val judul: String,
    val kategoriId: Int?,
    val status: String,
    val isDeleted: Boolean = false
)

@Entity(tableName = "tblPengarang")
data class Pengarang(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nama: String
)

@Entity(
    tableName = "tblBukuPengarang",
    primaryKeys = ["bukuId", "pengarangId"],
    foreignKeys = [
        ForeignKey(entity = Buku::class, parentColumns = ["id"], childColumns = ["bukuId"]),
        ForeignKey(entity = Pengarang::class, parentColumns = ["id"], childColumns = ["pengarangId"])
    ],
    indices = [Index("bukuId"), Index("pengarangId")]
)
data class BukuPengarangCrossRef(
    val bukuId: Int,
    val pengarangId: Int
)

@Entity(tableName = "tblAuditLog")
data class AuditLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val action: String,
    val tableName: String,
    val recordId: Int,
    val oldData: String? = null,
    val newData: String? = null
)