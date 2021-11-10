package com.games.quiz.domain.repository

import com.games.quiz.domain.entity.GameSettings
import com.games.quiz.domain.entity.Level
import com.games.quiz.domain.entity.Question

interface GameRepository {

    fun generateQuestion(
        maxsumValue: Int,
        countOfOptions: Int
    ): Question

    fun getGameSettings(level: Level): GameSettings
}