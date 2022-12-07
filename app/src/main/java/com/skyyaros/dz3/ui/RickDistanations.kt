package com.skyyaros.dz3.ui

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument

interface RickDestination {
    //val icon: ImageVector
    val route: String
}

object ListCharacters : RickDestination {
    //override val icon = Icons.Filled.PieChart
    override val route = "list_characters"
}

object CharacterDetail : RickDestination {
    //override val icon = Icons.Filled.AttachMoney
    override val route = "character_detail"
    const val rickArg = "rick_arg"
    val routeWithArgs = "$route/{$rickArg}"
    val arguments = listOf(
        navArgument(rickArg) { type = NavType.IntType }
    )
}

val rickTabRowScreens = listOf(ListCharacters, CharacterDetail)