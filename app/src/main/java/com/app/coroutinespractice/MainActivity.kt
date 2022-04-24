package com.app.coroutinespractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var tv: TextView
    var stopwatchJob: Job? = null

    val stopwatchFlow = flow<Int> {
        var currentSecond = 0
        while (true) {
            emit(currentSecond)
            delay(1000)
            currentSecond++
        }
    }

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
                if (stopwatchJob?.isActive == true) return
                stopwatchJob = lifecycleScope.launchWhenStarted {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        stopwatchFlow
                            .cancellable()
                            .collect {
                            tv.text = it.toString()
                        }
                    }
                }
            }
            R.id.stop -> {
                stopwatchJob?.cancel()
            }
        }
    }
}