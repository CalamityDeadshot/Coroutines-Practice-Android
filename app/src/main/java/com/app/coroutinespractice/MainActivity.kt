package com.app.coroutinespractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlinx.coroutines.NonCancellable.invokeOnCompletion

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var tv: TextView
    var isRunning = false
    var currentSecond = 0

    val scope = CoroutineScope(Dispatchers.IO)
    var timerJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv = findViewById(R.id.textView)
        findViewById<Button>(R.id.start).setOnClickListener(this)
        findViewById<Button>(R.id.stop).setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.start -> {
                if (timerJob?.isActive == true) return
                isRunning = true
                timerJob = scope.launch {
                    while (true) {
                        if (isRunning) {
                            delay(1000)
                            currentSecond++
                            withContext(Dispatchers.Main) {
                                tv.text = currentSecond.toString()
                            }
                        }
                    }
                }.also {
                    it.invokeOnCompletion {
                        scope.launch(Dispatchers.Main) {
                            currentSecond = 0
                            tv.text = currentSecond.toString()
                        }
                    }
                }
            }
            R.id.stop -> {
                timerJob?.cancel()
            }
        }
    }
}