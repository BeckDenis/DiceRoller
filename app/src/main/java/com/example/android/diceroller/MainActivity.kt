package com.example.android.diceroller

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.one_roll_dice.*
import kotlinx.android.synthetic.main.three_roll_dice.*
import kotlinx.android.synthetic.main.two_roll_dice.*
import kotlinx.coroutines.*
import java.util.*

private const val MAX_RANDOM_NUMBER = 5

class MainActivity : AppCompatActivity() {
    private var modeFactor: Int = 1

    private var rollOne: Int = 0
    private var rollTwo: Int = 0
    private var rollThree: Int = 0
    private var rollFour: Int = 0
    private var rollFive: Int = 0
    private var rollSix: Int = 0

    private var currentMode: Mode = Mode.ONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        roll_button.setOnClickListener {
            GlobalScope.launch(context = Dispatchers.Main) {
                state(false)
                startRoll()
                state(true)
            }
        }
        count_up_button.setOnClickListener { countUp() }
        reset_button.setOnClickListener { reset() }
        mode_button.setOnClickListener { changeMode() }
    }

    private suspend fun startRoll() {
        (0..9).forEach { _ ->
            when (currentMode) {
                Mode.ONE -> startRollModeOne()
                Mode.TWO -> startRollModeTwo()
                Mode.THREE -> startRollModeThree()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun countUp() {
        val rollNumber = result_text.text.toString().toInt()
        if (rollNumber < MAX_RANDOM_NUMBER * modeFactor) {
            result_text.text = "${rollNumber + 1}"
            when (currentMode) {
                Mode.ONE -> countUpModeOne()
                Mode.TWO -> countUpModeTwo()
                Mode.THREE -> countUpModeThree()
            }

        } else {
            count_up_button.isEnabled = false

            Toast.makeText(
                baseContext,
                "It's already ${MAX_RANDOM_NUMBER * modeFactor}.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private suspend fun startRollModeOne() {
        rollOne = rollDice()
        result_text.text = (rollOne).toString()
        dice_image1.setImageResource(rollDiceImage(rollOne))
        delay(50)
    }

    private suspend fun startRollModeTwo() {
        rollTwo = rollDice()
        dice_image2.setImageResource(rollDiceImage(rollTwo))

        rollThree = rollDice()
        dice_image3.setImageResource(rollDiceImage(rollThree))

        result_text.text = (rollTwo + rollThree).toString()
        delay(50)
    }

    private suspend fun startRollModeThree() {
        rollFour = rollDice()
        dice_image4.setImageResource(rollDiceImage(rollFour))

        rollFive = rollDice()
        dice_image5.setImageResource(rollDiceImage(rollFive))

        rollSix = rollDice()
        dice_image6.setImageResource(rollDiceImage(rollSix))

        result_text.text = (rollFour + rollFive + rollSix).toString()
        delay(50)
    }

    private fun rollDice(): Int {
        return Random().nextInt(MAX_RANDOM_NUMBER) + 1
    }

    private fun countUpModeOne() {
        rollOne++
        dice_image1.setImageResource(rollDiceImage(rollOne))
    }

    private fun countUpModeTwo() {
        when {
            rollTwo < MAX_RANDOM_NUMBER -> {
                rollTwo++
                dice_image2.setImageResource(rollDiceImage(rollTwo))
            }
            rollThree < MAX_RANDOM_NUMBER -> {
                rollThree++
                dice_image3.setImageResource(rollDiceImage(rollThree))
            }
        }
    }

    private fun countUpModeThree() {
        when {
            rollFour < MAX_RANDOM_NUMBER -> {
                rollFour++
                dice_image4.setImageResource(rollDiceImage(rollFour))
            }
            rollFive < MAX_RANDOM_NUMBER -> {
                rollFive++
                dice_image5.setImageResource(rollDiceImage(rollFive))
            }
            rollSix < MAX_RANDOM_NUMBER -> {
                rollSix++
                dice_image6.setImageResource(rollDiceImage(rollSix))
            }
        }
    }

    private fun reset() {
        result_text.setText(R.string.base_text)
        dice_image1.setImageResource(R.drawable.empty_dice)
        dice_image2.setImageResource(R.drawable.empty_dice)
        dice_image3.setImageResource(R.drawable.empty_dice)
        dice_image4.setImageResource(R.drawable.empty_dice)
        dice_image5.setImageResource(R.drawable.empty_dice)
        dice_image6.setImageResource(R.drawable.empty_dice)

        startState()
    }

    private fun changeMode() {
        reset()

        when (currentMode) {
            Mode.ONE -> {
                currentMode = Mode.TWO
                modeFactor = 2
                dice_image1.visibility = View.GONE
                two_roll_dice.visibility = View.VISIBLE
                three_roll_dice.visibility = View.GONE
            }

            Mode.TWO -> {
                currentMode = Mode.THREE
                modeFactor = 3
                dice_image1.visibility = View.GONE
                two_roll_dice.visibility = View.GONE
                three_roll_dice.visibility = View.VISIBLE
            }

            Mode.THREE -> {
                currentMode = Mode.ONE
                modeFactor = 1
                dice_image1.visibility = View.VISIBLE
                two_roll_dice.visibility = View.GONE
                three_roll_dice.visibility = View.GONE
            }
        }
    }

    private fun startState() {
        count_up_button.isEnabled = false
        reset_button.isEnabled = false
    }

    private fun state(value: Boolean) {
        roll_button.isEnabled = value
        count_up_button.isEnabled = value
        reset_button.isEnabled = value
        mode_button.isEnabled = value
    }

    private fun rollDiceImage(number: Int) = when (number) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }
}
