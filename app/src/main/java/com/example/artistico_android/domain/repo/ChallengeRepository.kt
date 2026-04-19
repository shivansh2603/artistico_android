package com.example.artistico_android.domain.repo

import com.example.artistico_android.domain.model.Challenge
import com.example.artistico_android.domain.model.PreviousWeek
import kotlinx.coroutines.flow.Flow

interface ChallengeRepository {
    fun observeActiveChallenge(): Flow<Challenge?>
    fun observePreviousWeeks(): Flow<List<PreviousWeek>>
}
