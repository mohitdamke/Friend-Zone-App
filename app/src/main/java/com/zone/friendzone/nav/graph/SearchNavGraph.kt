package com.zone.friendzone.nav.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zone.friendzone.nav.routes.Graph
import com.zone.friendzone.nav.routes.SearchRouteScreen
import com.zone.friendzone.presentation.screens.user.OtherUsers

fun NavGraphBuilder.searchNavGraph(rootNavController: NavHostController) {
    navigation(
        route = Graph.SearchGraph, startDestination = SearchRouteScreen.OtherProfile.route
    ) {
        composable(
            route = SearchRouteScreen.OtherProfile.route
        ) {
            val data = it.arguments!!.getString("data")
            OtherUsers(navController = rootNavController, uid = data!!)
        }
    }
}