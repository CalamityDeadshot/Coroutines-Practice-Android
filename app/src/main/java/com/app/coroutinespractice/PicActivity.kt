package com.app.coroutinespractice

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import kotlinx.coroutines.*
import java.io.IOException
import java.net.URL

class PicActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var loadButton: Button
    lateinit var imageView: ImageView
    lateinit var progressBar: ProgressBar

    val scope = CoroutineScope(Dispatchers.Main)

    val imageUrl = URL("https://alexandreesl.files.wordpress.com/2020/09/coroutines.png")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pic)

        loadButton = findViewById(R.id.load)
        loadButton.setOnClickListener(this)

        imageView = findViewById(R.id.image)
        progressBar = findViewById(R.id.progress)

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.load -> {
                imageView.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                scope.launch {
                    val imageBitmap = loadImageData()
                    imageView.setImageBitmap(imageBitmap)
                    imageView.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }

            }
        }
    }

    private suspend fun loadImageData(): Bitmap = withContext(Dispatchers.IO) {
        return@withContext BitmapFactory.decodeStream(imageUrl.openStream())
    }
}
