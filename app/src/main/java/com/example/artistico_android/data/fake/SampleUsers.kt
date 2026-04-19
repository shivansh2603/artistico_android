package com.example.artistico_android.data.fake

import com.example.artistico_android.R
import com.example.artistico_android.domain.model.User
import com.example.artistico_android.domain.model.UserRole

internal object SampleUsers {
    val artistica = User(
        id = "u_artistica",
        displayName = "Artistica",
        role = UserRole.DEV,
        avatarRes = R.drawable.placeholder_avatar_artistica,
        isPremium = true
    )

    val pragya = User(
        id = "u_pragya",
        displayName = "Pragya Singh",
        role = UserRole.USER,
        avatarRes = R.drawable.placeholder_avatar
    )

    val prateek = User(
        id = "u_prateek",
        displayName = "Prateek Rai",
        role = UserRole.DEV,
        bio = "Head of R&D.",
        avatarRes = R.drawable.placeholder_avatar,
        followersCount = 3,
        followingCount = 6,
        postsCount = 3
    )
    val shivansh = User(
        id = "u_shivansh",
        displayName = "Shivansh Prasad",
        role = UserRole.DEV,
        bio = "Android Developer",
        avatarRes = R.drawable.placeholder_avatar,
        followersCount = 3,
        followingCount = 6,
        postsCount = 3
    )

    val meera = User(
        id = "u_meera",
        displayName = "Meera Kapoor",
        role = UserRole.USER,
        avatarRes = R.drawable.placeholder_avatar
    )

    val rohan = User(
        id = "u_rohan",
        displayName = "Rohan Das",
        role = UserRole.USER,
        avatarRes = R.drawable.placeholder_avatar
    )
}
