package com.zone.friendzone.nav.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zone.friendzone.nav.routes.Graph
import com.zone.friendzone.nav.routes.ProfileRouteScreen
import com.zone.friendzone.presentation.screens.post.SavedScreen
import com.zone.friendzone.presentation.screens.profile.EditProfile
import com.zone.friendzone.presentation.screens.profile.Setting
import com.zone.friendzone.presentation.screens.story.AddStory

fun NavGraphBuilder.profileNavGraph(
    rootNavController: NavHostController,
    homeNavController: NavHostController
) {
    navigation(
        route = Graph.ProfileGraph, startDestination = ProfileRouteScreen.AddStory.route
    ) {
        composable(
            route = ProfileRouteScreen.AddStory.route
        ) {
            AddStory(navController = rootNavController)
        }
        composable(
            route = ProfileRouteScreen.EditProfile.route
        ) {
            EditProfile(navController = rootNavController)
        }
        composable(
            route = ProfileRouteScreen.Settings.route
        ) {
            Setting(navController = rootNavController)
        }
        composable(
            route = ProfileRouteScreen.SavedPosts.route
        ) {
            SavedScreen(navController = rootNavController, homeNavController = homeNavController)
        }
    }
}