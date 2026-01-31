package com.example.remedialucp2_027.navigasi


interface DestinasiNavigasi {
    val route: String
    val titleRes: String
}

object DestinasiHome : DestinasiNavigasi {
    override val route = "home"
    override val titleRes = "Home"
}

object DestinasiEntry : DestinasiNavigasi {
    override val route = "entry"
    override val titleRes = "Tambah Data"
}

object DestinasiDetail : DestinasiNavigasi {
    override val route = "detail_buku"
    override val titleRes = "Detail Buku"
    const val bukuIdArg = "bukuId"
    val routeWithArgs = "$route/{$bukuIdArg}"
}

object DestinasiUpdate : DestinasiNavigasi {
    override val route = "update_buku"
    override val titleRes = "Edit Buku"
    const val bukuIdArg = "bukuId"
    val routeWithArgs = "$route/{$bukuIdArg}"
}