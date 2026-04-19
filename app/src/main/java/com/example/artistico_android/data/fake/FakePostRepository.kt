package com.example.artistico_android.data.fake

import com.example.artistico_android.R
import com.example.artistico_android.domain.model.Comment
import com.example.artistico_android.domain.model.ExploreSection
import com.example.artistico_android.domain.model.Post
import com.example.artistico_android.domain.repo.PostRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakePostRepository @Inject constructor() : PostRepository {

    private val now: Instant = Instant.now()

    private val followingsPosts = listOf(
        Post(
            id = "p_following_1",
            author = SampleUsers.artistica,
            imageRes = R.drawable.placeholder_image_1,
            description = "Line drawing study of a quiet village.",
            hashtags = listOf("LineArt", "Sketch"),
            likeCount = 128,
            commentCount = 4,
            isLiked = false,
            createdAt = now.minus(5 * 30, ChronoUnit.DAYS)
        ),
        Post(
            id = "p_following_2",
            author = SampleUsers.meera,
            imageRes = R.drawable.placeholder_image_4,
            description = "Mountains in the monsoon.",
            hashtags = listOf("Photography", "Mountains"),
            likeCount = 54,
            commentCount = 2,
            isLiked = true,
            createdAt = now.minus(12, ChronoUnit.DAYS)
        ),
        Post(
            id = "p_following_3",
            author = SampleUsers.rohan,
            imageRes = R.drawable.placeholder_image_3,
            description = "Moody cloudscape.",
            hashtags = listOf("Clouds", "Landscape"),
            likeCount = 34,
            commentCount = 1,
            isLiked = false,
            createdAt = now.minus(3, ChronoUnit.DAYS)
        )
    )

    private val stillLifePosts = listOf(
        Post(
            id = "p_still_1",
            author = SampleUsers.pragya,
            imageRes = R.drawable.placeholder_image_2,
            description = "Afternoon light through the leaves.",
            hashtags = listOf("Photography", "StillLife"),
            likeCount = 87,
            commentCount = 0,
            isLiked = false,
            createdAt = now.minus(5 * 30, ChronoUnit.DAYS)
        ),
        Post(
            id = "p_still_2",
            author = SampleUsers.rohan,
            imageRes = R.drawable.placeholder_image_3,
            description = "Storm coming in.",
            hashtags = listOf("Photography", "Sky"),
            likeCount = 41,
            commentCount = 5,
            isLiked = true,
            createdAt = now.minus(6, ChronoUnit.DAYS)
        )
    )

    private val trendingPosts = listOf(
        Post(
            id = "p_trend_1",
            author = SampleUsers.pragya,
            imageRes = R.drawable.placeholder_image_5,
            description = "Streets of the old town.",
            hashtags = listOf("Photography", "Street"),
            likeCount = 210,
            commentCount = 12,
            isLiked = false,
            createdAt = now.minus(5 * 30, ChronoUnit.DAYS)
        ),
        Post(
            id = "p_trend_2",
            author = SampleUsers.meera,
            imageRes = R.drawable.placeholder_image_4,
            description = "From the window seat.",
            hashtags = listOf("Travel", "Photography"),
            likeCount = 92,
            commentCount = 3,
            isLiked = true,
            createdAt = now.minus(30, ChronoUnit.DAYS)
        )
    )

    private val sectionsFlow = MutableStateFlow(
        listOf(
            ExploreSection("s_my_followings", "My Followings", followingsPosts),
            ExploreSection("s_still_life", "Still Life", stillLifePosts),
            ExploreSection("s_trending", "Trending", trendingPosts)
        )
    )

    private val commentsFlow = MutableStateFlow<Map<String, List<Comment>>>(
        mapOf(
            "p_following_1" to listOf(
                Comment("c1", "p_following_1", SampleUsers.meera, "Love the minimal line work!", now.minus(4, ChronoUnit.DAYS)),
                Comment("c2", "p_following_1", SampleUsers.rohan, "This is beautiful.", now.minus(2, ChronoUnit.DAYS))
            )
        )
    )

    override fun observeExploreSections(): Flow<List<ExploreSection>> = sectionsFlow.asStateFlow()

    override suspend fun refresh() {
        delay(400) // simulate latency
    }

    override fun observePost(postId: String): Flow<Post?> =
        sectionsFlow.map { sections ->
            sections.asSequence().flatMap { it.posts.asSequence() }.firstOrNull { it.id == postId }
        }

    override fun observeComments(postId: String): Flow<List<Comment>> =
        commentsFlow.map { it[postId].orEmpty() }

    override suspend fun toggleLike(postId: String) {
        sectionsFlow.value = sectionsFlow.value.map { section ->
            section.copy(posts = section.posts.map { post ->
                if (post.id == postId) {
                    val willLike = !post.isLiked
                    post.copy(
                        isLiked = willLike,
                        likeCount = (post.likeCount + if (willLike) 1 else -1).coerceAtLeast(0)
                    )
                } else post
            })
        }
    }

    override suspend fun addComment(postId: String, text: String) {
        if (text.isBlank()) return
        val newComment = Comment(
            id = UUID.randomUUID().toString(),
            postId = postId,
            author = SampleUsers.prateek,
            text = text.trim(),
            createdAt = Instant.now()
        )
        commentsFlow.value = commentsFlow.value.toMutableMap().apply {
            this[postId] = (this[postId].orEmpty()) + newComment
        }
        sectionsFlow.value = sectionsFlow.value.map { section ->
            section.copy(posts = section.posts.map { post ->
                if (post.id == postId) post.copy(commentCount = post.commentCount + 1) else post
            })
        }
    }
}
