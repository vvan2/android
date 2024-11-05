package com.example.study4

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var timeTextView: TextView
    private lateinit var startButton: Button
    private lateinit var pauseButton: Button
    private lateinit var clearButton: Button
    private lateinit var seekBar: SeekBar

    private var time = 0L
    private var isRunning = false
    private var job: Job? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timeTextView = findViewById(R.id.timeTextView)
        startButton = findViewById(R.id.startButton)
        pauseButton = findViewById(R.id.pauseButton)
        clearButton = findViewById(R.id.clearButton)
        seekBar = findViewById(R.id.seekBar)

        // Set SeekBar max value. For example, assume 1 hour (3600 seconds).
        seekBar.max = 180

        startButton.setOnClickListener {
            if (!isRunning) {
                startTimer()
            }
        }

        pauseButton.setOnClickListener {
            pauseTimer()
        }

        clearButton.setOnClickListener {
            clearTimer()
        }
    }

    private fun startTimer() {
        isRunning = true
        job = CoroutineScope(Dispatchers.Main).launch {
            while (isRunning) {
                delay(1000)
                time += 1
                timeTextView.text = formatTime(time)
                seekBar.progress = time.toInt() // Update SeekBar progress
            }
        }
    }

    private fun pauseTimer() {
        isRunning = false
        job?.cancel()
    }

    private fun clearTimer() {
        if (isRunning) {
            time = 0L
            timeTextView.text = formatTime(time)
            seekBar.progress = 0
        } else {
            pauseTimer()
            time = 0L
            timeTextView.text = formatTime(time)
            seekBar.progress = 0
        }
    }

    private fun formatTime(seconds: Long): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, secs)
    }
}
