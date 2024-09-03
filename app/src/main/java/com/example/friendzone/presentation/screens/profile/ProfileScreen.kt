package com.example.friendzone.presentation.screens.profile

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.friendzone.common.PostItem
import com.example.friendzone.data.model.UserModel
import com.example.friendzone.dimension.FontDim
import com.example.friendzone.dimension.TextDim
import com.example.friendzone.nav.routes.AuthRouteScreen
import com.example.friendzone.nav.routes.MainRouteScreen
import com.example.friendzone.nav.routes.ProfileRouteScreen
import com.example.friendzone.ui.theme.DarkBlack
import com.example.friendzone.util.SharedPref
import com.example.friendzone.viewmodel.auth.AuthViewModel
import com.example.friendzone.viewmodel.home.HomeViewModel
import com.example.friendzone.viewmodel.user.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    context: Context = LocalContext.current,
) {
    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)
    val homeViewModel: HomeViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()

    val posts by userViewModel.posts.observeAsState(emptyList())
    val followerList by userViewModel.followerList.observeAsState(emptyList())
    val followingList by userViewModel.followingList.observeAsState(emptyList())
    val savedThreadIds by homeViewModel.savedPostIds.observeAsState(emptyList())
    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

    LaunchedEffect(Unit) {
        userViewModel.fetchPosts(currentUserId)
        userViewModel.getFollowers(currentUserId)
        userViewModel.getFollowing(currentUserId)
        homeViewModel.fetchSavedPost(currentUserId)
    }


    val postsToDisplay = posts
        .filter { it.userId == currentUserId }
        .sortedByDescending { it.timeStamp.toLong() }

    val savedPostsToDisplay = postsToDisplay.filter { savedThreadIds.contains(it.userId) }
    val unsavedPostsToDisplay = postsToDisplay.filter { !savedThreadIds.contains(it.userId) }

    LaunchedEffect(currentUserId) {
        if (currentUserId.isEmpty()) {
            navController.navigate(AuthRouteScreen.Login.route) {
                popUpTo(MainRouteScreen.Profile.route) { inclusive = true }
            }
        }
    }

    val user = UserModel(
        email = SharedPref.getEmail(context),
        name = SharedPref.getName(context),
        userName = SharedPref.getUserName(context),
        bio = SharedPref.getBio(context),
        imageUrl = SharedPref.getImageUrl(context)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = DarkBlack,
                    titleContentColor = com.example.friendzone.ui.theme.White,
                    actionIconContentColor = com.example.friendzone.ui.theme.White,
                    navigationIconContentColor = com.example.friendzone.ui.theme.White,
                    scrolledContainerColor = DarkBlack,
                ),
                title = {
                    Text(
                        text = "PROFILE",
                        maxLines = 1,
                        letterSpacing = 1.sp, fontSize = TextDim.titleTextSize,
                        overflow = TextOverflow.Visible,
                        fontFamily = FontDim.Bold,
                    )
                }, actions = {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Saved Posts",
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { navController.navigate(ProfileRouteScreen.SavedPosts.route) },
                        tint = White
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 10.dp))
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { navController.navigate(ProfileRouteScreen.Settings.route) },
                        tint = White
                    )
                }

            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                ProfileHeader(
                    modifier = modifier,
                    user = user,
                    navController = navController,
                    postCount = postsToDisplay.size,
                    followerCount = followerList.size,
                    followingCount = followingList.size
                )
            }

            items(savedPostsToDisplay + unsavedPostsToDisplay) { post ->
                PostItem(
                    post = post,
                    users = user,
                    navController = navController,
                    homeViewModel = homeViewModel
                )
            }
        }
    }
}

@Composable
fun ProfileHeader(
    modifier: Modifier,
    user: UserModel,
    navController: NavController,
    postCount: Int,
    followerCount: Int,
    followingCount: Int
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBlack),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(top = 20.dp))
        ProfileImage(modifier, user.imageUrl, navController)
        Text(
            text = user.name, fontSize = TextDim.titleTextSize, fontFamily = FontDim.Bold,
            color = com.example.friendzone.ui.theme.White
        )
        Spacer(modifier = Modifier.padding(top = 4.dp))
        Text(
            text = "@${user.userName}",
            fontSize = TextDim.secondaryTextSize,
            fontFamily = FontDim.Normal,
            color = com.example.friendzone.ui.theme.White
        )
        ProfileActions(modifier, navController)
        ProfileStats(modifier, postCount, followerCount, followingCount)
    }
}

@Composable
fun ProfileImage(
    modifier: Modifier,
    imageUrl: String,
    navController: NavController
) {
    Box(modifier = modifier) {
        Image(
            painter = rememberAsyncImagePainter(model = imageUrl),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .padding(2.dp)
                .border(width = 4.dp, color = White, shape = CircleShape)
                .padding(2.dp)
                .border(width = 4.dp, color = Black, shape = CircleShape)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Icon(
            imageVector = Icons.Default.AddCircle,
            contentDescription = null,
            modifier = Modifier
                .clickable { navController.navigate(ProfileRouteScreen.AddStory.route) }
                .clip(CircleShape)
                .size(34.dp)
                .align(Alignment.BottomEnd),
            tint = Black
        )
    }
}

@Composable
fun ProfileActions(
    modifier: Modifier,
    navController: NavController
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = { navController.navigate(ProfileRouteScreen.EditProfile.route) },
            modifier = Modifier
                .clip(RoundedCornerShape(40.dp))
                .padding(10.dp),
            elevation = ButtonDefaults.buttonElevation(10.dp),
            border = ButtonDefaults.outlinedButtonBorder,
            colors = ButtonDefaults.buttonColors(containerColor = DarkBlack, contentColor = White)
        ) {
            Text(
                text = "Edit Profile",
                fontSize = TextDim.bodyTextSize,
                fontFamily = FontDim.Normal,
            )
        }
    }
}

@Composable
fun ProfileStats(
    modifier: Modifier,
    postCount: Int,
    followerCount: Int,
    followingCount: Int
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ProfileStatItem(modifier, postCount, "posts")
        ProfileStatItem(modifier, followerCount, "followers")
        ProfileStatItem(modifier, followingCount, "following")
    }
}

@Composable
fun ProfileStatItem(
    modifier: Modifier,
    count: Int,
    label: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = count.toString(), fontSize = TextDim.titleTextSize, fontFamily = FontDim.Bold,
            color = com.example.friendzone.ui.theme.White
        )
        Text(
            text = label, fontSize = TextDim.secondaryTextSize, fontFamily = FontDim.Normal,
            color = com.example.friendzone.ui.theme.White
        )
    }
}
