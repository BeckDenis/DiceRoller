package com.example.android.diceroller

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import java.util.*

private const val MAX_RANDOM_NUMBER = 6

class MainViewModel : ViewModel() {
    private var modeFactor: Int = 1

    var rollOne: Int = 0
    var rollTwo: Int = 0
    var rollThree: Int = 0

    var resultScore: Int = 0
    var currentMode: Mode = Mode.ONE

    suspend fun startRoll() {
        when (currentMode) {
            Mode.ONE -> startRollModeOne()
            Mode.TWO -> startRollModeTwo()
            Mode.THREE -> startRollModeThree()
        }
        resultScore = countScore()
        delay(50)
    }

    fun countUp() {
        when {
            rollOne < MAX_RANDOM_NUMBER -> rollOne++
            rollTwo < MAX_RANDOM_NUMBER -> rollTwo++
            rollThree < MAX_RANDOM_NUMBER -> rollThree++
        }
        resultScore = countScore()
    }

    fun checkMaxScore() = resultScore == MAX_RANDOM_NUMBER * modeFactor

    private fun startRollModeOne() {
        rollOne = rollDice()
    }

    private fun startRollModeTwo() {
        rollOne = rollDice()
        rollTwo = rollDice()
    }

    private fun startRollModeThree() {
        rollOne = rollDice()
        rollTwo = rollDice()
        rollThree = rollDice()
    }

    private fun countScore() = rollOne + rollTwo + rollThree

    private fun rollDice() = Random().nextInt(MAX_RANDOM_NUMBER) + 1

    fun changeMode(mode: Mode) {
        currentMode = mode
        changeModeFactor()
    }

    private fun changeModeFactor() {
        modeFactor = when (currentMode) {
            Mode.ONE -> 1
            Mode.TWO -> 2
            Mode.THREE -> 3
        }
    }

    fun rollDiceImage(number: Int) = when (number) {
        0 -> R.drawable.empty_dice
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }

    fun reset() {
        rollOne = 0
        rollTwo = 0
        rollThree = 0
        resultScore = 0
    }
}