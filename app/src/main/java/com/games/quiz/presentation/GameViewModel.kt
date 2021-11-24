package com.games.quiz.presentation

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.games.quiz.data.GameRepositoryImpl
import com.games.quiz.domain.entity.GameSettings
import com.games.quiz.domain.entity.Level
import com.games.quiz.domain.entity.Question
import com.games.quiz.domain.usecases.GenerateQuestionUseCase
import com.games.quiz.domain.usecases.GetGameSettingsUsesCase

class GameViewModel : ViewModel() {

    private lateinit var gameSettings: GameSettings
    private lateinit var level: Level

    private val repository = GameRepositoryImpl

    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUsesCase = GetGameSettingsUsesCase(repository)


    private var timer: CountDownTimer? = null


    private val _formattedTime = MutableLiveData<String>()
    val formatedTime: LiveData<String>
        get() = _formattedTime

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question


    private val _percentOfRightAnswers = MutableLiveData<Int>()
    val percentOfRightAnswers: LiveData<Int>
        get() = _percentOfRightAnswers


    private var countOfRightAnswers = 0
    private var countOfQuestion = 0

    fun startGame(level: Level) {
        getGameSettings(level)
        startTimer()
        generateQuestion()
    }

    fun chooseAnswer(number: Int) {
        checkAnswer(number)
        generateQuestion()
    }

    private fun checkAnswer(number: Int) {
        val rightAnswer = question.value?.rightAnswer

        if (number == rightAnswer) {
            countOfRightAnswers++
        }
        countOfQuestion++
    }

    private fun getGameSettings(level: Level) {
        this.level = level
        this.gameSettings = getGameSettingsUsesCase(level)
    }

    private fun startTimer() {

        timer = object : CountDownTimer(
            gameSettings.gameTimeInSeconds * MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        ) {
            override fun onTick(millisUntilFinished: Long) {

                _formattedTime.value = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                finishGame()
            }
        }

        timer?.start()
    }

    private fun generateQuestion() {

        _question.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }

    // converting millisUntilFinished to human readable format
    private fun formatTime(millisUntilFinished: Long): String {
        val seconds = millisUntilFinished / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTES)
        return String.format("%02d:02d", minutes, leftSeconds)
    }


    private fun finishGame() {

    }


    override fun onCleared() {
        super.onCleared()

        timer?.cancel()

    }

    companion object {
        private const val MILLIS_IN_SECONDS = 100L
        private const val SECONDS_IN_MINUTES = 60
    }

}