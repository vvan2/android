package com.example.study3

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Broadcast 수신 시 처리할 코드
        Log.d("MyBroadcastReceiver", "Broadcast received!")
        // 여기에 추가 로직을 작성하세요
    }
}
