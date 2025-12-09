package com.vashuag.grocery.ui.presentation

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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                }) {
                    Icon(Icons.Filled.Add, "Add")
                }
            }
        }, topBar = {
            CenterAlignedTopAppBar(
                title = { TopBarTitle(route) },
                actions = { TopBarActions(route, navController) },
                navigationIcon = { TopBarNavigationIcon(route, navController) })
        }, snackbarHost = { SnackbarHost(hostState = snackBarHostState) }) { innerPadding ->
            PermissionsView {
                NavHost(
                    navController,
                    startDestination = AppRoutes.HomeScreen,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    composable<AppRoutes.HomeScreen> {
                        LaunchedEffect(Unit) {
                            route.value = AppRoutes.HomeScreen
                        }
                        HomeScreen()
                    }
                    composable<AppRoutes.ScanItemsScreen> {
                        LaunchedEffect(Unit) {
                            route.value = AppRoutes.ScanItemsScreen
                        }
                        ScanningScreen()
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

        else -> {

        }
    }
}