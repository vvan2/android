package com.example.study3

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private lateinit var seekBar: SeekBar
    private lateinit var timeTextView: TextView
    private lateinit var playPauseImage: ImageView
    private lateinit var likeunlikeImage: ImageView
    private lateinit var nextImage: ImageView
    private lateinit var prevImage: ImageView
    private lateinit var mainImage: ImageView
    private lateinit var reImage: ImageView
    private lateinit var textsong: TextView
    private lateinit var textsinger: TextView

    private var time = 0L
    private var isRunning = false
    private var job: Job? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        playPauseImage = findViewById(R.id.imageView12)
        timeTextView = findViewById(R.id.timeTextView)
        seekBar = findViewById(R.id.seekBar)
        likeunlikeImage = findViewById(R.id.imagelike)
        nextImage = findViewById(R.id.imageView10)
        prevImage = findViewById(R.id.imageView13)
        mainImage = findViewById(R.id.imageView9)
        reImage = findViewById(R.id.recycle)
        textsong = findViewById(R.id.textView14)
        textsinger = findViewById(R.id.textView15)

        loadStateFromSharedPreferences()
        seekBar.max = 180
        seekBar.progress = time.toInt()

        nextImage.setOnClickListener {
            mainImage.setImageResource(R.drawable.v4)
            textsong.text="Fujin"
            textsinger.text = "Vaundy"
            likeunlikeImage.setImageResource(R.drawable.ic_my_like_off)
            clearTimer()
        }

        reImage.setOnClickListener {
            clearTimer()
        }

        prevImage.setOnClickListener {
            mainImage.setImageResource(R.drawable.v3)
            textsong.text="Odoriko"
            textsinger.text = "Vaundy"
            likeunlikeImage.setImageResource(R.drawable.ic_my_like_off)
            clearTimer()
        }

        likeunlikeImage.setOnClickListener {
            val isLiked = likeunlikeImage.tag == R.drawable.ic_my_like_on
            likeunlikeImage.setImageResource(if (isLiked) R.drawable.ic_my_like_off else R.drawable.ic_my_like_on)
            likeunlikeImage.tag = if (isLiked) R.drawable.ic_my_like_off else R.drawable.ic_my_like_on
        }

        playPauseImage.setOnClickListener {
            if (!isRunning) {
                startTimer()
                playPauseImage.setImageResource(R.drawable.btn_miniplay_mvpause)
            } else {
                pauseTimer()
                playPauseImage.setImageResource(R.drawable.btn_miniplay_mvplay)
            }
        }

        findViewById<ImageView>(R.id.back).setOnClickListener {
            saveStateToSharedPreferences()
            finish() // HomeActivity 종료하고 MainActivity로 돌아감
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    time = progress.toLong()
                    timeTextView.text = formatTime(time)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }
    // 그럼 onPause -> 하고 넘어가는거면 저장하고 main으로 돌아가자마자 그쪽ㅇ
    override fun onPause() {
        super.onPause()
        saveStateToSharedPreferences() // Activity가 중지되기 전에 상태를 저장
//        pauseTimer() // Activity가 중지될 때 타이머를 멈춤
    }

    override fun onResume() {
        super.onResume()
        loadStateFromSharedPreferences() // Activity가 재개될 때 상태를 불러옴
        if (isRunning) {
            startTimer() // 이전에 실행 중이었으면 타이머를 다시 시작
        }
    }

    private fun startTimer() {
        isRunning = true
        job = CoroutineScope(Dispatchers.Main).launch {
            while (isRunning) {
                delay(1000)
                time += 1
                seekBar.progress = time.toInt()
                timeTextView.text = formatTime(time)
                saveStateToSharedPreferences() // 상태 저장
            }
        }
    }

    private fun pauseTimer() {
        isRunning = false
        job?.cancel()
    }

    private fun clearTimer() {
        pauseTimer()
        time = 0L
        timeTextView.text = formatTime(time)
        seekBar.progress = 0
    }
    // 근데 이게 put 시켜서 저장하는거면
    private fun saveStateToSharedPreferences() {
        val sharedPreferences = getSharedPreferences("MusicData", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("SONG_TITLE", textsong.text.toString())
            putString("ARTIST_NAME", textsinger.text.toString())
            putLong("TIME", time)
            putBoolean("IS_RUNNING", isRunning) // 타이머 상태 저장
            apply()
        }
    }
    // 이건 또 잘받아오긴해
    private fun loadStateFromSharedPreferences() {
        val sharedPreferences = getSharedPreferences("MusicData", Context.MODE_PRIVATE)
        time = sharedPreferences.getLong("TIME", 0L)
//        textsong.text = sharedPreferences.getString("SONG_TITLE", "Odoriko")
//        textsinger.text = sharedPreferences.getString("ARTIST_NAME", "Vaundy")
        Log.d("HomeActivity", "Loaded Song Title: $textsong, Artist Name: $textsinger")

        seekBar.progress = time.toInt()
        timeTextView.text = formatTime(time)
        isRunning = sharedPreferences.getBoolean("IS_RUNNING", false) // 타이머 상태 불러옴
    }

    private fun formatTime(seconds: Long): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, secs)
    }
    override fun onDestroy() {
        super.onDestroy()

        // HomeActivity에서 필요한 클린업 작업
        // 예: SharedPreferences에 저장된 값을 초기화
        val sharedPreferences = getSharedPreferences("MusicData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putLong("TIME", 0L)  // 시간을 0으로 초기화
        editor.putString("SONG_TITLE", "")  // 제목 초기화
        editor.putString("ARTIST_NAME", "")  // 아티스트 이름 초기화
        editor.apply()
    }

}
