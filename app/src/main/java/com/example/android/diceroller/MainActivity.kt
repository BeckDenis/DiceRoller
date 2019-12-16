package com.example.android.diceroller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.one_roll_dice.*
import kotlinx.android.synthetic.main.three_roll_dice.*
import kotlinx.android.synthetic.main.two_roll_dice.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        roll_button.setOnClickListener { roll() }
        count_up_button.setOnClickListener { countUp() }
        reset_button.setOnClickListener { reset() }

        updateImage()
        updateScore()
        changeView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.overflow_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mode_one -> viewModel.changeMode(Mode.ONE)
            R.id.mode_two -> viewModel.changeMode(Mode.TWO)
            R.id.mode_three -> viewModel.changeMode(Mode.THREE)
        }
        changeView()
        reset()
        return true
    }

    private fun roll() {
        GlobalScope.launch(context = Dispatchers.Main) {
            state(false)
            repeat(10) {
                viewModel.startRoll()
                updateImage()
                updateScore()
            }
            state(true)
        }
    }

    private fun countUp() {
        if (!viewModel.checkMaxScore()) {
            viewModel.countUp()
            updateScore()
            updateImage()
        } else {
            Toast.makeText(baseContext, getString(R.string.toast_text), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateImage() {
        when (viewModel.currentMode) {
            Mode.ONE -> {
                dice_image1.setImageResource(viewModel.rollDiceImage(viewModel.rollOne))
            }
            Mode.TWO -> {
                dice_image2.setImageResource(viewModel.rollDiceImage(viewModel.rollOne))
                dice_image3.setImageResource(viewModel.rollDiceImage(viewModel.rollTwo))
            }
            Mode.THREE -> {
                dice_image4.setImageResource(viewModel.rollDiceImage(viewModel.rollOne))
                dice_image5.setImageResource(viewModel.rollDiceImage(viewModel.rollTwo))
                dice_image6.setImageResource(viewModel.rollDiceImage(viewModel.rollThree))
            }
        }
    }

    private fun updateScore() {
        result_text.text = viewModel.resultScore.toString()
    }

    private fun state(value: Boolean) {
        roll_button.isEnabled = value
        count_up_button.isEnabled = value
        reset_button.isEnabled = value
    }

    private fun reset() {
        viewModel.reset()
        updateScore()
        updateImage()
    }

    private fun changeView() {
        when (viewModel.currentMode) {
            Mode.ONE -> {
                one_roll_dice.visibility = View.VISIBLE
                two_roll_dice.visibility = View.GONE
                three_roll_dice.visibility = View.GONE
            }

            Mode.TWO -> {
                one_roll_dice.visibility = View.GONE
                two_roll_dice.visibility = View.VISIBLE
                three_roll_dice.visibility = View.GONE
            }

            Mode.THREE -> {
                one_roll_dice.visibility = View.GONE
                two_roll_dice.visibility = View.GONE
                three_roll_dice.visibility = View.VISIBLE
            }
        }
    }
}
