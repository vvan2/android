package com.example.study3

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var seekBar2: SeekBar
    private lateinit var timer: Timer
    private lateinit var imageView: ImageView
    private lateinit var imagView1: ImageView
    private lateinit var nextImage: ImageView
    private lateinit var previImage: ImageView
    private lateinit var mainImage: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var artistTextView: TextView

    private val handler = Handler(Looper.getMainLooper())
    private val slideInterval: Long = 3000 // 3초마다 슬라이드

    private var time = 0L
    private var isRunning = false
    private var job: Job? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        imageView = findViewById(R.id.closebutton)
        imagView1 = findViewById(R.id.imageViewbanner)
        nextImage = findViewById(R.id.next)
        previImage = findViewById(R.id.before)
        mainImage = findViewById(R.id.startmain)

        titleTextView = findViewById(R.id.song_title)
        artistTextView = findViewById(R.id.song_singer)
        seekBar2 = findViewById(R.id.seekBar2)
        seekBar2.max = 180
        seekBar2.progress = 0

        // SharedPreferences에서 데이터 불러오기
        loadSongInfo()

        // BroadcastReceiver 등록
        val filter = IntentFilter("com.example.study3.UPDATE_TIMER")
        registerReceiver(timerReceiver, filter, RECEIVER_NOT_EXPORTED)

        mainImage.setOnClickListener {
            if (!isRunning) {
                startTimer()
                mainImage.setImageResource(R.drawable.btn_miniplay_mvpause)
            } else {
                pauseTimer()
                mainImage.setImageResource(R.drawable.btn_miniplayer_play)
            }
        }

        imageView.setOnClickListener {
            imagView1.visibility = View.GONE
        }

        // BottomNavigationView 설정
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.search -> {
                    loadFragment(SearchFragment())
                    true
                }
                R.id.somelse -> {
                    loadFragment(SomelseFragment())
                    true
                }
                R.id.stack -> {
                    loadFragment(StackFragment())
                    true
                }
                else -> false
            }
        }

        // 첫 화면에 fragment_home 추가
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        // 이미지 클릭 리스너 설정
        val imageView3: ImageView = findViewById(R.id.listimage)
        imageView3.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = getSharedPreferences("MusicData", Context.MODE_PRIVATE)
        val savedTime = sharedPreferences.getLong("TIME", 0L)  // 시간 불러오기
        val songTitle = sharedPreferences.getString("SONG_TITLE", "Odoriko") // 제목 불러오기
        val artistName = sharedPreferences.getString("ARTIST_NAME", "Vaundy") // 가수 이름 불러오기


        // 데이터가 null이 아니면 텍스트뷰에 설정
        titleTextView.text = songTitle ?: "제목없음"  // null이거나 빈 문자열이면 기본값 설정
        artistTextView.text = artistName ?: "가수없음"  // null이거나 빈 문자열이면 기본값 설정

        seekBar2.progress = savedTime.toInt()
        time = savedTime  // time을 savedTime으로 초기화
    }

    private val timerReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "com.example.study3.UPDATE_TIMER") {
                val time = intent.getLongExtra("time", 0L)
                seekBar2.progress = time.toInt()
                isRunning = true
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

    private fun loadSongInfo() {
        // SharedPreferences에서 제목, 가수, 시간 불러오기
        val sharedPreferences = getSharedPreferences("MusicData", Context.MODE_PRIVATE)
        time = sharedPreferences.getLong("TIME", 0L)
        titleTextView.text = sharedPreferences.getString("SONG_TITLE", "Odoriko")
        artistTextView.text = sharedPreferences.getString("ARTIST_NAME", "Vaundy")
        seekBar2.progress = time.toInt()
    }

    override fun onPause() {
        super.onPause()
        // SharedPreferences에 제목, 가수, 시간 저장
        val sharedPreferences = getSharedPreferences("MusicData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("SONG_TITLE", titleTextView.text.toString())
        editor.putString("ARTIST_NAME", artistTextView.text.toString())
        editor.putLong("TIME", time) // time을 저장
        editor.apply()

    }

    override fun onDestroy() {
        super.onDestroy()

        // Timer 종료
        if (::timer.isInitialized) {
            timer.cancel()  // 타이머가 초기화되어 있으면 종료
        }

        // SharedPreferences에 저장된 값 초기화 (필요시)
        val sharedPreferences = getSharedPreferences("MusicData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putLong("TIME", 0L)  // 시간을 0으로 초기화
        editor.putString("SONG_TITLE", "")  // 제목 초기화
        editor.putString("ARTIST_NAME", "")  // 아티스트 이름 초기화
        editor.apply()

        // BroadcastReceiver 해제
        unregisterReceiver(timerReceiver)
    }

    private fun startTimer() {
        isRunning = true
        job = CoroutineScope(Dispatchers.Main).launch {
            while (isRunning) {
                delay(1000)
                time += 1
                seekBar2.progress = time.toInt()

                saveTimerState()

                // SharedPreferences에 time 저장
                val sharedPreferences = getSharedPreferences("MusicData", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putLong("TIME", time)
                editor.apply()

                val intent = Intent("com.example.study3.UPDATE_TIMER")
                intent.putExtra("time", time)
                intent.putExtra("isRunning", isRunning)
                sendBroadcast(intent)
            }
        }
    }

    private fun pauseTimer() {
        isRunning = false
        job?.cancel()
        saveTimerState()  // 상태 저장
        // Broadcast로 상태 전달
        val intent = Intent("com.example.study3.UPDATE_TIMER")
        intent.putExtra("time", time)
        intent.putExtra("isRunning", isRunning)
        sendBroadcast(intent)
    }

    private fun clearTimer() {
        if (isRunning) {
            time = 0L
            seekBar2.progress = 0
        } else {
            pauseTimer()
            time = 0L
            seekBar2.progress = 0
        }
    }
    private fun saveTimerState() {
        val sharedPreferences = getSharedPreferences("MusicData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putLong("TIME", time)
        editor.putBoolean("IS_RUNNING", isRunning)
        editor.apply()
    }

    private fun formatTime(seconds: Long): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, secs)
    }
    private fun saveStateToSharedPreferences() {
        val sharedPreferences = getSharedPreferences("MusicData", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putLong("TIME", time)
            apply()
        }
    }


}
