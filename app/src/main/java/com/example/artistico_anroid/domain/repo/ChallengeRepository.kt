package com.example.artistico_anroid.domain.repo

import com.example.artistico_anroid.domain.model.Challenge
import com.example.artistico_anroid.domain.model.PreviousWeek
import kotlinx.coroutines.flow.Flow

interface ChallengeRepository {
    fun observeActiveChallenge(): Flow<Challenge?>
    fun observePreviousWeeks(): Flow<List<PreviousWeek>>
}
