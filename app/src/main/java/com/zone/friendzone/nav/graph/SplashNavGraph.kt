package com.zone.friendzone.nav.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zone.friendzone.nav.routes.Graph
import com.zone.friendzone.nav.routes.SplashRouteScreen
import com.zone.friendzone.presentation.screens.splash.SplashScreen

fun NavGraphBuilder.splashNavGraph(rootNavController: NavHostController) {
    navigation(
        route = Graph.SplashGraph, startDestination = SplashRouteScreen.Splash.route
    ) {
        composable(
            route = SplashRouteScreen.Splash.route
        ) {
            SplashScreen(navController = rootNavController)
        }

    }
}