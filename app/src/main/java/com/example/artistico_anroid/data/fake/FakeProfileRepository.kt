package com.example.artistico_anroid.data.fake

import com.example.artistico_anroid.R
import com.example.artistico_anroid.domain.model.Post
import com.example.artistico_anroid.domain.model.ProfileTab
import com.example.artistico_anroid.domain.model.User
import com.example.artistico_anroid.domain.repo.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeProfileRepository @Inject constructor() : ProfileRepository {

    private val currentUser = MutableStateFlow(SampleUsers.shivansh)

    private val ownPosts = listOf(
        Post(
            id = "pp_1",
            author = SampleUsers.shivansh,
            imageRes = R.drawable.placeholder_image_4,
            likeCount = 44,
            commentCount = 2,
            createdAt = Instant.now().minus(40, ChronoUnit.DAYS)
        ),
        Post(
            id = "pp_2",
            author = SampleUsers.shivansh,
            imageRes = R.drawable.placeholder_avatar_artistica,
            likeCount = 22,
            commentCount = 1,
            createdAt = Instant.now().minus(10, ChronoUnit.DAYS)
        ),
        Post(
            id = "pp_3",
            author = SampleUsers.shivansh,
            imageRes = R.drawable.placeholder_image_5,
            likeCount = 17,
            createdAt = Instant.now().minus(2, ChronoUnit.DAYS)
        )
    )

    private val bookmarks = listOf(
        Post(
            id = "pb_1",
            author = SampleUsers.meera,
            imageRes = R.drawable.placeholder_image_2,
            isLiked = true,
            createdAt = Instant.now().minus(5, ChronoUnit.DAYS)
        )
    )

    override fun observeCurrentUser(): Flow<User> = currentUser.asStateFlow()

    override fun observePosts(userId: String, tab: ProfileTab): Flow<List<Post>> =
        MutableStateFlow(
            when (tab) {
                ProfileTab.POSTS -> ownPosts
                ProfileTab.BOOKMARKS -> bookmarks
                ProfileTab.PINNED -> emptyList()
                ProfileTab.TROPHIES -> emptyList()
            }
        ).asStateFlow()
}
