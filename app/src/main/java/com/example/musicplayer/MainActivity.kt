package com.example.musicplayer

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
    private var trackIndex = 0
    private var playbackPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        with(viewBinding) {
            updateButtonsState()

            previous.setOnClickListener {
                trackIndex--
                if (trackIndex < 0) {
                    trackIndex = listOfTracks.size - 1
                }
                playMusic(listOfTracks[trackIndex])
                updateButtonsState()
            }

            play.setOnClickListener {
                playMusic(listOfTracks[trackIndex])
                updateButtonsState()
                isPaused = !isPaused
                Log.d("MusicPlayerTAG", "onClickPlay isPause: $isPaused")
            }

            next.setOnClickListener {
                trackIndex++
                if (trackIndex >= listOfTracks.size) {
                    trackIndex = 0
                }
                playMusic(listOfTracks[trackIndex])
                updateButtonsState()
            }

            pause.setOnClickListener {
                if (music?.isPlaying == true) {
                    music?.pause()
                } else if (isPaused) {
                    music?.start()
                }
                isPaused = !isPaused
                Log.d("MusicPlayerTAG", "onClickPause isPause: $isPaused")
                updateButtonsState()
            }
        }
    }

    private fun updateButtonsState() {
        viewBinding.previous.isEnabled = music?.isPlaying == true
        viewBinding.next.isEnabled = music?.isPlaying == true
        if (music?.isPlaying == true) {
            viewBinding.pause.visibility = View.VISIBLE
            viewBinding.play.visibility = View.GONE
        } else {
            viewBinding.play.visibility= View.VISIBLE
            viewBinding.pause.visibility= View.GONE
        }
    }

    private fun stopMusic() {
        music?.stop()
        music?.release()
        music = null
    }

    private fun playMusic(trackResId: Int) {
        stopMusic()
        music = MediaPlayer.create(this, trackResId)
        music?.start()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(INDEX_OF_TRACK, trackIndex)
        outState.putBoolean(IS_PAUSED, isPaused)
        music?.currentPosition?.let { outState.putInt(PLAYBACK_POSITION, it) }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        trackIndex = savedInstanceState.getInt(INDEX_OF_TRACK)
        isPaused = savedInstanceState.getBoolean(IS_PAUSED)
        playbackPosition = savedInstanceState.getInt(PLAYBACK_POSITION)
        Log.d("MusicPlayerTAG", "onRestoreInstanceState trackIndex: $trackIndex, isPaused: $isPaused")
        if (!isPaused) {
            playMusic(listOfTracks[trackIndex])
            music?.seekTo(playbackPosition)
            Log.d("MusicPlayerTAG", "onRestoreInstanceState playbackPosition: $playbackPosition")
        }
        updateButtonsState()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopMusic()
    }

    companion object {
        const val INDEX_OF_TRACK = "index"
        const val IS_PAUSED = "isPaused"
        const val PLAYBACK_POSITION = "playbackPosition"
        val listOfTracks = listOf(
            R.raw.nikelback_we_will_rock_you,
            R.raw.imagine_dragons_im_so_sorry,
            R.raw.merelin_menson_sweet_dreems
        )
    }
}
