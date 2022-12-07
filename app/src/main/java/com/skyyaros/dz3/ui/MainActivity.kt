package com.skyyaros.dz3.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.*
import androidx.paging.compose.collectAsLazyPagingItems
import com.skyyaros.dz3.R
import com.skyyaros.dz3.data.RickPagingSource
import com.skyyaros.dz3.ui.theme.Dz3Theme

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var  lazyListState: LazyListState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            this.window.statusBarColor = ContextCompat.getColor(this, R.color.black)
            this.window.navigationBarColor = ContextCompat.getColor(this, R.color.black)
            Dz3Theme {
                val navController = rememberNavController()
                val currentBackStack by navController.currentBackStackEntryAsState()
                val currentDestination = currentBackStack?.destination
                val currentScreen = if (currentDestination == null)
                    ListCharacters
                else if (currentDestination.route!!.contains('/'))
                    CharacterDetail
                else
                    rickTabRowScreens.find { it.route == currentDestination.route } ?: ListCharacters
                Scaffold(
                    topBar = {
                        RickTabRow(currentScreen) { navController.navigateToBack() }
                    }
                ) { innerPadding ->
                    RallyNavHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    @Composable
    fun RallyNavHost(navController: NavHostController, modifier: Modifier) {
        val items = mainViewModel.pageData.collectAsLazyPagingItems()
        lazyListState = rememberLazyListState()
        NavHost(
            navController = navController,
            startDestination = ListCharacters.route,
            modifier = modifier
        ) {
            composable(route = ListCharacters.route) {
                ListView(mainViewModel, items, lazyListState) { characterId ->
                    navController.navigateToCharacter(characterId)
                }
            }
            composable(
                route = CharacterDetail.routeWithArgs,
                arguments = CharacterDetail.arguments
            ) { navBackStackEntry ->
                val characterId = navBackStackEntry.arguments?.getInt(CharacterDetail.rickArg)
                DetailItem(mainViewModel, characterId!!)
            }
        }
    }

    private fun NavHostController.navigateSingleTopTo(route: String) {
        this.navigate(route) { launchSingleTop = true }
    }

    private fun NavHostController.navigateToCharacter(characterId: Int) {
        mainViewModel.lastIndex = lazyListState.firstVisibleItemIndex
        mainViewModel.lastOffset = lazyListState.firstVisibleItemScrollOffset
        this.navigateSingleTopTo("${CharacterDetail.route}/$characterId")
    }

    private fun NavHostController.navigateToBack() {
        this.navigate("list_characters") {
            popUpTo("list_characters") {inclusive = true}
        }
    }

    override fun onStop() {
        super.onStop()
        mainViewModel.lastIndex = lazyListState.firstVisibleItemIndex
        mainViewModel.lastOffset = lazyListState.firstVisibleItemScrollOffset
    }
}