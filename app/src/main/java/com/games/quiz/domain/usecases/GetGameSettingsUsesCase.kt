package com.games.quiz.domain.usecases

import com.games.quiz.domain.entity.GameSettings
import com.games.quiz.domain.entity.Level
import com.games.quiz.domain.entity.Question
import com.games.quiz.domain.repository.GameRepository

class GetGameSettingsUsesCase(
    private val repository: GameRepository
) {

    operator fun invoke(level: Level): GameSettings{
        return repository.getGameSettings(level)
    }
}