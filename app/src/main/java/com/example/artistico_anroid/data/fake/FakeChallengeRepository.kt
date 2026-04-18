package com.example.artistico_anroid.data.fake

import com.example.artistico_anroid.R
import com.example.artistico_anroid.domain.model.Challenge
import com.example.artistico_anroid.domain.model.PreviousWeek
import com.example.artistico_anroid.domain.repo.ChallengeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeChallengeRepository @Inject constructor() : ChallengeRepository {

    private val active = Challenge(
        id = "ch_active",
        title = "Art Journal",
        description = "An art journal is the same as a written journal, except that it incorporates " +
            "colors, images, patterns, and other materials. Some art journals have a lot of writing, " +
            "while others are purely filled with images. It's a form of creative self-care.",
        coverRes = R.drawable.logo_artistico,
        startsAt = Instant.now().minus(2, ChronoUnit.DAYS),
        endsAt = Instant.now().plus(4, ChronoUnit.DAYS).plus(5, ChronoUnit.HOURS)
    )

    private val previous = listOf(
        PreviousWeek("w10", 10, "Human Sketch", "22 - 28 Sep", R.drawable.placeholder_week_card),
        PreviousWeek("w9", 9, "Landscape", "15 - 21 Sep", R.drawable.placeholder_week_card),
        PreviousWeek("w8", 8, "Still Life", "8 - 14 Sep", R.drawable.placeholder_week_card),
        PreviousWeek("w7", 7, "Portraits", "1 - 7 Sep", R.drawable.placeholder_week_card)
    )

    override fun observeActiveChallenge(): Flow<Challenge?> = MutableStateFlow<Challenge?>(active).asStateFlow()
    override fun observePreviousWeeks(): Flow<List<PreviousWeek>> = MutableStateFlow(previous).asStateFlow()
}
