package com.example.remedialucp2_027.navigasi

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.remedialucp2_027.view.HalamanEntry
import com.example.remedialucp2_027.view.HalamanHome
import com.example.remedialucp2_027.view.HalamanUpdate

@Composable
fun PengelolaHalaman(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = DestinasiHome.route,
        modifier = modifier
    ) {

        composable(
            route = DestinasiHome.route
        ) {
            HalamanHome(
                navigateToItemEntry = {
                    navController.navigate(DestinasiEntry.route)
                },

                onDetailClick = { bukuId ->
                    navController.navigate("${DestinasiUpdate.route}/$bukuId")
                },
                modifier = Modifier
            )
        }

        composable(
            route = DestinasiEntry.route
        ) {
            HalamanEntry(
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }


        composable(
            route = DestinasiUpdate.routeWithArgs,
            arguments = listOf(navArgument(DestinasiUpdate.bukuIdArg) {
                type = NavType.IntType
            })
        ) {
            HalamanUpdate(
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}