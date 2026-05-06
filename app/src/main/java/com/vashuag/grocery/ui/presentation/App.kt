package com.vashuag.grocery.ui.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.vashuag.grocery.feature.compare.ui.CompareScreen
import com.vashuag.grocery.ui.presentation.camera.ScanningScreen
import com.vashuag.grocery.ui.presentation.home.HomeScreen
import com.vashuag.grocery.ui.theme.GroceryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val navController = rememberNavController()
    val route = remember { mutableStateOf<AppRoutes?>(null) }
    GroceryTheme {
        val snackBarHostState = remember {
            SnackbarHostState()
        }
        Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
            if (route.value == AppRoutes.HomeScreen) {
                FloatingActionButton(onClick = {
                    navController.navigate(AppRoutes.ScanItemsScreen)
                }, modifier = Modifier.testTag("home_add_item_fab")
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Add Item"
                    )
                }
            }
        }, topBar = {
            CenterAlignedTopAppBar(
                title = { TopBarTitle(route) },
                actions = { TopBarActions(route, navController) },
                navigationIcon = { TopBarNavigationIcon(route, navController) })
        }, snackbarHost = { SnackbarHost(hostState = snackBarHostState) }) { innerPadding ->
            Column(Modifier
                .fillMaxSize()
                .semantics { testTagsAsResourceId = true }
                .padding(innerPadding)) {
                PermissionsView {
                    NavHost(
                        navController,
                        startDestination = AppRoutes.HomeScreen,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        composable<AppRoutes.HomeScreen> {
                            LaunchedEffect(Unit) {
                                route.value = AppRoutes.HomeScreen
                            }
                            HomeScreen(
                                onCompareClick = { item ->
                                    navController.navigate(
                                        AppRoutes.CompareItemScreen(item.title)
                                    )
                                }
                            )
                        }
                        composable<AppRoutes.ScanItemsScreen> {
                            LaunchedEffect(Unit) {
                                route.value = AppRoutes.ScanItemsScreen
                            }
                            ScanningScreen()
                        }
                        composable<AppRoutes.CompareItemScreen> { backStackEntry ->
                            val compareRoute = backStackEntry.toRoute<AppRoutes.CompareItemScreen>()
                            LaunchedEffect(Unit) {
                                route.value = compareRoute
                            }
                            CompareScreen(initialQuery = compareRoute.itemName)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun TopBarTitle(route: MutableState<AppRoutes?>) {
    Text(
        text = when (route.value) {
            is AppRoutes.CompareItemScreen -> "Compare Prices"

            else -> {
                "Groco"
            }
        }
    )
}

@Composable
fun TopBarActions(
    route: MutableState<AppRoutes?>, navController: NavHostController
) {
    when (route.value) {

        else -> {

        }
    }

}

@Composable
fun TopBarNavigationIcon(
    route: MutableState<AppRoutes?>, navController: NavHostController
) {
    when (route.value) {
        is AppRoutes.ScanItemsScreen -> {
            IconButton({
                navController.popBackStack()
            }) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, "Back")
            }
        }
        is AppRoutes.CompareItemScreen -> {
            IconButton({
                navController.popBackStack()
            }) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, "Back")
            }
        }

        else -> {

        }
    }
}
