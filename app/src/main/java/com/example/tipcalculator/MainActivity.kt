package com.example.tipcalculator

import android.animation.ArgbEvaluator
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

private const val INITIAL_TIP = 15

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SeekBarTip.progress = INITIAL_TIP
        tipPercent.text = "$INITIAL_TIP%"
        updateTipDescrip(INITIAL_TIP)
        SeekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tipPercent.text = "$progress%"
                computeTipAndTotal()
                updateTipDescrip(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        } )

        BaseAmt.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                computeTipAndTotal()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })
    }

    @SuppressLint("SetTextI18n")
    private fun computeTipAndTotal() {
        if (BaseAmt.text.isEmpty()) {
            TipAmt.text = ""
            TotalAmt.text = ""
            return
        }
        var baseVal: Double = BaseAmt.text.toString().toDouble()
        var tipVal: Double = baseVal * SeekBarTip.progress / 100
        var totalVal: Double = baseVal + tipVal

        TipAmt.text = "%.2f".format(tipVal)
        TotalAmt.text = "%.2f".format(totalVal)
    }

    private fun updateTipDescrip(tipPercent : Int) {
        val tipDescription : String
        when (tipPercent) {
            in 0..9 -> tipDescription = "Poor"
            in 10..14 -> tipDescription = "Acceptable"
            in 15..19 -> tipDescription = "Good"
            in 20..24 -> tipDescription = "Great"
            else -> tipDescription = "Amazing"
        }

        TipColor.text = tipDescription
        val color = ArgbEvaluator().evaluate(( tipPercent.toFloat() / SeekBarTip.max), ContextCompat.getColor(this, R.color.WorstTipColor), ContextCompat.getColor(this, R.color.BestTipColor)) as Int
        TipColor.setTextColor(color)
    }
}