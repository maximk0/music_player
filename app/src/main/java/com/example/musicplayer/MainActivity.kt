package com.example.musicplayer

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.musicplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val viewBinding get() = _binding!!

    private var music: MediaPlayer? = null
    private var isPaused = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        with(viewBinding) {
            updateButtonsState()

            previous.setOnClickListener {

            }

            play.setOnClickListener {
                isPaused = !isPaused
                updateButtonsState()

                val serviceIntent = Intent(this@MainActivity, MusicPlayerService::class.java)
                startService(serviceIntent)
            }

            next.setOnClickListener {
                updateButtonsState()
            }

            pause.setOnClickListener {

                isPaused = !isPaused
                updateButtonsState()

                val serviceIntent = Intent(this@MainActivity, MusicPlayerService::class.java)
                serviceIntent.action = Constants.ACTION.STOP_FOREGROUND_ACTION
                startService(serviceIntent)
            }
        }
    }

    private fun updateButtonsState() {
        if (!isPaused) {
            viewBinding.pause.visibility = View.VISIBLE
            viewBinding.play.visibility = View.GONE
        } else {
            viewBinding.play.visibility= View.VISIBLE
            viewBinding.pause.visibility= View.GONE
        }
    }

}
