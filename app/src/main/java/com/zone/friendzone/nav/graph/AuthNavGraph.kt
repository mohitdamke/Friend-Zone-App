package com.zone.friendzone.nav.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zone.friendzone.nav.routes.AuthRouteScreen
import com.zone.friendzone.nav.routes.Graph
import com.zone.friendzone.presentation.screens.auth.LoginScreen
import com.zone.friendzone.presentation.screens.auth.RegisterScreen

fun NavGraphBuilder.authNavGraph(rootNavController: NavHostController) {
    navigation(
        route = Graph.AuthGraph, startDestination = AuthRouteScreen.Login.route
    ) {

        composable(
            route = AuthRouteScreen.Login.route
        ) {
            LoginScreen(navController = rootNavController)
        }

        composable(
            route = AuthRouteScreen.Register.route
        ) {
            RegisterScreen(navController = rootNavController)
        }
    }
}