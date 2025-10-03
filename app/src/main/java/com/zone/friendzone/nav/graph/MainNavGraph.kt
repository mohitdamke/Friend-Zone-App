package com.zone.friendzone.nav.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.zone.friendzone.nav.routes.Graph
import com.zone.friendzone.nav.routes.MainRouteScreen
import com.zone.friendzone.presentation.screens.chat.ChatScreen
import com.zone.friendzone.presentation.screens.home.HomeScreen
import com.zone.friendzone.presentation.screens.post.AddPostScreen
import com.zone.friendzone.presentation.screens.profile.ProfileScreen
import com.zone.friendzone.presentation.screens.search.SearchScreen

@Composable
fun MainNavGraph(
    rootNavController: NavHostController,
    homeNavController: NavHostController
) {

    NavHost(
        navController = homeNavController,
        route = Graph.MainScreenGraph,
        startDestination = MainRouteScreen.Home.route,
    ) {
        composable(route = MainRouteScreen.Home.route) {
            HomeScreen(rootNavController = rootNavController, homeNavController = homeNavController)
        }

        composable(route = MainRouteScreen.Search.route) {
            SearchScreen(navController = rootNavController)
        }

        composable(route = MainRouteScreen.AddPost.route) {
            AddPostScreen(navController = homeNavController)
        }

        composable(route = MainRouteScreen.Chat.route) {
            ChatScreen(navController = rootNavController)
        }

        composable(route = MainRouteScreen.Profile.route) {
            ProfileScreen(navController = rootNavController)
        }
    }
}